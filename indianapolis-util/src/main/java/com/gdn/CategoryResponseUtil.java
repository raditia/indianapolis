package com.gdn;

import com.gdn.response.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponseUtil {
    public static CategoryResponse categoryResponseCompleteAttribute = CategoryResponse.builder()
            .id(CategoryUtil.categoryMinusWarehouseCategoryList.getId())
            .name(CategoryUtil.categoryMinusWarehouseCategoryList.getName())
            .build();
    public static List<CategoryResponse> categoryResponseListCompleteAttribute = new ArrayList<CategoryResponse>(){{
        add(categoryResponseCompleteAttribute);
    }};
}
