/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.NewActionItemBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class NewActionItem extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    private String idreuniao;
    private String usertask;
    private String descricao;
    public String execute(){
        boolean aux;
        NewActionItemBean naiBean = new NewActionItemBean();
        naiBean.setDescricao(descricao);
        naiBean.setIdreuniao(idreuniao);
        naiBean.setUser((UserLogin) session.get("usersession"));
        naiBean.setUsertask(usertask);
        aux = naiBean.addActionItem();
        if (aux)
            return "success";
        else
            return "failure";
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getIdreuniao() {
        return idreuniao;
    }

    public void setIdreuniao(String idreuniao) {
        this.idreuniao = idreuniao;
    }

    public String getUsertask() {
        return usertask;
    }

    public void setUsertask(String usertask) {
        this.usertask = usertask;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    
    
}
