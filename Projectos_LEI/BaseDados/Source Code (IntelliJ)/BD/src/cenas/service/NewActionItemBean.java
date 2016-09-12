/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.ActionItem;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class NewActionItemBean {
    private RMIInterface server;
    private String idreuniao;
    private String usertask;
    private String descricao;
    UserLogin user;
    
    public NewActionItemBean(){
        try {
            server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
        } catch (RemoteException ex) {
            Logger.getLogger(NewAccountBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex){
            Logger.getLogger(NewAccountBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean addActionItem(){
        try {
            if (server.adicinar_actionitem(Integer.parseInt(idreuniao),usertask,descricao,user.getId()))
                return true;
            else
                return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewActionItemBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewActionItemBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean verifuser(){
        boolean aux;
        try {
            aux = server.correr_usernames(usertask);
            return aux;
        } catch (RemoteException ex) {
            Logger.getLogger(NewActionItemBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public RMIInterface getServer() {
        return server;
    }

    public void setServer(RMIInterface server) {
        this.server = server;
    }

    public String getIdreuniao() {
        return idreuniao;
    }

    public void setIdreuniao(String idreuniao) {
        this.idreuniao = idreuniao;
    }

    public String getUsertask() {
        return usertask;
    }

    public void setUsertask(String usertask) {
        this.usertask = usertask;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
    
    
}
