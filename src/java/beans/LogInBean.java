/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import CourseManagement.Course;
import UserManagement.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lia
 */
@Named(value = "logInBean")
@SessionScoped
public class LogInBean implements Serializable {
    private Long userId;
    private String password;
    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    private List<Course> courses;
    

    /**
     * Creates a new instance of LogInBean
     */
    public LogInBean() {
    }
    public String getUserId(){
        if(userId == null){
            return "";
        }
        return this.userId.toString();
    }
    
    public int getIndex(){
        return TeamManagementSystem.getIndex();
    }
    
    public void setIndex(int index){
        TeamManagementSystem.setIndex(index);
    }
    
    public void setUserId(String userId){
        this.userId = Long.parseLong(userId);
    }
    
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    
    public void logIn(){
        TeamManagementSystem.logIn();
    }
    
    public boolean showLogIn(){
        return TeamManagementSystem.showLogIn();
    }
    
    public List<Course> getCourses(){
        return TeamManagementSystem.fetchCourses(em);
    }
    
    public String findUser(){
        TeamManagementSystem.findUser(em, userId,password);
        courses = TeamManagementSystem.fetchCourses(em);
        if(TeamManagementSystem.getResponsibility() == null){
            String msg = "User Not Found";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
        }
        else if(TeamManagementSystem.getResponsibility().equals(User.STUDENT)){
            this.userId = null;
            return "coursePage";
        }
        else if(TeamManagementSystem.getResponsibility().equals(User.INSTRUCTOR)){
            this.userId = null;
            return "instructorPage";
        }
        
        return "index";
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
    
    public String setCourseCodeParm(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params=fc.getExternalContext().getRequestParameterMap();
        TeamManagementSystem.fetchCourse(em,params.get("courseCode"));
        return "coursePage";
    }
    
    public boolean showCourseOptions(){
        boolean value = TeamManagementSystem.showCourseOptions();
        return value;
    }
    
    public boolean showCourseOptionsErrorMsg(){
        return TeamManagementSystem.showCourseOptionsErrorMsg();
    }
    
    public boolean showCoursesTaughtBy(){
        TeamManagementSystem.fetchCourses(em);
        return true;
    }
    
}
