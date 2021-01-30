package com.app.services;

import com.app.model.Ticket;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
public class TicketsService {

    public Boolean validateTicket(Ticket ticket){
        if (ticket == null || Strings.isBlank(ticket.getReporter())) {
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

        if (!isValidString(ticket.getProject(), 50, true)) {
            return false;
        }

        //TODO validate status

        if (ticket.getComments() != null){
            for (String comment : ticket.getComments()){
                if (!isValidString(comment, 500, false)) {
                    return false;
                }
            }
        }


        return false;
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
}
