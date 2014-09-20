package br.com.vilaverde.cronos.view.produtos;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.model.Produto;

public class ProdutosDetalhe extends Activity {
	
	private static String CNT_LOG = "ProdutosDetalhe";
	private static FragmentManager fragmentManager = null;
	
	private ViewFlipper viewFlipper;
//	ArrayList<Produto> lstProdutos;
	List<Produto> lstProdutos = null;
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
		
    Animation animFlipInForeward;
    Animation animFlipOutForeward;
    Animation animFlipInBackward;
    Animation animFlipOutBackward;
    
	private int currentView = 0;
	private int currentIndex = 0;
	private int maxIndex = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos_detalhe);
               
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("position");
        
//        lstProdutos = (ArrayList<Produto>) getIntent().getSerializableExtra("lstProdutos");
        lstProdutos = (List<Produto>) getIntent().getSerializableExtra("lstProdutos");
        
        Produto produto = lstProdutos.get(position);
       
        
        Log.v(CNT_LOG, "Codigo = "+produto.getCodigo()+" Posisiton = "+position+ " Image_Path = "+produto.getImage_path());
        
        setProduto(produto);
        
        // Recuperar o ImageView 0
		ImageView iv = (ImageView) findViewById(R.id.produto_full_zero);
        
		// Setar a posicao atual
		currentIndex = position;
		
		// Saber a quantidade
		maxIndex = lstProdutos.size() - 1;
		
		// Recupera o ViewFlipper
		viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper1);

		// EFEITOS
//		slideLeftIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
//		slideLeftOut = AnimationUtils
//				.loadAnimation(getApplicationContext(), R.anim.slide_left_out);
//		slideRightIn = AnimationUtils
//				.loadAnimation(getApplicationContext(), R.anim.slide_right_in);
//		slideRightOut = AnimationUtils.loadAnimation(getApplicationContext(),
//				R.anim.slide_right_out);

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
				android.R.anim.fade_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

	    animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
	    animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
	    animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
	    animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);

		// Seta o Produto Atual
		Produto current_produto = lstProdutos.get(position); 
		
		// Seta a imagem pelo path no ImageView
		iv.setImageDrawable(Drawable.createFromPath(current_produto.getImage_path()));
		
		// Garbage Colector
		System.gc();
		
		// Setando o Gesture Detector
		gestureDetector = new GestureDetector(getApplicationContext(), new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					Log.v(CNT_LOG,"gestureDetector.onTouchEvent(event)");
					return true;
				}
				return false;
			}
		};

	}

    private void SwipeRight(){
    	Log.v(CNT_LOG,"SwipeRight() - VOLTANDO");
    	
		// ESQUERDA PARA DIREITA (VOLTANDO)    	
    	viewFlipper.setInAnimation(animFlipInBackward);
    	viewFlipper.setOutAnimation(animFlipOutBackward);
   
		if (currentIndex == 0) {
			// Faz Ir para o ULTIMO ELEMENTO
			currentIndex = maxIndex;
			// TODO COLOCAR ANIMACAO DE FINAL 
		} else {
			currentIndex = currentIndex - 1;
		}
		
		// Seta o Produto Atual
		Produto current_produto = lstProdutos.get(currentIndex);
		
		ImageView iv;
		// Seta a imagem pelo path no ImageView
		if (currentView == 0) {
			currentView = 2;
			iv = (ImageView) findViewById(R.id.produto_full_two);
		} else if (currentView == 2) {
			currentView = 1;
			iv = (ImageView) findViewById(R.id.produto_full_one);
		} else {
			currentView = 0;
			iv = (ImageView) findViewById(R.id.produto_full_zero);
		}
		
		iv.setImageDrawable(Drawable.createFromPath(current_produto.getImage_path()));
		System.gc();
		
    	viewFlipper.showPrevious();
    	setProduto(current_produto);
    }
    
    private void SwipeLeft(){
    	Log.v(CNT_LOG,"SwipeLeft() - AVANCANDO");
		//DIREITA PARA ESQUERDA (AVANCANDO)    	
    	viewFlipper.setInAnimation(animFlipInForeward);
    	viewFlipper.setOutAnimation(animFlipOutForeward);
    	
		if (currentIndex == maxIndex) {
			currentIndex = 0;
			// TODO COLOCAR ANIMACAO DE FINAL 
		} else {
			currentIndex = currentIndex + 1;
		}
		
		// Seta o Produto Atual
		Produto current_produto = lstProdutos.get(currentIndex);
		
		ImageView iv;
	
		if (currentView == 0) {
			currentView = 1;
			iv = (ImageView) findViewById(R.id.produto_full_one);
			
		} else if (currentView == 1) {
			currentView = 2;
			iv = (ImageView) findViewById(R.id.produto_full_two);
			
		} else {
			currentView = 0;
			iv = (ImageView) findViewById(R.id.produto_full_zero);
			
		}

		iv.setImageDrawable(Drawable.createFromPath(current_produto.getImage_path()));
		System.gc();
		
		viewFlipper.showNext();
    	setProduto(current_produto);
    }

	
	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
					Log.v(CNT_LOG,"> SWIPE_MAX_OFF_PATH");
					return false;
				}

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					
					//DIREITA PARA ESQUERDA (AVANCANDO)
					SwipeLeft();			
				} 
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					//ESQUERDA PARA DIREITA (VOLTANDO)
					SwipeRight();
				}
			} catch (Exception e) {
				// nothing
				e.printStackTrace();
			}
			return false;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}
		
	public void setProduto(Produto produto){
		Log.v(CNT_LOG,"setProduto("+produto.getId()+")");
		
		TextView txtViewDescricao = (TextView) findViewById(R.id.produto_detalhe_descricao);
		TextView txtViewPreco = (TextView) findViewById(R.id.produto_detalhe_preco);

		String title = produto.getCodigo()+" - "+produto.getDescricao_curta();
		this.setTitle(title);
        //txtViewDescricao.setText(produto.getDescricao_curta());
		txtViewDescricao.setText(produto.getDescricao());
		
		String strPreco = NumberFormat.getCurrencyInstance().format(produto.getPreco());
        txtViewPreco.setText(strPreco);
		
		// Criar um Fragment Manager para recuperar os fragments
		fragmentManager = getFragmentManager();
		
		// Acessar o fragment e chamar o metodo
		ProdutoDetalhesForm produtoForm = (ProdutoDetalhesForm) fragmentManager.findFragmentById(R.id.fragment_produto_detalhes_form);
		produtoForm.setProduto(produto);
		
	}
	
}
