package com.aleksandrphilimonov.finAssistApp.converter;

import com.aleksandrphilimonov.finAssistApp.dao.CategoryModel;
import com.aleksandrphilimonov.finAssistApp.service.CategoryDTO;

public class CategoryModelToDtoConverter implements Converter<CategoryModel, CategoryDTO> {
    @Override
    public CategoryDTO convert(CategoryModel source) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(source.getId());
        categoryDTO.setName(source.getName());
        categoryDTO.setUserId(source.getUserId());

        return categoryDTO;
    }
}
