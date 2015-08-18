/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp
 */
@ManagedBean
@SessionScoped
public class order implements Serializable {

    private int o_id;
    private int i_id;
    private int c_id;
    private int s_id;
    private String o_status;

    /**
     * Creates a new instance of order
     *
     * @param o_id
     * @param i_id
     * @param c_id
     * @param s_id
     * @param o_status
     */
    
    public order(int o_id, int i_id, int c_id, int s_id, String o_status) {
        this.o_id = o_id;
        this.i_id = i_id;
        this.c_id = c_id;
        this.s_id = s_id;
        this.o_status = o_status;
    }
    

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public int getI_id() {
        return i_id;
    }

    public void setI_id(int i_id) {
        this.i_id = i_id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }

}
