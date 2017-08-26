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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Lia
 */
@Named(value = "acceptNewStudentsBean")
@SessionScoped
public class AcceptNewStudentsBean implements Serializable {

    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Creates a new instance of AcceptNewStudentsBean
     */
    public AcceptNewStudentsBean() {
    }
    
    public boolean isLeader(){
       return TeamManagementSystem.isLeader(em);
    }
    
    public List<User> getCandidates(){
        return TeamManagementSystem.getCandidates(em);
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
    
    public void addSelectedStudent(SelectEvent event){
       TeamManagementSystem.addSelectedStudent(em,(User)event.getObject());  
    }
    
    public void removeSelectedStudent(UnselectEvent event){
        TeamManagementSystem.removeSelectedStudent((User) event.getObject());
    }
    
    public String acceptStudents(){
        TeamManagementSystem.acceptStudents(em,utx);
        return "coursePage";
    }
    
     public static void notEnoughSpaceMsg(int maxStudents){
        String msg = "Error there can only be " + maxStudents + " students in your team";
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
    }
    
}
