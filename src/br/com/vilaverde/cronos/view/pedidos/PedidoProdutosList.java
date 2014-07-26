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

    /*public static PedidoProdutosList newInstance(Pedido pedido2) {
    	PedidoProdutosList f = new PedidoProdutosList();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("pedido", pedido2);
        f.setArguments(args);

        return f;
    }

	
    public Pedido getPedido() {
    	if (getArguments() != null){
    		return (Pedido) getArguments().get("pedido");
    	}
    	else {
    		return null;
    	}
    }*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        
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
        
    
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//	    // TODO Auto-generated method stub
//		
//	    String[] values = new String[] {"Teste 1", "Teste 2"};
//	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//	    android.R.layout.simple_list_item_1, values);
//
//	    setListAdapter(adapter);

		
	    /*if (getPedido() != null){
	    	pedido = (Pedido) getPedido();
	    	Log.v(CNT_LOG, "OK FUNCIONOU");
	    		    	
			// Instancias Helpers
			pedidoHelper = new PedidoHelper(getActivity().getApplicationContext());
			pedidoProdutoHelper = new PedidoProdutosHelper(getActivity().getApplicationContext());
		
			// Buscar os Produtos desse pedido
			produtos = pedidoProdutoHelper.getProdutos(pedido);		
				
	        // Instanciar o Adapter e passar o array
			PedidosProdutosAdapter adapter = new PedidosProdutosAdapter(getActivity(), produtos);
	        
	        // Adicionar o Adapter a ListView
	        setListAdapter(adapter);
			
			

//		    String[] values = new String[] {};
//    	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//    	    android.R.layout.simple_list_item_1, values);
//
//    	    setListAdapter(adapter);

    	    
    	    
			
	    }
	    else {
	    	Log.v(CNT_LOG, "Criou o Fragment sem ter o parametro Pedido");
		    String[] values = new String[]{"Teste"};

    	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
    		android.R.layout.simple_list_item_1, values);

    	    setListAdapter(adapter);
	    }*/
//	    return super.onCreateView(inflater, container, savedInstanceState);		  
//	 }
}
