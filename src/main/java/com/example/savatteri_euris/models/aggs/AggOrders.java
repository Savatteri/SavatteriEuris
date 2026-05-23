package com.example.savatteri_euris.models.aggs;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
public class AggOrders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date lastStatusDate;
	private String status;
    private String eventCode;
    private Integer quantity;
    
    private Long customerId;
    private String customerName;
    private String customerFamilyName;
    
    private long productId;
    private String productCode;
    private String productName;
    

	
}
