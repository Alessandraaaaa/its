package com.app.services;

import com.app.dao.TicketsDao;
import com.app.dao.UsersDao;
import com.app.model.Ticket;
import com.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersDao usersDao;

    public List<User> getAll(){
        return usersDao.getAll();
    }
}
