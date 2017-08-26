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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Lia
 */
@Named(value = "registerUserBean")
@SessionScoped
public class RegisterUserBean implements Serializable {
    private Long userId;
    private String email;
    private String  password;
    private String firstName;
    private String lastName;
    private String responsibility;
    private String program;
    private String courseCode;
    private List<Course> courses = new ArrayList<Course> ();
    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Creates a new instance of RegisterUserBean
     */
    public RegisterUserBean() {
    }
    
    public void registerUser(){
        this.resetRegisterUser();
        TeamManagementSystem.registerUser();
    }
    
    public boolean showRegisterUser(){
        return TeamManagementSystem.showRegisterUser();
    }
    
    public String getUserId() {
        if (userId == null){
            return "";
        }
        return userId.toString();
    }

    public void setUserId(String userId) {
        this.userId = Long.parseLong(userId);
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPassword(){
        return this.password;
    }
    
     public void setPassword(String password){
         this.password = password;
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
     
     public String getResponsibility(){
         return this.responsibility;
     }
     
     public void setResponsibility(String responsibility){
         this.responsibility = responsibility;
     }
     
     public String getProgram(){
         return this.program;
     }
     
     public void setProgram(String program){
         this.program = program;
     }
     
     public String getCourseCode(){
         return this.courseCode;
     }
     
     public void setCourseCode(String courseCode){
         this.courseCode = courseCode;
     }
     
     public boolean showStudentPanel(){
         if(this.responsibility == null){
             return false;
         }
         return this.responsibility.equals(User.STUDENT);
     }
     
     public String addCourse(){
         Course c = TeamManagementSystem.addCourse(em,utx,courseCode);
         if(courses == null){
             courses = new ArrayList<Course>();
         }
         this.courses.add(c);
         this.courseCode = "";
         return "index";
     }
     
     public String newUser(){
         User u = TeamManagementSystem.findUser(em,userId);
         if (u == null){
            if(this.responsibility.equals(User.STUDENT)){
                TeamManagementSystem.newStudent(em,utx,this.firstName, this.lastName, this.email,this.password, this.program, this.userId);
            }
            else{
                TeamManagementSystem.newInstructor(em,utx,this.firstName, this.lastName, email,this.password, this.userId);
                
            }
            String msg = responsibility + " Created Successfully";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
           FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
           FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
            this.resetRegisterUser();
         }
         else{
               String msg = "Error While Creating " + responsibility;
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
         }
         return "index";
     }
     
     private void resetRegisterUser(){
         this.courseCode = "";
         this.courses = null;
         this.email = "";
         this.firstName = "";
         this.lastName = "";
         this.password = "";
         this.program = "";
         this.responsibility = "";
         this.userId = null;
         TeamManagementSystem.logOut();
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
}
