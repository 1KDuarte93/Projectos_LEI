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
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmiserver.AgendaItem;
import rmiserver.Meeting;
import rmiserver.RMIInterface;

/**
 *
 * @author kduarte
 */
public class AddItemBean {
    private RMIInterface server;
    private String username;
    private String item;
    private int id;
    
    public AddItemBean(){
        try {
            try {
                server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
                //server  = (RMIInterface) Naming.lookup("rmi://194.210.172.76:7000/receber");
            } catch (RemoteException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean addItemFunction() throws FileNotFoundException{
        try {
            server.adicinar_item(id, new AgendaItem(item, username));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    public ArrayList<String> printitems(){
        String itemresposta;
        ArrayList<String> arrayresposta = new ArrayList<String>();
        int i=0;
        try {
            itemresposta = server.mostrar_items(id, i);
            while(itemresposta != null){
                arrayresposta.add(itemresposta);
                i++;
                itemresposta = server.mostrar_items(id, i);
            }
            return arrayresposta;
        } catch (IOException ex) {
            return null;
        }
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
