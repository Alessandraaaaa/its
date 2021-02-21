package com.app.controllers;

import com.app.services.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;


@RestController
@RequestMapping("api")
public class RestTicketController {

    @Autowired
    TicketsService ticketsService;

    @GetMapping("/tickets")
    public String getTicket(Model model){
        return new Gson().toJson(ticketsService.getAllTickets() );
    }
}
