package com.gdn.category;

import com.gdn.response.CategoryResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface CategoryService {
    WebResponse<List<CategoryResponse>> findAllCategory();
}
