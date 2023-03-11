package com.aleksandrphilimonov.finAssistApp.service;

import com.aleksandrphilimonov.finAssistApp.converter.CategoryModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.converter.ReportModelToDtoConverter;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryDao;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryModel;
import com.aleksandrphilimonov.finAssistApp.dao.CategoryReportModel;
import com.aleksandrphilimonov.finAssistApp.exception.CustomException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryDao categoryDao;

    @Mock
    CategoryModelToDtoConverter categoryModelToDtoConverter;

    @Mock
    ReportModelToDtoConverter reportModelToDtoConverter;

    @Test
    public void get_all_by_user_id_success() {
        List<CategoryModel> categoryModelList = Arrays.asList(new CategoryModel(), new CategoryModel());

        when(categoryDao.getAllByUserId(1)).thenReturn(categoryModelList);

        when(categoryModelToDtoConverter.convert(new CategoryModel())).thenReturn(new CategoryDTO());

        List<CategoryDTO> categoryDTOList = Arrays.asList(new CategoryDTO(), new CategoryDTO());

        List<CategoryDTO> list = categoryService.getAllByUserId(1);

        assertEquals(categoryDTOList, list);

        verify(categoryDao, times(1)).getAllByUserId(1);
        int time = list.size();
        verify(categoryModelToDtoConverter, times(time)).convert(new CategoryModel());
    }

    @Test
    public void get_all_by_user_id_failed() {
        when(categoryDao.getAllByUserId(1)).thenReturn(Collections.emptyList());
        List<CategoryDTO> list = categoryService.getAllByUserId(1);
        assertTrue(list.isEmpty());

        verify(categoryDao, times(1)).getAllByUserId(1);
        verifyZeroInteractions(categoryModelToDtoConverter);
    }

    @Test
    public void insert_success() {
        when(categoryDao.findByNameAndUserId("asdf", 1)).thenReturn(null);

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("xyz");
        when(categoryDao.create("asdf", 1)).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("asdf");
        when(categoryModelToDtoConverter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = categoryService.insert("asdf", 1);
        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).findByNameAndUserId("asdf", 1);
        verify(categoryDao, times(1)).create("asdf", 1);
        verify(categoryModelToDtoConverter, times(1)).convert(categoryModel);
    }

    @Test
    public void insert_failed() {
        when(categoryDao.findByNameAndUserId("asdf", 1)).thenReturn(new CategoryModel());

        CategoryDTO categoryDTO = categoryService.insert("asdf", 1);
        assertNull(categoryDTO);

        verify(categoryDao, times(1)).findByNameAndUserId("asdf", 1);
        verify(categoryDao, times(0)).create("asdf", 1);
        verifyZeroInteractions(categoryModelToDtoConverter);
    }

    @Test
    public void update_success() {
        when(categoryDao.findByNameAndUserId("asdf", 1)).thenReturn(null);

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("asdf");
        when(categoryDao.update(1, "asdf", 1)).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("asdf");
        when(categoryModelToDtoConverter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = categoryService.update(1, "asdf", 1);

        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).findByNameAndUserId("asdf", 1);
        verify(categoryDao, times(1)).update(1, "asdf", 1);
        verify(categoryModelToDtoConverter, times(1)).convert(categoryModel);
    }

    @Test
    public void update_failed() {
        when(categoryDao.findByNameAndUserId("asdf", 1)).thenReturn(new CategoryModel());

        CategoryDTO categoryDTO = categoryService.update(1, "asdf", 1);
        assertNull(categoryDTO);

        verify(categoryDao, times(1)).findByNameAndUserId("asdf", 1);
        verify(categoryDao, times(0)).update(1, "asdf", 1);
        verifyZeroInteractions(categoryModelToDtoConverter);
    }

    @Test
    public void delete_success() {
        categoryService.delete(1, 1);
        verify(categoryDao, times(1)).delete(1, 1);
    }

    @Test(expected = CustomException.class)
    public void delete_failed() {
        doThrow(new CustomException("Invalid request. Category didn't delete.")).when(categoryDao).delete(1, 1);
        categoryService.delete(1, 1);
        verify(categoryDao, times(1)).delete(1, 1);
    }

    @Test
    public void get_list_report_success() {
        LocalDate date = LocalDate.now();

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("asdf");
        categoryModel.setUserId(1);

        List<CategoryModel> categoryModelList = new ArrayList<>();
        categoryModelList.add(categoryModel);
        when(categoryDao.getAllByUserId(1)).thenReturn(categoryModelList);

        assertEquals(categoryDao.getAllByUserId(1), categoryModelList);

        CategoryReportModel categoryReportModel = new CategoryReportModel();
        categoryReportModel.setTitle("asdf");
        categoryReportModel.setAmount(new BigDecimal(22));
        when(categoryDao.getReportMoneyOut(1, date, date)).thenReturn(Collections.singletonList(categoryReportModel));

        CategoryReportDTO categoryReportDTO = new CategoryReportDTO();
        categoryReportDTO.setTitle("asdf");
        categoryReportDTO.setAmount(new BigDecimal(22));
        when(reportModelToDtoConverter.convert(categoryReportModel)).thenReturn(categoryReportDTO);

        List<CategoryReportDTO> reportDTOList = new ArrayList<>();
        reportDTOList.add(categoryReportDTO);

        List<CategoryReportDTO> categoryReportDTOList = categoryService.getListReportMoneyOut(1, date, date);

        assertEquals(reportDTOList, categoryReportDTOList);

        verify(categoryDao, times(1)).getAllByUserId(1);
        int time = categoryReportDTOList.size();
        verify(categoryDao, times(time)).getReportMoneyOut(1, date, date);
        verify(reportModelToDtoConverter, times(time)).convert(categoryReportModel);
    }

    @Test
    public void get_list_report_failed() {
        LocalDate date = LocalDate.now();

        List<CategoryModel> categoryModelList = new ArrayList<>();
        categoryModelList.add(new CategoryModel());
        when(categoryDao.getAllByUserId(1)).thenReturn(categoryModelList);

        when(categoryDao.getReportMoneyOut(1, date, date)).thenReturn(Collections.emptyList());

        assertEquals(categoryDao.getAllByUserId(1), categoryModelList);
        assertEquals(categoryDao.getReportMoneyOut(1, date, date), Collections.emptyList());

        List<CategoryReportDTO> categoryReportDTOList = new ArrayList<>();
        List<CategoryReportDTO> reportDTOList = categoryService.getListReportMoneyOut(1, date, date);

        assertNotNull(reportDTOList);
        assertEquals(categoryReportDTOList, reportDTOList);

        verify(categoryDao, times(1)).getAllByUserId(1);
        verifyZeroInteractions(reportModelToDtoConverter);
    }
}