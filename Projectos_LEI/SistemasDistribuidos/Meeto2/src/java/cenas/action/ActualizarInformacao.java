/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.ActualizarInformacaoBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class ActualizarInformacao extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    
    @Override
    public String execute(){
        UserLogin user = (UserLogin)session.get("usersession");
        ArrayList<String> reunioes, convites, tarefas;
        UserLogin user2;
        ActualizarInformacaoBean aiBean = new ActualizarInformacaoBean();
        aiBean.setUser(user);
        
        reunioes = aiBean.actualizarReunioes();
        convites = aiBean.actualizarConvites();
        tarefas = aiBean.actualizarTarefas();
        user2 = aiBean.actualizarUser();
        session.replace("usersession", user2);
        session.replace("username", user2.getUsername());

        if (session.containsKey("minhasreunioes")){
            session.replace("minhasreunioes", reunioes);
            System.out.println("ACTUALIZOU REUNIOES");
        }
        else
            session.put("minhasreunioes", reunioes);
        if(session.containsKey("tarefas")){
            session.replace("tarefas", tarefas);
            System.out.println("ACTUALIZOU TAREFAS");
        }
        else{
            session.put("tarefas", tarefas);
        }
        if(session.containsKey("meusconvites")){
            session.replace("meusconvites", convites);
            System.out.println("ACTUALIZOU CONVITES: " + convites);
        }
        else
            session.put("meusconvites", convites);
        return"success";
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
}
