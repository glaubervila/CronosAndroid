package br.com.vilaverde.cronos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.dao.DepartamentosHelper;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.dao.VendedorHelper;
import br.com.vilaverde.cronos.httpclient.ConexaoHttpClient;
import br.com.vilaverde.cronos.httpclient.HttpGetTask;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;

public class Atualizar extends Activity  implements AsyncTaskCompleteListener<String>{

	private static String CNT_LOG = "Actualizar";
	
	SharedPreferences sharedPrefs = null;
	String serverHost = "";
	String task = "";
	String entidade = "";
	int aEnviar = 0;
	int enviados = 0;
    //public Handler handler = new Handler();  
	ProgressDialog progressDialog = null;
	//AlertDialog dialog = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//setContentView(R.layout.atualizar);
		// Instanciando o Gerenciador de Preferencias
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Este parametro deve ser setado na instancia da classe
		task = "push";
		entidade = "clientes";
		// Testar Conexao com Internet
		// 1 - Testar se Tem Conexao com internet
		Boolean conexao = ConexaoHttpClient.Conectado(this.getApplicationContext());
				
		if (conexao){
			Log.v(CNT_LOG,"CONEXAO ["+conexao+"]");
			// 2 - Escolher Remota ou Local
			selectLocalRemoteDialog().show();			
		}
		else {
			Log.w(CNT_LOG,"CONEXAO ["+conexao+"]");
			// Retornar Erro e Fechar
			noConectionDialog().show();
		}	
	}
	
	
	public boolean setServerHost(String serverhost){
		if (serverhost.isEmpty() || serverhost.equalsIgnoreCase("NULL")){
			Log.e(CNT_LOG, "SERVERHOST [ Empty or Null ]");
			// Mostra mensagem de erro
			noHostDialog().show();
			return false;
		}
		else {
			serverHost = serverhost;
			Log.v(CNT_LOG, "SERVERHOST [ "+serverhost+" ]");
			return true;
		}
	}
	
	public void sincronizar(){
		Log.v(CNT_LOG,"sincronizar()");
		
		// TODO: Saber se esta enviando ou recebendo
		if (task == "push"){
			// Envio de Dados
			// Setando a Progress
			progressDialog = ProgressDialog.show(this, "Enviando Dados", getResources().getString(R.string.atualizar), true, true);
			push();
		}
		else if (task == "pull"){
			// Recebendo Dados
			pull();
		}
	}
	
	public void push(){
		Log.v(CNT_LOG,"push()");

		if (entidade.equalsIgnoreCase("clientes")){
			// Enviar Clientes
			pushClientes();
		}
		else if (entidade.equalsIgnoreCase("pedidos")){
			// Enviar os pedidos			
			pushPedidos();						
		}
	}

	public void pull(){
		Log.v(CNT_LOG,"pull()");
 
		if (entidade.equalsIgnoreCase("vendedores")){
			pullVendedores();
		}
		else if (entidade.equalsIgnoreCase("departamentos")){		
			pullDepartamentos();						
		}
		else if (entidade.equalsIgnoreCase("produtos")){
			pullProdutos();						
		}
		else if (entidade.equalsIgnoreCase("clientes")){	
			pullClientes();						
		}
	}

	// ---------------------------------------< PUSH >-----------------------------------------
	
	public boolean pushClientes(){
		Log.v(CNT_LOG,"pushClientes()");
		// recuperar os clientes a serem enviados
		ClienteHelper clienteHelper = new ClienteHelper(getApplicationContext());
		
		
		List<Cliente> clientes = clienteHelper.getClientesAEnviar();

		if (clientes != null){
			// Setar o Total a Enviar
			aEnviar  = clientes.size();
			Log.v(CNT_LOG, "A ENVIAR ["+aEnviar+"]");
			
			for (int i=0; i< clientes.size();i++){
				Cliente cliente = clientes.get(i);

				Log.v(CNT_LOG, "ENVIANDO Cliente ["+cliente.getId()+"]");
				
				// Monta a String Json do pedido
				String jsonString = clienteHelper.writeJSON(cliente);
				
				// Seta os parametros e executa o metodo que vai enviar				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("classe", "ClientAndroid"));
				params.add(new BasicNameValuePair("action", "setClientes"));
				params.add(new BasicNameValuePair("data", jsonString));

				Log.v(CNT_LOG, "JSON ["+jsonString+"]");
				
		    	sendData(params);			

			}
			return true;
		}
		else {
			Log.v(CNT_LOG, "Nenhum Cliente a Enviar");
			Toast.makeText(this, "Nenhum Cliente a Enviar", Toast.LENGTH_LONG).show();
			entidade = "pedidos";
			push();
			return false;
		}
	}

	public void pullClientes(JSONObject json){
		Log.v(CNT_LOG,"pullPedidos()");

		try {
			
			int id = json.getInt("id");
			int id_servidor = json.getInt("id_servidor");
			
			// Apos enviar o cliente gravar o id sercidor e alterar os pedidos com o id
			ClienteHelper clienteHelper = new ClienteHelper(getApplicationContext());			
			Cliente cliente = clienteHelper.getCliente(id);

			cliente.setId_servidor(id_servidor);
			cliente.setStatus_servidor("1");
			
			
			if (clienteHelper.Alterar(cliente) > 0){
				// cliente alterado enviado com sucesso
				enviados++;	

				// Atualizando a Progress Dialog
				int count = enviados;
				String msg = "Enviando Cliente "+count+ " de "+aEnviar+".";
				progressDialog.setMessage(msg);

				if (enviados == aEnviar){
					Log.e(CNT_LOG,"ACABOU DE ENVIAR CLIENTES");
					// Enviou Todos
					String msg2 = enviados+" Clientes(s) Enviado(s)";
					Toast.makeText(this, msg2, Toast.LENGTH_LONG).show();
					enviados = 0;
					entidade = "pedidos";
					push();
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean pushPedidos(){
		Log.v(CNT_LOG,"pushPedidos()");
		// recuperar os pedidos a serem enviados
		PedidoHelper pedidoHelper = new PedidoHelper(getApplicationContext());
		PedidoProdutosHelper pedidoProdutosHelper = new PedidoProdutosHelper(getApplicationContext());
		
		List<Pedido> pedidos = pedidoHelper.getPedidosAEnviar();
		
		if (pedidos != null){
			// Setar o Total a Enviar
			aEnviar  = pedidos.size();
			Log.v(CNT_LOG, "A ENVIAR ["+aEnviar+"]");
			
			for (int i=0; i< pedidos.size();i++){
				Pedido pedido = pedidos.get(i);
				Log.v(CNT_LOG, "ENVIANDO PEDIDO ["+pedido.getId()+"]");
								
				// Para cada Pedido recuperar os produtos.
				List<PedidoProduto> produtos = pedidoProdutosHelper.getProdutos(pedido);
				// Setando os Produtos no pedido 
				pedido.setProdutos(produtos);
				
				// Recuperar o Id Cliente no Servidor
				ClienteHelper clienteHelper = new ClienteHelper(getApplicationContext());			
				Cliente cliente = clienteHelper.getCliente(Integer.parseInt(pedido.getId_cliente()));

				// Setando o id servidor no pedido substituindo o id_cliente local somente para o envio
				pedido.setId_cliente(""+cliente.getId_servidor());
				// Monta a String Json do pedido
				String jsonString = pedidoHelper.writeJSON(pedido);
				
				// Seta os parametros e executa o metodo que vai enviar				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("classe", "ClientAndroid"));
				params.add(new BasicNameValuePair("action", "setPedidos"));
				params.add(new BasicNameValuePair("data", jsonString));

				Log.v(CNT_LOG, "JSON ["+jsonString+"]");
				
		    	sendData(params);			
			}
			
			return true;
		}
		else {
			Log.v(CNT_LOG, "Nenhum Pedido a Enviar");
			String msg = "Nenhum Pedido a Enviar";
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//			progressDialog.dismiss();
//			finish();
			task = "pull";
			entidade = "vendedores";
			sincronizar();

			
			return false;
		}
	}
	
	public void pullPedidos(JSONObject json){
		Log.v(CNT_LOG,"pullPedidos()");
		
		try {
		
			String id = json.getString("id");
			int id_servidor = json.getInt("id_servidor");
			String dt_envio = json.getString("dt_envio");
			int status = json.getInt("status");
			
			// Apos enviar o pedido gravar o id do pedido no servidor a data de envio e alterar o status.
			PedidoHelper pedidoHelper = new PedidoHelper(getApplicationContext());
			Pedido pedido = pedidoHelper.getPedido(id);

			pedido.setId_servidor(id_servidor);
			pedido.setDt_envio(dt_envio);
			pedido.setStatus(status);
			
			if (pedidoHelper.Alterar(pedido) > 0){
				// Pedido alterado enviado com sucesso
				enviados++;	

				// Atualizando a Progress Dialog
				int count = enviados;
				String msg = "Enviando Pedido "+count+ " de "+aEnviar+".";
				progressDialog.setMessage(msg);

				if (enviados == aEnviar){
					Log.e(CNT_LOG,"ACABOU DE ENVIAR PEDIDOS");
					// Enviou Todos
					String msg2 = enviados+" Pedido(s) Enviado(s)";
					Toast.makeText(this, msg2, Toast.LENGTH_LONG).show();
				
//					progressDialog.dismiss();
//					finish();
					// TODO Por enquanto vai chamar o push por aki
					task = "pull";
					entidade = "vendedores";
					sincronizar();
				}

				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	// ---------------------------------------< PULL >-----------------------------------------
	public void pullVendedores(){
		Log.v(CNT_LOG,"pullVendedores()");
		// recuperar os Vendedores		
		// Seta os parametros e executa o metodo que vai enviar
		progressDialog.setMessage("Recebendo Vendedores");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("classe", "ClientAndroid"));
		params.add(new BasicNameValuePair("action", "getVendedores"));
			
		sendData(params);
	}
	
	public void pushVendedores(JSONObject json){
		Log.v(CNT_LOG,"pushVendedores()");
		
		progressDialog.setMessage("Atualizando Vendedores");
		VendedorHelper vendedorHelper = new VendedorHelper(this);
		Boolean r = vendedorHelper.inserirVendedoresJson(json);
		Log.v(CNT_LOG, "RESULT"+r);
		if (r){
			String msg ="Registros Atualizados.";
			progressDialog.setMessage(msg);

			// Executa o pull da proxima entidade
			entidade = "departamentos";
			pull();
		}
		else {			
			onFailure("Falha ao Atualizar os Registros de Vendedores");

		}
	}

	public void pullDepartamentos(){
		Log.v(CNT_LOG,"pullDepartamentos()");
		// recuperar os Vendedores		
		// Seta os parametros e executa o metodo que vai enviar
		progressDialog.setMessage("Recebendo Departamentos");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("classe", "ClientAndroid"));
		params.add(new BasicNameValuePair("action", "getDepartamentos"));
			
		sendData(params);
	}

	public void pushDepartamentos(JSONObject json){
		Log.v(CNT_LOG,"pushDepartamentos()");
		
		progressDialog.setMessage("Atualizando Departamentos");
		DepartamentosHelper departamentosHelper = new DepartamentosHelper(this);
		Boolean r = departamentosHelper.inserirDepartamentosJson(json);
		Log.v(CNT_LOG, "RESULT [ "+r+"]");
		if (r){
			String msg ="Registros Atualizados.";
			progressDialog.setMessage(msg);
			// Executa o pull da proxima entidade
			entidade = "produtos";
			pull();
		}
		else {			
			onFailure("Falha ao Atualizar os Registros de departamentos");
		}
	}

	public void pullProdutos(){
		Log.v(CNT_LOG,"pullProdutos()");
		// recuperar os Produtos		
		// Seta os parametros e executa o metodo que vai enviar
		progressDialog.setMessage("Recebendo Produtos");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("classe", "ClientAndroid"));
		params.add(new BasicNameValuePair("action", "getProdutos"));
			
		sendData(params);
	}

	public void pushProdutos(JSONObject json){
		Log.v(CNT_LOG,"pushProdutos()");
		
		progressDialog.setMessage("Atualizando Produtos");
		ProdutosHelper produtosHelper = new ProdutosHelper(this);
		Boolean r = produtosHelper.inserirProdutosJson(json);
		Log.v(CNT_LOG, "RESULT [ "+r+"]");
		if (r){
			String msg ="Registros Atualizados.";
			progressDialog.setMessage(msg);
			// Executa o pull da proxima entidade
			entidade = "clientes";
			pull();
		}
		else {			
			onFailure("Falha ao Atualizar os Registros de produtos");
		}
	}

	public void pullClientes(){
		Log.v(CNT_LOG,"pullClientes()");
		// recuperar os Clientes para um Vendedor		
		// Seta os parametros e executa o metodo que vai enviar
		progressDialog.setMessage("Recebendo Clientes");

		JSONObject data = new JSONObject();
		try {
		    data.put("id_vendedor", 1);
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("classe", "ClientAndroid"));
		params.add(new BasicNameValuePair("action", "getClientes"));
		params.add(new BasicNameValuePair("data", data.toString()));
			
		sendData(params);
	}

	public void pushClientes(JSONObject json){
		Log.v(CNT_LOG,"pushClientes()");
		
//		progressDialog.setMessage("Atualizando Clientes");
//		ProdutosHelper produtosHelper = new ProdutosHelper(this);
//		Boolean r = produtosHelper.inserirProdutosJson(json);
//		Log.v(CNT_LOG, "RESULT [ "+r+"]");
//		if (r){
//			String msg ="Registros Atualizados.";
//			progressDialog.setMessage(msg);
//			// Executa o pull da proxima entidade
//			entidade = "fotos";
//			pull();
//		}
//		else {			
//			onFailure("Falha ao Atualizar os Registros de clientes");
//		}
	}
	

	
//------------------------------ < SELE��O DO TIPO DE CONEXAO > ------------------------------	
	public Dialog selectLocalRemoteDialog(){
		
		return new AlertDialog.Builder(this)
        .setTitle(R.string.tipoConexaoTitulo)
        .setMessage(R.string.tipoConexaoDescricao)
        .setPositiveButton(R.string.btn_local, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            	setLocal();
            }
        })
        .setNegativeButton(R.string.btn_remota, new DialogInterface.OnClickListener()    {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            	setRemota();
            }
        })
        .create();		
	}

	public void setLocal(){
		if (setServerHost(sharedPrefs.getString("settingLocalHost", "NULL")))
		{
			sincronizar();
		}
	}
	public void setRemota(){
		if (setServerHost(sharedPrefs.getString("settingRemotaHost", "NULL")))
		{
			sincronizar();
		}
	}	
	
	
	public Dialog noConectionDialog() {
		
		return new AlertDialog.Builder(this)
        .setTitle(R.string.semConexaoTitulo)
        .setMessage(R.string.semConexaoDescricao)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {                   
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .create();		
	}
	
	public Dialog noHostDialog(){
		return new AlertDialog.Builder(this)
        .setTitle("Host Inv�lido")
        .setMessage("Host Inv�lido Verifique as configura��es.")
        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            	finish();
            }
        })
        .create();
	}

	public Dialog onFailure(String msg){
		// fechar tudo e mostrar a mensagem de erro
		return new AlertDialog.Builder(this)
        .setTitle("Aten��o")
        .setMessage(msg)
        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            	finish();
            }
        })
        .create();
	}
	

	public void sendData(ArrayList<NameValuePair> params){
		Log.v(CNT_LOG,"sendData()");
		HttpGetTask b = new HttpGetTask(this);
		b.setParametros(params);
		b.execute(serverHost);
	}
	
	@Override
	public void onTaskComplete(String result) {
		Log.v(CNT_LOG, "onTaskComplete()");
		
		Log.v(CNT_LOG,"TASK COMPLETE RESULT["+result+"]");
		
		JSONObject json = null;
		
		try {
			json = new JSONObject(result);		
			// Saber se a Resposta do Json Foi de Sucesso
			Boolean success =  (Boolean) json.get("success");

			if (success){			
				Log.v(CNT_LOG,"RETORNOU SUCCESS");
				String entidade = json.getString("entidade");
				Log.v(CNT_LOG,"Entidade "+entidade);
				// Envio
				if (entidade.equalsIgnoreCase("clientes")){
					pullClientes(json);
				}
				else if (entidade.equalsIgnoreCase("pedidos")){
					pullPedidos(json);
				}
				// Recebendo
				else if (entidade.equalsIgnoreCase("vendedores")){
					pushVendedores(json);
				}
				else if (entidade.equalsIgnoreCase("departamentos")){
					pushDepartamentos(json);
				}
				else if (entidade.equalsIgnoreCase("produtos")){
					pushProdutos(json);
				}
				else if (entidade.equalsIgnoreCase("pullclientes")){
					pushClientes(json);
				}
//				else if (entidade.equalsIgnoreCase("fotos")){
//					pushFotos(json);
//				}
				else {
					Log.v(CNT_LOG,"NENHUM");
				}	
			}
			else {
				Log.v(CNT_LOG,"RETORNOU FAILURE");
				String msg = json.get("msg").toString();
				onFailure(msg).show();
			}		
		}
		catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
			onFailure("Falha na Atualiza��o").show();
		}
	}
	
