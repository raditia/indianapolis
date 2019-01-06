package com.gdn.batch.fleet_recommendation;

import com.gdn.CffUtil;
import com.gdn.DatabaseQueryResultUtil;
import com.gdn.RecommendationUtil;
import com.gdn.WarehouseUtil;
import com.gdn.cff.CffService;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Recommendation;
import com.gdn.recommendation_algorithm.RecommendationProcessorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FleetRecommendationProcessorTest {

    @Mock
    private CffService cffService;
    @Mock
    private RecommendationProcessorService recommendationProcessorService;

    @InjectMocks
    private FleetRecommendationProcessor fleetRecommendationProcessor;

    private List<DatabaseQueryResult> resultList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process() throws Exception {
        ReflectionTestUtils.setField(fleetRecommendationProcessor, "rowCount", 1);
        ReflectionTestUtils.setField(fleetRecommendationProcessor, "warehouseId", WarehouseUtil.warehouseMinusWarehouseCategoryList.getId());
        resultList.add(DatabaseQueryResultUtil.databaseQueryResultCompleteAttribute);
        given(recommendationProcessorService
                .getThreeRecommendation(
                        DatabaseQueryResultUtil.databaseQueryResultList,
                        WarehouseUtil.warehouseMinusWarehouseCategoryList.getId()))
                .willReturn(RecommendationUtil.recommendationList);
        given(cffService.updateSchedulingStatus(DatabaseQueryResultUtil.databaseQueryResultCompleteAttribute.getCffId())).willReturn(CffUtil.cffCompleteAttribute);
        List<Recommendation> expectedResponse = fleetRecommendationProcessor.process(DatabaseQueryResultUtil.databaseQueryResultCompleteAttribute);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(RecommendationUtil.recommendationList));

        verify(recommendationProcessorService, times(1))
                .getThreeRecommendation(
                        DatabaseQueryResultUtil.databaseQueryResultList,
                        WarehouseUtil.warehouseMinusWarehouseCategoryList.getId());
        verify(cffService, times(resultList.size())).updateSchedulingStatus(DatabaseQueryResultUtil.databaseQueryResultCompleteAttribute.getCffId());
    }
}