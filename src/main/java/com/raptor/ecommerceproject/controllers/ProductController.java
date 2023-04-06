package com.raptor.ecommerceproject.controllers;

import com.raptor.ecommerceproject.models.Product;
import com.raptor.ecommerceproject.models.User;
import com.raptor.ecommerceproject.services.ProductService;
import com.raptor.ecommerceproject.services.UploadFileService;
import com.raptor.ecommerceproject.services.IUserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;



@Controller
@RequestMapping("/products")
public class ProductController {
    private final Logger LOGGER= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public String homeProduct(Model model){
        model.addAttribute("productos",productService.findAll());
        return "products/show";
    }

    @GetMapping("/create")
    public String viewCreate(){
        return "products/create";
    }

    @PostMapping("/save")
    public String saveProduct(Product product, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto {}",product);

        User user=userService.findById(Long.parseLong(session.getAttribute("idUser").toString())).get();
        product.setUser(user);
        //Guardar Imagen
        if(product.getId()==null){ //Cuando se crea un producto
            String nameImage=uploadFileService.saveImage(file);
            product.setImage(nameImage);

        }
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,Model model){
        Product product=new Product();
        Optional <Product> optionalProduct=productService.get(id);
        product=optionalProduct.get();
        //LOGGER.info("Producto Buscado {}",product);
        model.addAttribute("producto",product);
        return "products/edit";
    }

    @PostMapping("/update")
    public String updateProduct(Product product,@RequestParam("img") MultipartFile file) throws IOException {
        Product p=new Product();
        p=productService.get(product.getId()).get();
        if(file.isEmpty()){//Cuando se edita el producto, pero no se cambia la imagen

            product.setImage(p.getImage());
        }else{//Cuando se edita la imagen
            //Eliminar una imagen cuando no es por la de defecto
            if(!p.getImage().equals("default.jpg")){
                uploadFileService.deleteImage(p.getImage());
            }
            String nameImage=uploadFileService.saveImage(file);
            product.setImage(nameImage);
        }
        product.setUser(p.getUser());
        productService.update(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Product product=new Product();
        product=productService.get(id).get();
        //Eliminar una imagen cuando no se por la defecto
        if(!product.getImage().equals("default.jpg")){
            uploadFileService.deleteImage(product.getImage());
        }
        productService.delete(id);
        return "redirect:/products";
    }


}
