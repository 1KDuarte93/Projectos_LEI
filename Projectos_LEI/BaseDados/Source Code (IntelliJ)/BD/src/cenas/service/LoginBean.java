/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import com.opensymphony.xwork2.ActionSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class LoginBean extends ActionSupport implements SessionAware{
    private RMIInterface server;
    private Map<String, Object> session;
    private String username;
    private String password;
    
    public LoginBean() throws MalformedURLException{
        try {
            try {
                server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
                //server  = (RMIInterface) Naming.lookup("rmi://10.0.0.1:7000/receber");
            } catch (RemoteException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UserLogin getUserLogin() throws RemoteException{
        UserLogin userlogin;
        try{
            userlogin = server.consultar_conta(username, password);
            if (userlogin == null){
                //return "loginerror";
                return null;
            }
            else{
                //session.put("user", userlogin);
                //return "success";
                return userlogin;
            }
        }
        catch(Exception e){
            //return "errorconnection";
            return null;
        }
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
}
