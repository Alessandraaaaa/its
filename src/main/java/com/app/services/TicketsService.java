package com.app.services;

import com.app.dao.TicketsDao;
import com.app.model.Comment;
import com.app.model.Ticket;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketsService {

    @Autowired
    private TicketsDao ticketsDao;

    public Boolean validateTicket(Ticket ticket){
        if (ticket == null || Strings.isBlank(ticket.getReporter().getLogin())) {
            return false;
        }

        if (!isValidString(ticket.getSubject(), 50, true)) {
            return false;
        }
        if (!isValidString(ticket.getDescription(), 250, false)) {
            return false;
        }

        if (!isValidString(ticket.getDescription(), 250, false)) {
            return false;
        }

        if (!isValidString(ticket.getProject().getDescription(), 50, true)) {
            return false;
        }

        //TODO validate status
        try{
            Integer.valueOf(ticket.getStatus().getStatus());
        }catch (Exception ex){
            return false;
        }

        if (ticket.getComments() != null){
            for (Comment comment : ticket.getComments()){
                if (!isValidString(comment.getText(), 500, false)) {
                    return false;
                }
            }
        }


        return true;
    }

    public Boolean createTicket(Ticket ticket){
        return ticketsDao.createTicket(ticket);
    }

    public Ticket getTicket(int id){
        return ticketsDao.getTicket(id);
    }

    public Ticket updateTicket(Ticket ticket){
        return ticketsDao.updateTicket(ticket);
    }

    public Boolean updateAssignee(int id, int assigneeId){
        return ticketsDao.updateAssignee(id, assigneeId);
    }

    private boolean isValidString(String text, int size, Boolean mandatory) {
        if (mandatory && Strings.isBlank(text)){
            return false;
        }
        if (!Strings.isBlank(text) && text.length() > size){
            return false;
        }
        return true;
    }
    public List<Ticket> getAllTickets(){
        return ticketsDao.getAllTickets();
    }
}

