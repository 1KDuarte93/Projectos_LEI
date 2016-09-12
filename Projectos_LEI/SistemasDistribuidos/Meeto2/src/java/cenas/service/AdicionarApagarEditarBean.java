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
import rmiserver.AgendaItem;
import rmiserver.RMIInterface;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class AdicionarApagarEditarBean {
    RMIInterface server;
    String id_reuniao;
    String id_item;
    String descricao_item;
    String key;
    UserLogin user;
    public AdicionarApagarEditarBean(){
        try {
            server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
        } catch (RemoteException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean editaritem(){
        try {
            server.modificar_item((Integer.parseInt(id_reuniao)), (Integer.parseInt(id_item)), descricao_item, (user.getUsername()));
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean apagaritem(){
        try {
            server.eliminar_item((Integer.parseInt(id_reuniao)), (Integer.parseInt(id_item)));
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean adicionaritem(){
        try {
            AgendaItem item = new AgendaItem(descricao_item, user.getUsername());
            server.adicinar_item((Integer.parseInt(id_reuniao)), item);
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean addkey(){
        
        try {
            server.addkey((Integer.parseInt(id_reuniao)), (Integer.parseInt(id_item)), key);
            return true;
        } catch (RemoteException ex) {
            Logger.getLogger(AdicionarApagarEditarBean.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public RMIInterface getServer() {
        return server;
    }

    public void setServer(RMIInterface server) {
        this.server = server;
    }

    public String getId_reuniao() {
        return id_reuniao;
    }

    public void setId_reuniao(String id_reuniao) {
        this.id_reuniao = id_reuniao;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getDescricao_item() {
        return descricao_item;
    }

    public void setDescricao_item(String descricao_item) {
        this.descricao_item = descricao_item;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
    
    
}
