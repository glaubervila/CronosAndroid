package br.com.vilaverde.cronos;

import br.com.vilaverde.cronos.settings.SettingsActivity;
import br.com.vilaverde.cronos.view.clientes.ClientesList;
import br.com.vilaverde.cronos.view.pedidos.PedidoAberto;
import br.com.vilaverde.cronos.view.pedidos.PedidosList;
import br.com.vilaverde.cronos.view.produtos.ProdutosList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An activity representing a list of Produtos. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ProdutoDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ProdutoListFragment} and the item details (if present) is a
 * {@link ProdutoDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ProdutoListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ProdutoListActivity extends FragmentActivity implements
		ProdutoListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produto_list);

		if (findViewById(R.id.produto_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ProdutoListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.produto_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ProdutoListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ProdutoDetailFragment.ARG_ITEM_ID, id);
			ProdutoDetailFragment fragment = new ProdutoDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.produto_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ProdutoDetailActivity.class);
			detailIntent.putExtra(ProdutoDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
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
	        .setMessage("Você realmente deseja fechar o cronos?")
	        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            finish();    
		        }
	        })
		    .setNegativeButton("Não", null)
		    .show();
    }
}
