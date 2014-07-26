package br.com.vilaverde.cronos.view.pedidos;

import java.util.List;


import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;

public class PedidoProdutosList extends ListFragment {

	private String CNT_LOG = "PedidoProdutosList";
	private PedidoHelper pedidoHelper;   
	private PedidoProdutosHelper pedidoProdutoHelper = null;
		
	private Pedido pedido = null;
	private List<PedidoProduto> produtos = null;

	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    // Adiciona o Header A lista
	    View mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.pedidos_produtos_header, null);
	    getListView().addHeaderView(mHeaderView);

	    
        Bundle args = getArguments();
	      
	      if (args != null){
		        pedido = (Pedido) args.getSerializable("pedido");
		        
		        if (pedido != null) {
	
					pedidoProdutoHelper = new PedidoProdutosHelper(getActivity().getApplicationContext());
					
					// Buscar os Produtos desse pedido
					produtos = pedidoProdutoHelper.getProdutos(pedido);		
		        	
			        // Instanciar o Adapter e passar o array
					PedidosProdutosAdapter adapter = new PedidosProdutosAdapter(getActivity(), produtos);
			        
			        // Adicionar o Adapter a ListView
			        setListAdapter(adapter);
		      		        	
		        }
	      }	    
	}
// LIST SEM HEADER	
//  public void onCreate(Bundle savedInstanceState) {
//  super.onCreate(savedInstanceState); 
//  
//  Bundle args = getArguments();
//  
//  if (args != null){
//      pedido = (Pedido) args.getSerializable("pedido");
//      
//      if (pedido != null) {
//
//			pedidoProdutoHelper = new PedidoProdutosHelper(getActivity().getApplicationContext());
//			
//			// Buscar os Produtos desse pedido
//			produtos = pedidoProdutoHelper.getProdutos(pedido);		
//      	
//	        // Instanciar o Adapter e passar o array
//			PedidosProdutosAdapter adapter = new PedidosProdutosAdapter(getActivity(), produtos);
//	        
//	        // Adicionar o Adapter a ListView
//	        setListAdapter(adapter);
//      	
//      	
//      }
//  }
//}
  
	
}
