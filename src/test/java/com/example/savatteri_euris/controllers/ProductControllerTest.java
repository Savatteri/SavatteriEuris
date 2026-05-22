package com.example.savatteri_euris.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.savatteri_euris.models.dtos.ProductDto;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.services.CustomerService;
import com.example.savatteri_euris.services.ProductService;
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
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void deleteAll() {
    	getProductService().deleteAll();
    }
    
    @Test
    void shoundInsertWhenProductValid() throws Exception {

    	ProductDto product = new ProductDto();
    	product.setBaseStock(10);
    	product.setName("dentifricio");

        String productJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/product/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().string("insert complete"));

        
    }
    
    @Test
    void shoundInsertWhenProductInvalid() throws Exception {

    	ProductDto product = new ProductDto();
    	product.setBaseStock(null);
    	product.setName("dentifricio");

        String productJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/product/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest());
    }
}
