package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByCategoryId(int id);

}
