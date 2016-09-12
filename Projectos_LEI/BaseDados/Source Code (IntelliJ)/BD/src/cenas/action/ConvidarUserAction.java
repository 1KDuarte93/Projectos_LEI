/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.ConvidarUserBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class ConvidarUserAction extends ActionSupport implements SessionAware{
    private String userconvidado;
    private ConvidarUserBean convidarbean;
    private String id_reuniao;
    private Map<String, Object> session;
    
    public String execute(){
        String sucesso;
        
        convidarbean = new ConvidarUserBean();
        convidarbean.setConvidado(userconvidado);
        convidarbean.setId_reuniao((Integer.parseInt(id_reuniao)));
        convidarbean.setUser((UserLogin)session.get("usersession"));
        sucesso = convidarbean.convidarUserFunction();
        
        if (sucesso.equals("reuniaonaopertencente")){
            return "loginerror";
        }
        else if (sucesso.equals("success")){
            return "success";
        }
        else
            return "failure";
    }

    public String getUserconvidado() {
        return userconvidado;
    }

    public void setUserconvidado(String userconvidado) {
        this.userconvidado = userconvidado;
    }

    public ConvidarUserBean getConvidarbean() {
        return convidarbean;
    }

    public void setConvidarbean(ConvidarUserBean convidarbean) {
        this.convidarbean = convidarbean;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getId_reuniao() {
        return id_reuniao;
    }

    public void setId_reuniao(String id_reuniao) {
        this.id_reuniao = id_reuniao;
    }
    
    
}
