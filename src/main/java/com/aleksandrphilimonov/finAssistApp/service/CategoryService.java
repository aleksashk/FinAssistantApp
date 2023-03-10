package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.CategoryModelToDTOConverter;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryDao;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryModelToDTOConverter categoryModelToDTOConverter;

    public CategoryService() {
        this.categoryDao = new CategoryDao();
        this.categoryModelToDTOConverter = new CategoryModelToDTOConverter();
    }

    public List<CategoryDTO> getAllByUserId(long userId) {
        return categoryDao.getAllByUserId(userId).stream().map(categoryModelToDTOConverter::convert).collect(Collectors.toList());
    }

    public CategoryDTO insert(String name, long userId) {
        if (categoryDao.findByNameAndUserId(name, userId) == null) {
            CategoryModel categoryModel = categoryDao.create(name, userId);
            return categoryModelToDTOConverter.convert(categoryModel);
        }
        return null;
    }

    public CategoryDTO update(long id, String newName, long userId) {
        if (categoryDao.findByNameAndUserId(newName, userId) == null) {
            CategoryModel categoryModel = categoryDao.update(id, newName, userId);
            return categoryModelToDTOConverter.convert(categoryModel);
        }
        return null;
    }

    public void delete(long id, long userId) {
        categoryDao.delete(id, userId);
    }
}
