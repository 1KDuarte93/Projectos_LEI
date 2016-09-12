package rmiserver;

import java.rmi.Remote;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;



public interface RMIInterface extends Remote{
	public UserLogin registar_conta(String username, String password) throws RemoteException, FileNotFoundException, ClassNotFoundException, IOException;
	public UserLogin consultar_conta(String username, String password) throws RemoteException;
	public Meeting agendar_reuniao(Meeting m, UserLogin user) throws RemoteException, FileNotFoundException, IOException, ClassNotFoundException;
	public void adicinar_item(int id_reuniao, AgendaItem item)throws RemoteException, FileNotFoundException, IOException;
	public void modificar_item(int id_reuniao, int id_item, String descricao, String username)throws RemoteException, FileNotFoundException, IOException;
	public void eliminar_item(int id_reuniao, int id_item)throws RemoteException, FileNotFoundException, IOException;
	public String mostrar_items(int id_reuniao, int i) throws RemoteException;
	public void adicinar_actionitem(int id_reuniao, ActionItem actionitem)throws RemoteException, FileNotFoundException, IOException;
	public void modificar_actionitem(int id_reuniao, int id_item, String descricao)throws RemoteException, FileNotFoundException, IOException;
	public void eliminar_actionitem(int id_reuniao, int id_item)throws RemoteException, FileNotFoundException, IOException;
	public String mostrar_actionitems(int id_reuniao, int i) throws RemoteException;
	public String mostrar_reunioes(int i) throws RemoteException;
	public boolean convidar_user(String convidado, Meeting meeting) throws RemoteException, FileNotFoundException, IOException;
	public String imprimir_minhas_reunioes(UserLogin user, int i) throws RemoteException;
	public Meeting buscar_reuniao(int id, UserLogin user) throws RemoteException;
	public boolean aceitar_convite(int id, UserLogin user) throws RemoteException, FileNotFoundException, IOException;
	public String imprimir_convites(int id, UserLogin user) throws RemoteException;
	public ArrayList<String> mostrar_reunioes_a_que_vou(int i, UserLogin user) throws RemoteException;
        public String correr_actionitems(int id_reuniao, int id_actionitem, String username)throws RemoteException;
        public boolean realizar_tarefa(int id_reuniao, int id_actionitem) throws RemoteException;
        public boolean correr_usernames(String username) throws RemoteException;
        public boolean addkey(int id_reuniao, int id_item, String key) throws RemoteException;
}
