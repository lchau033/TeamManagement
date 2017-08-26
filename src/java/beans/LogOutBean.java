/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Lia
 */
@Named(value = "logOutBean")
@SessionScoped
public class LogOutBean implements Serializable {

    /**
     * Creates a new instance of LogOutBean
     */
    public LogOutBean() {
    }
    
    public String logOut(){
        TeamManagementSystem.logOut();
        return "index";
    }
    
}
