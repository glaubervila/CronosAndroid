package br.com.vilaverde.cronos.dao;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;

public class PedidoHelper extends DataHelper{

	/** Status
	 *-1 - Erro (Enviado com Erro)
	 * 0 - Aberto
	 * 1 - Fechado (À Enviar)
	 * 2 - Enviado
	 * 9 - Enviado COM ERRO 
	 */
	
	private final static String CNT_LOG = "PedidoHelper";
	private final static String TABELA = "pedidos";
	

	private Pedido pedidoAEnviar  = null;
	private Context context = null;
	
	//static int VERSAO_SCHEMA = 59;
	
	public PedidoHelper(Context context) {
		//super(context, VERSAO_SCHEMA);
		super(context);
		this.context = context;
	}

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
	

	public List<Pedido> getPedidosAEnviar(){

		Log.v(CNT_LOG, "Recuperar Pedidos A Enviar.");
			
		this.Open();
		String status = "1"; // Fechado a Enviar
		String where = "status = ?";
        String[] selectionArgs = new String[] {status};

		Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, null);
		
		List<Pedido> pedidos = null;
		
		if (c.getCount() > 0) {
			pedidos = bindValues(c);	
		}
		
		c.close();
		this.Close();
		return pedidos;
	}


	public Pedido getPedido(String id){
		Log.v(CNT_LOG, "Recuperar Pedido Pelo Id.");
			
		this.Open();
		String where = "_id = ?";
        String[] selectionArgs = new String[] {id};

		Cursor c = db.query(TABELA, null, where, selectionArgs, null, null, null);
		
		List<Pedido> pedidos = null;
		
		if (c.getCount() > 0) {
			pedidos = bindValues(c);	
		}
		
		c.close();
		this.Close();
		return pedidos.get(0);
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
			pedido.setValor_pago(c.getFloat(c.getColumnIndex("valor_pago")));
			pedido.setDesconto(c.getFloat(c.getColumnIndex("desconto")));
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
		
		// Id do Usuario = Vendedor
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String id_usuario = sharedPrefs.getString("settingVendedorId", "NULL");
		
		// Data do Pedido
		Date now = new Date();
		SimpleDateFormat  df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		
		String dt_inclusao = df.format(now);
		
		this.Open();
		
		ContentValues valores = new ContentValues();
		valores.put("id_usuario", id_usuario);
		valores.put("id_cliente", pedido.getId_cliente());
		valores.put("status", pedido.getStatus());
		valores.put("qtd_itens", pedido.getQtd_itens());
		valores.put("valor_total", pedido.getValor_total());
		valores.put("valor_pago", pedido.getValor_pago());
		valores.put("desconto", pedido.getDesconto());
		valores.put("finalizadora", pedido.getFinalizadora());
		valores.put("parcelamento", pedido.getParcelamento());
		valores.put("nfe", pedido.getNfe());
		valores.put("dt_inclusao", dt_inclusao);
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
			valores.put("id_servidor", pedido.getId_servidor());
			valores.put("id_usuario", pedido.getId_usuario());
			valores.put("id_cliente", pedido.getId_cliente());
			valores.put("status", pedido.getStatus());
			valores.put("qtd_itens", pedido.getQtd_itens());
			valores.put("valor_total", pedido.getValor_total());
			valores.put("valor_pago", pedido.getValor_pago());
			valores.put("desconto", pedido.getDesconto());
			valores.put("finalizadora", pedido.getFinalizadora());
			valores.put("parcelamento", pedido.getParcelamento());
			valores.put("nfe", pedido.getNfe());
			valores.put("dt_inclusao", pedido.getDt_inclusao());
			valores.put("dt_envio", pedido.getDt_envio());
			valores.put("observacao", pedido.getObservacao());
	    
		    //Alterar o registro com base no ID
	   		Log.v(CNT_LOG, "Alterando Pedido ["+pedido.getId()+"]");
	   		
		    linhaAlterada = db.update(TABELA, valores, "_id = " + id, null);

		    Log.v(CNT_LOG, "Pedido Alterado ["+linhaAlterada+"], Id ["+id+"]");
		    String debug = writeJSON(pedido);
		    Log.v(CNT_LOG,"DEBUG = "+debug);
	  }
	  catch (Exception e){
	  	Log.e(CNT_LOG, "Alterar - Error ["+e.getMessage()+"]");
	  }
	  finally {
	      this.Close();
	  }
		return linhaAlterada;
	}

    public boolean atualizarPedido(Pedido aberto){
    	Log.v(CNT_LOG, "atualizarPedido()");
    	
    	Pedido pedido = null;
    	if (aberto == null){
	    	// Recuperar o Pedido Aberto
	    	pedido = getPedidoAberto();
    	}
    	else {
    		pedido = aberto;
    	}
    	
    	// Recuperar o Total de Itens
    	PedidoProdutosHelper pedidoProdutoHelper = new PedidoProdutosHelper(context);
    	
    	float quantidade = pedidoProdutoHelper.getTotalItens(pedido);
    	Log.v(CNT_LOG, "QUANTIDADE ("+quantidade+")");

    	float valor_total = pedidoProdutoHelper.getValorTotalItens(pedido);
    	Log.v(CNT_LOG, "VALORTOTAL ("+valor_total+")");
    	
    	// Saber se o pedido tem finalizadora Dinheiro
    	if (pedido.getFinalizadora() == 1){
    		
    		// Aplicar Desconto  
    		float percentual = 10;   
    		  
    		float valorPago = (float) (valor_total - ((percentual / 100.0) * valor_total));
    		
    		float desconto = valor_total - valorPago;
    		
    		pedido.setValor_pago(valorPago);
    		pedido.setDesconto(desconto);

    		Log.v(CNT_LOG, "DESCONTO   "+ desconto);
    		Log.v(CNT_LOG, "VALORPAGO  "+ valorPago);    		
    		
    	}
    	else {
    		Log.v(CNT_LOG, "DEsfazer desconto");
    		pedido.setValor_pago(valor_total);
    		pedido.setDesconto(0);    		
    	}
    	
    	pedido.setQtd_itens(quantidade);
    	pedido.setValor_total(valor_total);
    	
    	if (Alterar(pedido) > -1){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public Boolean fechar(Pedido aberto) {
    	Log.v(CNT_LOG, "fechar()");

    	// Atualizo os Valores do Pedido
    	atualizarPedido(aberto);
    	
    	// Recuperar o Pedido Aberto
    	//Pedido pedido = getPedidoAberto();
    	// Alterar o Status Para Fechado
    	//pedido.setStatus(1); // Fechado
    	aberto.setStatus(1);  	
		// Update no Pedido 
    	if (Alterar(aberto) > -1){
    		return true;	
    	}
    	else {
    		return false;
    	}
    }
	
	public String writeJSON(Pedido pedido) {
		  JSONObject object = new JSONObject();
		  try {
		    object.put("id", pedido.getId());
		    object.put("id_usuario", pedido.getId_usuario());
		    object.put("id_cliente", pedido.getId_cliente());
		    object.put("status", pedido.getStatus());
		    object.put("qtd_itens", pedido.getQtd_itens());
		    object.put("valor_total", pedido.getValor_total());
		    object.put("valor_pago", pedido.getValor_pago());
		    object.put("desconto", pedido.getDesconto());
		    object.put("finalizadora", pedido.getFinalizadora());
		    object.put("parcelamento", pedido.getParcelamento());
		    object.put("nfe", pedido.getNfe());
		    object.put("dt_inclusao", pedido.getDt_inclusao());
		    object.put("dt_envio", pedido.getDt_envio());
		    object.put("observacao", pedido.getObservacao());
		    
		    if (pedido.getProdutos() != null){
			    PedidoProdutosHelper pedidoProdutoHelper = new PedidoProdutosHelper(this.context);
			    
			    List<PedidoProduto> aProdutos = pedido.getProdutos();
 
				String s ="";
				for (int i=0; i < aProdutos.size(); i++){
			    	PedidoProduto produto = aProdutos.get(i);
			    	String str = pedidoProdutoHelper.writeJSON(produto);
			    	//Log.v(CNT_LOG, "JSON PRODUTO ["+i+"] STR["+str+"]");

			    	s += (i == aProdutos.size() - 1) ? str : str + ",";		   
			    }
	    	
				object.put("produtos", s);
		    }
		    else {
		    	Log.e(CNT_LOG, "ERRO CORRIGIR... UM PEDIDO NAO PODE SER FECHADO SEM PRODUTOS");
		    }
		    
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		  //return object.toString().replace("\\", "");
		  return object.toString();
	} 
	
}

//public void enviarPedidos(Pedido pedido) {
//	Log.v(CNT_LOG, "enviarPedidos()");
//	
//	pedidoAEnviar = pedido;
//
//	// Parametros
//	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//  nameValuePairs.add(new BasicNameValuePair("classe", "ClientAndroid"));
//  nameValuePairs.add(new BasicNameValuePair("action", "setPedidos"));
//  nameValuePairs.add(new BasicNameValuePair("data", pedido.getJsonString()));
//	
//  // Crio um Objeto TaskPost
//	HttpTaskPost httpPost = new getPedidosHttp();
//	// Passo os Parametros
//	httpPost.setParametros(nameValuePairs);
//	// Passo o Contexto para disparar erros
//	httpPost.setContext(this.context);
//	
//	// Primeito tentar Remoto
//	if (this.REMOTE == true){ 	
//  	String urlRemoto = this.getServerHostRemote();
//  	Log.v(CNT_LOG, "UrlRemoto = "+urlRemoto);
//		httpPost.execute(urlRemoto);
//	}
//	else {
//		// Se falhar no Remoto Tentar no Local
//  	String urlLocal = this.getServerHostLocal();
//  	Log.v(CNT_LOG, "UrlLocal = "+urlLocal);
//		httpPost.execute(urlLocal);    		
//	}
//
//}
//
//public class getPedidosHttp extends HttpTaskPost {
//	
//	protected void onPreExecute() {
//		super.onPreExecute();
//		Log.v(CNT_LOG, "onPreExecute");	
//  }
//	protected void onPostExecute(String[] resultado) {
//		Log.v(CNT_LOG, "onPostExecute");
//
//		if (resultado[0] == "success"){
//			Log.v(CNT_LOG,"Passo 8");
//			// Passando a string para o metodo que vai inserir
//			taskSuccess(resultado[1]);
//		}
//		else {
//			Log.e(CNT_LOG,"Passo 10 - o json veio vazio");
//			taskFailed(resultado);
//		}
//	}	
//}
//protected void taskSuccess(String strJson){
//	JSONObject json = null;
//	
//	try {
//		json = new JSONObject(strJson);
//		
//		// Saber se a Resposta do Json Foi de Sucesso
//		Boolean success =  (Boolean) json.get("success");
//
//		if (success){			
//			Log.e(CNT_LOG, "Sucess ID ["+json.get("id")+"] ID_Servidor ["+json.get("id_servidor")+"]");
//			// Recuperar o Pedido que foi enviado Incluir o ID_SERVIDOR ALTERAR O STATUS PARA ENVIADO.
//			pedidoAEnviar.setId_servidor(json.getInt("id_servidor"));
//			pedidoAEnviar.setDt_envio(json.getString("dt_envio"));
//			pedidoAEnviar.setStatus(2); // Enviado com Sucesso
//			
//			// Alterar o Pedido
//
//	    	if (Alterar(pedidoAEnviar) < 0){
//  			Log.e(CNT_LOG, "Pedido Enviado mas Houve um erro no update do status");
//	    	}
//		
//			// TODO: ENVIAR OS CLIENTES
//			//inserirDepartamentosJson(json);
//		}
//		else {			
//			// No servidor se nao houver resultados na query retorna success=false
//			Log.e(CNT_LOG, "Nenhuma Alteraçao a ser Feita.");
//			Messages.showSuccessToast(this.context, "Nenhum Departamento a ser Alterado");
//		}
//	}
//	catch(JSONException e){
//		Log.e(CNT_LOG, "Error parsing Json "+e.toString());
//		Messages.showErrorAlert(this.context, "Houve um erro na Resposta do Servidor.");
//	}           
//}
//
//protected void taskFailed(String[] resultado){
//	Log.v(CNT_LOG, "taskFailed");
//	
//	// Se a flag REMOTE estiver true significa que ta na 1 tentativa
//	//virar a flag para false para a segunda tentativa se nao conseguir mostrar erro
//	if (this.REMOTE == true){
//		this.REMOTE = false;
//		enviarPedidos(pedidoAEnviar);
//	}
//	else {
//		// Se tiver dado falha nas 2 tentativas retorna mensagem de erro 
//		Messages.showErrorAlert(this.context, resultado[1].toString());
//	}
//}


//public void onCreate(SQLiteDatabase db) {
//Log.v(CNT_LOG, "Crianda a Tabela [ "+TABELA+" ]");
//
//String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+
//		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//			" id_servidor TEXT," +
//			" id_usuario TEXT," +
//			" id_cliente TEXT," +
//			" status INTEGER," +
//			" qtd_itens REAL," +
//			" valor_total REAL," +
//			" finalizadora INTEGER," +
//			" parcelamento INTEGER," +
//			" nfe INTEGER," +
//			" dt_inclusao TEXT," +
//			" dt_envio TEXT," +
//			" observacao TEXT" +
//		");";
//
//db.execSQL(sql);
//
//Log.v(CNT_LOG, "Tabela [ "+TABELA+" ] Criada com Sucesso!");
//}


//public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//Log.v(CNT_LOG, "onUprade - Drop Table ["+TABELA+"]");
//
//try {
//	db.execSQL("DROP TABLE IF EXISTS " + PedidoHelper.TABELA);
//}
//catch (Exception error){
//	Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
//}
//
//this.onCreate(db);
//}
