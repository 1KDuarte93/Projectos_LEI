/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.AdicionarApagarEditarBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

import java.util.Map;

/**
 *
 * @author kduarte
 */
public class AddKey extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String key;
    private String id_reuniao;
    private String id_item;

    @Override
    public String execute(){
        boolean aux;
        AdicionarApagarEditarBean aaeBean = new AdicionarApagarEditarBean();
        aaeBean.setId_item(id_item);
        aaeBean.setId_reuniao(id_reuniao);
        aaeBean.setKey(key);
        aaeBean.setUser((UserLogin)session.get("usersession"));
        aux=aaeBean.addkey();
        if(aux)
            return "success";
        else
            return "failure";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
    
}