//	@Override
//    public void onTaskComplete(String result)
//    {
//		Log.v(CNT_LOG,"TASK COMPLETE RESULT["+result+"]");
//		
//		JSONObject json = null;
//		
//		try {
//			json = new JSONObject(result);		
//			// Saber se a Resposta do Json Foi de Sucesso
//			//Boolean success =  (Boolean) json.get("success");
//
//			String entidade = json.getString("entidade");
//			Log.v(CNT_LOG,"Entidade "+entidade);
//			
//			if (entidade.equalsIgnoreCase("vendedores")){
//				Log.v(CNT_LOG,"VENDEDORES");
//				gravarVendedor(result);
//			}
//			else if (entidade == "pedidos"){
//				Log.v(CNT_LOG,"PEDIDOS");
//			}
//			else {
//				Log.v(CNT_LOG,"NENHUM");
//			}
////			if (success){			
////				Log.v(CNT_LOG,"RETORNOU SUCCESS");
////				
////				
////			}
////			else {
////				Log.v(CNT_LOG,"RETORNOU FAILURE");
////			}
//		}
//		catch(JSONException e){
//			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
//		}           
//
//		//progressDialog.dismiss();
//    }
//	
//
//	private void gravarVendedor(String result) {
//		Log.e(CNT_LOG, "gravarVendedor");
//		
//	}
//	public void teste2(){
//		Log.v(CNT_LOG, "TESTE 2");
//	 
//		// Parametros
//		String url = "http://192.168.0.200/cronos/main.php";		
//		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("classe", "ClientAndroid"));
//        nameValuePairs.add(new BasicNameValuePair("action", "getVendedores"));
////        nameValuePairs.add(new BasicNameValuePair("data", ""));
//		HttpGetTask b = new HttpGetTask(this);
//		b.setParametros(nameValuePairs);
//		b.execute(url);		
//
//
//	}
	
	

