package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Category;
import com.project.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
 private CategoryRepository crepo;
	
	public void saveCategory(Category c)
	{
		crepo.save(c);
	}
	public List<Category> getAll()
	{
		return crepo.findAll();
	}
	public void deletebyid(int id)
	{
		crepo.deleteById(id);
	}
	public Optional<Category> fetchbyid(int id)
	{
		return crepo.findById(id);
	}
}
