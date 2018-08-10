package com.gdn.recommendation_algorithm.implementation;

import com.gdn.*;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Product;
import com.gdn.recommendation.Recommendation;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.recommendation_algorithm.Helper;
import com.gdn.recommendation_algorithm.PickupProcessorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Helper.class)
public class RecommendationProcessorImplTest {

    @Mock
    private FleetProcessorService fleetProcessorService;
    @Mock
//    @InjectMocks
    private PickupProcessorService pickupProcessorService;

    @InjectMocks
    private RecommendationProcessorImpl recommendationProcessorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStatic(Helper.class);
    }

    @Test
    public void getThreeRecommendation(){
        given(fleetProcessorService.getTopThreeFleetsWillUsed(DatabaseQueryResultUtil.databaseQueryResultList)).willReturn(FleetUtil.topFleetsWillUsed);
        given(Helper.migrateIntoProductList(DatabaseQueryResultUtil.databaseQueryResultList)).willReturn(ProductUtil.productList);

        given(Helper.empty(ProductUtil.productList)).willReturn(false).willReturn(true);
        given(pickupProcessorService.getNextPickup(ProductUtil.productList, FleetUtil.fleetMotorCompleteAttribute)).willReturn(PickupUtil.pickup);

        List<Recommendation> threeRecommendations = recommendationProcessorService.getThreeRecommendation(DatabaseQueryResultUtil.databaseQueryResultList,WarehouseUtil.warehouseMinusWarehouseCategoryList.getId());

        assertThat(threeRecommendations, notNullValue());
        assertThat(threeRecommendations.isEmpty(), equalTo(false));

        for (Recommendation recommendation:threeRecommendations){
            assertThat(recommendation.getCbmTotal(), equalTo(RecommendationUtil.recommendation.getCbmTotal()));
            assertThat(recommendation.getPickupList(), equalTo(RecommendationUtil.recommendation.getPickupList()));
            assertThat(recommendation.getProductAmount(), equalTo(RecommendationUtil.recommendation.getProductAmount()));
            assertThat(recommendation.getWarehouseId(), equalTo(RecommendationUtil.recommendation.getWarehouseId()));
        }
        verify(fleetProcessorService, times(1)).getTopThreeFleetsWillUsed(DatabaseQueryResultUtil.databaseQueryResultList);
        verify(pickupProcessorService, times(1)).getNextPickup(ProductUtil.productList,FleetUtil.fleetMotorCompleteAttribute);

    }

}