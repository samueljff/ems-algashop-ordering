package com.fonseca.algashop.ordering.core.application.customer;

import com.fonseca.algashop.ordering.core.application.AbstractApplicationIT;
import com.fonseca.algashop.ordering.core.ports.commons.AddressData;
import com.fonseca.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.fonseca.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.fonseca.algashop.ordering.core.domain.model.customer.*;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerInput;
import com.fonseca.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;
import com.fonseca.algashop.ordering.infrastructure.adapters.in.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDate;
import java.util.UUID;

import static com.fonseca.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers.NotifyNewRegistrationInput;

class CustomerManagementApplicationServiceIT extends AbstractApplicationIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private ForNotifyingCustomers forNotifyingCustomers;

    @Autowired
    private ForQueryingCustomers forQueryingCustomers;

    @Test
    public void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = forQueryingCustomers.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "John",
                        "Doe",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7, 5)
                );
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(customerEventListener, Mockito.never()).listen(Mockito.any(CustomerArchivedEvent.class));
        Mockito.verify(forNotifyingCustomers).notifyNewRegistration(Mockito.any(NotifyNewRegistrationInput.class));
    }

    @Test
    public void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = forQueryingCustomers.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "Matt",
                        "Damon",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7, 5)
                );
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    public void shouldArchiveCustomerSuccessfully() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);

        CustomerOutput customerOutput = forQueryingCustomers.findById(customerId);

        Assertions.assertThat(customerOutput.getArchived()).isTrue();
        Assertions.assertThat(customerOutput.getArchivedAt()).isNotNull();

        AddressData address = customerOutput.getAddress();

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                ).containsExactly(
                        customerId,
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(customerOutput.getEmail()).matches(".+@anonymous.com");
        Assertions.assertThat(address.getNumber()).isEqualTo("Anonymized");
        Assertions.assertThat(address.getComplement()).isNull();
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomerId() {
        UUID nonExistentId = UUID.randomUUID();

        Assertions.assertThatThrownBy(() ->
                        customerManagementApplicationService.archive(nonExistentId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenCustomerAlreadyArchived() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatThrownBy(() ->
                        customerManagementApplicationService.archive(customerId))
                .isInstanceOf(CustomerArchivedException.class);
    }

    @Test
    void shouldChangeEmailSuccessfully() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.changeEmail(customerId, "new.email@email.com");

        CustomerOutput customerOutput = forQueryingCustomers.findById(customerId);

        Assertions.assertThat(customerOutput.getEmail())
                .isEqualTo("new.email@email.com");
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenChangingEmailOfNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatThrownBy(() ->
                customerManagementApplicationService.changeEmail(
                        nonExistingId, "email@email.com")
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldThrowCustomerEmailIsInUseExceptionWhenChangingEmailToExistingOne() {
        CustomerInput customer1 = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerInput customer2 = CustomerInputTestDataBuilder.aCustomer()
                .email("other@email.com")
                .build();

        UUID customerId1 = customerManagementApplicationService.create(customer1);
        customerManagementApplicationService.create(customer2);

        Assertions.assertThatThrownBy(() ->
                customerManagementApplicationService.changeEmail(
                        customerId1, "other@email.com")
        ).isInstanceOf(CustomerEmailIsInUseException.class);
    }

    @Test
    void givenArchivedCustomer_whenChangeEmail_thenThrowCustomerArchivedException() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatThrownBy(() ->
                customerManagementApplicationService.changeEmail(
                        customerId, "new.email@email.com")
        ).isInstanceOf(CustomerArchivedException.class);
    }

    @Test
    void givenValidCustomer_whenChangeEmailWithInvalidFormat_thenThrowIllegalArgumentException() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThatThrownBy(() ->
                customerManagementApplicationService.changeEmail(
                        customerId, "email-invalido")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}