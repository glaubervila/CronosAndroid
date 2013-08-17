//package br.com.vilaverde.cronos;
//
//
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import br.com.vilaverde.cronos.dao.VendedorHelper;
//import br.com.vilaverde.cronos.exception.HttpTaskPostException;
//import br.com.vilaverde.cronos.httpclient.HttpTask;
//import br.com.vilaverde.cronos.httpclient.HttpTaskPost;
//import br.com.vilaverde.cronos.httpclient.teste_ws;
//import br.com.vilaverde.cronos.model.Vendedor;
//import br.com.vilaverde.cronos.view.ClientesList;
//import br.com.vilaverde.cronos.view.DepartamentosFragment;
//import android.R.integer;
//import android.net.ConnectivityManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.provider.MediaStore.Images.Media;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.AssetManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//
//public class MainActivity extends Activity {
//
//	private static String CNT_LOG = "MainActivity";
//	
//    public Handler handler = new Handler();  
//	 
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		//setContentView(R.layout.activity_main);
//		//setContentView(R.layout.produtos_fragment_container);
//		
////        // Check whether the activity is using the layout version with
////        // the fragment_container FrameLayout. If so, we must add the first fragment
////        if (findViewById(R.id.produtos_fragment_container) != null) {
////
////            // However, if we're being restored from a previous state,
////            // then we don't need to do anything and should return or else
////            // we could end up with overlapping fragments.
////            if (savedInstanceState != null) {
////                return;
////            }
////
////            // Create an instance of ExampleFragment
////            DepartamentosFragment firstFragment = new DepartamentosFragment();
////
////            // In case this activity was started with special instructions from an Intent,
////            // pass the Intent's extras to the fragment as arguments
////            firstFragment.setArguments(getIntent().getExtras());
////
////            // Add the fragment to the 'fragment_container' FrameLayout
////            getFragmentManager().beginTransaction()
////                    .add(R.id.produtos_fragment_container, firstFragment).commit();
////        }
//		
//		
//		// Criando Um botao para Testes
////		Button teste = (Button) findViewById(R.id.bt_main_teste);
////		teste.setOnClickListener(onTeste);
//		
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//	    switch (item.getItemId()) {
//	            
//	        case R.id.menu_cliente_lista:
//	            // app icon in action bar clicked;          
//	    		startActivity(new Intent(this,ClientesList.class));
//	            return true;	            
//
//	        case R.id.menu_atualizar:          
//	    		//startActivity(new Intent(this,Atualizar.class));
//	        	new Atualizar(this);
//	            return true;	            
//	            
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
//	}
//	
//
//	private OnClickListener onTeste = new OnClickListener() {
//
//		public void onClick(View arg0) {
//
//			Log.v(CNT_LOG, "Clicou no Botao Teste");
//
//			teste();
//		}
//
//	};
//	
//	
//	public void teste(){
//
//
//	}
//	
//
//}
//
//
//      
//
//
