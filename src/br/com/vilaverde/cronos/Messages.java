package br.com.vilaverde.cronos;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Messages {

	
	
	public static void showErrorAlert(Context context, String title, String message){

		if (title.isEmpty()){
			message = "Atenção.";
		}

		if (message.isEmpty()){
			message = "Desculpe! Mas Houve uma falha.";
		}
		
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(message)
		       .setTitle("Atenção");
	
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();	
		
		dialog.show();
	}

	public static void showErrorAlert(Context context, String message){

		if (message.isEmpty()){
			message = "Desculpe! Mas Houve uma falha.";
		}
		
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(message)
		       .setTitle("Atenção");
	
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();	
		
		dialog.show();
	}

	
	public static void showSuccessToast(Context context, String message){
		
		if (message.isEmpty()){
			message = "Operação Realizada com Sucesso!";
		}
		
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		
	}
	

}
