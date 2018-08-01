package com.gdn.util;

import com.gdn.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtil {
    public static Category categoryMinusWarehouseCategoryList = Category.builder()
            .id("category_elektronik")
            .name("elektronik")
            .build();
    public static List<Category> categoryListMinusWarehouseCategory = new ArrayList<Category>(){{
        add(categoryMinusWarehouseCategoryList);
    }};
}
