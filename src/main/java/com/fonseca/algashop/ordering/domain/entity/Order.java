package com.fonseca.algashop.ordering.domain.entity;

import com.fonseca.algashop.ordering.domain.valueobjet.BillingInfo;
import com.fonseca.algashop.ordering.domain.valueobjet.Money;
import com.fonseca.algashop.ordering.domain.valueobjet.Quantity;
import com.fonseca.algashop.ordering.domain.valueobjet.ShippingInfo;
import com.fonseca.algashop.ordering.domain.valueobjet.id.CustomerId;
import com.fonseca.algashop.ordering.domain.valueobjet.id.OrderId;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

public class Order {

    private OrderId id;
    private CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItems;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    private BillingInfo billing;
    private ShippingInfo shipping;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Money shippingCost;
    private LocalDate expectedDeliveryDate;

    private Set<OrderItem> items;

    public Order(OrderId id, CustomerId customerId,
                 Money totalAmount, Quantity totalItems,
                 OffsetDateTime placedAt, OffsetDateTime paidAt,
                 OffsetDateTime canceledAt, OffsetDateTime readyAt,
                 BillingInfo billing, ShippingInfo shipping,
                 OrderStatus status, PaymentMethod paymentMethod,
                 Money shippingCost, LocalDate expectedDeliveryDate,
                 Set<OrderItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setCanceledAt(canceledAt);
        this.setReadyAt(readyAt);
        this.setBilling(billing);
        this.setShipping(shipping);
        this.setStatus(status);
        this.setPaymentMethod(paymentMethod);
        this.setShippingCost(shippingCost);
        this.setExpectedDeliveryDate(expectedDeliveryDate);
        this.setItems(items);
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public BillingInfo billing() {
        return billing;
    }

    public ShippingInfo shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Money shippingCost() {
        return shippingCost;
    }

    public LocalDate expectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Set<OrderItem> items() {
        return items;
    }

    private void setId(OrderId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setBilling(BillingInfo billing) {
        Objects.requireNonNull(billing);
        this.billing = billing;
    }

    private void setShipping(ShippingInfo shipping) {
        Objects.requireNonNull(shipping);
        this.shipping = shipping;
    }

    private void setStatus(OrderStatus status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod);
        this.paymentMethod = paymentMethod;
    }

    private void setShippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
    }

    private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    private void setItems(Set<OrderItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
