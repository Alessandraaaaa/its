package com.app.controllers;

import com.app.model.Ticket;
import com.app.services.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("ticket")
public class TicketsController {

    @Autowired
    TicketsService ticketsService;

    @GetMapping("/registration")
    public String getRegistration(Model model){
        model.addAttribute("ticket", new Ticket());
        return "registration";
    }

    @GetMapping("/{id}")
    public String getTicket(Model model, @PathVariable("id")int id){
        model.addAttribute("ticket", ticketsService.getTicket(id));
        return "ticket";
    }

    @PostMapping("/update")
    public String updateTicket(Ticket ticket, Model model){
        model.addAttribute("ticket", ticketsService.updateTicket(ticket));
        return "ticket";
    }

    @PostMapping("/create")
    public String createTicket(Ticket ticket, Model model){
        //localhost:8090/ticket/create
        if(ticketsService.validateTicket(ticket)){
            ticketsService.createTicket(ticket);
            model.addAttribute("created", true);
        } else {
            model.addAttribute("notValid", true);
        }
        return "registration";
    }


}
