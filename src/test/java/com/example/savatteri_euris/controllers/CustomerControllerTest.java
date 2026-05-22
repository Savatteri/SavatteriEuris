package com.example.savatteri_euris.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.savatteri_euris.models.dtos.CustomerDto;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.http.MediaType;

@ActiveProfiles("test")
@Getter
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	
	@Autowired
    private MockMvc mockMvc;

	@MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldInsertCustomerWhenValid() throws Exception {

    	CustomerDto customer = new CustomerDto();
        customer.setName("Giuseppe Savatteri");
        customer.setCodiceFiscale("SVTGPP90R28H456M");

        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/customer/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().string("insert complete"));


        verify(customerService, times(1)).save(any(Customer.class));
    }
}
