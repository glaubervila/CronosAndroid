package br.com.vilaverde.cronos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen2 extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splashscreen2);
		
        Handler h = new Handler();
        h.postDelayed(this, 2000); // Aqui está definido o tempo do splash em milesegundos
        //h.postDelayed(this, 100); // Rapidao so pra testar

	}

	@Override
	public void run() {

		Intent it =  new Intent(this, Cronos.class);
		
		startActivity(it);
		//startActivity(new Intent(this,MainMenuActivity.class));// Aqui inicia a Classe apos o splash
		finish();

	}
	
}
