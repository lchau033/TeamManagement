/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import UserManagement.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lia
 */
@Named(value = "createTeamBean")
@SessionScoped
public class CreateTeamBean implements Serializable {
    private String name;
    private String firstName;
    private String lastName;
    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Creates a new instance of CreateTeamBean
     */
    public CreateTeamBean() {
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public List<User> getMembers(){
        return TeamManagementSystem.getMembers();
    }
    
    public String addStudent(){
        TeamManagementSystem.addStudent(em,this.firstName,this.lastName);
        this.firstName = null;
        this.lastName = null;
        TeamManagementSystem.setIndex(0);
        return "coursePage";
    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    public String createTeam(){
        TeamManagementSystem.createTeam(em,utx,name);
        name = null;
        firstName = null;
        lastName = null;
        return "coursePage";
    }
    
    public static void teamNameErrorMsg(){
        String msg = "Error the team name is already used";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
    }
    
    public static void alreadyInTeamErrorMsg(){
        String msg = "Error one or more of the students listed are already in another team";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
    }
    
    public static void tooManyMembersErrorMsg(int maxStudents){
        String msg = "Error there can only be " + maxStudents + " students in your team";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
    }
    
    public static void teamCreatedMsg(String teamName){
        String msg =  teamName + " Created Successfully";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
        FacesContext.getCurrentInstance().getExternalContext()
             .getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }
    
    public boolean showStudentList(){
        return TeamManagementSystem.showStudentList();
    }
}
