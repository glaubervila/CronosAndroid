package br.com.vilaverde.cronos.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PedidoHelper extends DataHelper{

	/** Status
	 * 0 - Aberto
	 * 1 - Fechado
	 */
	
	private final static String CNT_LOG = "PedidoHelper";
	private final static String TABELA = "pedidos";
	
	private Context context = null;
	
	//static int VERSAO_SCHEMA = 39;
	
	public PedidoHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);
		this.context = context;
	}
//	public void onCreate(SQLiteDatabase db) {
//		Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//	
//		String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" id_usuario TEXT," +
//					" id_cliente TEXT," +
//					" status INTEGER," +
//					" qtd_itens REAL," +
//					" valor_total REAL," +
//					" finalizadora INTEGER," +
//					" parcelamento INTEGER," +
//					" nfe INTEGER," +
//					" dt_inclusao TEXT," +
//					" dt_envio TEXT," +
//					" observacao TEXT" +
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
//			db.execSQL("DROP TABLE IF EXISTS " + PedidoHelper.TABELA);
//		}
//		catch (Exception error){
//			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//		}
//		
//		this.onCreate(db);
//	}


	public List<Pedido> getPedidos(){
		Log.v(CNT_LOG, "Recupera Todos os Pedidos.");
			
		this.Open();
		Cursor c = db.query(TABELA, null, null, null, null, null, null);
	   
		if (c.getCount() > 0) {
			Log.v(CNT_LOG, "Total Pedidos [ "+c.getCount()+" ].");
			List<Pedido> pedidos = bindValues(c);
			    	  
			return pedidos;
		}
		else {
			return null;
		}
	}
	
	public Pedido getPedidoAberto(){

		Log.v(CNT_LOG, "Pesquisando Pedido Aberto.");
			
		this.Open();
		String status = "0"; // Aberto
		String where = "status = ?";
        String[] selectionArgs = new String[] {status};

		Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, null);

		if (c.getCount() > 0) {

			List<Pedido> pedidos = bindValues(c);
			    	  
			Pedido pedido = pedidos.get(0);
			
			return pedido;
		}
		else {
			return null;
		}
	}
	
	
	public List<Pedido> bindValues(Cursor c) {
		Log.v(CNT_LOG, "bindValues.");

		List<Pedido> lista = new ArrayList<Pedido>();
	      
		while(c.moveToNext()){
	    	  
			Pedido pedido = new Pedido();
			
			pedido.setId(c.getInt(c.getColumnIndex("_id")));
			pedido.setId_usuario(c.getString(c.getColumnIndex("id_usuario")));
			pedido.setId_cliente(c.getString(c.getColumnIndex("id_cliente")));
			pedido.setStatus(c.getInt(c.getColumnIndex("status")));
			pedido.setQtd_itens(c.getFloat(c.getColumnIndex("qtd_itens")));
			pedido.setValor_total(c.getFloat(c.getColumnIndex("valor_total")));
			pedido.setFinalizadora(c.getInt(c.getColumnIndex("finalizadora")));
			pedido.setParcelamento(c.getInt(c.getColumnIndex("parcelamento")));
			pedido.setNfe(c.getInt(c.getColumnIndex("nfe")));
			pedido.setDt_inclusao(c.getString(c.getColumnIndex("dt_inclusao")));
			pedido.setDt_envio(c.getString(c.getColumnIndex("dt_envio")));
			pedido.setObservacao(c.getString(c.getColumnIndex("observacao")));	
			
			lista.add(pedido);
		}

		c.close();

		return lista;
	}
	public long inserir(Pedido pedido){
		Log.v(CNT_LOG, "Inserindo Cliente. Nome ["+pedido.getId_cliente().toString()+"]");
		
		long linhasInseridas = 0;
		
		this.Open();
		
		ContentValues valores = new ContentValues();
		valores.put("id_usuario", pedido.getId_usuario());
		valores.put("id_cliente", pedido.getId_cliente());
		valores.put("status", pedido.getStatus());
		valores.put("qtd_itens", pedido.getQtd_itens());
		valores.put("valor_total", pedido.getValor_total());
		valores.put("finalizadora", pedido.getFinalizadora());
		valores.put("parcelamento", pedido.getParcelamento());
		valores.put("nfe", pedido.getNfe());
		valores.put("dt_inclusao", pedido.getDt_inclusao());
		valores.put("dt_envio", pedido.getDt_envio());
		valores.put("observacao", pedido.getObservacao());


      try {
		      linhasInseridas = db.insert(TABELA, null, valores);
		      
		      Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
		      
	    }
	    catch (Exception error){
	    	Log.e(CNT_LOG, "Falha ao Inserir Cliente");
	    }
	    finally {
	        this.Close();
	    }
	    
	    return linhasInseridas;
	 }
	    
	
    public Boolean Excluir(Pedido pedido) {
    	Log.v(CNT_LOG, "Excluir - Registro ["+pedido.getId()+"]");
    	
        long id = pedido.getId();

		this.Open();
			
		try { 

			db.beginTransaction();
			
			
	        //Exclui o registro com base no ID
			db.delete("pedido_produtos", "id_pedido = " + id, null);
	        db.delete("pedidos", "_id = " + id, null);

	        db.setTransactionSuccessful();
	        
	        Toast.makeText(context, "Pedido Excluido!", Toast.LENGTH_SHORT).show();
	        return true;
		}
		catch (Exception e){
		 	Log.e(CNT_LOG, "Excluir - Error ["+e.getMessage()+"]");
		 	e.printStackTrace();
		 	return false;
		}
		finally {
			db.endTransaction();
		    this.Close();
		}

    }

	public long Alterar(Pedido pedido) {
		Log.v(CNT_LOG, "Alterar - Registro ["+pedido.getId()+"]");
		
		long linhaAlterada = 0;
		
		this.Open();
		
		try { 
			// Recupera o Id
	        int id = pedido.getId();
	        
	        // Cria o objeto valores         
			ContentValues valores = new ContentValues();
			valores.put("id_usuario", pedido.getId_usuario());
			valores.put("id_cliente", pedido.getId_cliente());
			valores.put("status", pedido.getStatus());
			valores.put("qtd_itens", pedido.getQtd_itens());
			valores.put("valor_total", pedido.getValor_total());
			valores.put("finalizadora", pedido.getFinalizadora());
			valores.put("parcelamento", pedido.getParcelamento());
			valores.put("nfe", pedido.getNfe());
			valores.put("dt_inclusao", pedido.getDt_inclusao());
			valores.put("dt_envio", pedido.getDt_envio());
			valores.put("observacao", pedido.getObservacao());
	    
		    //Alterar o registro com base no ID
	   		Log.v(CNT_LOG, "Alterando Pedido [ "+pedido.getId()+"]");
	   		
		    linhaAlterada = db.update(TABELA, valores, "_id = " + id, null);
		    
		    Log.v(CNT_LOG, "Pedido Alterado ["+linhaAlterada+"], Id ["+id+"]");
	  }
	  catch (Exception e){
	  	Log.e(CNT_LOG, "Alterar - Error ["+e.getMessage()+"]");
	  }
	  finally {
	      this.Close();
	  }
		return linhaAlterada;
	}

    public boolean atualizarPedido(){
    	Log.v(CNT_LOG, "atualizarPedido()");
    	
    	// Recuperar o Pedido Aberto
    	Pedido pedido = getPedidoAberto();
    	
    	// Recuperar o Total de Itens
    	PedidoProdutosHelper pedidoProdutoHelper = new PedidoProdutosHelper(context);
    	
    	float quantidade = pedidoProdutoHelper.getTotalItens(pedido);
    	Log.v(CNT_LOG, "QUANTIDADE ("+quantidade+")");

    	float valor_total = pedidoProdutoHelper.getValorTotalItens(pedido);
    	Log.v(CNT_LOG, "VALORTOTAL ("+valor_total+")");
    	
    	pedido.setQtd_itens(quantidade);
    	pedido.setValor_total(valor_total);
    	
    	if (Alterar(pedido) > -1){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public Boolean fechar() {
    	Log.v(CNT_LOG, "fechar()");

    	// Atualizo os Valores do Pedido
    	atualizarPedido();
    	
    	// Recuperar o Pedido Aberto
    	Pedido pedido = getPedidoAberto();
    	// Alterar o Status Para Fechado
    	pedido.setStatus(1); // Fechado
    	    	
		// Update no Pedido 
    	if (Alterar(pedido) > -1){
    		return true;	
    	}
    	else {
    		return false;
    	}
    }

    
}
