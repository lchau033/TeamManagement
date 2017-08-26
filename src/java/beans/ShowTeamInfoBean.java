/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import TeamManagement.Team;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lia
 */
@Named(value = "showTeamInfoBean")
@SessionScoped
public class ShowTeamInfoBean implements Serializable {

    @PersistenceContext(unitName = "ProjetPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Creates a new instance of ShowTeamInfoBean
     */
    public ShowTeamInfoBean() {
    }
    
    public String getName(){
        return TeamManagementSystem.getTeamName();
    }
    
    public String getMembers(){
        return TeamManagementSystem.getMembers(em);
    }
    
    public String getLeader(){
        String msg = "entered" ;
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
           FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);
           FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
           FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
        return TeamManagementSystem.getLeader(em);
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
    
    public String getStatus(){
        return TeamManagementSystem.getTeamStatus();
    }
    
    public boolean hasTeam(){
        Team t = TeamManagementSystem.getTeam(em);
        return t!=null;
    }
    
    public Date getDate(){
        return TeamManagementSystem.getDateTeam();
    }
}
