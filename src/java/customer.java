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
public class customer extends account implements Serializable {

    /**
     * Creates a new instance of customer
     *
     * @param id
     * @param psw
     * @param type
     * @param name
     */
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<item> listings = new ArrayList<>();
    ArrayList<order> orders = new ArrayList<>();
    ArrayList<item> searchResult = new ArrayList<>();
    String inputKeyword;
    String selectedCategory;
    int selectedItemID;
    item selectedItem;
    order newOrder;
    String errorMessage;
    item currentItem;
    ArrayList<item> itemsByCategory = new ArrayList<>();

    public customer(int id, String psw, String type, String name) {

        super(id, psw, type, name);
        refreshLists();
    }

    //METHOD TO POPULATE "LISINGS" ARRAYLIST WITH THE 'VALID' ITEMS FROM DATABASE
    //ALSO TO GET VARIOUS CATEGORIES FROM DATABASE AND POPULATE "CATEGORIES" ARRAYLIST
    public void refreshLists() {
        listings.clear();
        categories.clear();
        orders.clear();
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
            rs = statement.executeQuery("select * from item where i_status = 'Valid'");

            while (rs.next()) {
                listings.add(new item(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(10), rs.getBoolean(11), rs.getString(12), rs.getString(13)));
            }

            rs = statement.executeQuery("select distinct i_category from item");
            while (rs.next()) {
                categories.add(rs.getString(1));
            }

            rs = statement.executeQuery("select * from orders where c_id = " + super.id);
            while (rs.next()) {
                orders.add(new order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5)));
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

    public String viewByCategory() {
        itemsByCategory.clear();

        for (item i : listings) {
            if (i.getI_category().equalsIgnoreCase(selectedCategory)) {
                itemsByCategory.add(i);
            }
        }
        return "customer_category.xhtml";

    }

