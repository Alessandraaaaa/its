package com.app.dao;

import com.app.model.Comment;
import com.app.model.Ticket;
import com.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsersDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getAll() {
        RowMapper<User> rowMapper = (rs, rowNumber) -> mapRow(rs);
        List<User> users = jdbcTemplate.query("SELECT u.id,u.login " +
                "FROM Users as u \n", rowMapper);
        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        return user;
    }
}
