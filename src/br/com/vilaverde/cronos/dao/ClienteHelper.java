package br.com.vilaverde.cronos.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ClienteHelper extends DataHelper{

	private final static String CNT_LOG = "ClienteHelper";
	private final static String TABELA = "clientes";
	
	//private static final int VERSAO_SCHEMA = 36;
	
	public ClienteHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);

	}

//	public void onCreate(SQLiteDatabase db) {
//		Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//		
//		String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" id_usuario TEXT," +
//					" tipo TEXT," +
//					" nome TEXT," +
//					" cpf TEXT," +
//					" cnpj TEXT," +
//					" rg TEXT," +
//					" inscricao_estadual TEXT," +
//					" telefone_fixo TEXT," +
//					" telefone_movel TEXT," +
//					" email TEXT," +
//					" status_servidor TEXT," +
//					" responsavel TEXT," +
//					" dt_inclusao TEXT," +
//					" observacao TEXT," +
//					" rua TEXT," +
//					" numero TEXT," +
//					" bairro TEXT," +
//					" cidade TEXT," +
//					" uf TEXT," +
//					" cep TEXT," +
//					" complemento TEXT" +	
//				");";
//		
//		db.execSQL(sql);
//		
//		Log.v(CNT_LOG, "Tabela [ "+TABELA+" ] Criada com Sucesso!");
//	}

