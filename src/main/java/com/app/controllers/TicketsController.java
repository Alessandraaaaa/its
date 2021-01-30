package com.app.controllers;

import com.app.model.Ticket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("ticket")
public class TicketsController {

    @GetMapping("/registration")
    public String getTicket(Model model){
        model.addAttribute("ticket", new Ticket());
        return "registration";
    }

    @PostMapping("/create")
    public String createTicket(Ticket ticket, Model model){
        //localhost:8090/ticket/create

        return "registration";
    }


}
