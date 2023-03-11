package com.aleksandrphilimonov.finAssistApp.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CategoryReportModel {
    private String title;
    private BigDecimal amount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal moneyIn) {
        this.amount = moneyIn.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryReportModel that = (CategoryReportModel) o;

        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
