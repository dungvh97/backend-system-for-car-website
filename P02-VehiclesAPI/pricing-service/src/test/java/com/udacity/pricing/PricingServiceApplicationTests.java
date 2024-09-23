package com.udacity.pricing;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

    
	@Test
	public void contextLoads() {
	}

    @Test
    public void testGetRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/services/price?vehicleId=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