//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.v(CNT_LOG, "onUprade - Drop Table ["+TABELA+"]");
//		
//		try {
//			db.execSQL("DROP TABLE IF EXISTS " + ClienteHelper.TABELA);
//		}
//		catch (Exception error){
//			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//		}
//		
//		this.onCreate(db);
//	}


	public long inserir(Cliente cliente){
		Log.v(CNT_LOG, "Inserindo Cliente. Nome ["+cliente.getNome().toString()+"]");
		
		long linhasInseridas = 0;
		
		this.Open();
		

		      ContentValues valores = new ContentValues();
		      valores.put("tipo", cliente.getTipo());
		      valores.put("nome", cliente.getNome());
		      valores.put("telefone_fixo", cliente.getTelefoneFixo());
		      valores.put("telefone_movel", cliente.getTelefoneMovel());
		      valores.put("email", cliente.getEmail());
		      valores.put("rua", cliente.getRua());
		      valores.put("numero", cliente.getNumero());
		      valores.put("bairro", cliente.getBairro());
		      valores.put("cidade", cliente.getCidade());
		      valores.put("uf", cliente.getUf());
		      valores.put("cep", cliente.getCep());
		      valores.put("observacao", cliente.getObservacao());

		      if (cliente.getTipo() == 1){
		    	  valores.put("cpf", cliente.getCpf());
		    	  valores.put("rg", cliente.getRg());
		    	  // aqui eu zero os campos de cnpj e ie para um caso de update
		    	  valores.put("cnpj", "");
		    	  valores.put("inscricao_estadual", "");   	  
		      }
		      else {
		    	  valores.put("cnpj", cliente.getCnpj());
		    	  valores.put("inscricao_estadual", cliente.getInscricao_estadual());   	  
		    	  // aqui eu zero os campos de cpf e rg para um caso de update
		    	  valores.put("cpf", "");
		    	  valores.put("rg", "");
		    	  
		      }

      try {
		      linhasInseridas = db.insert(TABELA, null, valores);
		      
		      //getWritableDatabase().insert(TABELA, null, valores);      
		      Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
		      
//		      if (linhasInseridas < 0 ){
//		    	  // Erro no Insert
//		    	  throw new Exception("Registro não Inserido");
//		      }
	    }
	    catch (Exception error){
	    	Log.e(CNT_LOG, "Falha ao Inserir Cliente");
	    }
	    finally {
	        this.Close();
	    }
	    
	    return linhasInseridas;
	 }

	
	public List<Cliente> getListaClientes(){
		Log.v(CNT_LOG, "Listando Clientes");
		
		this.Open();
		
		Cursor c = db.query(TABELA, null, null, null, null, null, "nome");
		
		List<Cliente> clientes = bindValues(c);

		c.close();

		this.Close();
		
		Log.v(CNT_LOG, "getLista, Total [ "+ clientes.size()+" ]");
		
	    return clientes;
	 }
	
	public Cursor getClientes() {
		
		this.Open();
		
		Cursor c = db.query(TABELA, null, null, null, null, null, null);
		this.Close();
		//return db.rawQuery("select _id, nome FROM clientes ORDER BY nome", null);
		
		return c;
	}

	
	public long Alterar(Cliente cliente) {

		long linhaAlterada = 0;
		
		this.Open();
		
		try { 
			// Recupera o Id
	        int id = cliente.getId();
	        
	        // Cria o objeto valores
	        ContentValues valores = new ContentValues();
         
	        //Carregar os novos valores nos campos que serão alterados
		      valores.put("tipo", cliente.getTipo());
		      valores.put("nome", cliente.getNome());
		      valores.put("telefone_fixo", cliente.getTelefoneFixo());
		      valores.put("telefone_movel", cliente.getTelefoneMovel());
		      valores.put("email", cliente.getEmail());
		      valores.put("rua", cliente.getRua());
		      valores.put("numero", cliente.getNumero());
		      valores.put("bairro", cliente.getBairro());
		      valores.put("cidade", cliente.getCidade());
		      valores.put("uf", cliente.getUf());
		      valores.put("cep", cliente.getCep());
		      valores.put("observacao", cliente.getObservacao());
	
		      if (cliente.getTipo() == 1){
		    	  valores.put("cpf", cliente.getCpf());
		    	  valores.put("rg", cliente.getRg());
		    	  // aqui eu zero os campos de cnpj e ie para um caso de update
		    	  valores.put("cnpj", "");
		    	  valores.put("inscricao_estadual", "");   	  
		      }
		      else {
		    	  valores.put("cnpj", cliente.getCnpj());
		    	  valores.put("inscricao_estadual", cliente.getInscricao_estadual());   	  
		    	  // aqui eu zero os campos de cpf e rg para um caso de update
		    	  valores.put("cpf", "");
		    	  valores.put("rg", "");		    	  
		      }
	    
		    //Alterar o registro com base no ID
		    linhaAlterada = db.update(TABELA, valores, "_id = " + id, null);
	   		Log.v(CNT_LOG, "Alterando Cliente [ "+cliente.getBairro()+"]");
		    Log.v(CNT_LOG, "Linha Alterada ["+linhaAlterada+"], Id ["+id+"]");

	  }
	  catch (Exception e){
	  	Log.e(CNT_LOG, "Alterar - Error ["+e.getMessage()+"]");
	  }
	  finally {
	      this.Close();
	  }
 
		return linhaAlterada;
	}
 
    public void Excluir(Cliente cliente) {
    	Log.v(CNT_LOG, "Excluir - Registro ["+cliente.getId()+"]");
    	
        long id = cliente.getId();

		this.Open();
		
		try { 

	        //Exclui o registro com base no ID
	        db.delete(TABELA, "_id = " + id, null);
        
		}
		catch (Exception e){
		 	Log.e(CNT_LOG, "Excluir - Error ["+e.getMessage()+"]");
		}
		finally {
		    this.Close();
		}

    }

    
	public List<Cliente> getClientes(String q){
		Log.v(CNT_LOG, "Pesquisando Clientes. Query [ "+q+" ]");
		
		this.Open();
		
		String where = "nome LIKE ?";
        String[] selectionArgs = new String[] {"%"+q+"%"};

		
        try { 

			Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, "nome");
			List<Cliente> clientes = bindValues(c);
			c.close();
			
			Log.v(CNT_LOG, "getClientes, Total [ "+ clientes.size()+" ]");
			
		    return clientes;
		}
		catch (Exception e){
		 	Log.e(CNT_LOG, "getClientes - Error ["+e.getMessage()+"]");
		 	return null;
		}
		finally {
		    this.Close();
		}
	 }

	public Cliente getCliente(int id){
		Log.v(CNT_LOG, "Pesquisando Cliente. Id [ "+id+" ]");
		
		this.Open();
		
		String where = "_id = ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        try { 

			Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, null);
			List<Cliente> clientes = bindValues(c);
			c.close();
			
			Log.v(CNT_LOG, "getCliente. Total [ "+ clientes.size()+" ]");
			
		    return clientes.get(0);
		}
		catch (Exception e){
		 	Log.e(CNT_LOG, "getCliente - Error ["+e.getMessage()+"]");
		 	return null;
		}
		finally {
		    this.Close();
		}
	 }

	
	
	public List<Cliente> bindValues(Cursor c) {
		Log.v(CNT_LOG, "bindValues.");

		List<Cliente> lista = new ArrayList<Cliente>();
	      
		while(c.moveToNext()){
	    	  
			Cliente cliente = new Cliente();
			
			cliente.setId(c.getInt(0));
			cliente.setId_usuario(c.getString(1));
			cliente.setTipo(c.getInt(2));
			cliente.setNome(c.getString(3));
			cliente.setCpf(c.getString(4));
			cliente.setCnpj(c.getString(5));
			cliente.setRg(c.getString(6));
			cliente.setInscricao_estadual(c.getString(7));
			cliente.setTelefoneFixo(c.getString(8));
			cliente.setTelefoneMovel(c.getString(9));
			cliente.setEmail(c.getString(10));
			cliente.setStatus_servidor(c.getString(11));
			cliente.setResponsavel(c.getString(12));
			cliente.setDt_inclusao(c.getString(13));
			cliente.setObservacao(c.getString(14));
			cliente.setRua(c.getString(15));
			cliente.setNumero(c.getString(16));
			cliente.setBairro(c.getString(17));
			cliente.setCidade(c.getString(18));
			cliente.setUf(c.getInt(19));
			cliente.setCep(c.getString(20));
			cliente.setComplemento(c.getString(21));
			
			lista.add(cliente);
		}

		c.close();

		return lista;
	}
    
}
