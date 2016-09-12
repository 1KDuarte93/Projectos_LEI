/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.AdicionarApagarEditarBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class EliminarItemAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    private String id_reuniao;
    private String id_item;
    private String descricao_item;
    
    @Override
    public String execute(){
        boolean aux;
        AdicionarApagarEditarBean aaeBean = new AdicionarApagarEditarBean();
        aaeBean.setDescricao_item(descricao_item);
        aaeBean.setId_item(id_item);
        aaeBean.setId_reuniao(id_reuniao);
        aaeBean.setUser((UserLogin)session.get("usersession"));
        aux = aaeBean.apagaritem();
        
        if (aux)
            return "success";
        else
            return "failure";
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
    
    
}
