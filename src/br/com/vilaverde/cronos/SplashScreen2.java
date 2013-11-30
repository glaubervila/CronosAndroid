package br.com.vilaverde.cronos;

import java.util.List;

import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.model.Produto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashScreen2 extends Activity {

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
			
		produtos = helper.getProdutosSemImagem();
		
		
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
	 
	            		if(helper.verificaImagem(produto)){
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
			startNewActivity();
		}

	}
	
	public void startNewActivity() {
		Log.v(CNT_LOG,"startNewActivity()");
		
		Intent it =  new Intent(this, Cronos.class);
		startActivity(it);

		finish();

	}

}
