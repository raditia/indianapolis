package com.gdn.category.implementation;

import com.gdn.category.CategoryService;
import com.gdn.repository.CategoryRepository;
import com.gdn.response.CategoryResponse;
import com.gdn.response.WebResponse;
import mapper.CategoryResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public WebResponse<List<CategoryResponse>> findAllCategory() {
        return WebResponse.OK(CategoryResponseMapper.toCategoryListResponse(categoryRepository.findAll()));
    }

}
