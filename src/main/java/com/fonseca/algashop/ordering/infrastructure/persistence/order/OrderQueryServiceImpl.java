package com.fonseca.algashop.ordering.infrastructure.persistence.order;

import com.fonseca.algashop.ordering.application.order.query.*;
import com.fonseca.algashop.ordering.application.utility.Mapper;
import com.fonseca.algashop.ordering.domain.model.order.OrderId;
import com.fonseca.algashop.ordering.domain.model.order.OrderNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;
    private final EntityManager entityManager;

    @Override
    public OrderDetailOutput findById(String id) {
        OrderPersistenceEntity entity = repository.findById(new OrderId(id).value().toLong()).orElseThrow(() -> new OrderNotFoundException());
        return mapper.convert(entity, OrderDetailOutput.class);
    }

    @Override
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        Long totalQueryResult = countTotalQueryResults(filter);
        if (totalQueryResult.equals(0L)) {
            PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
            return new PageImpl<>(new ArrayList<>(), pageRequest, totalQueryResult);
        }
        return filterQuery(filter, totalQueryResult);
    }

    private Long countTotalQueryResults(OrderFilter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Expression<Long> count = builder.count(root);
        Predicate[] predicates = toPredicate(builder, root, filter);

        criteriaQuery.select(count);
        criteriaQuery.where(predicates);

        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    private Page<OrderSummaryOutput> filterQuery(OrderFilter filter, Long totalQueryResult) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderSummaryOutput> criteriaQuery = builder.createQuery(OrderSummaryOutput.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Path<Object> customer = root.get("customer");
        criteriaQuery.select(
                builder.construct(OrderSummaryOutput.class,
                        root.get("id"),
                        root.get("totalItems"),
                        root.get("totalAmount"),
                        root.get("placedAt"),
                        root.get("paidAt"),
                        root.get("canceledAt"),
                        root.get("readyAt"),
                        root.get("status"),
                        root.get("paymentMethod"),
                        builder.construct(CustomerMinimalOutput.class,
                                customer.get("id"),
                                customer.get("firstName"),
                                customer.get("lastName"),
                                customer.get("email"),
                                customer.get("document"),
                                customer.get("phone")
                        )
                )
        );

        Predicate[] predicates = toPredicate(builder, root, filter);
        Order sortOrder = toSortOrder(builder, root, filter);

        criteriaQuery.where(predicates);
        if (sortOrder != null) {
            criteriaQuery.orderBy(sortOrder);
        }

        TypedQuery<OrderSummaryOutput> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(filter.getSize() * filter.getPage());
        typedQuery.setMaxResults(filter.getSize());
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, totalQueryResult);
    }

    private Order toSortOrder(CriteriaBuilder builder, Root<OrderPersistenceEntity> root, OrderFilter filter) {
        if (filter.getSortDirectionOrDefault() == Sort.Direction.ASC) {
            return builder.asc(root.get(filter.getSortByPropertyOrDefault().getPropertyName()));
        }

        if (filter.getSortDirectionOrDefault() == Sort.Direction.DESC) {
            return builder.desc(root.get(filter.getSortByPropertyOrDefault().getPropertyName()));
        }
        return null;
    }

    private Predicate[] toPredicate(CriteriaBuilder builder, Root<OrderPersistenceEntity> root, OrderFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCustomerId() != null) {
            predicates.add(builder.equal(root.get("customer").get("id"), filter.getCustomerId()));
        }

        if (filter.getStatus() != null && filter.getStatus().isBlank()) {
            predicates.add(builder.equal(root.get("status"), filter.getStatus().toUpperCase()));
        }

        if (filter.getOrderId() != null) {
            Long orderIdLongValue;
            try {
                OrderId orderId = new OrderId(filter.getOrderId());
                orderIdLongValue = orderId.value().toLong();
            } catch (IllegalArgumentException e) {
                orderIdLongValue = 0L;
            }
            predicates.add(builder.equal(root.get("id"), orderIdLongValue));
        }

        if (filter.getPlacedAtFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtFrom()));
        }

        if (filter.getPlacedAtTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtTo()));
        }

        if (filter.getTotalAmountFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountFrom()));
        }

        if (filter.getTotalAmountTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountTo()));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
