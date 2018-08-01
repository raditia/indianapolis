package com.gdn.recommendation.implementation;

import com.gdn.entity.Warehouse;
import com.gdn.mapper.RecommendationResponseMapper;
import com.gdn.pickup.PickupService;
import com.gdn.repository.CffRepository;
import com.gdn.repository.RecommendationRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.WebResponse;
import com.gdn.util.RecommendationResultUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecommendationServiceImplTest {

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job fleetRecommendationJob;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllRecommendationFleetResult() {
        Warehouse warehouse = Warehouse.builder()
                .id("warehouse_cawang")
                .build();
        given(recommendationResultRepository.findByWarehouse(warehouse)).willReturn(RecommendationResultUtil.recommendationResultListCompleteAttribute);

        WebResponse<RecommendationResponse> expectedResponse = recommendationService.findAllRecommendationFleetResult(warehouse.getId());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(RecommendationResponseMapper.toRecommendationResponse(RecommendationResultUtil.recommendationResultListCompleteAttribute))));

        verify(recommendationResultRepository, times(1)).findByWarehouse(warehouse);
    }

    @After
    public void tearDown() throws Exception {
    }
}