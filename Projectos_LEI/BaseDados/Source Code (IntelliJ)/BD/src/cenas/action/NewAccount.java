/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.NewAccountBean;
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
public class NewAccount extends ActionSupport implements SessionAware{
    private String username;
    private String password;
    private Map<String, Object> session;
    
    public String execute() throws MalformedURLException, RemoteException{
        UserLogin uresposta;
        NewAccountBean accountBean;
        accountBean = new NewAccountBean();
        accountBean.setUsername(username);
        accountBean.setPassword(password);
        uresposta = accountBean.newaccount();
        
        if (uresposta != null){
            if(session.containsKey("usersession"))
                session.replace("usersession", uresposta);
            else{
                session.put("usersession", uresposta);
                session.put("username", uresposta.getUsername());
            }
            return "success";
        }
        else 
            return "usernameinuse";
        
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

    public Map<String, Object> getSession() {
        return session;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
    
}
