package br.com.vilaverde.cronos.dao;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vilaverde.cronos.httpclient.HttpTaskPost;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Vendedor;
import android.R;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class VendedorHelper extends DataHelper{

	private final static String CNT_LOG = "VendedorHelper";
	private final static String TABELA = "vendedor";
	
	private Context context = null;
	//private static final int VERSAO_SCHEMA = 36;
	
	public VendedorHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);
		this.context = context;
	}
//
//	public void onCreate(SQLiteDatabase db) {
//		Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//		
//		String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" nome TEXT," +
//					" grupo INTEGER" +
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
//			db.execSQL("DROP TABLE IF EXISTS " + VendedorHelper.TABELA);
//		}
//		catch (Exception error){
//			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//		}
//		
//		this.onCreate(db);
//	}

	
	public long inserir(Vendedor vendedor){
		Log.v(CNT_LOG, "inserir");
		long linhasInseridas = 0;
		
		this.Open();
		
		      ContentValues valores = new ContentValues();
		      valores.put("nome", vendedor.getNome());
		      valores.put("grupo", vendedor.getGrupo());

      try {
    	  
    	  // Saber se tem Id se tiver setar e usar o replace
	      if (vendedor.getId() > 0){
	    	  valores.put("_id", vendedor.getId());
	    	  Log.v(CNT_LOG, "Replace Vendedor. Nome ["+vendedor.getNome().toString()+"]");
	    	  linhasInseridas = db.replace(TABELA, null, valores);
	      }
	      else {
	  		  Log.v(CNT_LOG, "Inserindo Vendedor. Nome ["+vendedor.getNome().toString()+"]");
	    	  // se nao tiver faz insert
		      linhasInseridas = db.insert(TABELA, null, valores);
	      }
	      //getWritableDatabase().insert(TABELA, null, valores);      
	      Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
		      
	      if (linhasInseridas < 0 ){
	    	  // Erro no Insert
	    	  //throw new Exception("Registro não Inserido");	    	  
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
	    
	    //return linhasInseridas;
	 }


	public void sincronizarVendedores()	{
		
		
		String urlPost = "http://10.0.0.102/cronos/main.php";
		//String urlPost = "http://192.168.0.3/cronos/main.php";
		
		
		// Parametros
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("classe", "ClientAndroid"));
        nameValuePairs.add(new BasicNameValuePair("action", "getVendedores"));
        

    	HttpTaskPost httpPost = new getVendedorHttp();
    	httpPost.setContext(context);
    	httpPost.setParametros(nameValuePairs);

    	httpPost.execute(urlPost);
		
	}
	
	public void deleteVendedores(){
    	Log.v(CNT_LOG, "Excluir - Registro [ Todos ]");
		    	
		this.Open();
				
		try { 
			db.delete(TABELA, null, null);
		}
		catch (Exception e){
		 	Log.e(CNT_LOG, "Excluir - Error ["+e.getMessage()+"]");
		}
		finally {
		    this.Close();
		}
	}
	
	
	public void inserirVendedoresJson(JSONObject json){
		Log.v(CNT_LOG, "inserirVendedoresJson");

		int count = 0;
		int erro = 0;
		
		// Apagar a Tabela de vendedores 
		
		// Fazer o parse para array
		try {
			
			JSONArray arrayVendedores = (JSONArray) json.get("rows");

			
	        for (int i = 0; i < arrayVendedores.length(); i++) {
	        	
	        	JSONObject vendedorItem = arrayVendedores.getJSONObject(i);        
	            
	        	// Criando objetos Vendedor
	        	Vendedor vendedor = new Vendedor();
	        	vendedor.setId(vendedorItem.getInt("id_usuario"));
	        	vendedor.setNome(vendedorItem.getString("Nome"));
	        	vendedor.setGrupo(vendedorItem.getInt("Grupo"));

	        	if (inserir(vendedor) > 0){
        			count++;
	        	}
	        	else {
	        		erro++;
	        	}
			
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public class getVendedorHttp extends HttpTaskPost {
		
		protected void onPreExecute() {
			
			super.onPreExecute();

			Log.v(CNT_LOG, "onPreExecute");	
			
	    }

		
	    protected void onPostExecute(JSONObject json) {
			Log.v(CNT_LOG, "onPostExecute");

			Log.v(CNT_LOG,"Passo 8");
	        if(json != null) {    
	    		Log.v(CNT_LOG,"Passo 9");
	    		taskSuccessful(json);
	        } else {
	    		Log.e(CNT_LOG,"Passo 10 - o json veio vazio");
	        	taskFailed();
	        }
	    }
	}
	
	protected void taskSuccessful(JSONObject json){
		Log.v(CNT_LOG, "taskSuccessful");
		try {  
			Boolean teste =  (Boolean) json.get("success");
			Log.v(CNT_LOG, "Resposta Succes = "+ teste.toString());
			
			// Testar se o Json veio com success true 
			if (teste.booleanValue() == true){ 
			
				inserirVendedoresJson(json);
			}
			else {
				taskFailed();
			}
		}
		catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
		}
	}
	
	protected void taskFailed(){
		Log.e(CNT_LOG, "Nao foi possivel recuperar as informacoes");
		
	}
	

}

