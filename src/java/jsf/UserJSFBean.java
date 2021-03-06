/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import ejb.UserBean;
import entities.SystemUser;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rhayan
 * Managed-bean for users of the system.
 */
@Named
@SessionScoped
public class UserJSFBean implements Serializable {

    @EJB
    UserBean userBean;

    private String email;
    private String password;
    private SystemUser user;
    private String name;
    private boolean admin;
    private BigDecimal balance;

    public UserJSFBean() {
    }  
    
    /**
     * Method for checking if the current logged in user is an admin.
     * @return true if logged in user is an admin; otherwise, false.
     */
    public boolean isAdmin() {
        admin = userBean.isAdmin(email);
        return admin;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getName() {
        name = userBean.getUser(email).getFirstname();
        return name;
    }

    public SystemUser getUser() {
        return userBean.getUser(email);
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {        
        return balance;
    }
    
    /**
     * Redirection for show page
     * @return 
     */
    public String show() {
        return "show";
    }

    //not in use
    public String continuePayment() {
        return "transfer";
    }

    //not in use
    public String continueRequest() {
        return "request";
    }

    /**
     * Method for loggin in
     * @return String index page upon successful login; otherwise, redirect to error page.
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        System.out.println("Username: " + email);
        System.out.println("Password: " + password);
        try {
            //this method will actually check in the realm for the provided credentials
            request.login(this.email, this.password);

        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            return "error";
        }
        return "/faces/users/index.xhtml";
    }

    /**
     * Method for logout
     * @return redirect to home page of the website; otherwise, display an error.
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            //this method will disassociate the principal from the session (effectively logging him/her out)
            request.logout();
            context.addMessage(null, new FacesMessage("User is logged out"));
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
        return "/faces/index.xhtml";
    }

    @PreDestroy
    public void preDestroy() {
        userBean.updateLastVisit(user);
    }
}
