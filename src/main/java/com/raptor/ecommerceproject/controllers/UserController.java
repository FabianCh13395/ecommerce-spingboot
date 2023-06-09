package com.raptor.ecommerceproject.controllers;

import com.raptor.ecommerceproject.models.Order;
import com.raptor.ecommerceproject.models.User;
import com.raptor.ecommerceproject.services.IOrderService;
import com.raptor.ecommerceproject.services.IUserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;


    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    @GetMapping("/register")
    public String createView(){
        return"User/registro";
    }

    @PostMapping("/save")
    public String saveUser(User user){
        user.setTypeUser("USER");
        logger.info("Datos del usuario registrado: {}",user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        return "redirect:/";
    }
    @GetMapping("/login")
    public String viewLogin(){
        return "User/login";
    }

    @GetMapping("/access")
    public String access(User user, HttpSession session){
        logger.info("Datos del usuario: {}",user);
        Optional<User> userOptional=userService.findById(Long.parseLong(session.getAttribute("idUser").toString()));
        //logger.info("Usuario email: {}",userOptional.get());
        if(userOptional.isPresent()){
                session.setAttribute("idUser",userOptional.get().getId());
                if(userOptional.get().getTypeUser().equals("ADMIN")){
                    return "redirect:/manager";
                }else{
                    return "redirect:/";
                }
        }else{
            logger.info("Usuario no existe por su email");
        }
        return "redirect:/";
    }

    //Método para mostrar las compras
    @GetMapping("/compras")
    public String purchasesUser(Model model, HttpSession session){
        model.addAttribute("sesion",session.getAttribute("idUser"));
        User user =userService.findById(Long.parseLong(session.getAttribute("idUser").toString())).get();
        List<Order> orders=orderService.findByUserOrder(user);
        model.addAttribute("orders",orders);
        return "User/compras";
    }

    //Detalles de la compra
    @GetMapping("/detalle/{id}")
    public String detailPurchase(@PathVariable Long id,HttpSession session,Model model){
        logger.info("Id de la orden: {}",id);
         Optional<Order>  order=orderService.findById(id);
         model.addAttribute("details",order.get().getDetails());
        //sesion
        model.addAttribute("sesion",session.getAttribute("idUser"));
        return "User/detallecompra";
    }

    //Cerra sesion
    @GetMapping("/cerrar")
    public String logout(HttpSession session){
        session.removeAttribute("idUser");
        return "redirect:/";
    }

}
