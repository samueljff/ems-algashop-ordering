package com.fonseca.algashop.ordering.domain.exceptions;

public class ErrorMessages {
    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is Invalid!";
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";
    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";
    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived cannot be changed!";
    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGE = "Cannot Change Order %s status from %s to %s";
    public static final String ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST = "Order %s expected delivery date cannot be in the past";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITEMS = "Order %s cannot be closed, it has no items";
}
