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
public class admin extends account implements Serializable {

    /**
     * Creates a new instance of admin
     */
    ArrayList<item> pendingLisitngs = new ArrayList<>();
    ArrayList<Integer> checkedItemID = new ArrayList<>();
    String errorMessage;

    public admin(int id, String psw, String type, String name) {
        super(id, psw, type, name);
        refreshLists();
    }

    private void refreshLists() {
        pendingLisitngs.clear();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, "dingorkarj2620", "1289968");
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from item where i_status = 'Invalid'");

            while (rs.next()) {
                pendingLisitngs.add(new item(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getBoolean(11), rs.getString(12), rs.getString(13)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String approveItems() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL, "dingorkarj2620", "1289968");
            statement = connection.createStatement();
            errorMessage = "Error 100";

            for (int i = 0; i < checkedItemID.size(); i++) {

                System.out.println("Test!!!!!!!!!!!!!!!!!!!!!!!");
                errorMessage = "Error 200";
                statement.executeUpdate("update item set i_status = 'Valid' where i_id = " + checkedItemID.get(i));
                errorMessage = "Error 300";
            }

            errorMessage = "Error 400";
            refreshLists();

            return "admin_confirm.xhtml";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "admin_error.xhtml";
    }

    public ArrayList<item> getPendingLisitngs() {
        return pendingLisitngs;
    }

    public void setPendingLisitngs(ArrayList<item> pendingLisitngs) {
        this.pendingLisitngs = pendingLisitngs;
    }

    public ArrayList<Integer> getCheckedItemID() {
        return checkedItemID;
    }

    public void setCheckedItemID(ArrayList<Integer> checkedItemID) {
        this.checkedItemID = checkedItemID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
