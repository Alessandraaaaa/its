package com.app.dao;

import com.app.model.*;
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
        int reporterId = createUser(ticket.getReporter().getLogin());
        int assigneeId = createUser(ticket.getAssignee().getLogin());
        jdbcTemplate.update("INSERT INTO tickets (project_id, reporter_id, subject, description, assignee_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                projectId, reporterId, ticket.getSubject(), ticket.getDescription(), assigneeId, ticket.getStatus().getId());
        return true;
    }

    public Ticket getTicket(int id) {
        RowMapper<Ticket> rowMapper = (rs, rowNumber) -> mapTicket(rs);
        List<Ticket> tickets = jdbcTemplate.query("SELECT t.id,t.subject,t.description, s.status, s.id as statusId," +
                "p.description as project, p.id as projectId, a.login as assignee, a.id as assigneeId, r.login as reporter," +
                " r.id as reporterId " +
                "FROM Tickets as t \n" +
                "LEFT JOIN status as s on s.id=t.status\n" +
                "LEFT JOIN projects as p on  p.id=t.project_id\n" +
                "LEFT JOIN users as r on r.id = t.reporter_id\n" +
                "LEFT JOIN users as a on a.id = t.assignee_id\n" +
                "WHERE t.id = " + id, rowMapper);
        if (tickets.size() > 0 ) {
            tickets.get(0).setComments(getTicketComments(id));
            return tickets.get(0);
        }
        return null;

    }

    private List<Comment> getTicketComments(int id) {
        RowMapper<Comment> rowMapper = (rs, rowNumber) -> mapComment(rs, id);
        List<Comment> tickets = jdbcTemplate.query("SELECT c.id, c.text " +
                "FROM Comments as c \n" +
                "WHERE c.ticket_id = " + id, rowMapper);
        return tickets;
    }

    private Comment mapComment(ResultSet rs, int ticketId) throws SQLException {
        Comment comment = new Comment();
        comment.setTicketId(ticketId);
        comment.setId(rs.getInt("id"));
        comment.setText(rs.getString("text"));

        return comment;
    }

    private Ticket mapTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setSubject(rs.getString("subject"));
        ticket.setDescription(rs.getString("description"));
        ticket.setAssignee(new User());
        ticket.getAssignee().setLogin(rs.getString("assignee"));
        ticket.getAssignee().setId(rs.getInt("assigneeId"));
        ticket.setReporter(new User());
        ticket.getReporter().setLogin(rs.getString("reporter"));
        ticket.getReporter().setId(rs.getInt("reporterid"));
        ticket.setStatus(new Status());
        ticket.getStatus().setId(rs.getInt("statusId"));
        ticket.getStatus().setStatus(rs.getString("status"));
        ticket.setProject(new Project());
        ticket.getProject().setDescription(rs.getString("project"));
        ticket.getProject().setId(rs.getInt("projectId"));
        return ticket;
    }

    public Ticket updateTicket(Ticket ticket){
        String sql = "UPDATE  Tickets " +
                "SET subject =  ?, description = ? " +
                "WHERE id = ? ";
        jdbcTemplate.update(sql, ticket.getSubject(), ticket.getDescription(), ticket.getId());
        updateUser(ticket.getAssignee());
        updateUser(ticket.getReporter());
        updateStatus(ticket.getStatus());
        for (Comment comment : ticket.getComments()){
            if (comment.getId() > 0 ){
                updateComment(comment);
            } else {
                comment.setTicketId(ticket.getId());
                createComment(comment);
            }

        }
        return ticket;
    }

    private void createComment(Comment comment) {
        String sql = "INSERT INTO Comments (ticket_id, text) " +
                "VALUES (?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(2, comment.getText());
                ps.setInt(1, comment.getTicketId());
                return ps;
            }
        }, holder);
        comment.setId((int)holder.getKeys().get("id"));
    }

    private void updateComment(Comment comment) {
        String sql = "UPDATE Comments " +
                "SET text =  ? " +
                "WHERE id = ? ";
        jdbcTemplate.update(sql, comment.getText(), comment.getId());
    }

    private void updateStatus(Status status) {
        String sql = "UPDATE Status " +
                "SET status =  ? " +
                "WHERE id = ? ";
        jdbcTemplate.update(sql, status.getStatus(), status.getId());
    }

    private void updateUser(User user) {
        String sql = "UPDATE Users " +
                "SET login =  ? " +
                "WHERE id = ? ";
        jdbcTemplate.update(sql, user.getLogin(), user.getId());
    }

    public int createProject(Ticket ticket){
        String sql = "INSERT INTO projects (description) " +
                        "VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, ticket.getProject().getDescription());
                return ps;
            }
        }, holder);
        ticket.getProject().setId((int)holder.getKeys().get("id"));
        return ticket.getProject().getId();
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

