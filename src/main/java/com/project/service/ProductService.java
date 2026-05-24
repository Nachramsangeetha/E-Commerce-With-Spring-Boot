package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Product;
import com.project.repository.ProductRepository;

@Service
public class ProductService {
 @Autowired
	private ProductRepository prepo;
 public void saveproduct(Product p)
 {
	 prepo.save(p);
 }
 public List<Product> getAll()
 {
	 return prepo.findAll();
 }
 public void deltebyid(long id)
 {
	 prepo.deleteById(id);
 }
 public Optional<Product> fetchByid(long id)
 {
	 return prepo.findById(id);
 }
 public List<Product> getprobycatid(int id)
 {
	 return prepo.findAllByCategoryId(id);
 }
}
