package br.com.vilaverde.cronos.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vilaverde.cronos.Messages;
import br.com.vilaverde.cronos.httpclient.HttpTaskPost;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Produto;
import br.com.vilaverde.cronos.model.Vendedor;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

public class ProdutosHelper extends DataHelper{

	private final static String CNT_LOG = "ProdutosHelper";
	private final static String TABELA = "produtos";
	
	private Context context = null;
	
	private static Boolean REMOTE = false;
	//static int VERSAO_SCHEMA = 36;
	
	public ProdutosHelper(Context context) {
		super(context);
		this.context = context;
	}
	
	public String getTable(){
		return TABELA;
	}
//	public void onCreate(SQLiteDatabase db) {
//		Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//			
//		String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" status INTEGER," +
//					" codigo INTEGER," +
//					" categoria_id INTEGER," +
//					" descricao_curta TEXT," +
//					" descricao TEXT," +
//					" quantidade INTEGER," +
//					" preco FLOAT," +
//					" image_name TEXT," +
//					" image_path TEXT," +
//					" image_size TEXT," +
//					" image_id INTEGER," +
//					" image_status INTEGER" +
//				");";
//		
//		db.execSQL(sql);
//		
//		Log.v(CNT_LOG, "Tabela [ "+TABELA+" ] Criada com Sucesso!");
//	}
			
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table ["+TABELA+"]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABELA);
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreate(db);
	}
	
	public long inserir(Produto produto){
		Log.v(CNT_LOG, "inserir");
		long linhasInseridas = 0;
		
		this.Open();
		
		      ContentValues valores = new ContentValues();
		      //valores.put("_id", produto.getId());
		      valores.put("status", produto.getStatus());
		      valores.put("codigo", produto.getCodigo());
		      valores.put("categoria_id", produto.getCategoria_id());
		      valores.put("descricao_curta", produto.getDescricao_curta());
		      valores.put("descricao", produto.getDescricao());
		      valores.put("quantidade", produto.getQuantidade());
		      valores.put("preco", produto.getPreco());
		      
		      valores.put("image_name", produto.getImage_name());
		      valores.put("image_path", produto.getImage_path());
		      valores.put("image_size", produto.getImage_size());
		      valores.put("image_id", produto.getImage_id());
		      valores.put("image_status", produto.getImage_status());
      try {
    	  
    	  // Saber se tem Id se tiver setar e usar o replace
	      if (produto.getId() > 0){
	    	  valores.put("_id", produto.getId());
	    	  Log.v(CNT_LOG, "Replace Produto. Codigo = "+valores.getAsString("codigo")+" Descricao ="+valores.getAsString("descricao_curta")+" Image_Path = "+valores.getAsString("image_path")+" Image_Id = "+valores.getAsString("image_id"));
	    	  linhasInseridas = db.replace(TABELA, null, valores);
	      }
	      else {
	  		  Log.v(CNT_LOG, "Inserindo Produto. Codigo = "+produto.getCodigo()+" Descricao ="+produto.getDescricao_curta());
	    	  // se nao tiver faz insert
		      linhasInseridas = db.insert(TABELA, null, valores);
	      }

	      Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
		      
    	  return linhasInseridas;

		      
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
	
	public void inserir(JSONArray produtos){
		Log.v(CNT_LOG, "inserir Json");
		
		// Para Cada Item no array transformar em um objeto
		for (int i = 0; i < produtos.length(); i++) {

			JSONObject ProdutoItem;
			try {
				ProdutoItem = produtos.getJSONObject(i);
				
	        	Produto produto = new Produto();
	        	
	        	produto.setStatus(1);	        	
	        	produto.setId(ProdutoItem.getInt("_id"));
	        	produto.setCodigo(ProdutoItem.getInt("codigo"));
	        	produto.setCategoria_id(ProdutoItem.getInt("categoria_id"));
	        	produto.setDescricao_curta(ProdutoItem.getString("descricao_curta"));
	        	produto.setDescricao(ProdutoItem.getString("descricao"));
	        	produto.setQuantidade(ProdutoItem.getInt("quantidade"));
	        	produto.setPreco(ProdutoItem.getDouble("preco"));

	        	
	        	// Tratamento das Imagens
	        	produto.setImage_name(ProdutoItem.getString("image_name"));
	        	
	        	
	        	// Procurar a Imagem Local
	        	String path_images = this.getPathImages();
	            String image_name  = produto.getImage_name();
	            String image_where = "%"+path_images+"/"+image_name+"%"; 	       
	            Log.v(CNT_LOG,"Chegou Aki");
	            Cursor cursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	           		null, android.provider.MediaStore.Images.Media.DATA + " like ?", 
	           		new String[] {image_where},
	           		null);
	            Log.v(CNT_LOG,"Chegou Aki2");	            
	           	if (cursor != null) {
	         	
	           		if (cursor.getCount() > 0){
		           		Log.v(CNT_LOG,"Tem "+cursor.getCount()+" imagem(s) " );
		           		
		           		cursor.moveToPosition(0);
			        	//TODO: Testar o tamanho da imagem pra ver se e do mesmo tamanho
		           		
		           		// Setando as variaveis de Imagem
		           		produto.setImage_id(cursor.getLong(cursor.getColumnIndex("_id")));
		           		produto.setImage_path(cursor.getString(cursor.getColumnIndex("_data")));
		           		produto.setImage_size(cursor.getString(cursor.getColumnIndex("_size")));
		           		produto.setImage_status(1);
		           		
		           		Log.v(CNT_LOG,"DATA ="+cursor.getString(cursor.getColumnIndex("_data")) );
//		           		Log.v(CNT_LOG,"DISPLAY_NAME ="+cursor.getString(cursor.getColumnIndex("_display_name")) );
		           		Log.v(CNT_LOG,"IMAGE_ID ="+produto.getImage_id());
		           		
	           		}
	           		else {
	           			produto.setImage_status(0);
	           		}
	           	}
	        	cursor.close();
	        	//Log.v(CNT_LOG, "Codigo = "+produto.getCodigo()+"Produto = "+produto.getDescricao_curta());
	        	this.inserir(produto);
	        	
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}        
		}
		
	}
	
	public Cursor getProdutos() {
		Log.v(CNT_LOG, "getProdutos()");		
		this.Open();
		
		Cursor c = db.query(TABELA, null, null, null, null, null, null);

		
		Log.w(CNT_LOG, "Total Registros = "+c.getCount());
		////return db.rawQuery("select _id, nome FROM clientes ORDER BY nome", null);
		this.Close();		
		return c;
	} 

	public Cursor getProdutosByDepartamentos(int departamento_id) {
		Log.v(CNT_LOG, "getProdutos()");		
		this.Open();
		
		String depart_id = Integer.toString(departamento_id) ;
		
		String where = "categoria_id = ?";
        String[] selectionArgs = {depart_id};
	
		Cursor c = db.query(TABELA, null, where, selectionArgs,null , null, null);

		
		Log.w(CNT_LOG, "Total Registros = "+c.getCount());
		////return db.rawQuery("select _id, nome FROM clientes ORDER BY nome", null);
		this.Close();		
		return c;
	}
	
	public List<Produto> ListProdutosByDepartamentos(int departamento_id){
		Log.v(CNT_LOG, "getProdutos. Depart [ "+departamento_id+" ]");
		
		this.Open();
		
		//String depart_id = Integer.toString(departamento_id) ;
		String depart_id = "1000";
		//Log.w(CNT_LOG, "HARDCODE! Depart [ "+depart_id+" ]");
		
		String where = "categoria_id = ?";
        String[] selectionArgs = {depart_id};
		
//		String where = "descricao Like ?";
//        String[] selectionArgs = {"%caneta%"};
    	
//		String where = "codigo = ?";
//        String[] selectionArgs = {"2738"};

		Cursor c = db.query(TABELA, null, where, selectionArgs,null , null, null);
		//Cursor c = db.query(TABELA, null, null, null,null , null, null);
		
		List<Produto> lista = new ArrayList<Produto>();
	      
		while(c.moveToNext()){
	    	  
			Produto produto = new Produto();
			
			produto.setId(c.getInt(c.getColumnIndex("_id")));
			produto.setStatus(c.getInt(c.getColumnIndex("status")));
			produto.setCodigo(c.getInt(c.getColumnIndex("codigo")));
			produto.setCategoria_id(c.getInt(c.getColumnIndex("categoria_id")));
			produto.setDescricao_curta(c.getString(c.getColumnIndex("descricao_curta")));
			produto.setDescricao(c.getString(c.getColumnIndex("descricao")));
			produto.setQuantidade(c.getInt(c.getColumnIndex("quantidade")));
			produto.setPreco(c.getDouble(c.getColumnIndex("preco")));
			produto.setImage_name(c.getString(c.getColumnIndex("image_name")));
			produto.setImage_path(c.getString(c.getColumnIndex("image_path")));
			produto.setImage_size(c.getString(c.getColumnIndex("image_size")));
			produto.setImage_id(c.getLong(c.getColumnIndex("image_id")));
			produto.setImage_status(c.getInt(c.getColumnIndex("image_status")));
		      
			lista.add(produto);
		}

		c.close();

		this.Close();
		
		Log.v(CNT_LOG, "getLista, Total [ "+ lista.size()+" ]");
		
	    return lista;
	 }

	
	
	public void getWSProdutos(){
		Log.v(CNT_LOG, "getWSProdutos()");
					
		// Parametros
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("classe", "ClientAndroid"));
        nameValuePairs.add(new BasicNameValuePair("action", "getProdutos"));
		
        // Crio um Objeto TaskPost
    	HttpTaskPost httpPost = new getProdutosHttp();
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
	
	
	public class getProdutosHttp extends HttpTaskPost {
	
	
		protected void onPostExecute(String[] resultado) {
			Log.v(CNT_LOG, "onPostExecute");

			if (resultado[0] == "success"){
				// Passando a string para o metodo que vai inserir
				taskSuccess(resultado[1]);
			}
			else {
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
				
				// recuperar os objetos na resposta
				JSONArray arrayObjetos = (JSONArray) json.get("rows");
				
				
				//inserir(arrayObjetos);

				LongOperation MyTask= new LongOperation();
		        MyTask.execute(arrayObjetos);
			}
			else {			
				// No servidor se nao houver resultados na query retorna success=false
				Log.e(CNT_LOG, "Nenhuma Alteraçao a ser Feita.");
				Messages.showSuccessToast(this.context, "Nenhum Produto a ser Alterado");
			}
		}
		catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
			Messages.showErrorAlert(this.context, "Houve um erro na Resposta do Servidor.");
		}           
		
		// Teste de mensagem de sucesso
		//Messages.showSuccessToast(this.context, "Produtos Atualizados");
	}
	
	protected void taskFailed(String[] resultado){
		Log.v(CNT_LOG, "taskFailed");
		
		// Se a flag REMOTE estiver true significa que ta na 1 tentativa
		//virar a flag para false para a segunda tentativa se nao conseguir mostrar erro
		if (this.REMOTE == true){
			this.REMOTE = false;
			getWSProdutos();
		}
		else {
			// Se tiver dado falha nas 2 tentativas retorna mensagem de erro 
			Messages.showErrorAlert(this.context, resultado[1].toString());
		}
	}
	
	
	private class LongOperation extends AsyncTask<JSONArray, Void, String> {
		@Override
		protected String doInBackground(JSONArray... params) {

			inserir(params[0]);
			return null;
		}
		protected void onPostExecute(String result) {
			Log.v(CNT_LOG,"Fim da Importacao");
	    }		
	}



}
