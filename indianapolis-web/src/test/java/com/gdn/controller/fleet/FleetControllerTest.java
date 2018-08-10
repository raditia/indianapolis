package com.gdn.controller.fleet;

import com.gdn.FleetResponseUtil;
import com.gdn.FleetUtil;
import com.gdn.fleet.FleetService;
import com.gdn.response.WebResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(FleetController.class)
public class FleetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FleetService fleetService;

    @Test
    public void findAllFleetDistinctOrderByCbmCapacityDesc_OK() throws Exception {
        given(fleetService.findDistinctAllFleetOrderByCbmCapacityDesc())
                .willReturn(WebResponse.OK(FleetResponseUtil.descendingFleetResponseListCompleteAttribute));
        mockMvc.perform(get("/api/fleet")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].name", equalTo(FleetUtil.fleetVanCompleteAttribute.getName())))
                .andExpect(jsonPath("$.data[1].name", equalTo(FleetUtil.fleetMotorCompleteAttribute.getName())));
        verify(fleetService, times(1)).findDistinctAllFleetOrderByCbmCapacityDesc();
    }
}