//	public void Sincronizar(){
//		Log.v(CNT_LOG, "Inicio da Atualizcao");	
//		
//		// Classe responsavel por sincronizar o tablet com o servidor
//		
//		// 1 - Testar se Tem Conexao com internet
//		Boolean conexao = ConexaoHttpClient.Conectado(this.context);
//				
//		if (conexao){
//		
//			// 2 - Recuperar os Vendedores
//			Log.v(CNT_LOG, "1 - Recuperar os Vendedores");
//			//getVendedores();
//			
//			// 3 - Recuperar os Produtos
//			Log.v(CNT_LOG, "2 - Recuperar os Produtos");	
//			//getProdutos();
//			
//			// 4 - Recuperar os Departamentos
//			Log.v(CNT_LOG, "3 - Recuperar os Departamentos");
//			//getDepartamentos();
//			
//			Log.v(CNT_LOG, "4 - Enviar Pedidos");
//			getPedidosEnviar();
//		}
//		else{
//			Messages.showErrorAlert(this.context, "Verifique sua Conex�o com a Internet!");
//		}
//		
//
//	}
	
//	public void getProdutos(){
//		Log.v(CNT_LOG, "getProdutos()");
//		
//	
//		ProdutosHelper produtosHelper = new ProdutosHelper(this.context);
//		
//		produtosHelper.getWSProdutos();
//				 
//	}
	
