package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.vilaverde.cronos.Messages;
import br.com.vilaverde.cronos.dao.DataHelper;
import br.com.vilaverde.cronos.exception.HttpTaskPostException;


import android.R.integer;
import android.animation.AnimatorSet.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpTaskPost extends AsyncTask<String, integer, String[]> {
	
	private static Context context = null;
	
	private static String  CNT_LOG = "HttpTaskPost"; 
	
	private  ArrayList<NameValuePair> parametros;

	
	public ArrayList<NameValuePair> getParametros() {
		return parametros;
	}

	public void setContext(Context context){
		this.context = context;
	}

	public void setParametros(ArrayList<NameValuePair> parametros) {
		this.parametros = parametros;
	}


	protected void onPreExecute() {
		
		super.onPreExecute();
		Log.v(CNT_LOG, "onPreExecute");		
		
    }


	protected String[] doInBackground(String... urls){
		
		// Setando Variavel de Resposta
		String[] resultado =  new String[2];
		
		// Para Requests Post recuperar os parametros 
		parametros = this.getParametros();
		
		Log.v(CNT_LOG, "doInBackground = "+urls[0].toString());
		
		try {
			
			Log.v(CNT_LOG, "1 - Recuperando a Conexao HttpClient");
	        HttpClient httpClient = new DefaultHttpClient();
	          
	          
	        HttpPost httpPost = new HttpPost(urls[0].toString());
	
	        Log.v(CNT_LOG, "2 - Montando os Parametros do Post");
	        httpPost.setEntity(new UrlEncodedFormEntity(parametros));
	          
	        Log.v(CNT_LOG, "3 - Executando a Requisicao Http");
	        HttpResponse response = httpClient.execute(httpPost);

	        Log.v(CNT_LOG, "4 - Lendo a Resposta");
//          //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        
	        Log.v(CNT_LOG, "5 - Criando a String de Resposta");
			StringBuilder stringBuilder = new StringBuilder();
			//stringBuilder.append(reader.readLine() + "\n");         
			//String line = "0";

			//while ((line = reader.readLine()) != null) {
			//	stringBuilder.append(line + "\n");
			//}
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
				resultado[0] = "success";
				resultado[1] = strResult.toString();
	        }
	        else {
				Log.v(CNT_LOG, "5 - Resposta Vazia");
				
				resultado[0] = "failure";
				resultado[1] = "Não Houve Resposta do Servidor!";
	        }
		}
		catch (ConnectTimeoutException e) {
			e.printStackTrace();
			resultado[0] = "failure";
			resultado[1] = "Tempo Limite, Verifique sua conexão com a internet";			
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
			resultado[0] = "failure";
			resultado[1] = e.getMessage();
        }
		catch (HttpHostConnectException e) {
			e.printStackTrace();
			resultado[0] = "failure";
			resultado[1] = e.getMessage();
        }
		catch (IOException e) {
			e.printStackTrace();
			resultado[0] = "failure";
			resultado[1] = e.getMessage();
        }
		catch (Exception e){  
			e.printStackTrace();  
			resultado[0] = "failure";
			resultado[1] = e.getMessage();
		}


		return resultado;
		

	}
	
//	protected JSONObject doInBackground( String... url){
//
//		Log.v(CNT_LOG, "doInBackground("+url[0].toString());
//
//		parametros = this.getParametros();
//		
//		
//        try {  
//
//           Log.v(CNT_LOG, "Recuperando a Conexao HttoClient");
//           HttpClient httpClient = new DefaultHttpClient();
//           
//           
//           HttpPost httpPost = new HttpPost(url[0].toString());
//
//           Log.v(CNT_LOG, "Passo 2");
//           httpPost.setEntity(new UrlEncodedFormEntity(parametros));
//           
//           Log.v(CNT_LOG, "Passo 3");
//           HttpResponse response = httpClient.execute(httpPost);
//
//           Log.v(CNT_LOG, "Passo 4");
//           //BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
//           BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//           
//           Log.v(CNT_LOG, "Passo 5");
//           StringBuilder stringBuilder = new StringBuilder();
//           stringBuilder.append(reader.readLine() + "\n");         
//           String line = "0";
//           while ((line = reader.readLine()) != null) {
//               stringBuilder.append(line + "\n");
//           }
//           reader.close();
//           String strResult = stringBuilder.toString();
//           
//           Log.v(CNT_LOG, "Passo 6");
//           Log.v(CNT_LOG, "StringResult: "+strResult);
//           
//   			JSONObject j;
//  		
//	   		try {
//	   			j = new JSONObject(strResult);
//	   			return j;
//	   		}
//	   		catch(JSONException e){
//	   			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
//	   			return null;
//	   		}           
//		}
//        catch (Exception e){  
//            e.printStackTrace();  
//           //retornaMensagemErro(e);
//        }
//		return null;  
// 		
//	}
//
	

}


