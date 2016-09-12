/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.AddItemBean;
import com.opensymphony.xwork2.ActionSupport;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Map;
import rmiserver.Meeting;
import rmiserver.UserLogin;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author kduarte
 */
public class AddItemAction extends ActionSupport implements SessionAware{
    private AddItemBean itemBean;
    private Meeting meeting;
    private Map<String, Object> session;
    private String item;

    @Override
    public String execute() throws MalformedURLException, RemoteException, NotBoundException{
        UserLogin user;
        
        int idmeeting;
        boolean respostab;
        ArrayList<String> arrayresposta;
        user = (UserLogin)session.get("usersession");

        idmeeting = (Integer)session.get("id_meeting");
      
        if (user != null){
            itemBean = new AddItemBean();
            itemBean.setId(idmeeting);
            itemBean.setItem(item);
            itemBean.setUser(user);
            itemBean.setUsername(user.getUsername());
            try {
                respostab = itemBean.addItemFunction();
                if (respostab){
                    arrayresposta = itemBean.printitems();
                    if (session.containsKey("agendaitems")){
                        session.replace("agendaitems", arrayresposta);
                    }
                    else{
                        session.put("agendaitems", arrayresposta);
                    }
                    return "success";
                }
                else
                    return "failure";
                //printitems;
                //print dos items
                
            } catch (FileNotFoundException ex) {
                return "failure";
            }
        }
        else
            return "loginerror";
    } 
    
    public AddItemBean getItemBean() {
        return itemBean;
    }

    public void setItemBean(AddItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Map<String, Object> getSession() {
        return session;
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    
}
