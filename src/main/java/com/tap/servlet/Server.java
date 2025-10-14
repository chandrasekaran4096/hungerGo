package com.tap.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import com.tap.dao.impl.UserDAOimpl;
import com.tap.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LoginServlet")
public class Server extends HttpServlet {

    private String dbPassword = "root";
    private String dbUsername = "root";
    private String dbUrl = "jdbc:mysql://localhost:3306/food_delivery";
    private String select = "SELECT password FROM user WHERE username = ?";

    private int count = 3;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("username");
        String inputPassword = req.getParameter("password");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        String storedPassword = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement pstmt = con.prepareStatement(select);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                storedPassword = rs.getString("password");
            } else {
                out.println("❌ Username not found!");
                RequestDispatcher rd = req.getRequestDispatcher("login.html");
                rd.include(req, resp);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Database error occurred.");
            return;
        }

        if (storedPassword.equals(inputPassword)) {
            HttpSession session = req.getSession();

            UserDAOimpl userDAO = new UserDAOimpl();
            User user = userDAO.getUser(name);

            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("user_id", user.getUserID());
                System.out.println("✅ User in session: " + user);
                resp.sendRedirect("checkout.jsp");
            } else {
                out.println("❌ User object not found in DAO!");
                RequestDispatcher rd = req.getRequestDispatcher("login.html");
                rd.include(req, resp);
            }

        } else if (count > 0) {
            out.println("❌ Invalid password! You have " + count + " attempts left.");
            RequestDispatcher rd = req.getRequestDispatcher("login.html");
            rd.include(req, resp);
            count--;
        } else {
            out.println("❌ No more attempts. Please try again later.");
        }
    }
}
