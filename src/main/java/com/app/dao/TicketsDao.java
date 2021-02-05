package com.app.dao;

import com.app.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

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

    public Ticket getTicket(int id) {
        RowMapper<Ticket> rowMapper = (rs, rowNumber) -> mapTicket(rs);
        List<Ticket> tickets = jdbcTemplate.query("SELECT t.id,t.subject,t.description, s.status, " +
                "p.description as project, a.login as assignee, r.login as reporter " +
                "FROM Tickets as t \n" +
                "LEFT JOIN status as s on s.id=t.status\n" +
                "LEFT JOIN projects as p on  p.id=t.project_id\n" +
                "LEFT JOIN users as r on r.id = t.reporter_id\n" +
                "LEFT JOIN users as a on r.id = t.assignee_id\n" +
                "WHERE t.id = " + id, rowMapper);
        return tickets.size() > 0 ? tickets.get(0) : null;

    }

    private Ticket mapTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setSubject(rs.getString("subject"));
        ticket.setDescription(rs.getString("description"));
        ticket.setAssignee(rs.getString("assignee"));
        ticket.setReporter(rs.getString("reporter"));
        ticket.setStatus(rs.getString("status"));
        ticket.setProject(rs.getString("project"));
        return ticket;
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

