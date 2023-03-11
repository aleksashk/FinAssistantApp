package com.aleksandrphilimonov.finAssistApp.converter;

import com.aleksandrphilimonov.finAssistApp.dao.CategoryReportModel;
import com.aleksandrphilimonov.finAssistApp.service.CategoryReportDTO;

import java.math.BigDecimal;

public class ReportModelToDtoConverter implements Converter<CategoryReportModel, CategoryReportDTO>{
    @Override
    public CategoryReportDTO convert(CategoryReportModel source) {
        CategoryReportDTO categoryReportDTO = new CategoryReportDTO();
        categoryReportDTO.setTitle(source.getTitle());
        categoryReportDTO.setAmount(new BigDecimal(String.valueOf(source.getAmount())));

        return categoryReportDTO;
    }
}
