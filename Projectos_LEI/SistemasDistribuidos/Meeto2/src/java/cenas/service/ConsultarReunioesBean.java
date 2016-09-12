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
public class ConsultarReunioesBean{
    private RMIInterface server;
    private UserLogin user;
    private ArrayList<String> sarray;
    
    public ConsultarReunioesBean() throws MalformedURLException, RemoteException, NotBoundException{
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
    
    public ArrayList<String> consultarReunioesfunction() throws RemoteException{
        int i, j, k;
        ArrayList<String> smeeting;
        String itemresposta;
        sarray = new ArrayList<String>();
        try{
            //public Meeting buscar_reuniao(int id, UserLogin user)
            smeeting = server.mostrar_reunioes_a_que_vou(0, user);
            i=0;
            while(smeeting != null){
                if (!smeeting.get(0).equals("-1")){
                    for(k=0; k<smeeting.size();k++){
                        sarray.add(smeeting.get(k));
                    }
                    //buscar items
                    j=0;
                    itemresposta = server.mostrar_items(i, j);
                    sarray.add("Items:");
                    while(itemresposta != null){
                        sarray.add(itemresposta);
                        j++;
                        itemresposta = server.mostrar_items(i, j);
                    }
                    sarray.add("");
                }
                i++;
                smeeting = server.mostrar_reunioes_a_que_vou(i, user);
            }
            //Busca de Items
            return sarray;
        }
        catch(Exception e){
            //return "errorconnection";
            return null;
        }
    }
    
    public ArrayList<String> getSarray(){
        return sarray;
    }
    public void setSarray(ArrayList<String> sarray){
        this.sarray = sarray;
    }
    
    public void setUser(UserLogin user){
        this.user = user;
    }
    public UserLogin getUser(){
        return user;
    }
}

