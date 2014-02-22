package br.com.vilaverde.cronos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.httpclient.ConexaoHttpClient;
import br.com.vilaverde.cronos.httpclient.HttpGetTask;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.model.Produto;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Enviar implements AsyncTaskCompleteListener<String>{

	private static String CNT_LOG = "Enviar";
	private Context context = null;
	
	SharedPreferences sharedPrefs = null;
	String serverHost = "";

	ProgressDialog progressDialog = null;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    private Handler handler = new Handler();
	
	int aEnviar = 0;
	int enviados = 0;	
	
	int clientesEnviados = 0;
	int pedidosEnviados = 0;
	
	//user supplied variables
    private String host = "maguideposito.servehttp.com";
    private int port = 80;
    private String path = "cronos/main.php";
    private String scheme = "http";
	
	public Enviar(Context context) {
		this.context = context;
		
		// Instanciando o Gerenciador de Preferencias
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("Enviando Dados");
	}
	
	public void enviar(){
		Log.v(CNT_LOG, "enviar()");

		// Testar Conexao com Internet
		// 1 - Testar se Tem Conexao com internet
		Boolean conexao = ConexaoHttpClient.Conectado(this.context);
				
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
	
	private void sincronizarClientes() {
		pushClientes();
	}	

	private void sincronizarPedidos() {
		pushPedidos();
	}	
	
	private void showSuccessDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		
		String msg = "Nenhum dado à ser enviado.";
		if (clientesEnviados != 0 || pedidosEnviados !=0) {
			msg = "Clientes: " + clientesEnviados + "\n" + "Pedidos: " + pedidosEnviados  ;
		}
	
        builder.setTitle("Dados Enviados")
        	   .setMessage(msg)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   dialog.dismiss();
                   }
               });
        builder.create().show();
	}

	public void pushClientes(){
		Log.v(CNT_LOG,"pushClientes()");
		
		mProgressStatus = 0;
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMessage("Enviando Clientes");
		progressDialog.show();

		// recuperar os clientes a serem enviados
		final ClienteHelper clienteHelper = new ClienteHelper(this.context);
		
		final List<Cliente> clientes = clienteHelper.getClientesAEnviar();

		if (clientes != null){
			// Setar o Total a Enviar
			aEnviar  = clientes.size();
			Log.v(CNT_LOG, "A ENVIAR ["+aEnviar+"]");
			
			progressDialog.setMax(aEnviar);
			
	        Thread thread = new Thread(new Runnable() {
	            public void run() {
	                while (mProgressStatus < aEnviar) {
	                	
	            		// recupera o Cliente
	    				Cliente cliente = clientes.get(mProgressStatus);

	    				Log.v(CNT_LOG, "ENVIANDO Cliente ["+cliente.getId()+"]");
	    				
	    				// Monta a String Json do pedido
	    				String jsonString = clienteHelper.writeJSON(cliente);
	    				
	    				// Seta os parametros e executa o metodo que vai enviar				
	    				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
	    				params.add(new BasicNameValuePair("classe", "ClientAndroid"));
	    				params.add(new BasicNameValuePair("action", "setClientes"));
	    				params.add(new BasicNameValuePair("data", jsonString));
	    				
	    				Log.v(CNT_LOG, "JSON ["+jsonString+"]");
	    				


	    				String result;
						// ENVIAR DADOS
	    				try {
	    					
	    					Log.v(CNT_LOG, "1 - Recuperando a Conexao HttpClient");
	    			        HttpClient httpClient = new DefaultHttpClient();
	    			                  
	    			        // Testar se o Host tem porta, se tiver divide o host no : o primeiro indice e o host o outro e a porta
	    			        if (serverHost.contains(":")) {
	    				        String s[] = serverHost.toString().split(":");
	    				        host = s[0];
	    				        port = Integer.parseInt(s[1]);
	    			        }
	    			        else {
	    			        	host = serverHost.toString();
	    			        }
	    			        URI uri = URIUtils.createURI(scheme, host, port, path, null, null);
	    			        Log.w(CNT_LOG, "URI    [ "+uri.toString()+" ]");

	    		        
	    			        HttpPost httpPost = new HttpPost(uri);       
	    			        //HttpPost httpPost = new HttpPost(strings[0].toString());
	    			
	    			        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
	    			        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

	    			        Log.v(CNT_LOG, "3 - Executando a Requisicao Http");
	    			        HttpResponse response = httpClient.execute(httpPost);

	    			        Log.v(CNT_LOG, "4 - Lendo a Resposta");
	    			        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    			        
	    			        Log.v(CNT_LOG, "5 - Criando a String de Resposta");
	    					StringBuilder stringBuilder = new StringBuilder();

	    					String line = null;
	    					String LS = System.getProperty("line.separator");

	    					while ((line = reader.readLine()) != null){
	    						stringBuilder.append(line + LS);
	    					}

	    					reader.close();
	    					String strResult = stringBuilder.toString();
	    					Log.v(CNT_LOG, "StringResult: "+strResult);

	    			        // Testando a Resposta
	    					if (strResult != null && !strResult.isEmpty()) {
	    						result = strResult.toString();
	    						
	    						JSONObject json = null;
	    						try {
	    							json = new JSONObject(result);		
	    							// Saber se a Resposta do Json Foi de Sucesso
	    							Boolean success =  (Boolean) json.get("success");
	    							
	    							if (success){			
	    								Log.v(CNT_LOG,"RETORNOU SUCCESS");
	    						
	    								int id = json.getInt("id");
	    								int id_servidor = json.getInt("id_servidor");
	    						
	    								// Apos enviar o cliente gravar o id sercidor e alterar os pedidos com o id
	    								cliente.setId_servidor(id_servidor);
	    								cliente.setStatus_servidor("1");
	    							    						
	    								if (clienteHelper.Alterar(cliente) > 0){
	    									// cliente alterado enviado com sucesso
	    									Log.v(CNT_LOG, "cliente alterado enviado com sucesso");
	    									clientesEnviados++;
	    								}
	    								else {
	    									Log.v(CNT_LOG, "Falhou alterar o cliente");
	    								}
    								}
    							} 
    							catch (JSONException e) {
    								e.printStackTrace();
    							}

	    			        }
	    			        else {
	    						Log.v(CNT_LOG, "5 - Resposta Vazia");
	    			        }				
	    				}
	    				catch (ConnectTimeoutException e) {
	    					Log.v(CNT_LOG, "ConnectTimeout");
	    					e.printStackTrace();		
	    					result = "{success:false,msg=\"Tempo Expirado.\"}";
	    				}
	    				catch (Exception e){
	    					Log.v(CNT_LOG, "Erro de Conexao ["+e.toString()+"]");
	    					e.printStackTrace();
	    					result = "{success:false,msg=\"Falha ao conectar com Servidor.\"}"; 
	    		           // handle "error connecting to the server"
	    				}
	    				
            			progressDialog.setProgress(mProgressStatus);
            			mProgressStatus = mProgressStatus +1;
	 	            		
	                    // Update the progress bar
	                    mHandler.post(new Runnable() {
	                        public void run() {
	                        	progressDialog.setProgress(mProgressStatus);
	                        }
	                    });
	                }
	                Log.v(CNT_LOG, "Saiu do While");
	                	                
	                handler.post(new Runnable() {
						@Override
						public void run() {
							sincronizarPedidos();
							progressDialog.dismiss();
						}
					});
	            }	            
	        });
	        
	        // Startando a thread
  	        thread.start();
		}
		else {
			Log.v(CNT_LOG, "Nenhum Cliente a Enviar");
			progressDialog.dismiss();
			
			sincronizarPedidos();
		}
	}


