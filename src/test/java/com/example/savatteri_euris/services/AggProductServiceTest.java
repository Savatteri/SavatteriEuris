package com.example.savatteri_euris.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.savatteri_euris.exceptions.EventCodeException;
import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.AggOrdersRepo;
import com.example.savatteri_euris.models.repos.AggProductRepo;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;

@ActiveProfiles("test")
@Getter
@SpringBootTest
public class AggProductServiceTest {
	
	@Autowired
	AggProductRepo aggProductRepo;

    @BeforeEach
    private void deleteAll() {
    	aggProductRepo.deleteAll();
    }
    
    
}
