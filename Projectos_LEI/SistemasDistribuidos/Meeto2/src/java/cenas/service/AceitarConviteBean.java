/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class AceitarConviteBean {
    RMIInterface server;
    String idconvite;
    UserLogin user;
    
    public AceitarConviteBean(){
    try {
            try {
                server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
                //server  = (RMIInterface) Naming.lookup("rmi://194.210.172.76:7000/receber");
            } catch (RemoteException ex) {
                Logger.getLogger(AceitarConviteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(AceitarConviteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean aceitarconvite(){
        boolean aux;
        int intidconvite;
        intidconvite = Integer.parseInt(idconvite);
        try {
            aux = server.aceitar_convite(intidconvite, user);
            if(aux)
                return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AceitarConviteBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AceitarConviteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public RMIInterface getServer() {
        return server;
    }

    public void setServer(RMIInterface server) {
        this.server = server;
    }

    public String getIdconvite() {
        return idconvite;
    }

    public void setIdconvite(String idconvite) {
        this.idconvite = idconvite;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
    
    
}
