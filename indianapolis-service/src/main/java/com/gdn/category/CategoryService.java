package com.gdn.category;

import com.gdn.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    Category save (Category category);
}
