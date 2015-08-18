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
public class account implements Serializable{

    protected int id;
    protected String psw;
    protected String type;
    protected String name;

    /**
     * Creates a new instance of account
     *
     * @param id     
     * @param psw     
     * @param type     
     * @param name     
     */
    public account(int id, String psw, String type, String name) {
        this.id = id;
        this.psw = psw;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
}
