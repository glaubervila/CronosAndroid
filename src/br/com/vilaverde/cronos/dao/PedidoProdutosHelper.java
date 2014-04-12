package br.com.vilaverde.cronos.dao;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;

public class PedidoProdutosHelper extends DataHelper{

	private final static String CNT_LOG = "PedidoProdutosHelper";
	private final static String TABELA = "pedido_produtos";
	
	private Context context = null;
	
	//static int VERSAO_SCHEMA = 40;
	
	public PedidoProdutosHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);
		this.context = context;
	}

//	public void onCreate(SQLiteDatabase db) {
//		Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//	
//		// Criacao da Tabela Pedido
//		String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" id_pedido TEXT," +
//					" id_produto TEXT," +
//					" quantidade REAL," +
//					" valor REAL," +
//					" valor_total REAL," +
//					" observacao TEXT" +
//				");";
//		
//		db.execSQL(sql);
//		Log.v(CNT_LOG, "Tabela [ "+TABELA+" ] Criada com Sucesso!");	
//
//	}

	
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.v(CNT_LOG, "onUprade - Drop Table ["+TABELA+"]");
//		
//		try {
//			db.execSQL("DROP TABLE IF EXISTS " + PedidoProdutosHelper.TABELA);
//		}
//		catch (Exception error){
//			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//		}
//		
//		this.onCreate(db);
//	}

	public List<PedidoProduto> getProdutos(Pedido pedido){
		Log.v(CNT_LOG, "getProdutos("+pedido.getId()+")");
			
		this.Open();
			
		String sql = "SELECT a.*, b.descricao FROM pedido_produtos a INNER JOIN produtos b ON a.id_produto = b._id WHERE a.id_pedido = ?";

		Cursor c = db.rawQuery(sql, new String[]{String.valueOf(pedido.getId())});
		
		Log.v(CNT_LOG, "Pesquisando Produtos no Pedido. Produtos = "+c.getCount());
		
		
		List<PedidoProduto> produtos = bindValues(c);
		    	
		return produtos;

	}
	
	
	public List<PedidoProduto> bindValues(Cursor c) {
		Log.v(CNT_LOG, "bindValues");

		List<PedidoProduto> lista = new ArrayList<PedidoProduto>();
	      
		while(c.moveToNext()){
	    	  
			PedidoProduto produto = new PedidoProduto();

		
			produto.setId(c.getInt(c.getColumnIndex("_id")));
			produto.setId_pedido(c.getString(c.getColumnIndex("id_pedido")));
			produto.setId_produto(c.getString(c.getColumnIndex("id_produto")));
			
			
			produto.setDescricao(c.getString(c.getColumnIndex("descricao")));
			produto.setQuantidade(c.getFloat(c.getColumnIndex("quantidade")));
			produto.setValor(c.getFloat(c.getColumnIndex("valor")));
			produto.setValor_total(c.getFloat(c.getColumnIndex("valor_total")));
			produto.setObservacao(c.getString(c.getColumnIndex("observacao")));
		
			lista.add(produto);
		}

		c.close();

		return lista;
	}
	
	public long inserir(PedidoProduto produto){
		Log.v(CNT_LOG, "Inserindo Produto no Pedido. Id ["+produto.getId_pedido()+"] Produto ["+produto.getId_produto()+"]");
		
		long linhasInseridas = 0;

		ContentValues valores = new ContentValues();
		valores.put("id_pedido", produto.getId_pedido());
		valores.put("id_produto", produto.getId_produto());
		valores.put("valor", produto.getValor());
		valores.put("observacao", produto.getObservacao());

		
		// Verificar se esse produto ja esta incluido
		PedidoProduto old_produto = getPedidoProdutoById(produto);
		if (old_produto != null){

			// Quantidade = quantidade antiga + quantidade nova
			float quantidade =  (float) (produto.getQuantidade() + old_produto.getQuantidade());
			float valor_total = (float) (produto.getValor() * quantidade);
			valores.put("quantidade", quantidade);
			valores.put("valor_total", valor_total);
						
			valores.put("_id", old_produto.getId());
			// colocar o id de pedido_produto para fazer um update
		}
		else {
			valores.put("quantidade", produto.getQuantidade());
			valores.put("valor_total", produto.getValor_total());
		}
		
		this.Open();

		try {
			
			// FIXME: Usar Transacao Para Atualizar o Pedido
			linhasInseridas = db.replace(TABELA, null, valores);
			
			Log.v(CNT_LOG, "Linhas Inseridas ["+linhasInseridas+"]");
			
			// Atualizar o Pedido
			PedidoHelper pedidoHelper = new PedidoHelper(context);
			if (pedidoHelper.atualizarPedido(null) == true) {
				return linhasInseridas;
			}
			else {
				Log.e(CNT_LOG, "Falha ao Atualizar o pedido mas o produto foi inserido");
			}
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Inserir Produto");
			return -1;
		}
		finally {
		    this.Close();
		}
	    
	    return linhasInseridas;
	 }

	public long alterar(PedidoProduto produto){
		Log.v(CNT_LOG, "Alterar Produto no Pedido. Id ["+produto.getId_pedido()+"] Produto ["+produto.getId_produto()+"]");
		
		long linhasInseridas = 0;

		ContentValues valores = new ContentValues();
		valores.put("id_pedido", produto.getId_pedido());
		valores.put("id_produto", produto.getId_produto());
		valores.put("valor", produto.getValor());
		valores.put("observacao", produto.getObservacao());
		valores.put("quantidade", produto.getQuantidade());


		double valorTotal = (produto.getQuantidade() * produto.getValor());
	
		valores.put("valor_total", valorTotal);
	
		this.Open();

		try {
			
			// FIXME: Usar Transacao Para Atualizar o Pedido
			linhasInseridas = db.update(TABELA, valores,"_id = " + produto.getId(), null);
			
			Log.v(CNT_LOG, "Linhas Alteradas ["+linhasInseridas+"]");
			
			// Atualizar o Pedido
			PedidoHelper pedidoHelper = new PedidoHelper(context);
			if (pedidoHelper.atualizarPedido(null) == true) {
				return linhasInseridas;
			}
			else {
				Log.e(CNT_LOG, "Falha ao Atualizar o pedido mas o produto foi alterado");
			}
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Alterar Produto");
			return -1;
		}
		finally {
		    this.Close();
		}
	    
	    return linhasInseridas;
	 }


	public boolean deleteProduto(PedidoProduto produto) {
		Log.v(CNT_LOG, "deleteProduto("+produto.getId()+")");
		
		long id_produto = produto.getId();

		this.Open();
	
		try { 
			//Exclui o registro com base no ID
			db.delete(TABELA, "_id = " + id_produto, null);
			
			// Atualizar o Pedido
			PedidoHelper pedidoHelper = new PedidoHelper(context);
			if (pedidoHelper.atualizarPedido(null) == true) {
				return true;
			}
			else {
				Log.e(CNT_LOG, "Falha ao Atualizar o pedido mas o produto foi removido");
				// Retorno true pq nao estou usando transacao
				return true;
			}
		}
		catch (Exception e){
			Log.e(CNT_LOG, "Excluir - Error ["+e.getMessage()+"]");
			e.printStackTrace();
			return false;
		}
		finally {
			this.Close();
		}
	}

	
	public PedidoProduto getPedidoProdutoById(PedidoProduto produto){
		Log.v(CNT_LOG, "getPedidoProdutoById("+produto.getId_produto()+")");
		
		this.Open();
		
		String id_pedido = ""+produto.getId_pedido();
		String id_produto = ""+produto.getId_produto();
        String[] selectionArgs = new String[] {id_pedido,id_produto};
      
        try {
  
    		String sql = "SELECT a.*, b.descricao FROM pedido_produtos a INNER JOIN produtos b ON a.id_produto = b._id WHERE a.id_pedido = ? AND a.id_produto = ?";

    		Cursor c = db.rawQuery(sql, selectionArgs);
			
			if (c.getCount() != 0){
				Log.v(CNT_LOG, "Pesquisando Produto no Pedido. COUNT = "+c.getCount());
				
				List<PedidoProduto> produtos = bindValues(c);
				return produtos.get(0);
			}
			
			Log.v(CNT_LOG, "Produto Nao está no Pedido");
			return null;		

        }
	    catch (Exception error){
	    	Log.e(CNT_LOG, "Falha ao Inserir Cliente");
	    	return null;
	    }
	    finally {
	        this.Close();
	    }
	}

	
	public boolean deleteProdutos(Pedido pedido) {
		Log.v(CNT_LOG, "deleteProdutos("+pedido.getId()+")");
		
		long id_pedido = pedido.getId();

		this.Open();
	
		try { 
			//Exclui o registro com base no ID
			db.delete(TABELA, "id_pedido = " + id_pedido, null);
			return true;
		}
		catch (Exception e){
			Log.e(CNT_LOG, "Excluir - Error ["+e.getMessage()+"]");
			e.printStackTrace();
			return false;
		}
		finally {
			this.Close();
		}
	}

	public float getTotalItens(Pedido pedido) {
		Log.v(CNT_LOG, "getTotalItens("+pedido.getId()+")");
		
		String sql = "SELECT COUNT(*) as qtd_itens FROM pedido_produtos WHERE id_pedido = ?";

		this.Open();
		
		try { 		
			Cursor c = db.rawQuery(sql, new String[]{String.valueOf(pedido.getId())});
			
			Log.v(CNT_LOG, "Quantidade Produtos no Pedido. Produtos = "+c.getCount());
			if (c.getCount() != 0){
				c.moveToFirst();
				return c.getFloat(c.getColumnIndex("qtd_itens"));			
			}
			return -1;
		}
		catch (Exception e){
			Log.e(CNT_LOG, "getTotalItens - Error ["+e.getMessage()+"]");
			e.printStackTrace();
			return -1;
		}
		finally {
			this.Close();
		}
	}
	
	public float getValorTotalItens(Pedido pedido) {
		Log.v(CNT_LOG, "getValorTotalItens("+pedido.getId()+")");
		
		String sql = "SELECT SUM(valor_total) as valor_total FROM pedido_produtos WHERE id_pedido = ?";

		this.Open();
		
		try { 		
			Cursor c = db.rawQuery(sql, new String[]{String.valueOf(pedido.getId())});
			

			if (c.getCount() != 0){
				c.moveToFirst();
				float valor_total = c.getFloat(c.getColumnIndex("valor_total"));
				
				Log.v(CNT_LOG, "Valor Total Produtos no Pedido. Valor ["+valor_total+"]");
				
				return valor_total; 			
			}
			return -1;
		}
		catch (Exception e){
			Log.e(CNT_LOG, "getValorTotalItens - Error ["+e.getMessage()+"]");
			e.printStackTrace();
			return -1;
		}
		finally {
			this.Close();
		}
	}
	
	public String writeJSON(PedidoProduto produto) {
		  JSONObject object = new JSONObject();
		  try {
		    object.put("id", produto.getId());
		    object.put("id_pedido", produto.getId_pedido());
		    object.put("id_produto", produto.getId_produto());
		    object.put("quantidade", produto.getQuantidade());
		    object.put("valor", produto.getValor());
		    object.put("valor_total", produto.getValor_total());
		    object.put("observacao", produto.getObservacao());
		    
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		return object.toString();
	} 	
}
