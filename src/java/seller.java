/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp
 */
@ManagedBean
@SessionScoped
public class seller extends account implements Serializable {

    /**
     * Creates a new instance of seller
     */
    //variable of item type
    item i = new item();

    //get and set methods
    public item getI() {
        return i;
    }

    public void setI(item i) {
        this.i = i;
    }

    //constructor
    public seller(int id, String psw, String type, String name) {

        super(id, psw, type, name);
    }

    //method to enter data in item table
    public String insert() {
        
        //loading the driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return "Internal Error";
        }

        //database variables
        final String db = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {

            //connecting database
            con = DriverManager.getConnection(db, "dingorkarj2620", "1289968");
            st = con.createStatement();

            rs = st.executeQuery("select * from nextitemnumber");
            if (rs.next()) {
                i.setI_id(rs.getInt(1));
            }

            //calling method to update status of the item
            updateStatus();

            //insert data into item
            st.executeUpdate("insert into item values (" + i.getI_id() + "," + super.id + ", '" + i.getI_type() + "','" + i.getI_title() + "','" + i.getI_description() + "'," + i.getI_price() + "," + i.getI_min() + "," + i.getI_max() + ",0,'" + i.getI_status() + "'," + i.isI_featured() + ", '" + i.getI_category() + "', '" + i.getI_img() + "')");
            st.executeUpdate("update nextitemnumber set next_i_id = " + (i.getI_id() + 1));

            return "insert_successful.xhtml";

        } catch (SQLException e) {
            e.printStackTrace();
            return "insert_fail.xhtml";

        } finally {

            try {
                con.close();
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return "insert_fail.xhtml";
            }
        }
    }

    //method to update status
    public void updateStatus() {

        //loading the driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //database variables
        final String db = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            //connecting database
            con = DriverManager.getConnection(db, "dingorkarj2620", "1289968");
            st = con.createStatement();
            rs = st.executeQuery("select * from account where a_id = " + super.id);

            //updating the status
            if (rs.next()) {
                //premium seller
                if (rs.getString(3).equalsIgnoreCase("premium")) {
                    //update the item status
                    i.setI_status("Valid");

                } else {
                    //regular seller
                    if (rs.getString(3).equalsIgnoreCase("regular")) {
                        //checking price lower or higher than 100
                        if (i.getI_price() <= 100) {
                            //update status valid
                            i.setI_status("Valid");
                        } else {
                            //update status pending
                            i.setI_status("Invalid");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String clearInsert(){
        
        i=null;
        return "seller_home.xhtml";
        
    }
}
