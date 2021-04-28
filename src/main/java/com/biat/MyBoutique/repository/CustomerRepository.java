package com.biat.MyBoutique.repository;

import com.biat.MyBoutique.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByEnabled(Boolean enabled);

}
