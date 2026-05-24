package com.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.project.dto.ProductDTO;
import com.project.entity.Admin;
import com.project.entity.Category;
import com.project.entity.Product;
import com.project.repository.AdminRepository;
import com.project.service.AdminService;
import com.project.service.CategoryService;
import com.project.service.ProductService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
	@Autowired
	 private CategoryService cservice;
	 @Autowired
	 private ProductService pservice;
	 @Autowired
	 private AdminService aservice;
	 public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
	@GetMapping("/admin")
	public String admin()
	{
		return "register";
	}
	@RequestMapping("/register")
	public String register(String email, String password)
	{
		if(email!=null && password!=null)
		{
			Admin a =new Admin();
			a.setEmail(email);
			a.setPassword(password);
			aservice.save(a);
			return "redirect:/register";
		}
		
		return "register";
	}
	@GetMapping("/login")
	public String login()
	{
		return "login";
	}
   @PostMapping("/login")
   public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password)
   {
	   ModelAndView mv=new ModelAndView();
	   List<Admin> list= aservice.fetchAll();
	   for(Admin a:list)
	   {
		   if(a.getEmail().equals(email) && a.getPassword().equals(password))
		   {
			   mv.setViewName("admin");
			   mv.addObject("userobject",a);
			   return mv;
		   }
	   }
	   mv.setViewName("/login");
	   return mv;
   }
    @GetMapping("/admin/categories")
    public ModelAndView categorypage()
    {
    	List<Category> list = cservice.getAll();
    	ModelAndView mav= new ModelAndView("categories","categories", list);
    	return mav;
    }
    @GetMapping("/admin/categories/add")
    public ModelAndView addcategory()
    {
    	Category c =new Category();
    	ModelAndView mav= new ModelAndView("categoriesAdd","category",c);
    	return mav;
    }
    @PostMapping("/admin/categories/add")
    public String postAddCategory(@ModelAttribute("category")Category c)
    {
    	cservice.saveCategory(c);
    	return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String delteCategory(@PathVariable("id")int id)
    {
    	cservice.deletebyid(id);
    	return  "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public ModelAndView updateCategory(@PathVariable("id") int id)
    {
    	ModelAndView mv=new ModelAndView();
    	Optional<Category> category =cservice.fetchbyid(id);
    	if(category.isPresent())
    	{
    		mv.addObject("category",category.get());
    		mv.setViewName("categoriesAdd");
    	
    }
    	else
    	{
    		mv.setViewName("error");
    	}
    	return mv;
}
    @GetMapping("/admin/products")
    public ModelAndView productpage()
    {
    	ModelAndView mv=new ModelAndView();
    	List<Product> list =pservice.getAll();
    	mv.addObject("products",list);
    	mv.setViewName("products");
    	 return mv;
    }
    @GetMapping("/admin/products/add")
   public ModelAndView AddProduct()
   {
    	ModelAndView mv= new ModelAndView();
    	ProductDTO p =new ProductDTO();
    	mv.addObject("productDTO",p);
    	mv.addObject("categories",cservice.getAll());
    	
    	mv.setViewName("productsAdd");
    	return mv;
    	
   }
   
   
    @PostMapping("/admin/products/add")
    public ModelAndView postAddProduct(HttpServletRequest request, 
                                       @RequestParam("productImage") MultipartFile file,
                                       @RequestParam("imgName") String imgName) 
    {
        Product pro = new Product();
        
        // 1. Manually pull the fields from the request map
        // If id is empty (new product), default it to 0
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            pro.setId(Long.parseLong(idParam));
        }
        
        pro.setName(request.getParameter("name"));
        pro.setPrice(Double.parseDouble(request.getParameter("price")));
        pro.setDescription(request.getParameter("description"));
        pro.setWeight(Double.parseDouble(request.getParameter("weight")));
        
        // Get categoryId string, parse it, and fetch the category entity
        int catId = Integer.parseInt(request.getParameter("categoryId"));
        pro.setCategory(cservice.fetchbyid(catId).get());
        
        // 2. Handle file upload processing
        String imageUUID;
        if (!file.isEmpty()) 
        {
            imageUUID = file.getOriginalFilename();
            Path path = Paths.get(uploadDir, imageUUID);
            try {
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        else 
        {
            imageUUID = imgName;
        }
        pro.setImageName(imageUUID);
        
        // 3. Save to database
        pservice.saveproduct(pro); 
        
        // 4. Redirect using ModelAndView
        return new ModelAndView("redirect:/admin/products");
    }
    @GetMapping("/admin/product/delete/{id}")
    public String deleteproduct(@PathVariable("id") long id)
    {
    	pservice.deltebyid(id);
    	return  "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public ModelAndView updateProduct(@PathVariable("id") long id) {
        // 1. Fetch the data
        Product pro = pservice.fetchByid(id).get();
        ProductDTO pdt = new ProductDTO();
        
        // 2. Map the properties efficiently
        BeanUtils.copyProperties(pro, pdt);
        if (pro.getCategory() != null) {
            pdt.setCategoryId(pro.getCategory().getId());
        }
        
        // 3. Initialize ModelAndView with the view name "productsAdd"
        ModelAndView modelAndView = new ModelAndView("productsAdd");
        
        // 4. Add objects directly to the ModelAndView container
        modelAndView.addObject("categories", cservice.getAll());
        modelAndView.addObject("productDTO", pdt);
        
        return modelAndView;
    }
}
