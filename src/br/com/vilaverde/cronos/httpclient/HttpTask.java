package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpTask extends AsyncTask<String, integer, String> {

	private static String  CNT_LOG = "HttpTask";
	
	protected void onPreExecute() {
		
		super.onPreExecute();

		Log.v(CNT_LOG, "onPreExecute");
		
    }

	
	@Override
	protected String doInBackground(String... url) {

		Log.v(CNT_LOG, "doInBackground("+url[0].toString());
		
        try {  

            Log.v(CNT_LOG, "Recuperando a Conexao HttoClient");
            ConexaoHttpClient httpClient = new ConexaoHttpClient();  

            
            Log.v(CNT_LOG, "Executando getJson");            
            JSONObject jsonObject = httpClient.getJson(url[0]);
            
			Boolean teste =  (Boolean) jsonObject.get("success");
			
			Log.v(CNT_LOG, "Resposta Succes = "+ teste.toString());
            //ArrayList<GrupoProduto> listaGrupoProduto = (ArrayList<GrupoProduto>) gProdRest.getListaGrupoProduto();  
            //Intent i = new Intent(getApplicationContext(), ListaGrupoProduto.class);  
            //i.putExtra("lista", listaGrupoProduto);  
            //startActivity(i);  
        }
        catch(JSONException e){
			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
		}
        catch (Exception e) {  
            e.printStackTrace();  
            //gerarToast(e.getMessage());  
        }  
        return null;  		
	}
	
}


//public class HttpTask extends AsyncTask<Object, Object, Object> {
//	private static final String CNT_LOG = "HTTP_TASK";
//
//	@Override
//	protected JSONObject doInBackground(Object... params) {
//    	// Performed on Background Thread
//    	Log.v(CNT_LOG,"Passo 1");
//    	
//    	HttpUriRequest request = (HttpUriRequest) params[0];
//    	
//    	HttpClient client = new DefaultHttpClient();
//    	Log.v(CNT_LOG,"Passo 2");
//        try {
//        	Log.v(CNT_LOG,"Passo 3");
//        	// The UI Thread shouldn't be blocked long enough to do the reading in of the stream.
//        	HttpResponse response =  client.execute(request);
//
//        	Log.v(CNT_LOG,"Passo 4");
//        	
//        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//			StringBuilder builder = new StringBuilder();
//			
//			Log.v(CNT_LOG,"Passo 5");
//			for (String line = null; (line = reader.readLine()) != null; ) {
//			    builder.append(line).append("\n");
//			}
//			Log.v(CNT_LOG,"Passo 6");
//			
//			JSONTokener tokener = new JSONTokener(builder.toString());
//			JSONObject json = new JSONObject(tokener);
//			
//			Log.v(CNT_LOG,"Passo 7");
//			return json;
//
//        } catch (Exception e) {
//        	Log.e(CNT_LOG,e.toString());
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//    protected void onPostExecute(JSONObject json) {
//		Log.v(CNT_LOG,"Passo 8");
//    	// Done on UI Thread
//        if(json != null) {    
//    		Log.v(CNT_LOG,"Passo 9");
//        	taskHandler.taskSuccessful(json);
//        } else {
//    		Log.e(CNT_LOG,"Passo 10 - o json veio vazio");
//        	taskHandler.taskFailed();
//        }
//    }
//
//	
//    public static interface HttpTaskHandler {
//        void taskSuccessful(JSONObject json);
//        void taskFailed();
//    }
//
//	HttpTaskHandler taskHandler;
//
//	public void setTaskHandler(HttpTaskHandler taskHandler) {
//    		this.taskHandler = taskHandler;
//	}
//
//
//
//}

//}

//class HttpGetter extends AsyncTask<URI, Void, Void> {
//
//	protected Void doInBackground(URI... urls) {
//        // TODO Auto-generated method stub
//		StringBuilder builder = new StringBuilder();
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(urls[0]);
//
//        
//        try {
//                HttpResponse response = client.execute(httpGet);
//                StatusLine statusLine = response.getStatusLine();
//                int statusCode = statusLine.getStatusCode();
//                if (statusCode == 200) {
//                        HttpEntity entity = response.getEntity();
//                        InputStream content = entity.getContent();
//                        BufferedReader reader = new BufferedReader(
//                                        new InputStreamReader(content));
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                                builder.append(line);
//                        }
//                        Log.v("Getter", "Your data: " + builder.toString()); //response data
//                } else {
//                        Log.e("Getter", "Failed to download file");
//                }
//        } catch (ClientProtocolException e) {
//                e.printStackTrace();
//        } catch (IOException e) {
//                e.printStackTrace();
//        }
//        
//        return null;
//	}
//}
