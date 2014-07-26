package br.com.vilaverde.cronos.view.pedidos;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.view.produtos.DepartamentosFragment;


public class PedidoView extends Activity  {
		
		private final static String CNT_LOG = "PedidoView";
			
		public Pedido pedido = null;
		public PedidoHelper helper = null;

		private PedidoProdutosHelper pedidoProdutoHelper;	

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);	
			setContentView(R.layout.pedidoview);
			
	        	
	        Intent intent = getIntent();
	        int position = intent.getExtras().getInt("position");
	        
	        pedido = (Pedido) intent.getExtras().getSerializable("pedido");
	        
			pedidoProdutoHelper = new PedidoProdutosHelper(getApplicationContext());
			
			// Buscar os Produtos desse pedido
			List<PedidoProduto> produtos = pedidoProdutoHelper.getProdutos(pedido);		

	        Log.v(CNT_LOG, "Pedido ["+pedido.getCliente()+"] Posicao ["+position+"]");
			
	        if (findViewById(R.id.pedidodoprodutos_container) != null) {

	            if (savedInstanceState != null) {
	                return;
	            }

	            // Create an instance of ExampleFragment
	            PedidoProdutosList produtosFragment = new PedidoProdutosList();

	            // In case this activity was started with special instructions from an Intent,
	            // pass the Intent's extras to the fragment as arguments
	            produtosFragment.setArguments(getIntent().getExtras());

	            // Add the fragment to the 'fragment_container' FrameLayout
	            getFragmentManager().beginTransaction()
	                    .replace(R.id.pedidodoprodutos_container, produtosFragment).commit();
	        }

	        
	        
//	        PedidoProdutosList fragment = (PedidoProdutosList)
//                    getFragmentManager().findFragmentById(R.id.fragment_pedido_produtos_list);
//
//	        Log.v(CNT_LOG, "Debug: 1");
//
//			if (fragment==null || ! fragment.isInLayout()) {
//	        	// start new Activity
//		        Log.v(CNT_LOG, "start new Activity");
//		        
//        		PedidoProdutosList list = new PedidoProdutosList();
//        		Log.v(CNT_LOG, "Debug: 2");
//                // Make new fragment to show this selection.
//            	list = PedidoProdutosList.newInstance(pedido);
//
//                // Execute a transaction, replacing any existing fragment
//                // with this one inside the frame.
//                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                Log.v(CNT_LOG, "Debug: 3");
//                
//                ft.add(R.id.fragment_pedido_produtos_list, list);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();	        
//                Log.v(CNT_LOG, "Debug: 4");
//	        	
//	        }
//        	else {
////    			fragment.update(...);
////        		Log.v(CNT_LOG, "Update Activity");
//        		
//        		PedidoProdutosList list = new PedidoProdutosList();
//        		list = PedidoProdutosList.newInstance(pedido);
//        		
//                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                
//                ft.remove(fragment);
//                ft.replace(R.id.fragment_pedido_produtos_list, list);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();	        
//        		
//        	} 
	        		
		}
}