//	public void sendData(ArrayList<NameValuePair> params){
//		Log.v(CNT_LOG,"sendData()");
//		HttpGetTask b = new HttpGetTask(null);
//		b.setParametros(params);
//		b.execute(serverHost);	
//	}
	
	public void pushPedidos(){
		Log.v(CNT_LOG,"pushPedidos()");

		mProgressStatus = 0;
		aEnviar = 0;
		
		progressDialog.setProgress(0);
		progressDialog.setMessage("Enviando Pedidos");
		progressDialog.show();

		// recuperar os pedidos a serem enviados
		final PedidoHelper pedidoHelper = new PedidoHelper(this.context);
		final PedidoProdutosHelper pedidoProdutosHelper = new PedidoProdutosHelper(this.context);
		final List<Pedido> pedidos = pedidoHelper.getPedidosAEnviar();
		final ClienteHelper clienteHelper = new ClienteHelper(this.context);
		
		if (pedidos != null){
			// Setar o Total a Enviar
			aEnviar  = pedidos.size();
			Log.v(CNT_LOG, "A ENVIAR ["+aEnviar+"]");

			progressDialog.setMax(aEnviar);
			
	        Thread thread = new Thread(new Runnable() {
	            public void run() {
	                while (mProgressStatus < aEnviar) {

	            		// recupera o Cliente
	                	Pedido pedido = pedidos.get(mProgressStatus);

	    				Log.v(CNT_LOG, "ENVIANDO PEDIDO ["+pedido.getId()+"]");
	    				
	    				// Para cada Pedido recuperar os produtos.
	    				List<PedidoProduto> produtos = pedidoProdutosHelper.getProdutos(pedido);
	    				// Setando os Produtos no pedido 
	    				pedido.setProdutos(produtos);

	    				
	    				// Recuperar o Id Cliente no Servidor		
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
	    				
	    				String result;
						// ENVIAR DADOS
	    				try {
	    					
	    					Log.v(CNT_LOG, "1 - Recuperando a Conexao HttpClient");
	    			        HttpClient httpClient = new DefaultHttpClient();
	    			                  
	    			        // Testar se o Host tem porta, se tiver divide o host no : o primeiro indice e o host o outro e a porta
	    			        if (serverHost.contains(":")) {
	    				        String s[] = serverHost.toString().split(":");
	    				        host = s[0];
	    				        port = Integer.parseInt(s[1]);
	    			        }
	    			        else {
	    			        	host = serverHost.toString();
	    			        }
	    			        URI uri = URIUtils.createURI(scheme, host, port, path, null, null);
	    			        Log.w(CNT_LOG, "URI    [ "+uri.toString()+" ]");
	    		        
	    			        HttpPost httpPost = new HttpPost(uri);       
	    			
	    			        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
	    			        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

	    			        Log.v(CNT_LOG, "3 - Executando a Requisicao Http");
	    			        HttpResponse response = httpClient.execute(httpPost);

	    			        Log.v(CNT_LOG, "4 - Lendo a Resposta");
	    			        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    			        
	    			        Log.v(CNT_LOG, "5 - Criando a String de Resposta");
	    					StringBuilder stringBuilder = new StringBuilder();

	    					String line = null;
	    					String LS = System.getProperty("line.separator");

	    					while ((line = reader.readLine()) != null){
	    						stringBuilder.append(line + LS);
	    					}

	    					reader.close();
	    					String strResult = stringBuilder.toString();
	    					Log.v(CNT_LOG, "StringResult: "+strResult);

	    			        // Testando a Resposta
	    					if (strResult != null && !strResult.isEmpty()) {
	    						result = strResult.toString();
	    						
	    						JSONObject json = null;
	    						try {
	    							json = new JSONObject(result);		
	    							// Saber se a Resposta do Json Foi de Sucesso
	    							Boolean success =  (Boolean) json.get("success");
	    							
	    							if (success){			
	    								Log.v(CNT_LOG,"RETORNOU SUCCESS");
	    								
	    								String id = json.getString("id");
	    								int id_servidor = json.getInt("id_servidor");
	    								String dt_envio = json.getString("dt_envio");
	    								int status = json.getInt("status");
	    								
	    								// Apos enviar o pedido gravar o id do pedido no servidor a data de envio e alterar o status.
	    								pedido.setId_servidor(id_servidor);
	    								pedido.setDt_envio(dt_envio);
	    								pedido.setStatus(status);
	    								
	    								if (pedidoHelper.Alterar(pedido) > 0){
	    									Log.v(CNT_LOG, "Pedido alterado enviado com sucesso");
	    									pedidosEnviados++;
	    								}
	    								else {
	    									Log.v(CNT_LOG, "Falhou alterar o pedido");
	    								}
	    								    								
    								}
    							} 
    							catch (JSONException e) {
    								e.printStackTrace();
    							}

	    			        }
	    			        else {
	    						Log.v(CNT_LOG, "5 - Resposta Vazia");
	    			        }				
	    				}
	    				catch (ConnectTimeoutException e) {
	    					Log.v(CNT_LOG, "ConnectTimeout");
	    					e.printStackTrace();		
	    					result = "{success:false,msg=\"Tempo Expirado.\"}";
	    				}
	    				catch (Exception e){
	    					Log.v(CNT_LOG, "Erro de Conexao ["+e.toString()+"]");
	    					e.printStackTrace();
	    					result = "{success:false,msg=\"Falha ao conectar com Servidor.\"}"; 
	    		           // handle "error connecting to the server"
	    				}
	    				
            			progressDialog.setProgress(mProgressStatus);
            			mProgressStatus = mProgressStatus +1;
	 	            		
	                    // Update the progress bar
	                    mHandler.post(new Runnable() {
	                        public void run() {
	                        	progressDialog.setProgress(mProgressStatus);
	                        }
	                    });
	                }
	                Log.v(CNT_LOG, "Saiu do While");
	                	                
	                handler.post(new Runnable() {
						@Override
						public void run() {
							progressDialog.dismiss();
							showSuccessDialog();
						}
					});
	            }	            
	        });
	        
	        // Startando a thread
  	        thread.start();						
		}
		else {
			Log.v(CNT_LOG, "Nenhum Pedido a a Enviar");
			progressDialog.dismiss();
			
			showSuccessDialog();
		}
		
	}
	
	//------------------------------ < SELEÇÃO DO TIPO DE CONEXAO > ------------------------------	
		public Dialog selectLocalRemoteDialog(){
			
			return new AlertDialog.Builder(this.context)
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
			sincronizarClientes();
		}
	}
	public void setRemota(){
		if (setServerHost(sharedPrefs.getString("settingRemoteHost", "NULL")))
		{
			sincronizarClientes();
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
	
		
	public Dialog noConectionDialog() {
		
		return new AlertDialog.Builder(this.context)
        .setTitle(R.string.semConexaoTitulo)
        .setMessage(R.string.semConexaoDescricao)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {                   
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            	dialog.dismiss();
            }
        })
        .create();		
	}
		
		public Dialog noHostDialog(){
			return new AlertDialog.Builder(this.context)
	        .setTitle("Host Inválido")
	        .setMessage("Host Inválido Verifique as configurações.")
	        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	dialog.dismiss();
	            	//finish();
	            }
	        })
	        .create();
		}

		@Override
		public void onTaskComplete(String result) {
			// TODO Auto-generated method stub
			
		}

	
}
