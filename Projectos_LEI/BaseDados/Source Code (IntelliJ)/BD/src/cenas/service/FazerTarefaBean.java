/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.service;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class FazerTarefaBean {
    private RMIInterface server;
    private String id_tarefa;
    private UserLogin user;
    public FazerTarefaBean(){
        try {
            server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
        } catch (RemoteException ex) {
            Logger.getLogger(FazerTarefaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(FazerTarefaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String tdone(){
        try {
            if (server.realizar_tarefa(Integer.parseInt(id_tarefa),user.getId()))
                return "success";
            else
                return "failure";
        } catch (RemoteException e) {
            return "failure";
        }
    }

    public RMIInterface getServer() {
        return server;
    }

    public void setServer(RMIInterface server) {
        this.server = server;
    }

    public String getId_tarefa() {
        return id_tarefa;
    }

    public void setId_tarefa(String id_tarefa) {
        this.id_tarefa = id_tarefa;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }


}
