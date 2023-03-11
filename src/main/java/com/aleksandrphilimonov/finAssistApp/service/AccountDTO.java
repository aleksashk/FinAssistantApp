package com.aleksandrphilimonov.finAssistApp.service;

import java.math.BigDecimal;

public class AccountDTO {
    private long id;

    private String title;

    private BigDecimal balance;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + balance +
                ", userId=" + userId +
                '}';
    }
}
