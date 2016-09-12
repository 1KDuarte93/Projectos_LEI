/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenas.action;

import cenas.service.AceitarConviteBean;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import rmiserver.UserLogin;

/**
 *
 * @author kduarte
 */
public class AceitarConvite extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    String idconvite;
    AceitarConviteBean aceitarCBean;
    
    @Override
    public String execute(){
        boolean aux;
        UserLogin user = (UserLogin) session.get("usersession");
        aceitarCBean = new AceitarConviteBean();
        aceitarCBean.setIdconvite(idconvite);
        aceitarCBean.setUser(user);
        aux = aceitarCBean.aceitarconvite();
        
        if (aux){
            return "success";
        }
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

    public String getIdconvite() {
        return idconvite;
    }

    public void setIdconvite(String idconvite) {
        this.idconvite = idconvite;
    }

    public AceitarConviteBean getAceitarCBean() {
        return aceitarCBean;
    }

    public void setAceitarCBean(AceitarConviteBean aceitarCBean) {
        this.aceitarCBean = aceitarCBean;
    }
    
    
}
