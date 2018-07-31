package com.gdn.category.implementation;

import com.gdn.entity.Category;
import com.gdn.mapper.CategoryResponseMapper;
import com.gdn.repository.CategoryRepository;
import com.gdn.response.CategoryResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category = Category.builder()
            .id("category_elektronik")
            .name("elektronik")
            .build();
    private List<Category> categoryList = new ArrayList<Category>(){{
        add(category);
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllCategory() {
        given(categoryRepository.findAll()).willReturn(categoryList);

        WebResponse<List<CategoryResponse>> expectedResponse = categoryService.findAllCategory();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(CategoryResponseMapper.toCategoryListResponse(categoryList))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(categoryRepository, times(1)).findAll();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(categoryRepository);
    }
}