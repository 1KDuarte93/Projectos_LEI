package rmiserver;

import java.io.Serializable;


public class Convite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Meeting meeting;
	//nao sei mais o que
	public Convite(Meeting meeting){
		this.meeting = meeting;
	}
	
	public Meeting getMeeting(){
		return meeting;
	}
}
