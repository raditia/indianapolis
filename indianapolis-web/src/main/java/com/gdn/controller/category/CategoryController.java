package com.gdn.controller.category;

import com.gdn.category.CategoryService;
import com.gdn.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(
            value = "/category",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Category> findAll(){
        return categoryService.findAll();
    }

}
