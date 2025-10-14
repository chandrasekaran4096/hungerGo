package com.tap.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tap.dao.OrderDAO;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.util.DBConnectionMenu;

public class OrderDAOImpl implements OrderDAO {

    // ✅ Table name should be 'orders'
    private String select = "SELECT * FROM orders";
    private String select1 = "SELECT * FROM orders WHERE user_id = ?";
    private String insert = "INSERT INTO orders (restaurant_id, user_id, order_date, total_amount, status, payment_mode) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();

        try (Connection con = DBConnectionMenu.getConnection();
             Statement stmt = con.createStatement()) {

            ResultSet res = stmt.executeQuery(select);

            while (res.next()) {
                int orderID = res.getInt("order_id");
                int resId = res.getInt("restaurant_id"); // ✅ corrected column name
                int userId = res.getInt("user_id");
                Timestamp orderDate = res.getTimestamp("order_date");
                double totalAmo = res.getDouble("total_amount");
                String status = res.getString("status");
                String payment = res.getString("payment_mode");

                Order order = new Order(orderID, resId, userId, orderDate, totalAmo, status, payment);
                list.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Order getAllOrder(int userId) {
        Order order = null;

        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(select1)) {

            pstmt.setInt(1, userId);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int orderID = res.getInt("order_id");
                int resId = res.getInt("restaurant_id"); // ✅ corrected column name
                Timestamp orderDate = res.getTimestamp("order_date");
                double totalAmo = res.getDouble("total_amount");
                String status = res.getString("status");
                String payment = res.getString("payment_mode");

                order = new Order(orderID, resId, orderDate, totalAmo, status, payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public int addOrder(Order order) {
        int orderId = 0;

        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            // ✅ Corrected column names
            pstmt.setInt(1, order.getRestaurantID());
            pstmt.setInt(2, order.getUserID());
            pstmt.setTimestamp(3, order.getOrderDate());
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setString(5, order.getStatus());
            pstmt.setString(6, order.getPamentMode());

            int i = pstmt.executeUpdate();
            System.out.println("Order Table Inserted Rows: " + i);

            ResultSet id = pstmt.getGeneratedKeys();
            if (id.next()) {
                orderId = id.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> userOrders = new ArrayList<>();

        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(select1)) {

            pstmt.setInt(1, userId);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int orderID = res.getInt("order_id");
                int resId = res.getInt("restaurant_id"); // ✅ corrected column name
                Timestamp orderDate = res.getTimestamp("order_date");
                double totalAmo = res.getDouble("total_amount");
                String status = res.getString("status");
                String payment = res.getString("payment_mode");

                Order order = new Order(orderID, resId, userId, orderDate, totalAmo, status, payment);

                // Fetching associated order items
                OrderItemDAOImpl itemDAO = new OrderItemDAOImpl();
                List<OrderItem> allOrderItem = itemDAO.getAllOrderItem(orderID);
                order.setItems(allOrderItem);

                userOrders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userOrders;
    }
}
