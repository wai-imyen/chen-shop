package com.yen.androbe.object;

public class Login {

    protected FormError errors;
    protected String accountToken;
    protected Integer customer_id;

    public FormError getErrors() {
        return errors;
    }

    public void setErrors(FormError errors) {
        this.errors = errors;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }
}
