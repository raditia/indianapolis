package com.gdn.category;

import com.gdn.entity.Category;
import org.springframework.stereotype.Service;

public interface CategoryService {

    Category save (Category category);
    void saveDefaultCategory();

}