    public String searchByKeyword() {
        searchResult.clear();

        try {
            String[] inputKeywordArray = inputKeyword.split("\\s+");

            for (String x : inputKeywordArray) {
                for (item i : listings) {
                    String[] array1 = i.getI_title().split("\\s+");
                    String[] array2 = i.getI_description().split("\\s+");
                    String[] array3 = i.getI_category().split("\\s+");

                    for (String s : array1) {
                        if (s.equalsIgnoreCase(x)) {
                            searchResult.add(i);
                        }
                    }
                    for (String s : array2) {
                        if (s.equalsIgnoreCase(x)) {
                            searchResult.add(i);
                        }
                    }
                    for (String s : array3) {
                        if (s.equalsIgnoreCase(x)) {
                            searchResult.add(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<item> uniqueList = new ArrayList<>();

        for (item i : searchResult) {
            if (!uniqueList.contains(i)) {
                uniqueList.add(i);
            }
        }

        searchResult = uniqueList;
        if (searchResult.isEmpty()) {
            errorMessage = "No search result!";
            return "customer_error.xhtml";
        } else {
            return "customer_searchResult.xhtml";
        }
    }

    public String buy(int inputid) {
        selectedItemID = inputid;
        String orderStatus;

        for (item i : listings) {
            if (i.getI_id() == selectedItemID) {
                //MATCH THE ITEM ID AND GET ITEM FROM LOCALLY STORED LISTINGS ARRAYLIST
                selectedItem = i;
            }
        }
        if (selectedItem.getI_max() >= selectedItem.getI_sold() + 1) {
            //IF MAXIMUM CAPACTIY IS NOT EXCEEDED

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                e.printStackTrace();
            }

            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/dingorkarj2620";
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;

            //IF THERE EXISTS AN ORDER FOR CURRENT CUSTOMER AND ITEM  
            boolean itemAlreadyPurchased = false;
            for (order o : orders) {
                if (o.getI_id() == selectedItemID) {
                    itemAlreadyPurchased = true;
                }
            }

            try {
                connection = DriverManager.getConnection(DATABASE_URL, "dingorkarj2620", "1289968");
                statement = connection.createStatement();
                errorMessage = "Interna1 Error:100";

                if (itemAlreadyPurchased == true) {
                    //CUSTOMER HAS ALREADY PURCHASED THIS ITEM
                    errorMessage = "You have already purchased!";
                    return "customer_error.xhtml";
                } else {
                    //CUSTOMER HAS NOT PURCHASED THIS ITEM
                    rs = statement.executeQuery("select * from nextordernumber");
                    errorMessage = "Internal Error:200";

                    int nextOrderNumber = 0;
                    if (rs.next()) {
                        nextOrderNumber = rs.getInt(1);
                    }

                    //UPDATE ITEM SOLD LOCALLY
                    int sold = selectedItem.getI_sold() + 1;
                    selectedItem.setI_sold(sold);

                    //GET MINIMUM REQUIRED ITEM SOLD VALUE LOCALLY
                    int min = selectedItem.getI_min();

                    //DETERMINE NEW ORDER STATUS
                    if ((sold) >= min) {
                        //IF MINIMUM NUMBER OF ITEMS SOLD IS TRUE
                        orderStatus = "Valid";
                    } else {
                        //MINIMUM NUMBER OF ITEMS ARE NOT SOLD
                        orderStatus = "Invalid";
                    }
                    System.out.println("selectedItemID is " + selectedItemID);
                    //UPDATE NUMBER OF ITEMS SOLD IN ITEM TABLE
                    statement.executeUpdate("update item set i_sold =" + sold + " where i_id = " + selectedItemID);
                    errorMessage = "Internal Error300";

                    //UPDATE ORDER STATUS OF ALL ORDERS FOR SELECTED ITEM
                    //ERROR***ERROR***ERROR***ERROR***ERROR
                    statement.executeUpdate("update orders set o_status = 'Valid' where i_id = "+ selectedItemID);
                    errorMessage = "Internal Error400";
                    //CREATE A NEW ORDER
                    //ERROR***ERROR***ERROR***ERROR***ERROR
                    statement.executeUpdate("insert into orders values(" + nextOrderNumber + ", " + selectedItemID + ", " + super.getId() + ", " + selectedItem.getS_id() + " , '" + orderStatus + "')");
                    errorMessage = "Internal Error500";

                    //UPDATE NEXT ORDER NUMBER
                    statement.executeUpdate("update nextordernumber set next_o_id =" + (nextOrderNumber + 1));
                    errorMessage = "Internal Error600";

                    //CREATE A NEW ORDER OBJECT
                    newOrder = new order(nextOrderNumber, selectedItemID, super.getId(), selectedItem.getS_id(), orderStatus);
                    errorMessage = "Internal Error700";

                    refreshLists();

                    return "confirmation.xhtml";
                }
            } catch (Exception e) {
                e.printStackTrace();
                //errorMessage = "Internal Error1";
                return "customer_error.xhtml";
            } finally {
                try {
                    connection.close();
                    statement.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    //errorMessage = "Internal Error2";
                    return "customer_error.xhtml";
                }
            }
        } else {
            //MAXIMUM NUMBER OF ITEMS ARE SOLD ALREADY
            errorMessage = "MAXIMUM NUMBER OF ITEMS ARE SOLD ALREADY\nSALE IS OVER FOR THIS ITEM";
        }

        return "customer_error.xhtml";
    }

    public ArrayList<item> getListings() {
        return listings;
    }

    public void setListings(ArrayList<item> listings) {
        this.listings = listings;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public int getSelectedItemID() {
        return selectedItemID;
    }

    public void setSelectedItemID(int selectedItemID) {
        this.selectedItemID = selectedItemID;
    }

    public item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public order getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(order newOrder) {
        this.newOrder = newOrder;
    }

    public ArrayList<order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<order> orders) {
        this.orders = orders;
    }

    public String getInputKeyword() {
        return inputKeyword;
    }

    public void setInputKeyword(String inputKeyword) {
        this.inputKeyword = inputKeyword;
    }

    public ArrayList<item> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<item> searchResult) {
        this.searchResult = searchResult;
    }

    public item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(item currentItem) {
        this.currentItem = currentItem;
    }

    public ArrayList<item> getItemsByCategory() {
        return itemsByCategory;
    }

    public void setItemsByCategory(ArrayList<item> itemsByCategory) {
        this.itemsByCategory = itemsByCategory;
    }

}
