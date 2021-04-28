package com.biat.MyBoutique.repository;

import com.biat.MyBoutique.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findByCartCustomer_Id(Long id);
}
