package com.gdn.recommendation.implementation;

import com.gdn.RecommendationResponseUtil;
import com.gdn.RecommendationResultUtil;
import com.gdn.WarehouseUtil;
import com.gdn.entity.Warehouse;
import com.gdn.pickup.PickupService;
import com.gdn.repository.CffRepository;
import com.gdn.repository.RecommendationRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecommendationServiceImplTest {

    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private RecommendationResultRepository recommendationResultRepository;
    @Mock
    private CffRepository cffRepository;
    @Mock
    private PickupService pickupService;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @Mock
    private Job fleetRecommendationJob;
    @Mock
    private JobLauncher jobLauncher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void executeBatch_OK() throws Exception {
        given(cffRepository.findDistinctWarehouse()).willReturn(WarehouseUtil.warehouseListMinusWarehouseCategoryList);
        given(recommendationRepository.getRowCount(WarehouseUtil.warehouseMinusWarehouseCategoryList))
                .willReturn(1);
        JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                .addString("warehouse", WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
                .addString("rowCount", String.valueOf(1))
                .toJobParameters();
        jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);

        recommendationService.executeBatch();

        verify(cffRepository, times(1)).findDistinctWarehouse();
        verify(recommendationRepository, times(1)).getRowCount(WarehouseUtil.warehouseMinusWarehouseCategoryList);
        verify(jobLauncher, times(1)).run(fleetRecommendationJob, fleetRecommendationJobParameters);
    }

    @Test
    public void findAllRecommendationFleetResult() {
        Warehouse warehouse = Warehouse.builder()
                .id("warehouse_cawang")
                .build();
        given(recommendationResultRepository.findByWarehouse(warehouse)).willReturn(RecommendationResultUtil.recommendationResultListCompleteAttribute);

        WebResponse<RecommendationResponse> expectedResponse = recommendationService.findAllRecommendationFleetResult(warehouse.getId());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(RecommendationResponseUtil.recommendationResponseCompleteAttribute)));

        verify(recommendationResultRepository, times(1)).findByWarehouse(warehouse);
    }

    @After
    public void tearDown() throws Exception {
    }
}