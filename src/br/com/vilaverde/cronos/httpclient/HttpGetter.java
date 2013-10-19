package br.com.vilaverde.cronos.httpclient;

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.os.AsyncTask;

public class  HttpGetter extends AsyncTask<String, Void, Void> {

	private static final String TAG = "HttpGetter";
	
	public static final int HTTP_TIMEOUT = 30 * 1000;
	private static HttpClient httpClient;
	
	private Context context;
	
	@Override
	protected Void doInBackground(String... url) {
		
				
		
		return null;
	}

}
