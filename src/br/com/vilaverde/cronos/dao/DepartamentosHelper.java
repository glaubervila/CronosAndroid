package br.com.vilaverde.cronos.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vilaverde.cronos.Messages;
import br.com.vilaverde.cronos.httpclient.HttpTaskPost;
import br.com.vilaverde.cronos.model.Departamento;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.Vendedor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class DepartamentosHelper extends DataHelper{

	
	private final static String CNT_LOG = "DepartamentosHelper";
	private final static String TABELA = "departamentos";
	
	private Context context = null;
	//private static final int VERSAO_SCHEMA = 36;
	
	public DepartamentosHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);
		this.context = context;
	}

	
	public long inserir(Departamento departamento){
		Log.v(CNT_LOG, "inserir");
		long linhasInseridas = 0;
		
		this.Open();
		
		      ContentValues valores = new ContentValues();
		      valores.put("departamento", departamento.getDepartamento());

      try {
    	  
    	  // Saber se tem Id se tiver setar e usar o replace
	      if (departamento.getId() > 0){
	    	  valores.put("_id", departamento.getId());
	    	  Log.v(CNT_LOG, "Replace Departamento. Nome ["+departamento.getDepartamento().toString()+"]");
	    	  linhasInseridas = db.replace(TABELA, null, valores);
	      }
	      else {
	  		  Log.v(CNT_LOG, "Inserindo Departamento. Nome ["+departamento.getDepartamento().toString()+"]");
	    	  // se nao tiver faz insert
		      linhasInseridas = db.insert(TABELA, null, valores);
	      }
	      //getWritableDatabase().insert(TABELA, null, valores);      
	      Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
		      
	      if (linhasInseridas < 0 ){
	    	  // Erro no Insert
	    	  //throw new Exception("Registro n�o Inserido");	    	  
	      }
	      else {
	    	  return linhasInseridas;
	      }
		      
	    }
	    catch (Exception e){
	    	Log.e(CNT_LOG, "Falha ao Inserir Vendedor");
	    	e.printStackTrace();
	    	return 0;
	    }
	    finally {
	        this.Close();
	        return 0;
	    }
	 }

	public List<Departamento> getDepartamentos(){
		Log.v(CNT_LOG, "Recupera Todos os Departamentos.");
			
		this.Open();
		
//		String status = "0"; // Aberto
//		String where = "status = ?";
//        String[] selectionArgs = new String[] {status};
//
//		Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, null);

		// TODO: Trazer somentos os departamentos com produtos ativos
		Cursor c = db.query(TABELA, null, null, null, null, null, null);
	   
		if (c.getCount() > 0) {
			Log.v(CNT_LOG, "Total Departamentos [ "+c.getCount()+" ].");
			List<Departamento> departamentos = bindValues(c);
			    	  
			return departamentos;
		}
		else {
			return null;
		}
	}