//	public void getVendedores(){
//		Log.v(CNT_LOG, "getProdutos()");
//		
//	
//		VendedorHelper vendedorHelper = new VendedorHelper(this.context);
//		
//		vendedorHelper.sincronizarVendedores();
//				 
//	}

//	public void getDepartamentos(){
//		Log.v(CNT_LOG, "getDepartamentos()");
//		
//		DepartamentosHelper departamentosHelper = new DepartamentosHelper(this.context);
//		
//		departamentosHelper.sincronizarDepartamentos();
//				 
//	}

	
//	public void getPedidosEnviar(){
//		Log.v(CNT_LOG, "getPedidosEnviar()");
//		
//		PedidoHelper pedidoHelper = new PedidoHelper(this.context);
//		PedidoProdutosHelper pedidoProdutosHelper = new PedidoProdutosHelper(this.context);
//		
//		List<Pedido> pedidos = pedidoHelper.getPedidosAEnviar();
//		
//		if (pedidos != null){
//
//			for (int i=0; i< pedidos.size();i++){
//				Pedido pedido = pedidos.get(i);
//				Log.v(CNT_LOG, "ENVIANDO PEDIDO ["+pedido.getId()+"]");
//				
//				// Para cada Pedido recuperar os produtos.
//				List<PedidoProduto> produtos = pedidoProdutosHelper.getProdutos(pedido);
//				// Setando os Produtos no pedido 
//				pedido.setProdutos(produtos);
//				
//				String jsonString = pedidoHelper.writeJSON(pedido);
//				Log.v(CNT_LOG, "JSON ["+jsonString+"]");
//				
//				pedido.setJsonString(jsonString);
//				pedidoHelper.enviarPedidos(pedido);
//			}
//			Log.v(CNT_LOG, "Cabou");
//		}
//		else {
//			Log.v(CNT_LOG, "Nenhum Pedido a Enviar");
//		}
//				 
//	}

	
	

