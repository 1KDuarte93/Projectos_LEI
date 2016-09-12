/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.Meeting;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class ConvidarUserBean {
    private RMIInterface server;
    private String convidado;
    private int id_reuniao;
    private UserLogin user;
    
    public ConvidarUserBean(){
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
    
    public String convidarUserFunction(){
        Meeting reuniao;
        boolean sucesso;
        try {
            reuniao = server.buscar_reuniao(id_reuniao, user);
            if (reuniao == null){
                return "reuniaonaopertencente";
            }
            sucesso = server.convidar_user(convidado, reuniao);
            if (sucesso){
                return "success";
            }
            else{
                return "failure";
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ConvidarUserBean.class.getName()).log(Level.SEVERE, null, ex);
            return "failure";
        } catch (IOException ex) {
            Logger.getLogger(ConvidarUserBean.class.getName()).log(Level.SEVERE, null, ex);
            return "failure";
        }
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    public String getConvidado() {
        return convidado;
    }

    public void setConvidado(String convidado) {
        this.convidado = convidado;
    }

    public int getId_reuniao() {
        return id_reuniao;
    }

    public void setId_reuniao(int id_reuniao) {
        this.id_reuniao = id_reuniao;
    }
    
    
}
