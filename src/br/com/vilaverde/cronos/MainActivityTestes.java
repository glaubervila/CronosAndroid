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
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import br.com.vilaverde.cronos.httpclient.HttpTask;
//import br.com.vilaverde.cronos.httpclient.teste_ws;
//import br.com.vilaverde.cronos.view.ClientesList;
//import android.net.ConnectivityManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.provider.MediaStore.Images.Media;
//import android.app.Activity;
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
//public class MainActivityTestes extends Activity {
//
//	private static String CNT_LOG = "MainActivity";
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		
//		// Criando Um botao para Testes
//		Button teste = (Button) findViewById(R.id.bt_main_teste);
//		teste.setOnClickListener(onTeste2);
//
////		Button teste2 = (Button) findViewById(R.id.bt_main_teste2);
////		teste2.setOnClickListener(onTeste2);
//
//		
////			String imageName = "000000.JPG";
////			String sd_path = Environment.getExternalStorageDirectory().toString() + "/Imagens/";
////			Log.v(CNT_LOG, "Diretorio = "+sd_path);
////			
////			String file_path = sd_path + imageName;
////			Log.v(CNT_LOG, "File Path = "+file_path);
////			
////			File file = new File(sd_path, imageName); // imageName == nome da tua imagem
////			Log.v(CNT_LOG, "Imagem existe = "+file.exists());
////			
////			Log.v(CNT_LOG, "AbsolutePath = "+file.getAbsolutePath());
//			
//			
////			Bitmap bitmap = BitmapFactory.decodeFile("file:///android_asset/Pictures/teste.png");
//			//Bitmap bitmap = getBitmapFromAssets("file:///android_asset/Pictures/teste.png");
//			
////			String path = "file:///android_asset/"+Environment.DIRECTORY_PICTURES ;
////			Log.v(CNT_LOG, "Diretorio = "+path);
////			String filePath = path + "/teste.png";
////			Log.v(CNT_LOG, "File = "+filePath);
////			
////			
////			File file = new File(path, "teste.png");
////			Log.v(CNT_LOG, "Imagem existe = "+file.exists());
////			
////			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
////			Log.v(CNT_LOG, "Bitmap decodeFile");
////			
////			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
////			
////			Log.v(CNT_LOG, "ImageView.setImageBitmap");
////			imageView.setImageBitmap(bitmap);
////		}
////		catch (Exception e) {
////			Log.e(CNT_LOG, "Erro = "+e.getMessage());
////		}
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
//	        case android.R.id.home:
//	            // app icon in action bar clicked go home;
////	            Intent intent = new Intent(this, MainActivity.class);
////	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////	            startActivity(intent);
////	            return true;
//         
//	        case R.id.menu_cliente_lista:
//	            // app icon in action bar clicked;          
//	    		startActivity(new Intent(this,ClientesList.class));
//	            return true;	            
//	            
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
//	}
//	
//	private OnClickListener onTeste2 = new OnClickListener() {
//	
//		public void onClick(View arg0) {
//			
//			Log.v(CNT_LOG, "Clicou no Botao Teste 2");
//
//			
//	        new AsyncTask<Void, Void, Void>() {  
//	            @Override  
//	            protected Void doInBackground(Void... params) {  
//	                try {  
//	                	Log.v(CNT_LOG, "Entrou na Task");
//	        			download_imagem();  
//	                }
//	                catch (Exception e) {  
//	                    e.printStackTrace();  
//	                }  
//	                return null;  
//	            }  
//	        }.execute();  			
//
//		}
//		
//	};
//	
//	private OnClickListener onTeste = new OnClickListener() {
//
//		public void onClick(View arg0) {
//
//			Log.v(CNT_LOG, "Clicou no Botao Teste");
//
//			Log.v(CNT_LOG, "Tentando Carregar a Imagem");
//
//			try {
//				//String path = Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_PICTURES+"/teste.png";
//				
//				String teste = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";
//				File fteste = new File(teste);
//				File[] ateste = fteste.listFiles();
//				
//				try {
//					@SuppressWarnings("unused")
//					Bitmap bmteste = BitmapFactory.decodeFile("/mnt/sdcard/Pictures/000227.JPG");
//		            ImageView myImage2 = (ImageView) findViewById(R.id.imageView1);
//		            Log.v(CNT_LOG, "ImageView");
//		            myImage2.setImageBitmap(bmteste);
//				}
//				catch (Exception e) {
//					
//				}
//
//				
//				
//				
//				String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/teste.png";				
//				Log.v(CNT_LOG, "Path = "+path);
//				File imgFile = new File(path);
//		        
//		        
//		        if(imgFile.exists())
//		        {
////					Log.v(CNT_LOG, "Arquivo Existe");
////		            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
////		            Log.v(CNT_LOG, "Bitmap");
////		            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
////		            Log.v(CNT_LOG, "ImageView");
////		            myImage.setImageBitmap(myBitmap);
//		        }
//		        else {                   
//		        	Log.v(CNT_LOG, "Deu Merda");
//		            //Toast.makeText(v.getContext(),"no IMAGE IS PRESENT'",Toast.LENGTH_SHORT).show();
//		    	}
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			
//			//boolean teste = Conectado(getApplicationContext());
//			//Log.v(CNT_LOG, "CONEXAO ["+teste+"]");
//	        
//			//retornarTodos();
//  //  		String url = "http://192.168.0.171:8080/teste_android.php";
//    		
//  //  		new HttpTask().execute(url);
//    
//			
//			
//			
//		}
//
//	};
//	
////	public static boolean Conectado(Context context) {
////        try {
////            ConnectivityManager cm = (ConnectivityManager)
////            context.getSystemService(Context.CONNECTIVITY_SERVICE);
////            String LogSync = null;
////            String LogToUserTitle = null;
////            Object handler;
////			if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
////                    LogSync += "\nConectado a Internet 3G ";
////                    LogToUserTitle += "Conectado a Internet 3G ";
////                    //handler.sendEmptyMessage(0);
////                    Log.d(CNT_LOG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
////                    return true;
////            } else if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
////                    LogSync += "\nConectado a Internet WIFI ";
////                    LogToUserTitle += "Conectado a Internet WIFI ";
////                    //handler.sendEmptyMessage(0);
////                    Log.d(CNT_LOG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
////                    return true;
////            } else {
////                    LogSync += "\nNão possui conexão com a internet ";
////                    LogToUserTitle += "Não possui conexão com a internet ";
////                    //handler.sendEmptyMessage(0);
////                    Log.e(CNT_LOG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
////                    Log.e(CNT_LOG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
////                    return false;
////            }
////        } catch (Exception e) {
////                Log.e(CNT_LOG,e.getMessage());
////                return false;
////        }
////    }
//	
//
////    private void retornarTodos() {  
////        new AsyncTask<Void, Void, Void>() {  
////            @Override  
////            protected Void doInBackground(Void... params) {  
////                try {  
////            		String url = "http://192.168.0.171:8080/teste_android.php";
////                	Log.v(CNT_LOG, "entrou no doInBackground");
////                    teste_ws ws = new teste_ws();  
////                    //String resposta = ws.get(url);
////                    
////                    //Log.v(CNT_LOG, "Resposta = "+ resposta);
////                    
////                    // TESTANDO JSON
////                    JSONObject jsonObject = ws.getJson(url);
////                    
////        			Boolean teste =  (Boolean) jsonObject.get("success");
////        			
////        			Log.v(CNT_LOG, "Resposta Succes = "+ teste.toString());
////                    //ArrayList<GrupoProduto> listaGrupoProduto = (ArrayList<GrupoProduto>) gProdRest.getListaGrupoProduto();  
////                    //Intent i = new Intent(getApplicationContext(), ListaGrupoProduto.class);  
////                    //i.putExtra("lista", listaGrupoProduto);  
////                    //startActivity(i);  
////                }
////                catch(JSONException e){
////        			Log.e(CNT_LOG, "Error parsing Json "+e.toString());
////        		}
////                catch (Exception e) {  
////                    e.printStackTrace();  
////                    //gerarToast(e.getMessage());  
////                }  
////                return null;  
////            }  
////        }.execute();  
////    }  
//	
//    private void download_imagem(){
//    
//    	Log.v(CNT_LOG, "Fazendo Download da Imagem");
//    	
//    	String sd_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";
//    	
//    	String imageName = "000239.JPG";
//    	
//    	String fileUrl = "http://magui.servehttp.com:6980/imagens_produtos/000239.JPG";
//    	Log.v(CNT_LOG, "Url = "+fileUrl);
//    	
//    	File file = new File(sd_path, imageName); // imageName == nome da tua imagem
//    	
//    	Log.v(CNT_LOG, "Caminho destino = "+file.getAbsolutePath());
//    	
//    	Bitmap bmImg = null;
//    	URL myFileUrl = null;
//        Bitmap image = null;
//        
//    	try {
//    		//http://magui.servehttp.com:6980/imagens_produtos/000239.JPG
//    	    myFileUrl = new URL(fileUrl); // fileUrl == url para a tua imagem
//    	}
//    	catch (MalformedURLException e) {
//    	    // TODO Auto-generated catch block
//    	    e.printStackTrace();
//    	}
//
//    	try {
//    	    HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
//    	    
//    	    Log.v(CNT_LOG, "HttpUrlConnection ");
//    	    
//    	    conn.setDoInput(true);
//    	    conn.connect();
//    	    Log.v(CNT_LOG, "Connectado");
//
//    	    // Bufered
////            BufferedInputStream buf;
////    	    InputStream is = conn.getInputStream();
////            buf = new BufferedInputStream(is);
////    	    bmImg = BitmapFactory.decodeStream(buf);  // se a imagem for descodificada, é garantido que estás a obter uma imagem
//    	    
//    	    InputStream is = conn.getInputStream();
//    	    Log.v(CNT_LOG, "ImputStream ");
//    	    bmImg = BitmapFactory.decodeStream(is);  // se a imagem for descodificada, é garantido que estás a obter uma imagem
//    	    
//   	    
//    	    Log.v(CNT_LOG, "Bitmap Factory ");
//    	    if (bmImg != null) {
//    	    	  	    	
//    	        // Gravar a imagem no SD
//    	    	Log.v(CNT_LOG, "Gravando Imagem ");
//    	        try {
//
//
//    	            OutputStream fOut = null;
//                    //File file = new File(sd_path,imageName);
//                    fOut = new FileOutputStream(file);
//
//            	    Log.v(CNT_LOG, "File = "+file.getAbsolutePath());
//                    
//                    bmImg.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
//                    fOut.flush();
//                    fOut.close();
//
//                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
//
//            	    Log.v(CNT_LOG, "Media Store");
//                    
//            	    Log.v(CNT_LOG, "File Exists = "+file.exists());
//            		//FileOutputStream out = new FileOutputStream(file);
//					//image.compress(Bitmap.CompressFormat.JPEG, 100, out);
//    	            //out.flush();
//    	            //out.close();
//    	        } catch (Exception e) {
//    	            e.printStackTrace();
//    	        }
//    	    }
//    	    else {
//    	        // A imagem não é válida.
//    	    	Log.v(CNT_LOG, "Imagem não é Valida ");
//    	    }
//
//    	}
//    	catch (IOException e) {
//    	    // TODO Auto-generated catch block
//    	    e.printStackTrace();
//    	}
//    	
//    } 
//	
//}
//
//
//      
//
//
