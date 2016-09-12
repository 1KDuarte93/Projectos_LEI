
package cenas.service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import rmiserver.ActionItem;
import rmiserver.Meeting;
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
        } catch (RemoteException ex) {
            Logger.getLogger(NewAccountBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex){
            Logger.getLogger(NewAccountBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Meeting> actualizarConvites() throws RemoteException{
        ArrayList<Meeting> smeeting;
        smeeting = server.mostrar_reunioes_a_que_vou(user,0);
        return smeeting;
    }
    
    public ArrayList<ActionItem> actualizarTarefas(){
        ArrayList<ActionItem> sitems;
        try {
            sitems = server.mostrar_meus_action_items(user.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
        return sitems;
    }

    public ArrayList<Meeting> actualizarReunioes() throws RemoteException{
        ArrayList<Meeting> smeeting;
        smeeting = server.mostrar_reunioes_a_que_vou(user,1);
        return smeeting;
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
