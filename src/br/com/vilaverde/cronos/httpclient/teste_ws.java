package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


public class teste_ws {

	private static final String CNT_LOG = "teste_ws";
	
	public static final int HTTP_TIMEOUT = 30 * 1000;
	private static HttpClient httpClient;
	
	private static  HttpClient getHttpClient(){
	
	
		
		if (httpClient == null){
			Log.v(CNT_LOG, "Criando o httpClient");
			
			httpClient = new DefaultHttpClient();
			final HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(httpParams, HTTP_TIMEOUT);
		}
		return httpClient;
	}
	
	public final String get(String url) {

		//String[] result  = new String[2];
		//HttpGet httpGet = new HttpGet(url);
		//HttpResponse response;
		Log.v(CNT_LOG, "executaHttpGet");		
		BufferedReader bufferedReader = null;
		String resultado = "";
		try {
//			HttpClient client = getHttpClient();
//			Log.v(CNT_LOG, "Passo 1");
//			
//			HttpGet httpGet = new HttpGet(url);
//			Log.v(CNT_LOG, "Passo 2");
//			
//			HttpResponse httpResponse = client.execute(httpGet);
//			Log.v(CNT_LOG, "Passo 3");
//			
//			 HttpEntity entity = httpResponse.getEntity();  
//			  
//	            if (entity != null) {  
//	            	Log.v(CNT_LOG, "Passo 4");
//	                InputStream instream = entity.getContent();
//	                Log.v(CNT_LOG, "Passo 3");
//	                result[1] = instream.toString();
//	                Log.v(CNT_LOG, "Passo 4");
//	                instream.close();  
//	            }
//	            else {
//	            	Log.v(CNT_LOG, "FUDEU");
//	            }
	            
			Log.v(CNT_LOG, "executaHttpGet - Entrou no Try");
			HttpClient client = getHttpClient();
			Log.v(CNT_LOG, "Passo 1");
			
			HttpGet httpGet = new HttpGet(url);
			Log.v(CNT_LOG, "Passo 2");
			
			HttpResponse httpResponse = client.execute(httpGet);
			Log.v(CNT_LOG, "Passo 3");
			
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));	
			Log.v(CNT_LOG, "Passo 4");
			
			StringBuffer stringBuffer = new StringBuffer("");
			Log.v(CNT_LOG, "Passo 5");
			
			String line = "";
			Log.v(CNT_LOG, "Passo 6");
			String LS = System.getProperty("line.separator");
			Log.v(CNT_LOG, "Passo 7");
			
			while ((line = bufferedReader.readLine()) != null){
				Log.v(CNT_LOG, "Passo 8");
				stringBuffer.append(line + LS);
			}
			Log.v(CNT_LOG, "Passo 9");
			bufferedReader.close();
			Log.v(CNT_LOG, "Passo 10");
			resultado = stringBuffer.toString();
			Log.v(CNT_LOG, "Passo 11");
			return resultado;

 		}
		catch (ClientProtocolException e) {
            Log.e("CNT_LOG", "ClientProtocolException", e);
        	return null;
		} catch (IOException e) {
            Log.e("CNT_LOG", "IOException"+ e.getMessage());
        	return null;
		}
		catch (Exception e) {
			Log.v(CNT_LOG, "executaHttpGet - Erro = "+e.getMessage());
			return null;
		}
		finally {

		}
		
	}

	
	public JSONObject getJson(String url){
		
		JSONObject j;
		String strResult = get(url);
		try {
			j = new JSONObject(strResult);
			return j;
		}
		catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
			return null;
		}
				
	}
	
	
	
	public static boolean Conectado(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
            String LogSync = null;
            String LogToUserTitle = null;
            Object handler;
			if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                    LogSync += "\nConectado a Internet 3G ";
                    LogToUserTitle += "Conectado a Internet 3G ";
                    //handler.sendEmptyMessage(0);
                    Log.d(CNT_LOG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                    return true;
            } else if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                    LogSync += "\nConectado a Internet WIFI ";
                    LogToUserTitle += "Conectado a Internet WIFI ";
                    //handler.sendEmptyMessage(0);
                    Log.d(CNT_LOG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                    return true;
            } else {
                    LogSync += "\nNão possui conexão com a internet ";
                    LogToUserTitle += "Não possui conexão com a internet ";
                    //handler.sendEmptyMessage(0);
                    Log.e(CNT_LOG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                    Log.e(CNT_LOG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                    return false;
            }
        } catch (Exception e) {
                Log.e(CNT_LOG,e.getMessage());
                return false;
        }
    }

	
}