//	public AlertDialog getDialog() {
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		
//	    // Get the layout inflater
//	    LayoutInflater inflater = this.getLayoutInflater();
//
//	    builder.setTitle("Enviar Pedido");
//	    
//	    // Inflate and set the layout for the dialog
//	    // Pass null as the parent view because its going in the dialog layout
//	    final View finalizarView = inflater.inflate(R.layout.atualizar_dialog, null);
//	    builder.setView(finalizarView);
//	    
//	    // Recuperar os elementos da dialog
//
//   
//	    return builder.create();
//
//	}


//	

//  DialogFragment newFragment = MyAlertDialogFragment.newInstance(R.string.atualizar);
//  newFragment.show(getFragmentManager(), "dialog");

	
//	public static class MyAlertDialogFragment extends DialogFragment {
//
//	    public static MyAlertDialogFragment newInstance(int title) {
//	        MyAlertDialogFragment frag = new MyAlertDialogFragment();
//	        Bundle args = new Bundle();
//	        args.putInt("title", title);
//	        frag.setArguments(args);
//	        
//	        return frag;
//	    }
//
//	    @Override
//	    public Dialog onCreateDialog(Bundle savedInstanceState) {
//	        int title = getArguments().getInt("title");
//      
//	        return new AlertDialog.Builder(getActivity())
//	                .setIcon(R.drawable.ic_add)
//	                .setTitle(title)
//	                .create();
//	    }
//	    
//	    public void teste(String titulo) {
//	    	
//	    }
//	}
	
