/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

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
public class LoginVerificacao extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    
    @Override
    public String execute() throws MalformedURLException, RemoteException{

        UserLogin user = (UserLogin)session.get("usersession");
        if (user!=null){
            return "go2menu";
        }
        else{
            return "go2loginpage";
        }
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return session;
    }
    
}

