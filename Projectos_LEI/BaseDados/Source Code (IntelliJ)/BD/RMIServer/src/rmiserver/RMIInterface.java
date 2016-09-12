package rmiserver;

import java.rmi.Remote;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface RMIInterface extends Remote{
	public UserLogin registar_conta(String username, String password) throws RemoteException, FileNotFoundException, ClassNotFoundException, IOException;
	public UserLogin consultar_conta(String username, String password) throws RemoteException;
	public Meeting agendar_reuniao(Meeting m, UserLogin user) throws RemoteException, FileNotFoundException, IOException, ClassNotFoundException;
	public String adicinar_item(int id_reuniao, String item, int user_id)throws RemoteException, FileNotFoundException, IOException;
	public boolean modificar_item(int id_reuniao, int id_item, String descricao, int user_id)throws RemoteException, FileNotFoundException, IOException;
	public boolean eliminar_item(int id_reuniao, int id_item, int user_id)throws RemoteException, FileNotFoundException, IOException;
	public ArrayList<String> mostrar_items(int id_reuniao) throws RemoteException;
	public boolean adicinar_actionitem(int id_reuniao, String target_user, String descricao, int user_id)throws RemoteException, FileNotFoundException, IOException;
	public ArrayList<ActionItem> mostrar_meus_action_items(int user_id) throws RemoteException;
	public boolean convidar_user(String convidado,int id_reuniao, int inviter_id) throws RemoteException, FileNotFoundException, IOException;
	public Meeting buscar_reuniao(int id, UserLogin user) throws RemoteException;
	public boolean aceitar_convite(int id, UserLogin user) throws RemoteException, FileNotFoundException, IOException;
	public ArrayList<Meeting> mostrar_reunioes_a_que_vou( UserLogin user, int vou) throws RemoteException;
        public String correr_actionitems(int id_reuniao, int id_actionitem, String username)throws RemoteException;
        public boolean realizar_tarefa(int id_actionitem,int user_id) throws RemoteException;
        public boolean correr_usernames(String username) throws RemoteException;
        public boolean addkey(int id_reuniao, int id_item, String key, int user_id) throws RemoteException;
}
