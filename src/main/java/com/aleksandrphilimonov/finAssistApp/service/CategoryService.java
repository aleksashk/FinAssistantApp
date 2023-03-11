package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.CategoryModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.converter.ReportModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryDao;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryModel;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryModelToDtoConverter categoryModelToDtoConverter;
    private final ReportModelToDtoConverter reportModelToDtoConverter;

    public CategoryService(CategoryDao categoryDao, CategoryModelToDtoConverter categoryModelToDtoConverter, ReportModelToDtoConverter reportModelToDtoConverter) {
        this.categoryDao = categoryDao;
        this.categoryModelToDtoConverter = categoryModelToDtoConverter;
        this.reportModelToDtoConverter = reportModelToDtoConverter;
    }

    public List<CategoryDTO> getAllByUserId(long userId) {
        return categoryDao.getAllByUserId(userId)
                .stream()
                .map(categoryModelToDtoConverter::convert)
                .collect(toList());
    }

    public CategoryDTO insert(String name, long userId) {
        if (categoryDao.findByNameAndUserId(name, userId) == null) {
            CategoryModel categoryModel = categoryDao.create(name, userId);
            return categoryModelToDtoConverter.convert(categoryModel);
        }
        return null;
    }

    public CategoryDTO update(long id, String newName, long userId) {
        if (categoryDao.findByNameAndUserId(newName, userId) == null) {
            CategoryModel categoryModel = categoryDao.update(id, newName, userId);
            return categoryModelToDtoConverter.convert(categoryModel);
        }
        return null;
    }

    public void delete(long id, long userId) {
        categoryDao.delete(id, userId);
    }

    public List<CategoryReportDTO> getListReportMoneyIn(long userId, LocalDate fromDate, LocalDate toDate) {
        return categoryDao.getReportMoneyIn(userId, fromDate, toDate).stream().map(reportModelToDtoConverter::convert).collect(toList());
    }

    public List<CategoryReportDTO> getListReportMoneyOut(long userId, LocalDate fromDate, LocalDate toDate) {
        return categoryDao.getReportMoneyOut(userId, fromDate, toDate).stream().map(reportModelToDtoConverter::convert).collect(toList());
    }
}
