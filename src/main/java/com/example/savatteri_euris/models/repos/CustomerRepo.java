package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.facts.Customer;


public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
