package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import br.com.vilaverde.cronos.AsyncTaskCompleteListener;
import android.os.AsyncTask;
import android.util.Log;

public class HttpGetTask extends AsyncTask<String, Void, String>
{

    private static final String CNT_LOG = "HttpGetTask";

	private AsyncTaskCompleteListener<String> callback;
    
	private  ArrayList<NameValuePair> parametros;
	
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
	          
	        
        	//HttpPost httpPost = new HttpPost("http://192.168.0.200/cronos/main.php?classe=ClientAndroid&action=getVendedores");
	        HttpPost httpPost = new HttpPost(strings[0].toString());
	
	        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
	        httpPost.setEntity(new UrlEncodedFormEntity(parametros));
	          
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
