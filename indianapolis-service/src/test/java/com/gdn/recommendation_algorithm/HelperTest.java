package com.gdn.recommendation_algorithm;

import com.gdn.ProductUtil;
import com.gdn.recommendation.Product;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


public class HelperTest {

    @Test
    public void emptyTrue() {
        List<Product> productList = ProductUtil.productList;

        boolean empty = Helper.empty(productList);

        assertThat(empty, notNullValue());
        assertThat(empty, equalTo(false));

    }

    @Test
    public void emptyFalse() {
        List<Product> productList = new ArrayList<>();

        boolean empty = Helper.empty(productList);

        assertThat(empty, notNullValue());
        assertThat(empty, equalTo(true));

    }

}