package com.app.dao;

import com.app.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class TicketsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean createTicket(Ticket ticket){
        int projectId = createProject(ticket);
        int reporterId = createUser(ticket.getReporter());
        int assigneeId = createUser(ticket.getAssignee());
        jdbcTemplate.update("INSERT INTO tickets (project_id, reporter_id, subject, description, assignee_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                projectId, reporterId, ticket.getSubject(), ticket.getDescription(), assigneeId, Integer.valueOf(ticket.getStatus()));
        return true;
    }

    public int createProject(Ticket ticket){
        String sql = "INSERT INTO projects (description) " +
                        "VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, ticket.getProject());
                return ps;
            }
        }, holder);

        return (int)holder.getKeys().get("id");
    }

    public int createUser(String name){
        String sql = "INSERT INTO users (login) " +
                "VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                return ps;
            }
        }, holder);

        return (int)holder.getKeys().get("id");
    }
}

