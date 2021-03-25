package com.app.controllers;

import com.app.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    UsersService usersService;

    @GetMapping("/{ticketId}")
    public String getAllForTicket(Model model, @PathVariable("ticketId")int ticketId){
        model.addAttribute("users", usersService.getAll());
        model.addAttribute("ticketId", ticketId);
        return "users";
    }
}
