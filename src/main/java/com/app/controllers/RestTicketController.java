package com.app.controllers;

import com.app.model.Ticket;
import com.app.services.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/tickets/{id}")
    public String getTicket(Model model, @PathVariable("id")int id){
        return new Gson().toJson(ticketsService.getTicket(id) );
    }

    @PostMapping("/tickets/{id}/change_assignee/{assigneeId}")
    public String updateAssignee( Model model, @PathVariable("id")int id, @PathVariable("assigneeId")int assigneeId){
        Ticket ticket = ticketsService.getTicket(id);
        return new Gson().toJson(ticketsService.updateAssignee(id, assigneeId));
    }
}
