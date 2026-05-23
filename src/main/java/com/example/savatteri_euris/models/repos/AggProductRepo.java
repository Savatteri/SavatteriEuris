package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;


public interface AggProductRepo extends JpaRepository<AggProduct, Long> {
	public AggProduct findOneById(Long id);
	public AggProduct findOneByCode(String code);
	
	@Modifying
	@Query("UPDATE AggProduct a set a.stock = a.stock - :quantity where a.id = :id AND a.stock >= :quantity")
	int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);
	
	@Modifying
	@Query("UPDATE AggProduct a set a.stock = a.stock + :quantity where a.id = :id")
	int increaseStock(@Param("id") Long id, @Param("quantity") int quantity);

}