//	public class HttpGetTask extends AsyncTask<String, Void, String>
//	{
//	
//	    private AsyncTaskCompleteListener<String> callback;
//	    
//		private  ArrayList<NameValuePair> parametros;
//		
//		public ArrayList<NameValuePair> getParametros() {
//			return parametros;
//		}
//
//		public void setParametros(ArrayList<NameValuePair> parametros) {
//			this.parametros = parametros;
//		}
//
//	    public HttpGetTask(AsyncTaskCompleteListener<String> callback)
//	    {
//	       this.callback = callback;
//	    }
//
//	    protected String doInBackground(String... strings)
//	    {
//			Log.v(CNT_LOG, "doInBackground = "+strings[0].toString());
//	    	
//			// Setando Variavel de Resposta
//	    	String result = "false";		
//	    	
//			// Para Requests Post recuperar os parametros 
//			parametros = this.getParametros();
//						
//			try {
//				
//				Log.v(CNT_LOG, "1 - Recuperando a Conexao HttpClient");
//		        HttpClient httpClient = new DefaultHttpClient();
//		          
//		        
//	        	//HttpPost httpPost = new HttpPost("http://192.168.0.200/cronos/main.php?classe=ClientAndroid&action=getVendedores");
//		        HttpPost httpPost = new HttpPost(strings[0].toString());
//		
//		        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
//		        httpPost.setEntity(new UrlEncodedFormEntity(parametros));
//		          
//		        Log.v(CNT_LOG, "3 - Executando a Requisicao Http");
//		        HttpResponse response = httpClient.execute(httpPost);
//
//		        Log.v(CNT_LOG, "4 - Lendo a Resposta");
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//		        
//		        Log.v(CNT_LOG, "5 - Criando a String de Resposta");
//				StringBuilder stringBuilder = new StringBuilder();
//
//				String line = null;
//				String LS = System.getProperty("line.separator");
//
//				while ((line = reader.readLine()) != null){
//					stringBuilder.append(line + LS);
//				}
//
//				reader.close();
//				String strResult = stringBuilder.toString();
//				Log.v(CNT_LOG, "StringResult: "+strResult);
//
//		        // Testando a Resposta
//				if (strResult != null && !strResult.isEmpty()) {
//					result = strResult.toString();
//		        }
//		        else {
//					Log.v(CNT_LOG, "5 - Resposta Vazia");
//		        }				
//			}
//			catch (ConnectTimeoutException e) {
//				e.printStackTrace();		
//			}
//			catch (Exception e){
//				e.printStackTrace();
//	           // handle "error connecting to the server"
//			}
//
//			return result;
//	    }
//
//	    @Override
//	    protected void onPostExecute(String result)
//	    {
//	       callback.onTaskComplete(result);
//	    }
//
//
//	}
	

}
