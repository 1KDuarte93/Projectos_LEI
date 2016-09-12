package rmiserver;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;




public class RMIServer extends UnicastRemoteObject implements RMIInterface {
	
	ArrayList<UserLogin> Clientes_registados_list= new ArrayList<UserLogin>();
	ArrayList<Meeting> reunioes = new ArrayList<Meeting>();
	
	@SuppressWarnings("unchecked")
	protected RMIServer() throws RemoteException, IOException, ClassNotFoundException {
		super();
                this.registar_conta("admin", "admin");
                this.registar_conta("kevin", "duarte");
		/*FileInputStream fin = new FileInputStream("reunioes.tmp");
		ObjectInputStream ois = new ObjectInputStream(fin);
		
		FileInputStream fin2 = new FileInputStream("clientes.tmp");
		ObjectInputStream ois2 = new ObjectInputStream(fin2);
		
		reunioes = (ArrayList<Meeting>) ois.readObject();
		Clientes_registados_list = (ArrayList<UserLogin>) ois2.readObject();
		System.out.println("Carregado!");
		
		ois.close();
		ois2.close();*/
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	
	public UserLogin consultar_conta(String username, String password) throws RemoteException{
		int i;
		for (i=0; i<Clientes_registados_list.size(); i++){
			if ((Clientes_registados_list.get(i).getUsername()).equals(username) && (Clientes_registados_list.get(i).getPassword()).equals(password))
				return Clientes_registados_list.get(i);
		}
		return null;
		
	}
	
	public UserLogin registar_conta(String username, String password) throws RemoteException, ClassNotFoundException, FileNotFoundException, IOException{ /*return true para sucesso e false para conta existente*/
		int i;
		//FileOutputStream fout = new FileOutputStream("clientes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		
		
		for (i=0; i<Clientes_registados_list.size(); i++){
			if ((Clientes_registados_list.get(i).getUsername()).equals(username)){
				//oos.close();
				return null; //null -> Cliente ja existente
			}
		}
		UserLogin aux = new UserLogin(username, password);
		Clientes_registados_list.add(aux);
		//oos.writeObject(Clientes_registados_list);
		//oos.close();
		return aux;
		
	}
	
	public synchronized Meeting agendar_reuniao(Meeting m, UserLogin user) throws RemoteException, ClassNotFoundException, FileNotFoundException, IOException {
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		
		m.setId(reunioes.size() +1);
		m.addPessoal(user.getUsername());
		reunioes.add(m);
		
		
		//O id é o tamanho das reunioes existentes+1, visto nao dar para apagar reunioes, funca
		
		//oos.writeObject(reunioes);
		//oos.close();
		return m;
	}
	
	public void adicinar_item(int id_reuniao, AgendaItem item)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).addItem(item);
		//oos.writeObject(reunioes);
		//oos.close();
	}

	public synchronized void modificar_item(int id_reuniao, int id_item, String descricao_item, String username)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getItems().get(id_item).setDescricao(descricao_item);
                reunioes.get(id_reuniao).getItems().get(id_item).setUsername(username);
		//oos.writeObject(reunioes);
		//oos.close();
	}

	public synchronized void eliminar_item(int id_reuniao, int id_item)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getItems().remove(id_item);
		//oos.writeObject(reunioes);
		//oos.close();
		
	}
	
        @Override
	public String mostrar_items(int id_reuniao, int i) throws RemoteException{
		if (i>=reunioes.get(id_reuniao).getItems().size())
			return null;
		return "" + i + "-[" + reunioes.get(id_reuniao).getItems().get(i).getUsername() + "] - " + reunioes.get(id_reuniao).getItems().get(i).getDescricao() + " || Decisão chave: " + reunioes.get(id_reuniao).getItems().get(i).getKey();
	}
	
        @Override
	public void adicinar_actionitem(int id_reuniao, ActionItem actionitem)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).addActionitems(actionitem);
		//oos.writeObject(reunioes);
		//oos.close();
		
	}

        @Override
	public synchronized void modificar_actionitem(int id_reuniao, int id_item, String descricao_item)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getActionitems().get(id_item).setDescricao(descricao_item);
		//oos.writeObject(reunioes);
		//oos.close();
	}

        @Override
	public synchronized void eliminar_actionitem(int id_reuniao, int id_item)throws IOException{
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getActionitems().remove(id_item);
		//oos.writeObject(reunioes);
		//oos.close();
	}
	
        @Override
	public String mostrar_actionitems(int id_reuniao, int i) throws RemoteException{
		if (i>=reunioes.get(id_reuniao).getActionitems().size())
			return null;
		return "[" + reunioes.get(id_reuniao).getActionitems().get(i).getUsername() + "] - " + reunioes.get(id_reuniao).getActionitems().get(i).getDescricao();
	}

        @Override
	public String mostrar_reunioes(int i) throws RemoteException{
		if (i >= reunioes.size())
			return null;
		return "[" + reunioes.get(i).getId() + "] - " + reunioes.get(i).getTitulo() + " -Autor: " + reunioes.get(i).getUserlogin().getUsername() + "||Quem vai:" + reunioes.get(i).getPessoal();
	}

        @Override
	public boolean convidar_user(String convidado, Meeting meeting) throws IOException{
		//convidado.getConvites().add(new Convite(meeting));
		
		int i;
		for (i=0; i<Clientes_registados_list.size(); i++){
			if ((Clientes_registados_list.get(i).getUsername()).equals(convidado)){
				//FileOutputStream fout = new FileOutputStream("clientes.tmp");
				//ObjectOutputStream oos = new ObjectOutputStream(fout);
				Clientes_registados_list.get(i).addConvite(new Convite(meeting));
				//oos.writeObject(Clientes_registados_list);
				//oos.close();
				return true;
				}
		}
		return false;
	}
	
        @Override
	public String imprimir_minhas_reunioes(UserLogin user, int i) throws RemoteException{
		if (i>=reunioes.size())
			return null;
		if (reunioes.get(i).getUserlogin().getUsername().equals(user.getUsername())){
			return "[" + reunioes.get(i).getId() + "] - " + reunioes.get(i).getTitulo();
		}
		else
			return "-1";
			
	}
	
        @Override
	public Meeting buscar_reuniao(int id, UserLogin user) throws RemoteException{
		for (int i=0; i<reunioes.size(); i++){
			if ((reunioes.get(i).getId() == id) && (reunioes.get(i).getUserlogin().getUsername().equals(user.getUsername())))
				return reunioes.get(i);
		}
		return null;
	}
	
	@SuppressWarnings("unused")
        @Override
	public boolean aceitar_convite(int id, UserLogin user) throws IOException{
                for(int i=0; i<Clientes_registados_list.size(); i++){
                    if ((Clientes_registados_list.get(i).getUsername()).equals(user.getUsername())){
                        if(id >= user.getConvites().size() || id<0)
                            return false;
                        for(int j=0; j<reunioes.size();j++){
                            if(reunioes.get(j).getData().equals(Clientes_registados_list.get(i).getConvites().get(id).getMeeting().getData()) &&
					reunioes.get(j).getLocalizacao().equals(Clientes_registados_list.get(i).getConvites().get(id).getMeeting().getLocalizacao()) &&
					reunioes.get(j).getTitulo().equals(Clientes_registados_list.get(i).getConvites().get(id).getMeeting().getTitulo())){
                                reunioes.get(j).addPessoal(Clientes_registados_list.get(i).getUsername());
                                Clientes_registados_list.get(i).getConvites().remove(id);
                                return true;
                            }
                        }
                    }
                }
            return false;
	}
	
        @Override
	public String imprimir_convites(int id, UserLogin user) throws RemoteException{
            for(int i=0; i<Clientes_registados_list.size();i++){
                if((Clientes_registados_list.get(i).getUsername()).equals(user.getUsername())){
                    if(id>=Clientes_registados_list.get(i).getConvites().size())
                        return null;
                    else{
                        return "[" + id + "] - " + Clientes_registados_list.get(i).getConvites().get(id).getMeeting().getTitulo() +
                                "<br/>Autor: " + Clientes_registados_list.get(i).getConvites().get(id).getMeeting().getUserlogin().getUsername() + "";
                    }
                }
            }
            return null;
	}
	
	public ArrayList<String> mostrar_reunioes_a_que_vou(int id, UserLogin user) throws RemoteException{
		if (id >= reunioes.size())
			return null;
		else if (reunioes.get(id).getPessoal().contains(user.getUsername())){
                    ArrayList<String> sarray = new ArrayList<String>();
                    sarray.add("[" + id + "] - " + reunioes.get(id).getTitulo());
                    sarray.add("Autor: " + reunioes.get(id).getUserlogin().getUsername());
                    sarray.add("Objectivos: " + reunioes.get(id).getResultado());
                    sarray.add("Data: " + reunioes.get(id).getData());
                    sarray.add("Duração: " + reunioes.get(id).getData());
                    sarray.add("Hora de inicio: " + reunioes.get(id).getData());
                    sarray.add("Localização: " + reunioes.get(id).getLocalizacao());
                    sarray.add("Users presentes: " + reunioes.get(id).getPessoal());
                    return sarray;
		}
                else{
                    ArrayList<String> sarray = new ArrayList<String>();
                    sarray.add("-1");
                    return sarray;
                }	
	}
	
        public String correr_actionitems(int id_reuniao, int id_actionitem, String username)throws RemoteException{
            if (id_reuniao >= reunioes.size()){
                return null;
            }
            if (id_actionitem >= reunioes.get(id_reuniao).getActionitems().size()){
                return "-2";
            }
            if ((reunioes.get(id_reuniao).getActionitems().get(id_actionitem).getUsername()).equals(username)){
                if (reunioes.get(id_reuniao).getActionitems().get(id_actionitem).getDone())
                    return ("Reuniao:" + reunioes.get(id_reuniao).getTitulo() +" | Descrição: " + reunioes.get(id_reuniao).getActionitems().get(id_actionitem).getDescricao() + " - (Feito!)");
                else
                    return ("Reuniao:" + reunioes.get(id_reuniao).getTitulo() +" | Descrição: " + reunioes.get(id_reuniao).getActionitems().get(id_actionitem).getDescricao() + " - (Por fazer)");
            }
            else{
                return "-1";
            }
        }
        
        public boolean realizar_tarefa(int id_reuniao, int id_actionitem) throws RemoteException{
            reunioes.get(id_reuniao).getActionitems().get(id_actionitem).setDone(true);
            return true;
        }
       
        public boolean correr_usernames(String username) throws RemoteException{
            for(int i=0; i<Clientes_registados_list.size(); i++){
                if((Clientes_registados_list.get(i).getUsername()).equals(username)){
                    return true;
                }
            }
            return false;
        }

        public boolean addkey(int id_reuniao, int id_item, String key) throws RemoteException{
            reunioes.get(id_reuniao).getItems().get(id_item).setKey(key);
            return true;
        }
        
	public static void main(String args[]) throws RemoteException, IOException, ClassNotFoundException{
		try{
			RMIServer rmii = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("receber", rmii);
                        //r.rebind("rmi://"+rmi_hostname+":"+rmi_port+"/receber", rmi);
			System.out.println("RMIServer ready...");
		}catch(RemoteException re){
			System.out.println("RemoteException on RMIServer.main: " + re);
		}

		
		
		
	}


}
