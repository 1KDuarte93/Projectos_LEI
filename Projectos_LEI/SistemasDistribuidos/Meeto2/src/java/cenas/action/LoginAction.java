/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.LoginBean;
import com.opensymphony.xwork2.ActionSupport;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;
/**
 *
 * @author kduarte
 */
public class LoginAction extends ActionSupport implements SessionAware{
    private LoginBean loginBean;
    private String username;
    private String password;
    private Map<String, Object> session;
    
    
    @Override
    public String execute() throws MalformedURLException, RemoteException{

        UserLogin user = (UserLogin)session.get("usersession");
        if (user!=null){
            return "success";
        }
        loginBean = new LoginBean();
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        user = loginBean.getUserLogin();
        
        if (user != null){
            session.put("username", user.getUsername());
            session.put("usersession", user);
            return "success";
        }
        else
            return "loginerror";
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }    

    
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public Map<String, Object> getSession() {
        return session;
    }
    
}
