/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.Meeting;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class CreateMeetingBean{
    private RMIInterface server;
    private String username;
    private String password;
    private Meeting meeting;
    private UserLogin user;
    
    public CreateMeetingBean() throws MalformedURLException, RemoteException, NotBoundException{
        try {
            try {
                server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
                //server  = (RMIInterface) Naming.lookup("rmi://10.0.0.1:7000/receber");
            } catch (RemoteException ex) {
                Logger.getLogger(CreateMeetingBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(CreateMeetingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Meeting createMeetingfunction() throws RemoteException{

        try{
            meeting = server.agendar_reuniao(meeting, user);
            return meeting;
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
    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
    }
    public Meeting getMeeting(){
        return meeting;
    }
    public void setUser(UserLogin user){
        this.user = user;
    }
    public UserLogin getUser(){
        return user;
    }
}

