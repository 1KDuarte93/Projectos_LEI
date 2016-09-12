/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.ActualizarInformacaoBean;
import com.opensymphony.xwork2.ActionSupport;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.ActionItem;
import rmiserver.AgendaItem;
import rmiserver.Meeting;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class ActualizarInformacao extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    
    @Override
    public String execute() throws RemoteException {
        UserLogin user = (UserLogin)session.get("usersession");
        ArrayList<String> reunioes, convites, tarefas;
        UserLogin user2;
        ActualizarInformacaoBean aiBean = new ActualizarInformacaoBean();
        aiBean.setUser(user);

        reunioes = convertMeetingToStringArray(aiBean.actualizarReunioes());
        convites = convertMeetingToStringArray(aiBean.actualizarConvites());
        tarefas = convertActionItemToStringArray(aiBean.actualizarTarefas());
        user2 = aiBean.actualizarUser();
        session.replace("usersession", user2);
        session.replace("username", user2.getUsername());

        if (session.containsKey("minhasreunioes")){
            session.replace("minhasreunioes", reunioes);
        }
        else
            session.put("minhasreunioes", reunioes);
        if(session.containsKey("meusconvites")){
            session.replace("meusconvites", convites);
        }
        else
            session.put("meusconvites", convites);
        if(session.containsKey("tarefas")){
            session.replace("tarefas", tarefas);
        }
        else{
            session.put("tarefas", tarefas);
        }
        return"success";
    }

    private ArrayList<String> convertActionItemToStringArray(ArrayList<ActionItem> a) {
        ArrayList<String> s= new ArrayList<String>();
        String status = "";

        if (a != null && !a.isEmpty()){
            for (ActionItem tmp : a) {
                if (tmp.getDone() == 0){
                    status = "Incompleto";
                }
                else
                    status = "Completo";
                s.add("Action Item ["+tmp.getId()+"]: "+tmp.getDescricao()+" | Status: "+status);
            }
        }
        return s;
    }

    public ArrayList<String> convertMeetingToStringArray (ArrayList<Meeting> m){
        ArrayList<String> s= new ArrayList<String>();

        if (m != null && !m.isEmpty()){
            for (Meeting tmp : m) {
                s.add("[" + tmp.getId() + "]");
                s.add("Titulo: " + tmp.getTitulo());
                s.add("Objectivo: " + tmp.getResultado());
                s.add("Localizacao: " + tmp.getLocalizacao());
                s.add("Data: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(tmp.getData()));
                s.add("Duracao: " + tmp.getDuracao());
                s.add("Utilizadores: " + tmp.getPessoal().toString());
                s.add("Items: ");

                for (AgendaItem i : tmp.getItems()) {
                    s.add("["+i.getId()+"]"+" "+i.getDescricao() + "| Key Decision: "+i.getKey());
                }
                s.add("\n");
            }
        }
        return s;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    
}
