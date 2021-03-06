/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.CreateMeetingBean;
import com.opensymphony.xwork2.ActionSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.Meeting;
import rmiserver.UserLogin;
/**
 *
 * @author kduarte
 */
public class CreateMeeting extends ActionSupport implements SessionAware{
    private String titulo;
    private String objectivo;
    private String localizacao;
    private String data;
    private String horainicio;
    private String duracao;
    private Map<String, Object> session;
    /*String titulo, String resultado, String localizacao,
			String data, String duracao, UserLogin userlogin*/
    
    @Override
    public String execute() throws MalformedURLException, RemoteException, NotBoundException{
        Meeting meeting;
        Meeting meetingr;
        CreateMeetingBean meetingBean;
        UserLogin user = (UserLogin) session.get("usersession");
        Date d = convertToDate(data);
        if (d == null){
            return "error";
        }
        meeting = new Meeting(titulo, objectivo, localizacao, d, duracao);
        meetingBean = new CreateMeetingBean();
        meetingBean.setMeeting(meeting);
        meetingBean.setUsername(user.getPassword());
        meetingBean.setPassword(user.getPassword());
        meetingBean.setUser(user);
        meetingr = meetingBean.createMeetingfunction();
        
        
        if (meetingr != null){
            String paraimprimir ="<br>Titulo: " + meetingr.getTitulo() +
                "<br>Objectivo: " + meetingr.getResultado() + "<br>Localizacao: " + meetingr.getLocalizacao() + 
                "<br>Data: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(meetingr.getData()) + "<br>Duracao: " + meetingr.getDuracao() + "<br>Users presentes: "
                + meetingr.getPessoal().toString() + "";
            session.put("meeting_adicionado_string", paraimprimir);
            session.put("id_meeting", (meetingr.getId()));
            session.put("agendaitems", meetingr.getItems().get(0).getDescricao());
            return "success";
        }
        else {
            return "error";
        }
    }

    public Date convertToDate (String unformattedData){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            cal.setTime(sdf.parse(unformattedData));// all done
            return cal.getTime();
        } catch (ParseException e) {
            return null;
        }
    }
    
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setObjectivo(String objectivo) {
        this.objectivo = objectivo;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getObjectivo() {
        return objectivo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getData() {
        return data;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }
    
    
    
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


    public Map<String, Object> getSession() {
        return session;
    }
    
    
}
