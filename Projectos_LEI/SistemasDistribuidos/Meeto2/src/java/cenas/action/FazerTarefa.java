/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.FazerTarefaBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class FazerTarefa extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    private String id_tarefa;
    
    @Override
    public String execute(){
        String aux;
        FazerTarefaBean ftBean = new FazerTarefaBean();
        ftBean.setId_tarefa(id_tarefa);
        ftBean.setUser((UserLogin)session.get("usersession"));
        aux = ftBean.tdone();
        
        return aux;
       
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getId_tarefa() {
        return id_tarefa;
    }

    public void setId_tarefa(String id_tarefa) {
        this.id_tarefa = id_tarefa;
    }

    
    
}
