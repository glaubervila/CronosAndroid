package br.com.vilaverde.cronos.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import br.com.vilaverde.cronos.model.Produto;

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
			
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.v(CNT_LOG, "onUprade - Drop Table ["+TABELA+"]");
//		
//		try {
//			db.execSQL("DROP TABLE IF EXISTS " + TABELA);
//		}
//		catch (Exception error){
//			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//		}
//		
//		this.onCreate(db);
//	}
	
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
	
	public Boolean inserirProdutosJson(JSONObject json){
		Log.v(CNT_LOG, "inserirProdutosJson()");
		int count = 0;
		int auxCount = 0;
		int erros = 0;
		
		this.Open();
		db.beginTransaction();
		try {
			
			// Primeiro Setar Todos os Produtos para NAO ATIVOS
			Log.v(CNT_LOG,"Setando TODOS os produtos para inativos");
			db.execSQL("UPDATE "+TABELA+" SET status = -1");
			
			JSONArray arrayProdutos = (JSONArray) json.get("rows");
			// Para Cada Item no array transformar em um objeto
			for (int i = 0; i < arrayProdutos.length(); i++) {
	
				JSONObject ProdutoItem;

				ProdutoItem = arrayProdutos.getJSONObject(i);
				
	        	Produto produto = new Produto();
	        	
	        	produto.setStatus(1);	        	
	        	produto.setId(ProdutoItem.getInt("_id"));
	        	produto.setCodigo(ProdutoItem.getInt("codigo"));
	        	produto.setCategoria_id(ProdutoItem.getInt("categoria_id"));
	        	produto.setDescricao_curta(ProdutoItem.getString("descricao_curta"));
	        	produto.setDescricao(ProdutoItem.getString("descricao"));
	        	produto.setQuantidade(ProdutoItem.getInt("quantidade"));
	        	produto.setPreco(ProdutoItem.getDouble("preco"));
	        	// SETANDO O PRODUTO COMO ATIVO
	        	produto.setStatus(1);
	        	
	        	// Tratamento das Imagens
	        	produto.setImage_name(ProdutoItem.getString("image_name"));

//           		produto.setImage_id(0);
//           		produto.setImage_path("");
//           		produto.setImage_size("");
//	        	produto.setImage_status(0);
//	        	
//	        	// Procurar a Imagem Local
//	        	String path_images = this.getPathImages();
//	            String image_name  = produto.getImage_name();
//	            String image_where = "%"+path_images+"/"+image_name+"%"; 	       

//	            Cursor cursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//	           		null, android.provider.MediaStore.Images.Media.DATA + " like ?", 
//	           		new String[] {image_where},
//	           		null);
//	            
//	           	if (cursor != null) {
//	         	
//	           		if (cursor.getCount() > 0){
//		           		//Log.v(CNT_LOG,"Tem "+cursor.getCount()+" imagem(s) " );
//		           		
//		           		cursor.moveToPosition(0);
//			        	//TODO: Testar o tamanho da imagem pra ver se e do mesmo tamanho
//		           		
//		           		// Setando as variaveis de Imagem
//		           		produto.setImage_id(cursor.getLong(cursor.getColumnIndex("_id")));
//		           		produto.setImage_path(cursor.getString(cursor.getColumnIndex("_data")));
//		           		produto.setImage_size(cursor.getString(cursor.getColumnIndex("_size")));
//		           		produto.setImage_status(1);
//		           		
//		           		//Log.v(CNT_LOG,"DATA ="+cursor.getString(cursor.getColumnIndex("_data")) );
////		           		Log.v(CNT_LOG,"DISPLAY_NAME ="+cursor.getString(cursor.getColumnIndex("_display_name")) );
//		           		//Log.v(CNT_LOG,"IMAGE_ID ="+produto.getImage_id());
//		           		
//	           		}
//	           		else {
//	           			produto.setImage_status(0);
//	           		}
//	           	}
//	        	cursor.close();
	        	
	        	ContentValues valores = new ContentValues();
	        	valores.put("_id", produto.getId());
	        	valores.put("status", produto.getStatus());
	        	valores.put("codigo", produto.getCodigo());
	        	valores.put("categoria_id", produto.getCategoria_id());
	        	valores.put("descricao_curta", produto.getDescricao_curta());
	        	valores.put("descricao", produto.getDescricao());
	        	valores.put("quantidade", produto.getQuantidade());
	        	valores.put("preco", produto.getPreco());
  
	        	valores.put("image_name", produto.getImage_name());
//	        	valores.put("image_path", produto.getImage_path());
//	        	valores.put("image_size", produto.getImage_size());
//	        	valores.put("image_id", produto.getImage_id());
//	        	valores.put("image_status", produto.getImage_status());
	        	
	        	db.replace(TABELA, null, valores);
        		count++;
        		auxCount++;

	        	Log.v(CNT_LOG, "REPLACE - Codigo = "+produto.getCodigo()+" Produto = "+produto.getDescricao_curta());        	
//	        	// Garbage Colector
	        	if (auxCount == 100){
	        		Log.w(CNT_LOG, "DISPARANDO GARBAGE COLECTOR");
	        		System.gc();
	        		auxCount = 0;
	        	}
	        }
	        Log.v(CNT_LOG, "Count["+count+"] Erros["+erros+"]");
        	db.setTransactionSuccessful();
        	return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		catch(SQLException e){
			Log.e(CNT_LOG,"SQLException="+e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			 db.endTransaction();
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

	public List<Produto> getListProdutos(String q){
		Log.v(CNT_LOG, "Pesquisando Produtos. Query [ "+q+" ]");
		this.Open();			

		Cursor c = null;

		String where = "descricao_curta LIKE ? AND status = 1";
        String[] selectionArgs = {"%"+q+"%"};
        
		c = db.query(TABELA, null, where, selectionArgs,null , null,"descricao_curta");
		
		Log.v(CNT_LOG, "Produtos Count["+c.getCount()+"]");
		List<Produto> lista = bindValues(c);
		return lista;
	}

	public List<Produto> getListProdutos(){
		Log.v(CNT_LOG, "Pesquisando Produtos.");
		this.Open();			

		Cursor c = null;

		String where = " status = ?";
	    String[] selectionArgs = {"1"};
	    
		c = db.query(TABELA, null, where, selectionArgs,null,null,"descricao_curta");
		
		Log.v(CNT_LOG, "Produtos Count["+c.getCount()+"]");
		List<Produto> lista = bindValues(c);
		return lista;
	}

	
	public Cursor getProdutosByDepartamentos(int departamento_id) {
		Log.v(CNT_LOG, "getProdutos()");		
		this.Open();
		
		String depart_id = Integer.toString(departamento_id) ;
		
		String where = "categoria_id = ? AND status = 1";
        String[] selectionArgs = {depart_id};
	
		Cursor c = db.query(TABELA, null, where, selectionArgs,null , null, null);

		Log.w(CNT_LOG, "Total Registros = "+c.getCount());

		this.Close();		
		return c;
	}
	
	public List<Produto> ListProdutosByDepartamentos(int departamento_id){
		Log.v(CNT_LOG, "getProdutos. Depart [ "+departamento_id+" ]");
		
		this.Open();
		// So retornar os produtos com status diferente de inativo (-1)		
		String depart_id = Integer.toString(departamento_id) ;
		String where = "categoria_id = ? AND status = 1";
        String[] selectionArgs = {depart_id};

		Cursor c = db.query(TABELA, null, where, selectionArgs,null , null, null);
		
		List<Produto> lista = bindValues(c);


		this.Close();
		
		Log.v(CNT_LOG, "getLista, Total [ "+ lista.size()+" ]");
		
	    return lista;
	 }

	public List<Produto> getProdutosSemImagem(){
		Log.v(CNT_LOG, "getProdutosSemImagem()");
		
		this.Open();
		// Todos os Produtos com status = 1 (ativo) e com image_status = 0 (sem Imagem)

		String where = "status = 1 AND image_status = 0";

		Cursor c = db.query(TABELA, null, where, null,null , null, null);
		
		List<Produto> lista = bindValues(c);


		this.Close();
		
		Log.v(CNT_LOG, "Produtos Sem Imagem, Total [ "+ lista.size()+" ]");
		
	    return lista;
	 }

	
	public List<Produto> bindValues(Cursor c) {
		Log.v(CNT_LOG, "bindValues.");

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

		return lista;
	}

	public boolean verificaImagem(Produto produto){
		Log.v(CNT_LOG, "verificaImagem("+produto.getCodigo()+")");
		
		produto.setImage_id(0);
		produto.setImage_path("");
		produto.setImage_size("");
		produto.setImage_status(0);
		
		// Procurar a Imagem Local
		String path_images = this.getPathImages();
	    String image_name  = produto.getImage_name();
	    String image_where = "%"+path_images+"/"+image_name+"%";
   
	    
        Cursor cursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				   		null, android.provider.MediaStore.Images.Media.DATA + " like ?", 
				   		new String[] {image_where},
				   		null);
    
        boolean result;
        
		if (cursor != null) {
        	if (cursor.getCount() > 0){
	       		cursor.moveToPosition(0);
	       		//TODO: Testar o tamanho da imagem pra ver se e do mesmo tamanho
	       		
	      		// Setando as variaveis de Imagem
	       		produto.setImage_id(cursor.getLong(cursor.getColumnIndex("_id")));
	       		produto.setImage_path(cursor.getString(cursor.getColumnIndex("_data")));
	       		produto.setImage_size(cursor.getString(cursor.getColumnIndex("_size")));
	       		produto.setImage_status(1);
	       		
	    		if (updateImage(produto)){
	    			result = true;
	    		}
	    		else {
	    			result = false;
	    		}
	   		}
	   		else {
	   			Log.w(CNT_LOG, "NAO TEM IMAGEM");
	   			result = false;
	   		}
        }
        else {
        	result = false;
        }
        cursor.close();
        
        return result;
	}
	
	public boolean updateImage(Produto produto){
		Log.v(CNT_LOG, "updateImage("+produto.getCodigo()+","+produto.getImage_id()+")");
		
		int linhaAlterada = 0;
		
    	ContentValues valores = new ContentValues();
    	valores.put("_id", produto.getId());
		
    	valores.put("image_path", produto.getImage_path());
    	valores.put("image_size", produto.getImage_size());
    	valores.put("image_id", produto.getImage_id());
    	valores.put("image_status", produto.getImage_status());
    	
		this.Open();
        try {
    		 linhaAlterada = db.update(TABELA, valores, "_id = " + produto.getId(), null);  		      
  	    }
  	    catch (Exception error){
  	    	Log.e(CNT_LOG, "Falha ao fazer update na Imagem do produto");
  	    }
  	    finally {
  	        this.Close();
  	    }		
        
        if (linhaAlterada > 0){
        	return true;
        }
        else {
        	return false;
        }
	}

}

