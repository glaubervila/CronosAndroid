package br.com.vilaverde.cronos;

import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.dao.VendedorHelper;
import br.com.vilaverde.cronos.httpclient.ConexaoHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

//public class Atualizar extends Activity{
public class Atualizar {

	
	private static String CNT_LOG = "Actualizar";
	
    public Handler handler = new Handler();  

	private Context context = null;
	//private static final int VERSAO_SCHEMA = 1;

	
	
	public Atualizar(Context context) {
		super();
		this.context = context;
		
		
		Sincronizar();
	}
	

	public void Sincronizar(){
		Log.v(CNT_LOG, "Inicio da Atualizcao");	
		
		// Classe responsavel por sincronizar o tablet com o servidor
		
		// 1 - Testar se Tem Conexao com internet
		Boolean conexao = ConexaoHttpClient.Conectado(this.context);
				
		if (conexao){
		
			// 2 - Recuperar os Vendedores
			Log.v(CNT_LOG, "1 - Recuperar os Vendedores");
			
			//getVendedores();
			
			// 3 - Recuperar os Produtos
			Log.v(CNT_LOG, "2 - Recuperar os Produtos");
			
			getProdutos();
			

			
			
		}
		else{
			Messages.showErrorAlert(this.context, "Verifique sua Conexão com a Internet!");
		}
		

	}
	
	public void getProdutos(){
		Log.v(CNT_LOG, "getProdutos()");
		
	
		ProdutosHelper produtosHelper = new ProdutosHelper(this.context);
		
		produtosHelper.getWSProdutos();
				 
	}
	
	public void getVendedores(){
		Log.v(CNT_LOG, "getProdutos()");
		
	
		VendedorHelper vendedorHelper = new VendedorHelper(this.context);
		
		vendedorHelper.sincronizarVendedores();
				 
	}


	
}
