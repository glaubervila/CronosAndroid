package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;



public class ConexaoHttpClient {

	private static String CNT_LOG = "ConexaoHttpClient";
	
	public static final int HTTP_TIMEOUT = 5 * 1000; // 30 *1000 = 30 seconds
	private static HttpClient httpClient;
	
	static  HttpClient getHttpClient(){
	
		if (httpClient == null){
			Log.v(CNT_LOG, "Criando o httpClient");
			

			final HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(httpParams, HTTP_TIMEOUT);

			httpClient = new DefaultHttpClient(httpParams);
//			HttpParams httpParams = new BasicHttpParams();
//			// Set the timeout in milliseconds until a connection is established.
//			// The default value is zero, that means the timeout is not used. 
//			int timeoutConnection = 3000;
//			HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
//			// Set the default socket timeout (SO_TIMEOUT) 
//			// in milliseconds which is the timeout for waiting for data.
//			int timeoutSocket = 5000;
//			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
//			httpClient = new DefaultHttpClient(httpParams);
		}
		return httpClient;
	}
	
	public static String executaHttpPost (String url, ArrayList<NameValuePair> parametrosPost) throws Exception {
		Log.v(CNT_LOG, "executaHttpPost");
		BufferedReader bufferedReader = null;
		
		try {
			Log.v(CNT_LOG, "Entrou no Try");
			HttpClient client = getHttpClient();
			Log.v(CNT_LOG, "Passo 1");
			HttpPost httpPost = new HttpPost(url);
			Log.v(CNT_LOG, "Passo 2");
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametrosPost);
			Log.v(CNT_LOG, "Passo 3");
			httpPost.setEntity(formEntity);
			Log.v(CNT_LOG, "Passo 4");
			HttpResponse httpResponse = client.execute(httpPost);
			Log.v(CNT_LOG, "Passo 5");
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			Log.v(CNT_LOG, "Passo 6");
			StringBuffer stringBuffer = new StringBuffer("");
			Log.v(CNT_LOG, "Passo 7");
			String line = "";
			Log.v(CNT_LOG, "Passo 8");
			String LS = System.getProperty("line.separator");
			Log.v(CNT_LOG, "Passo 9");
			while ((line = bufferedReader.readLine()) != null){
				Log.v(CNT_LOG, "Passo 10");
				stringBuffer.append(line + LS);
			}
			Log.v(CNT_LOG, "Passo 11");
			bufferedReader.close();
			Log.v(CNT_LOG, "Passo 12");
			String resultado = stringBuffer.toString();
			Log.v(CNT_LOG, "Passo 13");
			return resultado;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String executaHttpGet (String url) throws Exception {
		Log.v(CNT_LOG, "executaHttpGet");		
		BufferedReader bufferedReader = null;
		String resultado = "";
		try {
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
         	return resultado;
		} catch (IOException e) {
             Log.e("CNT_LOG", "IOException", e);
             
             
         	return resultado;
		}
		catch (Exception e) {
			Log.v(CNT_LOG, "executaHttpGet - Erro = "+e.getMessage());
			return resultado;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public JSONObject getJson(String url) throws Exception{
		Log.v(CNT_LOG, "getJson = "+url.toString());
		
		JSONObject j;
		String strResult = executaHttpGet(url);
		
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
