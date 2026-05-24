package com.project.controller;

import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.project.service.CategoryService;
import com.project.service.ProductService;
@Controller
public class PageController {
@Autowired
private CategoryService cservice;
@Autowired
private ProductService pservice;
 @GetMapping("/shop")
 public ModelAndView shop()
 {
	 ModelAndView mv= new ModelAndView();
	 mv.addObject("categories",cservice.getAll());
	 mv.addObject("products",pservice.getAll());
	 mv.setViewName("shop");
	 
	 return mv;
 }
 @GetMapping("/shop/category/{id}")
 public ModelAndView shopByCategory(@PathVariable("id")int id)
 {
	 ModelAndView mv= new ModelAndView();
	 mv.addObject("categories",cservice.getAll());
	 mv.addObject("products",pservice.getprobycatid(id));
	 
 mv.setViewName("shop");
	 
	 return mv;
	
 }
 @GetMapping("/shop/viewproduct/{id}")
 public ModelAndView viewproduct(@PathVariable("id")int id)
 {
	 ModelAndView mv= new ModelAndView();
	 mv.addObject("product",pservice.fetchByid(id).get());
	 mv.setViewName("viewProduct");
	 return mv;
 
}
 @GetMapping("/userform")
 public String form()
 {
	 return "userform.html";
	 
 }
}
