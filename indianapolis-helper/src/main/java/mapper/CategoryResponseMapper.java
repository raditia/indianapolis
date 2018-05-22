package mapper;

import com.gdn.entity.Category;
import com.gdn.response.CategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponseMapper {

    public static CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryResponse> toCategoryListResponse(List<Category> categoryList){
        return categoryList.stream()
                .map(CategoryResponseMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

}