//	public List<String> getArrayDepartamentos(){
//		
//		String[] a = null;
//		
//		List<Departamento> departamentos = getDepartamentos();
//		
//		List<String> list = new ArrayList<String>(departamentos.size());  
//		for (int i=0;i<=departamentos.size();i++){
//			Departamento departamento = departamentos.get(i);
//			list.add(departamento.getDepartamento());
//		}
//		
//		return list;
//		
//	}

	
	public List<Departamento> bindValues(Cursor c) {
		Log.v(CNT_LOG, "bindValues.");

		List<Departamento> lista = new ArrayList<Departamento>();
	      
		while(c.moveToNext()){
	    	  
			Departamento departamento = new Departamento();
			
			departamento.setId(c.getInt(c.getColumnIndex("_id")));
			departamento.setDepartamento(c.getString(c.getColumnIndex("departamento")));
			
			lista.add(departamento);
		}

		c.close();

		return lista;
	}

	
	
	public void sincronizarDepartamentos(){
		Log.v(CNT_LOG, "sincronizarDepartamentoes()");
		
		// Parametros
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("classe", "ClientAndroid"));
        nameValuePairs.add(new BasicNameValuePair("action", "getDepartamentos"));
		
        // Crio um Objeto TaskPost
    	HttpTaskPost httpPost = new getDepartamentosHttp();
    	// Passo os Parametros
    	httpPost.setParametros(nameValuePairs);
    	// Passo o Contexto para disparar erros
    	httpPost.setContext(this.context);
    	
    	// Primeito tentar Remoto
    	if (this.REMOTE == true){ 	
	    	String urlRemoto = this.getServerHostRemote();
	    	Log.v(CNT_LOG, "UrlRemoto = "+urlRemoto);
			httpPost.execute(urlRemoto);
    	}
    	else {
    		// Se falhar no Remoto Tentar no Local
	    	String urlLocal = this.getServerHostLocal();
	    	Log.v(CNT_LOG, "UrlLocal = "+urlLocal);
			httpPost.execute(urlLocal);    		
    	}

	}
	
	public void inserirDepartamentosJson(JSONObject json){
		Log.v(CNT_LOG, "inserirDepartamentosJson");

		int count = 0;
		int erro = 0;
		
		// Fazer o parse para array
		try {
			
			JSONArray arrayDepartamentos = (JSONArray) json.get("rows");

			
	        for (int i = 0; i < arrayDepartamentos.length(); i++) {
	        	
	        	JSONObject departamentoItem = arrayDepartamentos.getJSONObject(i);        
	            
	        	// Criando objetos departamento
	        	Departamento departamento = new Departamento();
	        	departamento.setId(departamentoItem.getInt("_id"));
	        	departamento.setDepartamento(departamentoItem.getString("departamento"));

	        	if (inserir(departamento) > 0){
        			count++;
	        	}
	        	else {
	        		erro++;
	        	}
			
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	
	
	public class getDepartamentosHttp extends HttpTaskPost {
		
		protected void onPreExecute() {
			super.onPreExecute();
			Log.v(CNT_LOG, "onPreExecute");	
	    }
		protected void onPostExecute(String[] resultado) {
			Log.v(CNT_LOG, "onPostExecute");

			if (resultado[0] == "success"){
				Log.v(CNT_LOG,"Passo 8");
				// Passando a string para o metodo que vai inserir
				taskSuccess(resultado[1]);
			}
			else {
				Log.e(CNT_LOG,"Passo 10 - o json veio vazio");
				taskFailed(resultado);
			}
		}	
	}
	
	protected void taskSuccess(String strJson){
		JSONObject json = null;
		
		try {
			json = new JSONObject(strJson);
			
			// Saber se a Resposta do Json Foi de Sucesso
			Boolean success =  (Boolean) json.get("success");

			if (success){			
				inserirDepartamentosJson(json);
			}
			else {			
				// No servidor se nao houver resultados na query retorna success=false
				Log.e(CNT_LOG, "Nenhuma Altera�ao a ser Feita.");
				Messages.showSuccessToast(this.context, "Nenhum Departamento a ser Alterado");
			}
		}
		catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
			Messages.showErrorAlert(this.context, "Houve um erro na Resposta do Servidor.");
		}           
	}
	
	protected void taskFailed(String[] resultado){
		Log.v(CNT_LOG, "taskFailed");
		
		// Se a flag REMOTE estiver true significa que ta na 1 tentativa
		//virar a flag para false para a segunda tentativa se nao conseguir mostrar erro
		if (this.REMOTE == true){
			this.REMOTE = false;
			sincronizarDepartamentos();
		}
		else {
			// Se tiver dado falha nas 2 tentativas retorna mensagem de erro 
			Messages.showErrorAlert(this.context, resultado[1].toString());
		}
	}

	
	
    static String[] departamentos = {
        "Vidros",
        "Plasticos"
    };

	public static String[] getArrayDepartamentos(){
		return departamentos;
	}

	
}
