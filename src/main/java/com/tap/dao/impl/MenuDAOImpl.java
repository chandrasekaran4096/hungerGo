package com.tap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.model.Menu;
import com.tap.util.DBConnectionMenu;

public class MenuDAOImpl {

    private String insert = "INSERT INTO `menu` (`restaurant_id`,`item_name`,`description`,`price`,`isavalible`,`ratings`,`imagepath`) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private String delete = "DELETE FROM `menu` WHERE `menu_id`=?";
    private String selectAllMenus = "SELECT * FROM menu";
    private String selectByRestaurant = "SELECT * FROM `menu` WHERE restaurant_id=?";
    private String selectById = "SELECT * FROM menu WHERE menu_id=?";

    // 1. Get all menus (no filter)
    public List<Menu> getAllMenus() {
        List<Menu> list = new ArrayList<>();
        try (Connection con = DBConnectionMenu.getConnection();
             Statement stmt = con.createStatement()) {

            ResultSet res = stmt.executeQuery(selectAllMenus);
            while (res.next()) {
                Menu menu = new Menu(
                    res.getInt("menu_id"),
                    res.getInt("restaurant_id"),
                    res.getString("item_name"),
                    res.getString("description"),
                    res.getDouble("price"),
                    res.getString("isavalible"),
                    res.getInt("ratings"),
                    res.getString("imagepath")
                );
                list.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Get all menus for a specific restaurant
    public List<Menu> getAllmenu(int restaurant_id) {
        List<Menu> list = new ArrayList<>();
        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(selectByRestaurant)) {

            pstmt.setInt(1, restaurant_id);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Menu menu = new Menu(
                    res.getInt("menu_id"),
                    restaurant_id,
                    res.getString("item_name"),
                    res.getString("description"),
                    res.getDouble("price"),
                    res.getString("isavalible"),
                    res.getInt("ratings"),
                    res.getString("imagepath")
                );
                list.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Add a new menu item
    public void addMenu(Menu menu) {
        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insert)) {

            pstmt.setInt(1, menu.getRestaurant_id());
            pstmt.setString(2, menu.getItem_name());
            pstmt.setString(3, menu.getDescription());
            pstmt.setDouble(4, menu.getPrice());
            pstmt.setString(5, menu.getIsavalible());
            pstmt.setInt(6, menu.getRatings());
            pstmt.setString(7, menu.getImagepath());

            int i = pstmt.executeUpdate();
            System.out.println("Inserted rows: " + i);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Delete a menu item by menu_id
    public void deleteMenu(int menu_id) {
        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(delete)) {

            pstmt.setInt(1, menu_id);
            int i = pstmt.executeUpdate();
            System.out.println("Deleted rows: " + i);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Get a specific menu by menu_id
    public Menu getMenu(int menu_id) {
        Menu menu = null;
        try (Connection con = DBConnectionMenu.getConnection();
             PreparedStatement pstmt = con.prepareStatement(selectById)) {

            pstmt.setInt(1, menu_id);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                menu = new Menu(
                    menu_id,
                    res.getInt("restaurant_id"),
                    res.getString("item_name"),
                    res.getString("description"),
                    res.getDouble("price"),
                    res.getString("isavalible"),
                    res.getInt("ratings"),
                    res.getString("imagepath")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    // 6. Optional: updateMenu() method can be added later
}
