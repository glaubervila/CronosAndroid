package br.com.vilaverde.cronos;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import br.com.vilaverde.cronos.dao.DataHelper;
import br.com.vilaverde.cronos.settings.SettingsActivity;
import br.com.vilaverde.cronos.view.clientes.ClientesList;
import br.com.vilaverde.cronos.view.pedidos.PedidoAberto;
import br.com.vilaverde.cronos.view.pedidos.PedidosList;
import br.com.vilaverde.cronos.view.produtos.DepartamentosFragment;
import br.com.vilaverde.cronos.view.produtos.ProdutosFragment;
import br.com.vilaverde.cronos.view.produtos.ProdutosList;

public class Cronos extends FragmentActivity 
        implements DepartamentosFragment.OnHeadlineSelectedListener{

    
	private String CNT_LOG = "MainActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cronos);
       
       
        // Chamando o data Helper para forcar a criacao das tabelas
        DataHelper helper = new DataHelper(this);
        
        
        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            DepartamentosFragment firstFragment = new DepartamentosFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}
		
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	            
	        case R.id.menu_cliente_lista:
	            // app icon in action bar clicked;          
	    		startActivity(new Intent(this,ClientesList.class));
	            return true;	            

	        case R.id.menu_pedidos:          
	    		startActivity(new Intent(this,PedidosList.class));
	            return true;	            
	            
	        case R.id.menu_atualizar:          
	    		startActivity(new Intent(this,Atualizar.class));
	            return true;	            

	        case R.id.menu_novo_pedido:          
	    		startActivity(new Intent(this,PedidoAberto.class));
	            return true;	            

	        case R.id.menu_configuracao:          
	    		startActivity(new Intent(this,SettingsActivity.class));
	            return true;	            

	        case R.id.menu_produtos:          
	    		startActivity(new Intent(this,ProdutosList.class));
	            return true;	            

	        case R.id.menu_enviar_pedido:          
	    		
	            return enviar();	            

	        case R.id.sair:          
	    		onBackPressed();
	            return true;	            
	            
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ProdutosFragment articleFrag = (ProdutosFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ProdutosFragment newFragment = new ProdutosFragment();
            Bundle args = new Bundle();
            args.putInt(ProdutosFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
    
    public boolean enviar(){
    	
    	Enviar e = new Enviar(this);
    	
    	e.enviar();
    	
    	return true;
    }
    
    public void onBackPressed() {
    	// Confirmar antes de sair
    	new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Sair?")
	        .setMessage("Voc� realmente deseja fechar o cronos?")
	        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            finish();    
		        }
	        })
		    .setNegativeButton("N�o", null)
		    .show();
    }
    
}

      


