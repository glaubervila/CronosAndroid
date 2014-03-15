package br.com.vilaverde.cronos;

import java.io.File;
import java.util.List;

import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.model.Produto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashScreen2 extends Activity implements Runnable {

	private static String CNT_LOG = "SplashScreen2";
	
	private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private int total_produtos = 0;

    private Handler mHandler = new Handler();
    private Handler handler = new Handler();

    private int atualizadas = 0;
    private int nao_atualizadas = 0;

    public List<Produto> produtos;
    ProdutosHelper helper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splashscreen2);

		Log.v(CNT_LOG,"onCreate");
		
		helper = new ProdutosHelper(this);
//        Handler h = new Handler();
//        h.postDelayed(this, 2000); // Aqui está definido o tempo do splash em milesegundos
        //h.postDelayed(this, 100); // Rapidao so pra testar
		

		// Verificar os Produtos que estao ativos e sem Imagem
		Log.v(CNT_LOG,"Verificar Produtos Ativos Sem Imagem");
			
		produtos = helper.getProdutosSemImagem(2);
		
		
		if (produtos.size() > 0){
		
			total_produtos = produtos.size();
			
			//total_produtos = 1;
			
			mProgress = (ProgressBar) findViewById(R.id.progress_bar_image);
			mProgress.setMax(total_produtos);
			
	        // Start lengthy operation in a background thread
	        Thread thread = new Thread(new Runnable() {
	            public void run() {
	                while (mProgressStatus < total_produtos) {                	
	            		// recupera o produto
	            		Produto produto = produtos.get(mProgressStatus);
	 
	            		if(helper.verificaImagem(produto, false, null)){
	            			atualizadas = atualizadas + 1;
	            			mProgress.setProgress(mProgressStatus);
	            			mProgressStatus = mProgressStatus +1;
	            		}
	            		else {
	            			nao_atualizadas = nao_atualizadas +1;
	            			mProgress.setProgress(mProgressStatus);
	            			mProgressStatus = mProgressStatus +1;
	            		}
	
	                    // Update the progress bar
	                    mHandler.post(new Runnable() {
	                        public void run() {
	                            mProgress.setProgress(mProgressStatus);
	                        }
	                    });
	                }
	                Log.v(CNT_LOG, "Saiu do While");
	                handler.post(new Runnable() {
						@Override
						public void run() {
							Log.v(CNT_LOG, "Criar a Intent Activity aki");
							Log.v(CNT_LOG, "TOTAL DE IMAGENS A ATUALIZAR     = "+total_produtos);
							Log.v(CNT_LOG, "TOTAL DE IMAGENS ATUALIZADAS     = "+atualizadas);
							Log.w(CNT_LOG, "TOTAL DE IMAGENS NAO ATUALIZADAS = "+nao_atualizadas);
						
							startNewActivity();			
						}
					});
	            }	            
	        });
	        
	        // Startando a thread
	        thread.start();        
		}
		else {
	        Handler h = new Handler();
	        h.postDelayed(this, 2000); // Aqui está definido o tempo do splash em milesegundos
		
		}

	}
	
	public void startNewActivity() {
		Log.v(CNT_LOG,"startNewActivity()");
		
		
//		File pictures_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//	    String sd_path = pictures_dir +"/Produtos/";
//		 
//		MediaScannerWrapper teste = new MediaScannerWrapper(this, sd_path , "image/jpeg");
//		teste.scan();
		
		Intent it =  new Intent(this, Cronos.class);
		startActivity(it);

		finish();

	}

	public void run() {
		startNewActivity();
	}
	
//	public class MediaScannerWrapper implements  
//	MediaScannerConnection.MediaScannerConnectionClient {
//	    private MediaScannerConnection mConnection;
//	    private String mPath;
//	    private String mMimeType;
//
//	    // filePath - where to scan; 
//	    // mime type of media to scan i.e. "image/jpeg". 
//	    // use "*/*" for any media
//	    public MediaScannerWrapper(Context ctx, String filePath, String mime){
//	        mPath = filePath;
//	        mMimeType = mime;
//	        mConnection = new MediaScannerConnection(ctx, this);
//	        
//	        Log.v("MediaScanner", "Wrapper");
//	    }
//
//	    // do the scanning
//	    public void scan() {
//	        mConnection.connect();
//	        Log.v("MediaScanner", "scan");
//	    }
//
//	    // start the scan when scanner is ready
//	    public void onMediaScannerConnected() {
//	        mConnection.scanFile(mPath, mMimeType);
//	        Log.w("MediaScannerWrapper", "media file scanned: " + mPath);
//	    }
//
//	    public void onScanCompleted(String path, Uri uri) {
//	        // when scan is completes, update media file tags
//	    	Log.w("MediaScannerWrapper", "onScanCompleted" + path);
//	    }
//	}
}
