package com.tap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.tap.dao.UserDAO;
import com.tap.model.User;
import com.tap.util.DBConnection;

public class UserDAOimpl implements UserDAO {

    // SQL queries
    private String insert = "INSERT INTO `user` (`userID`, `name`, `username`, `password`, `email`, `phone_number`, `address`, `role`, `create_date`, `last_login_date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String update = "UPDATE `user` SET `name` = ?, `username` = ?, `password` = ?, `email` = ?, `phone_number` = ?, `address` = ? WHERE `userID` = ?";
    private String select = "SELECT * FROM `user` WHERE `username` = ?";  // ✅ FIXED: Changed from name=? to username=?
    public String delete = "DELETE FROM `user` WHERE `userID` = ?";

    @Override
    public List<User> getAllUsers() {
        // Not implemented
        return null;
    }

    @Override
    public User getUser(String user) {
        User u = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(select)) {

            pstmt.setString(1, user); // Set username instead of name
            ResultSet result = pstmt.executeQuery();

            if (result.next()) { // ✅ Use 'if' since username should be unique
                int userid = result.getInt("userID");
                String name = result.getString("name");
                String username = result.getString("username");
                String pass = result.getString("password");
                String email = result.getString("email");
                String phone = result.getString("phone_number");
                String address = result.getString("address");
                String role = result.getString("role");
                Timestamp createdate = result.getTimestamp("create_date");
                Timestamp lastlogindate = result.getTimestamp("last_login_date");

                // ✅ Store actual timestamps
                u = new User(userid, name, username, pass, email, phone, address, role, createdate, lastlogindate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u; // Will return null if not found (which is expected behavior)
    }

    @Override
    public void addUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insert)) {

            pstmt.setInt(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone_number());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, user.getRole());
            pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // Current time for created
            pstmt.setTimestamp(10, new Timestamp(System.currentTimeMillis())); // Current time for login

            int i = pstmt.executeUpdate();
            System.out.println(i + " user(s) added");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(update)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone_number());
            pstmt.setString(6, user.getAddress());
            pstmt.setInt(7, user.getUserID());

            int i = pstmt.executeUpdate();
            System.out.println(i + " user(s) updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(delete)) {

            pstmt.setInt(1, id);
            int i = pstmt.executeUpdate();
            System.out.println(i + " user(s) deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
