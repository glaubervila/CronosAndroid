package br.com.vilaverde.cronos.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
