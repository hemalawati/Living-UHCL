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
 * @author Hema
 */
@ManagedBean
@SessionScoped
public class item implements Serializable {

    /**
     * Creates a new instance of item
     */
    //attributes
    private int i_id;
    private int s_id;
    private String i_type;
    private String i_title;
    private String i_description;
    private double i_price;
    private int i_min;
    private int i_max;
    private int i_sold;
    private String i_status;
    private boolean i_featured;
    private String i_category;
    private String i_img;

    //constructor
    public item(int item_id, int seller_id, String type, String title, String description, double price, int min, int max, int sold, String status, boolean featured, String category, String i_img) {
        i_id = item_id;
        s_id = seller_id;
        i_type = type;
        i_title = title;
        i_description = description;
        i_price = price;
        i_min = min;
        i_max = max;
        i_featured = featured;
        i_sold = sold;
        i_status = status;
        i_category = category;
        this.i_img = i_img;
    }

    public item() {
    }

//get and set methods
    public int getI_id() {
        return i_id;
    }

    public void setI_id(int i_id) {
        this.i_id = i_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getI_type() {
        return i_type;
    }

    public void setI_type(String i_type) {
        this.i_type = i_type;
    }

    public String getI_title() {
        return i_title;
    }

    public void setI_title(String i_title) {
        this.i_title = i_title;
    }

    public String getI_description() {
        return i_description;
    }

    public void setI_description(String i_description) {
        this.i_description = i_description;
    }

    public double getI_price() {
        return i_price;
    }

    public void setI_price(double i_price) {
        this.i_price = i_price;
    }

    public int getI_min() {
        return i_min;
    }

    public void setI_min(int i_min) {
        this.i_min = i_min;
    }

    public int getI_max() {
        return i_max;
    }

    public void setI_max(int i_max) {
        this.i_max = i_max;
    }

    public int getI_sold() {
        return i_sold;
    }

    public void setI_sold(int i_sold) {
        this.i_sold = i_sold;
    }

    public String getI_status() {
        return i_status;
    }

    public void setI_status(String i_status) {
        this.i_status = i_status;
    }

    public boolean isI_featured() {
        return i_featured;
    }

    public void setI_featured(boolean i_featured) {
        this.i_featured = i_featured;
    }

    public String getI_category() {
        return i_category;
    }

    public void setI_category(String i_category) {
        this.i_category = i_category;
    }

    public String getI_img() {
        return i_img;
    }

    public void setI_img(String i_img) {
        this.i_img = i_img;
    }

}
