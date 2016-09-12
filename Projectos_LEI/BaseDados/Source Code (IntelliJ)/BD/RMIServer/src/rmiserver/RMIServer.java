package rmiserver;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {
	
	ArrayList<UserLogin> Clientes_registados_list= new ArrayList<UserLogin>();
	ArrayList<Meeting> reunioes = new ArrayList<Meeting>();
        static Connection conn;
	
	@SuppressWarnings("unchecked")
	protected RMIServer() throws RemoteException, IOException, ClassNotFoundException {
		super();
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
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * from user where username=? and password=?;");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()){

				return null;
			}
			rs.first();
			UserLogin tmp = new UserLogin (rs.getInt("id"),rs.getString("username"), rs.getString("password"));
			conn.commit();
			return tmp;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}


		/*int i;
		for (i=0; i<Clientes_registados_list.size(); i++){
			if ((Clientes_registados_list.get(i).getUsername()).equals(username) && (Clientes_registados_list.get(i).getPassword()).equals(password))
				return Clientes_registados_list.get(i);
		}
		return null;
		*/
		
	}
	
	public UserLogin registar_conta(String username, String password) throws RemoteException, ClassNotFoundException, FileNotFoundException, IOException{ /*return true para sucesso e false para conta existente*/
		try {
			int new_user_id;
			conn.setAutoCommit(false);

			CallableStatement cstmt = conn.prepareCall("{? = call create_account(?,?)}");
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, username);
			cstmt.setString(3, password);
			cstmt.execute();
			new_user_id = cstmt.getInt(1);

			if (new_user_id < 1)
				return null; // Já existe

			/*PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT username from user where username=? FOR UPDATE;");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				return null;
			}
			stmt = RMIServer.conn.prepareStatement("INSERT into user(username,password) VALUES (?,?);",Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				new_user_id = rs.getInt(1);
			}
			else{
				return null;
			}*/
			UserLogin aux = new UserLogin(new_user_id,username, password);
			conn.commit();
			return aux;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}


		/*
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
		*/
	}
	
	public synchronized Meeting agendar_reuniao(Meeting m, UserLogin user) throws RemoteException, ClassNotFoundException, FileNotFoundException, IOException {
		try {
			int new_meeting_id;
			conn.setAutoCommit(false);

			CallableStatement cstmt = conn.prepareCall("{? = call add_meeting(?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, m.getTitulo());
			cstmt.setString(3, m.getResultado());
			cstmt.setTimestamp(4, new java.sql.Timestamp(m.getData().getTime()));
			cstmt.setString(5, m.getDuracao());
			cstmt.setString(6, m.getLocalizacao());
			cstmt.setInt(7, user.getId());
			cstmt.execute();
			new_meeting_id = cstmt.getInt(1);
			m.setId(new_meeting_id);
			m.getPessoal().add(user.getUsername());

			// Adicionar o item_default ao arraylist
			m.getItems().add(new AgendaItem("Any other business",1));
			/*

			//Inserir reuniao

			PreparedStatement stmt = RMIServer.conn.prepareStatement("INSERT into meeting(title,outcome,date,duration,location,leader_id,finished) VALUES (?,?,?,?,?,?,0);",Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, m.getTitulo());
			stmt.setString(2, m.getResultado());
			stmt.setTimestamp(3, new java.sql.Timestamp(m.getData().getTime()));
			stmt.setString(4, m.getDuracao());
			stmt.setString(5, m.getLocalizacao());
			stmt.setInt(6, user.getId());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				new_meeting_id = rs.getInt(1);
				m.setId(new_meeting_id);
			}
			else{
				return null;
			}

			// Associar o utilizador à reunião

			stmt = RMIServer.conn.prepareStatement("INSERT into meeting_user(id_meeting,id_user,accepted) VALUES (?,?,1);");
			stmt.setInt(1, new_meeting_id);
			stmt.setInt(2, user.getId());
			stmt.executeUpdate();
			m.getPessoal().add(user.getUsername());

			// Adicionar o item_default ao arraylist
			m.getItems().add(new AgendaItem("Any other business",1));

			*/

			conn.commit();
			return m;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/*
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		
		m.setId(reunioes.size() +1);
		m.addPessoal(user.getUsername());
		reunioes.add(m);
		
		
		//O id é o tamanho das reunioes existentes+1, visto nao dar para apagar reunioes, funca
		
		//oos.writeObject(reunioes);
		//oos.close();
		return m;

		*/
	}
	
	public String adicinar_item(int id_reuniao,String item, int user_id)throws IOException{
		try {
			int new_item_id;
			conn.setAutoCommit(false);

			CallableStatement cstmt = conn.prepareCall("{? = call add_item(?,?,?)}");
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setInt(2, id_reuniao);
			cstmt.setString(3, item);
			cstmt.setInt(4, user_id);
			cstmt.execute();
			new_item_id = cstmt.getInt(1);
			if (new_item_id < 1)
				return null;

			/*
			//Vamos ver se o gajo tá na reunião

			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, user_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				// Não pertence à reunião
				return null;
			}
			else {
			*/

				/*

				//Inserir item

				stmt = RMIServer.conn.prepareStatement("INSERT into item(name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, item);
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					new_item_id = rs.getInt(1);
				} else {
					return null;
				}

				// Associar o item à reunião

				stmt = RMIServer.conn.prepareStatement("INSERT into meeting_item(id_meeting,id_item) VALUES (?,?);");
				stmt.setInt(1, id_reuniao);
				stmt.setInt(2, new_item_id);
				stmt.executeUpdate();

				*/

			conn.commit();
			return "ok";
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public synchronized boolean modificar_item(int id_reuniao, int id_item, String descricao_item, int user_id)throws IOException{
		try {
			conn.setAutoCommit(false);

			//Vamos ver se o gajo tá na reunião

			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, user_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				// Não pertence à reunião
				return false;
			}
			else{

				stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_item m WHERE m.id_meeting = ? AND m.id_item = ?");
				stmt.setInt(1, id_reuniao);
				stmt.setInt(2, id_item);
				ResultSet rs2 = stmt.executeQuery();
				if (!rs2.next()){
					// Não existe esse item nessa reunião
					return false;
				}
				else{

					stmt.executeUpdate("LOCK TABLE item WRITE");

					// Alterar o item
					stmt = RMIServer.conn.prepareStatement("UPDATE item SET name = ? WHERE id = ? ;");
					stmt.setString(1, descricao_item);
					stmt.setInt(2,id_item);
					stmt.executeUpdate();

					stmt.executeUpdate("UNLOCK TABLES");
				}
			}

			conn.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		/*
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getItems().get(id_item).setDescricao(descricao_item);
		//oos.writeObject(reunioes);
		//oos.close();
		*/
	}

	public synchronized boolean eliminar_item(int id_reuniao, int id_item, int user_id)throws IOException{
		try {
			conn.setAutoCommit(false);
			CallableStatement cstmt = conn.prepareCall("{? = call delete_item(?,?,?)}");
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setInt(2, id_reuniao);
			cstmt.setInt(3, id_item);
			cstmt.setInt(4, user_id);
			cstmt.execute();
			int result = cstmt.getInt(1);
			if (result < 1)
				return false;

			/*

			//Vamos ver se o gajo tá na reunião

			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, user_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				// Não pertence à reunião
				return false;
			}
			else{
				stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_item m WHERE m.id_meeting = ? AND m.id_item = ?");
				stmt.setInt(1, id_reuniao);
				stmt.setInt(2, id_item);
				ResultSet rs2 = stmt.executeQuery();
				if (!rs2.next()){
					// Não existe esse item nessa reunião
					return false;
				}
				else{
					// Eliminar o item
					stmt = RMIServer.conn.prepareStatement("DELETE FROM item WHERE id = ?;");
					stmt.setInt(1, id_item);
					stmt.executeUpdate();
					// Eliminar as relações
					stmt = RMIServer.conn.prepareStatement("DELETE FROM meeting_item WHERE id_item = ? ;");
					stmt.setInt(1, id_item);
					stmt.executeUpdate();
				}
			}
			*/
			conn.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		/*
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).getItems().remove(id_item);
		//oos.writeObject(reunioes);
		//oos.close();
		*/
		
	}
	
        @Override
	public ArrayList<String> mostrar_items(int id_reuniao) throws RemoteException{
		ArrayList<String> items = new ArrayList<String>();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * from item i,meeting_item r where r.id_meeting=? and i.id = r.id_item;");
			stmt.setInt(1, id_reuniao);
			ResultSet rs = stmt.executeQuery();

			rs.first();
			do{
				items.add(rs.getString("name"));

			}while(rs.next());
			conn.commit();
			return items;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/*if (i>=reunioes.get(id_reuniao).getItems().size())
			return null;
		return "" + i + "-[" + reunioes.get(id_reuniao).getItems().get(i).getUsername() + "] - " + reunioes.get(id_reuniao).getItems().get(i).getDescricao() + " || Decisão chave: " + reunioes.get(id_reuniao).getItems().get(i).getKey();
		*/
	}
	
        @Override
	public boolean adicinar_actionitem(int id_reuniao, String target_user, String descricao, int user_id) throws IOException{
		try {
			int new_action_item_id;
			int target_user_id;
			conn.setAutoCommit(false);

			//Vamos ver se o gajo tá na reunião

			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, user_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				// Não pertence à reunião
				return false;
			}

			stmt = RMIServer.conn.prepareStatement("SELECT * from user where username=?;");
			stmt.setString(1, target_user);
			rs = stmt.executeQuery();
			if (!rs.next()){
				// NÃO EXISTE NINGUEM COM ESSE NOME
				return false;
			}
			target_user_id = rs.getInt("id");

			CallableStatement cstmt = conn.prepareCall("{call add_action_item(?,?,?)}");
			cstmt.setString(1, descricao);
			cstmt.setInt(2, id_reuniao);
			cstmt.setInt(3, target_user_id);
			cstmt.execute();

			/*

			//Adicionar action item

			stmt = RMIServer.conn.prepareStatement("INSERT into action_item(name,is_done) VALUES (?,0);",Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, descricao);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				new_action_item_id = rs.getInt(1);
			}
			else{
				return false;
			}

			// Associar o target user ao action item
			stmt = RMIServer.conn.prepareStatement("INSERT into action_item_user(id_action_item,id_meeting,id_user) VALUES (?,?,?);");
			stmt.setInt(1, new_action_item_id);
			stmt.setInt(2, id_reuniao);
			stmt.setInt(3, target_user_id);
			stmt.executeUpdate();

			*/

			conn.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		/*
		//FileOutputStream fout = new FileOutputStream("reunioes.tmp");
		//ObjectOutputStream oos = new ObjectOutputStream(fout);
		reunioes.get(id_reuniao).addActionitems(actionitem);
		//oos.writeObject(reunioes);
		//oos.close();
		*/
		
	}
	
        @Override
	public ArrayList<ActionItem> mostrar_meus_action_items(int user_id) throws RemoteException{
		ArrayList<ActionItem> a= new ArrayList<ActionItem>();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * from action_item a, action_item_user r where a.id=r.id_action_item AND r.id_user = ?;");
			stmt.setInt(1, user_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()){

				return a;
			}
			do{
				a.add(new ActionItem(rs.getString("name"),rs.getInt("is_done"),rs.getInt("id")));
			}while(rs.next());
			conn.commit();
			return a;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/*if (i>=reunioes.get(id_reuniao).getActionitems().size())
			return null;
		return "[" + reunioes.get(id_reuniao).getActionitems().get(i).getUsername() + "] - " + reunioes.get(id_reuniao).getActionitems().get(i).getDescricao();
		*/
	}

        @Override
	public boolean convidar_user(String convidado, int id_reuniao, int inviter_id) throws IOException{
		try {

			conn.setAutoCommit(false);

			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, inviter_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				// Não pertence à reunião
				return false;
			}

			stmt = RMIServer.conn.prepareStatement("SELECT * from user where username=?;");
			stmt.setString(1, convidado);
			rs = stmt.executeQuery();
			if (!rs.next()){
				// NÃO EXISTE NINGUEM COM ESSE NOME
				return false;
			}
			rs.first();
			int user_id  = rs.getInt("id");


			//Inserir item

			stmt = RMIServer.conn.prepareStatement("INSERT into meeting_user(id_meeting,id_user,accepted) VALUES (?,?,0);");
			stmt.setInt(1, id_reuniao);
			stmt.setInt(2, user_id);
			stmt.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		/*

		/// /convidado.getConvites().add(new Convite(meeting));

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
		*/
	}
	
        @Override
	public Meeting buscar_reuniao(int id, UserLogin user) throws RemoteException{
		/*for (int i=0; i<reunioes.size(); i++){
			if ((reunioes.get(i).getId() == id) && (reunioes.get(i).getUserlogin().getUsername().equals(user.getUsername())))
				return reunioes.get(i);
		}
		return null;*/
			return null;
	}
	
	@SuppressWarnings("unused")
        @Override
	public boolean aceitar_convite(int id, UserLogin user) throws IOException{
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * from meeting_user m WHERE m.id_meeting = ? AND m.id_user = ?;");
			stmt.setInt(1, id);
			stmt.setInt(2,user.getId());
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()){
				// NÃO ESTAS CONVIDADO PARA ESSA REUNIAO
				return false;
			}

			//Aceitar

			stmt = RMIServer.conn.prepareStatement("UPDATE meeting_user SET accepted = 1 WHERE id_meeting = ? AND id_user = ? ;");
			stmt.setInt(1, id);
			stmt.setInt(2,user.getId());
			stmt.executeUpdate();

			conn.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

             /*   for(int i=0; i<Clientes_registados_list.size(); i++){
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
            return false;*/
	}
	
	public ArrayList<Meeting> mostrar_reunioes_a_que_vou(UserLogin user,int vou) throws RemoteException{
		ArrayList<Meeting> m= new ArrayList<Meeting>();
		try {
			conn.setAutoCommit(false);
			String resultado="";
			PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * from meeting m,meeting_user r WHERE r.id_user = ? AND accepted = ? AND m.id = r.id_meeting;");
			stmt.setInt(1, user.getId());
			stmt.setInt(2, vou);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()){
				return null;
			}
			rs.first();
			do {
				Meeting tmp = new Meeting(rs.getString("title"), rs.getString("outcome"), rs.getString("location"), new java.util.Date(rs.getTimestamp("date").getTime()), rs.getString("duration"));
				int current_meeting_id = rs.getInt("id");
				tmp.setId(rs.getInt("id"));


				// VAI BUSCAR OS USERS DESTA REUNIAO
				stmt = RMIServer.conn.prepareStatement("SELECT * from user u, meeting m,meeting_user r WHERE r.id_meeting = ? AND accepted = 1 AND u.id = r.id_user AND m.id = r.id_meeting;");
				stmt.setInt(1, current_meeting_id);
				ResultSet rs_tmp = stmt.executeQuery();

				if (rs_tmp.next()){
					do {
						tmp.getPessoal().add(rs_tmp.getString("username"));
					} while (rs_tmp.next());
				}

				// VAI BUSCAR OS ITEMS DESTA REUNIAO

				stmt = RMIServer.conn.prepareStatement("SELECT * from item i, meeting m,meeting_item r WHERE r.id_meeting = ? AND i.id = r.id_item AND m.id = r.id_meeting;");
				stmt.setInt(1, current_meeting_id);
				ResultSet rs_tmp2 = stmt.executeQuery();

				if (rs_tmp2.next()) {
					do {
						AgendaItem item_a_adicionar = new AgendaItem(rs_tmp2.getString("name"),rs_tmp2.getInt("id"));

						// VAI BUSCAR A KEY DECISION DESTE ITEM

						stmt = RMIServer.conn.prepareStatement("SELECT * FROM key_decisions WHERE id_item = ?;");
						stmt.setInt(1, rs_tmp2.getInt("id"));
						ResultSet rs_tmp3 = stmt.executeQuery();
						if (rs_tmp3.next()){
							item_a_adicionar.setKey(rs_tmp3.getString("decision"));
						}
						tmp.getItems().add(item_a_adicionar);

					} while (rs_tmp2.next());
				}

				m.add(tmp);
			}while(rs.next());

			conn.commit();
			return m;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (conn != null) {
				try {
					System.err.print("Error ocurred. Transaction is being rollbacked.");
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/*if (id >= reunioes.size())
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
         */
	}
	
        public String correr_actionitems(int id_reuniao, int id_actionitem, String username)throws RemoteException{

           /* if (id_reuniao >= reunioes.size()){
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
            }*/
			return "";
        }
        
        public boolean realizar_tarefa(int id_actionitem,int user_id) throws RemoteException{
			try {
				conn.setAutoCommit(false);

				//Vamos ver se o gajo realmente tem o action item

				PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM action_item_user r WHERE r.id_action_item=? AND r.id_user = ?");
				stmt.setInt(1, id_actionitem);
				stmt.setInt(2, user_id);
				ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					// Não tem este action item
					return false;
				}

				stmt = RMIServer.conn.prepareStatement("UPDATE action_item SET is_done = 1 WHERE id = ?;");
				stmt.setInt(1, id_actionitem);
				stmt.executeUpdate();

				conn.commit();
				return true;
			} catch (SQLException ex) {
				ex.printStackTrace();
				if (conn != null) {
					try {
						System.err.print("Error ocurred. Transaction is being rollbacked.");
						conn.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
        }
       
        public boolean correr_usernames(String username) throws RemoteException{
            for(int i=0; i<Clientes_registados_list.size(); i++){
                if((Clientes_registados_list.get(i).getUsername()).equals(username)){
                    return true;
                }
            }
            return false;
        }

        public boolean addkey(int id_reuniao, int id_item, String key, int user_id) throws RemoteException{
			try {
				conn.setAutoCommit(false);

				//Vamos ver se o gajo tá na reunião

				PreparedStatement stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_user m WHERE m.id_meeting = ? AND m.id_user = ? AND accepted = 1");
				stmt.setInt(1, id_reuniao);
				stmt.setInt(2, user_id);
				ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					// Não pertence à reunião
					return false;
				}
				else{
					stmt = RMIServer.conn.prepareStatement("SELECT * FROM meeting_item m WHERE m.id_meeting = ? AND m.id_item = ?");
					stmt.setInt(1, id_reuniao);
					stmt.setInt(2, id_item);
					ResultSet rs2 = stmt.executeQuery();
					if (!rs2.next()){
						// Não existe esse item nessa reunião
						return false;
					}
					else{
						// Adicionar key decision
						stmt = RMIServer.conn.prepareStatement("INSERT INTO key_decisions(id_item,decision) VALUES (?,?);");
						stmt.setInt(1, id_item);
						stmt.setString(2, key);
						stmt.executeUpdate();
					}
				}

				conn.commit();
				return true;
			} catch (SQLException ex) {
				ex.printStackTrace();
				if (conn != null) {
					try {
						System.err.print("Error ocurred. Transaction is being rollbacked.");
						conn.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return false;
			}

            /*reunioes.get(id_reuniao).getItems().get(id_item).setKey(key);
            return true;*/
        }
        
	public static void main(String args[]) throws RemoteException, IOException, ClassNotFoundException{
		if (args.length < 3){
			System.out.println("<DB IP> <DB Name> <DB Username> (<DB Password>)");
			System.exit(0);
		}
		String db_password;
		if (args.length <= 3)
			db_password = "";
		else
			db_password = args[3];
		try{
			RMIServer rmii = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("receber", rmii);
                        //r.rebind("rmi://"+rmi_hostname+":"+rmi_port+"/receber", rmi);
			System.out.println("RMIServer ready...");
                        Class.forName("com.mysql.jdbc.Driver") ;
			try {
				RMIServer.conn = DriverManager.getConnection("jdbc:mysql://"+args[0]+"/"+args[1], args[2], db_password);
				System.out.println("Connected to the MySQL Database.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}catch(RemoteException re){
			System.out.println("RemoteException on RMIServer.main: " + re);
		}

		
		
		
	}


}
