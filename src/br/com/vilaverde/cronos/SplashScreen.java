package br.com.vilaverde.cronos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity implements Runnable {

	private static boolean SPLASH = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splashscreen);
		
		// Se a Variavel Splash estiver verdadeira exibe a janela de splash
		if (SPLASH){
	        Handler h = new Handler();
	        h.postDelayed(this, 2000); // Aqui está definido o tempo do splash em milesegundos
	        //h.postDelayed(this, 100); // rapidao so pra piscar 			
		}
		else {
			// Se nao estiver ativa inicia a mainActivity
			startActivity(new Intent(this,Cronos.class));// Aqui inicia a Classe apos o splash
			finish();
		}
		

	}

	@Override
	public void run() {

		Intent it =  new Intent(this, SplashScreen2.class);
		
		startActivity(it);
		//startActivity(new Intent(this,MainMenuActivity.class));// Aqui inicia a Classe apos o splash
		finish();

	}
	
}
