package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import br.com.vilaverde.cronos.AsyncTaskCompleteListener;

public class HttpGetTask extends AsyncTask<String, Void, String>
{

    private static final String CNT_LOG = "HttpGetTask";

	private AsyncTaskCompleteListener<String> callback;
    
	private  ArrayList<NameValuePair> parametros;
	
	//user supplied variables
    private String host = "maguideposito.servehttp.com";
    private int port = 80;
    private String path = "cronos/main.php";
    private String scheme = "http";
	
	
	public ArrayList<NameValuePair> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<NameValuePair> parametros) {
		this.parametros = parametros;
	}

    public HttpGetTask(AsyncTaskCompleteListener<String> callback)
    {
       this.callback = callback;
    }

    protected String doInBackground(String... strings)
    {
		Log.v(CNT_LOG, "doInBackground = "+strings[0].toString());
    	
		// Setando Variavel de Resposta
    	String result = "false";		
    	
		// Para Requests Post recuperar os parametros 
		parametros = this.getParametros();
					
		try {
			
			Log.v(CNT_LOG, "1 - Recuperando a Conexao HttpClient");
	        HttpClient httpClient = new DefaultHttpClient();
	                  
	        // Testar se o Host tem porta, se tiver divide o host no : o primeiro indice e o host o outro e a porta
	        if (strings[0].toString().contains(":")) {
		        String s[] = strings[0].toString().split(":");
		        host = s[0];
		        port = Integer.parseInt(s[1]);
	        }
	        else {
	        	host = strings[0].toString();
	        }
	        URI uri = URIUtils.createURI(scheme, host, port, path, null, null);
	        Log.w(CNT_LOG, "URI    [ "+uri.toString()+" ]");

        
	        HttpPost httpPost = new HttpPost(uri);       
	        //HttpPost httpPost = new HttpPost(strings[0].toString());
	
	        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
	        httpPost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

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

		return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
       callback.onTaskComplete(result);
    }
}
