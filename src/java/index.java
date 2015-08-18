/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp
 */
@ManagedBean
@SessionScoped

public class index implements Serializable {

    private int login_id;
    private String login_psw;
    private customer aCustomer = null;
    private seller aSeller = null;
    private admin anAdmin = null;
    private String errorMessage;
    private ArrayList<item> featuredItems = new ArrayList<>();
    private item currentItem;
    private Integer id = null;
    private String name;

    public String login() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        login_id = id;

        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, "dingorkarj2620", "1289968");
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from item where i_featured = 1 and i_status = 'Valid'");
            featuredItems.clear();
            while (rs.next()) {
                featuredItems.add(new item(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getBoolean(11), rs.getString(12), rs.getString(13)));
            }

            rs = statement.executeQuery("select * from account where a_id = " + login_id);

            if (rs.next()) {
                //ID IS FOUND
                if (rs.getString(2).equals(login_psw)) {
                    //PASSWORD IS FOUND
                    if (rs.getString(3).equalsIgnoreCase("customer")) {
                        //IS CUSTOMER
                        aCustomer = new customer(login_id, login_psw, "customer", rs.getString(4));
                        name = rs.getString(4);
                        return "customer_home.xhtml";
                    } else {
                        //NOT A CUSTOMER
                        if (rs.getString(3).equalsIgnoreCase("regular")) {
                            //IS REGULAR SELLER
                            aSeller = new seller(login_id, login_psw, "regular", rs.getString(4));
                            name = rs.getString(4);
                            return "seller_home";
                        } else {
                            //NOT A CUSTOMER OR REGULAR SELLER
                            if (rs.getString(3).equalsIgnoreCase("premium")) {
                                //IS PREMIUM SELLER
                                aSeller = new seller(login_id, login_psw, "premium", rs.getString(4));
                                name = rs.getString(4);
                                return "seller_home";
                            } else {
                                //NOT A CUSTOMER, REGULAR OR PREMIUM SELLER
                                if (rs.getString(3).equalsIgnoreCase("admin")) {
                                    //IS ADMIN
                                    anAdmin = new admin(login_id, login_psw, "admin", rs.getString(4));
                                    name = rs.getString(4);
                                    return "admin_home";
                                }
                            }
                        }
                    }
                } else {
                    //PASSWORD IS INCORRECT
                    errorMessage = "Password is not correct!";
                    return "error";
                }

            } else {
                //ID NOT FOUND
                errorMessage = "ID is not correct!";
                return "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Internal Error: 800";
            return "error";
        } finally {
            try {
                connection.close();
                statement.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "Internal Error: 900";
                return "error";
            }
        }
        errorMessage = "Internal Error";
        return "error";
    }

    public String logout() {
        aCustomer = null;
        aSeller = null;
        anAdmin = null;
        login_id = 0;

        return null;
    }

    public String viewCurrentItem(item i) {
        currentItem = i;
        return "item_page.xhtml";
    }

    public String home() {
        if (aCustomer != null) {
            return "customer_home.xhtml";
        } else {
            if (aSeller != null) {
                return "seller_home.xhtml";
            } else {
                if (anAdmin != null) {
                    return "admin_home.xhtml";
                } else {
                    return "error.xhtml";
                }
            }
        }
    }

    public void viewByCategory() {
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getLogin_psw() {
        return login_psw;
    }

    public void setLogin_psw(String login_psw) {
        this.login_psw = login_psw;
    }

    public customer getaCustomer() {
        return aCustomer;
    }

    public void setaCustomer(customer aCustomer) {
        this.aCustomer = aCustomer;
    }

    public seller getaSeller() {
        return aSeller;
    }

    public void setaSeller(seller aSeller) {
        this.aSeller = aSeller;
    }

    public admin getAnAdmin() {
        return anAdmin;
    }

    public void setAnAdmin(admin anAdmin) {
        this.anAdmin = anAdmin;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<item> getFeaturedItems() {
        return featuredItems;
    }

    public void setFeaturedItems(ArrayList<item> featuredItems) {
        this.featuredItems = featuredItems;
    }

    public item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(item currentItem) {
        this.currentItem = currentItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
