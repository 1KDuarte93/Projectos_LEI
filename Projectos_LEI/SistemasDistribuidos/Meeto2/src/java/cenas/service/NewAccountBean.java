/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class NewAccountBean {
    private RMIInterface server;
    private String username;
    private String password;
    
    public NewAccountBean(){
        
        try {
            server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(NewAccountBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public UserLogin newaccount(){
        UserLogin userlogin;
        try{
            userlogin = server.registar_conta(username, password);
            System.out.println("NOVA CONTA: " + userlogin.getUsername());
            return userlogin;
        }
        catch(ClassNotFoundException | IOException e){
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

    public RMIInterface getServer() {
        return server;
    }

    public void setServer(RMIInterface server) {
        this.server = server;
    }
    
}
