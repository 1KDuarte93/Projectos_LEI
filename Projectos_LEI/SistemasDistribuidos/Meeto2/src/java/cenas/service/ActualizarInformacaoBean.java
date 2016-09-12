
package cenas.service;

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
public class ActualizarInformacaoBean {
    private RMIInterface server;
    private UserLogin user;
    public ActualizarInformacaoBean(){
        try {
            server = (RMIInterface) LocateRegistry.getRegistry(7000).lookup("receber");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ActualizarInformacaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> actualizarConvites(){
        int i;
        ArrayList<String> arrayconvites = new ArrayList<String>();
        String sconvites;
        
        try {
            i=0;
            sconvites = server.imprimir_convites(i, user);
            while(sconvites != null){
                arrayconvites.add(sconvites);
                i++;
                sconvites = server.imprimir_convites(i, user);
            }
            return arrayconvites;
        } catch (RemoteException ex) {
            return null;
        }
    }
    
    public ArrayList<String> actualizarTarefas(){
        ArrayList<String> tarefasarray = new ArrayList<String>();
        String starefa;
        int i, j, aux;
        i=0;
        j=0;
        aux=0;
        try {
            starefa=server.correr_actionitems(i, j, user.getUsername());
            while(starefa!=null){
                if (starefa.equals("-2")){
                    i++;
                    j=0;
                }
                else if(!starefa.equals("-1")){
                    tarefasarray.add("[" + aux + "] - " +starefa);
                    aux++;
                }
                j++;
                starefa=server.correr_actionitems(i, j, user.getUsername());
            }
            return tarefasarray;
        } catch (RemoteException ex) {
            Logger.getLogger(ActualizarInformacaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<String> actualizarReunioes(){
        ArrayList<String> sarray = new ArrayList<String>();
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
            System.out.println("Nao buscou: " + e);
            //return "errorconnection";
            return null;
        }
        
    }
    
    public UserLogin actualizarUser(){
        UserLogin user2;
        try {
            user2 = server.consultar_conta(user.getUsername(), user.getPassword());
            return user2;
        } catch (RemoteException ex) {
            Logger.getLogger(ActualizarInformacaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
    
    
}
