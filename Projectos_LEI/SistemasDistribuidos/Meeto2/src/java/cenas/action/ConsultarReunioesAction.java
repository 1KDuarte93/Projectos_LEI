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

import cenas.service.ConsultarReunioesBean;
import cenas.service.LoginBean;
import com.opensymphony.xwork2.ActionSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;
/**
 *
 * @author kduarte
 */
public class ConsultarReunioesAction extends ActionSupport implements SessionAware{
    private ConsultarReunioesBean consultarBean;
    private Map<String, Object> session;
    
    
    @Override
    public String execute() throws MalformedURLException, RemoteException, NotBoundException{
        ArrayList<String> resposta;
        UserLogin user = (UserLogin)session.get("usersession");
        if (user == null){
            return "failure";
        }
        
        consultarBean = new ConsultarReunioesBean();
        consultarBean.setUser(user);
        resposta = consultarBean.consultarReunioesfunction();
        
        //Print das reunioes
        session.put("minhasreunioes", resposta);
        
        return "success";
    }
    
    public ConsultarReunioesBean getConsultarBean() {
        return consultarBean;
    }

    public void setConsultarBean(ConsultarReunioesBean consultarBean) {
        this.consultarBean = consultarBean;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return session;
    }
    
    
}
