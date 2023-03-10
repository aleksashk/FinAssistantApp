package com.aleksandrphilimonov.finAssistApp.dao;

import java.math.BigDecimal;

public class AccountModel {
    private long id;

    private String title;

    private BigDecimal amount;

    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", userId=" + userId +
                '}';
    }
}
