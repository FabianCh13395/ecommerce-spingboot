package com.raptor.ecommerceproject.controllers;

import com.raptor.ecommerceproject.models.Product;
import com.raptor.ecommerceproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String home(Model model){
        List<Product> productList=productService.findAll();
        model.addAttribute("products",productList);
        return "User/home";
    }


}