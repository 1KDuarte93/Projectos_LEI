package cenas.action;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpSession;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;
import rmiserver.UserLogin;
import ws.GetHttpSessionConfigurator;

@ServerEndpoint(value = "/cenas.action", configurator=GetHttpSessionConfigurator.class)
public class WebSocketAnnotation{
    private String username;
    private Session sessionchat;
    private HttpSession session;
    UserLogin temp;
    String tempS;
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet();
    
    public WebSocketAnnotation() {

    }

    @OnOpen
    public void start(Session sessionchat, EndpointConfig config) {
        System.out.println("hellobro");
        this.sessionchat = sessionchat;
        session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //this.username = username;
        users.add(this); // new user connected
        temp = (UserLogin) session.getAttribute("usersession");
        username= temp.getUsername();
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
        users.remove(this); // this user is gone
        
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	String m = new StringBuffer(message).toString();
    	sendMessage("[" + username + "] " + m);
    }
    
    @OnError
    public void handleError(Throwable t) {
        users.remove(this); // this user is gone
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
        Iterator <WebSocketAnnotation> it = users.iterator();
        WebSocketAnnotation temp;
    	// uses *this* object's sessionchat to call sendText()
    	try {
            while (it.hasNext()){
                temp = it.next();
		temp.sessionchat.getBasicRemote().sendText(text);
            }
        } catch (IOException e) {
            // clean up once the WebSocket connection is closed
            try {
		this.sessionchat.close();
            } catch (IOException e1) {
		e1.printStackTrace();
            }
	}
    }
}
