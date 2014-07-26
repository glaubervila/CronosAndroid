package br.com.vilaverde.cronos.view.pedidos;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.Produto;

public class PedidosList extends Activity implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener {

	private String CNT_LOG = "PedidosList";

	private static final int SELECIONAR = 1;
	
	private PedidoHelper helper;   
	private PedidosListAdapter adapter; 
	private List<Pedido> lstPedidos;
    ListView listView;

    // Pesquisa
    private SearchView mSearchView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedidos_list);
				
		// Crio o listview passando o id da listview no xml
        listView = (ListView) findViewById(R.id.pedidos_list);

    	// Instanciar o Helper 
        helper = new PedidoHelper(this);
		
        // Recuperar o Array de Clientes no DB
        lstPedidos = helper.getPedidos();
//        String title = lstPedidos.size()+" Produtos";
//        setTitle(title);

        
        // Instanciar o Adapter e passar o array
        adapter = new PedidosListAdapter(this, lstPedidos);
        
        // Adicionar o Adapter a ListView
        listView.setAdapter(adapter);
        
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				
				Pedido pedido = lstPedidos.get(position);
									
				Log.v(CNT_LOG, "Pedido ["+pedido.getCliente()+"] Index ["+index+"] Posicao ["+position+"]");
				// Chamar a janela de Detalhes do Pedido	  			        		
				Intent intent = new Intent(getApplicationContext(), PedidoView.class);
				intent.putExtra("position", position);
				intent.putExtra("pedido", pedido);
				
				startActivity(intent);

				return false;
			}		
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedidos_search, menu);
		
        // pesquisa
		mSearchView = (SearchView) menu.findItem(R.id.pedidos_search_action_search).getActionView();
        setupSearchView();
		
		return true;
	}

	private void setupSearchView() {

		mSearchView.setIconifiedByDefault(true);
		
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnCloseListener(this);
    }

    public boolean onQueryTextChange(String newText) {
    	Log.v(CNT_LOG, "onQueryTextChange - Query ["+newText+"]");
        return false;
    }

	public boolean onQueryTextSubmit(String query) {
		Log.v(CNT_LOG, "onQueryTextSubmit - Query ["+query+"] : submitted");
	    
	    onPesquisar(query);
	    return false;
	}

    public boolean onClose() {
    	Log.v(CNT_LOG, "onClose - Listar Todos");

    	// Mostrar Todos
    	listarTodos();

        return false;
    }

		
    public void  onPesquisar(String query){
    	// Limpar a Lista
    	lstPedidos.clear();
    	
//    	lstPedidos = helper.getListProdutos(query);
//
//        String title = lstPedidos.size()+" Produtos Encontrados";
//        setTitle(title);
//    	
//        adapter = new PedidosListAdapter(this, lstPedidos);
//        listView.setAdapter(adapter);
//        
//    	adapter.notifyDataSetChanged();
    	
    }

    public void listarTodos(){
    	lstPedidos.clear();
    	
        lstPedidos = helper.getPedidos();
        
        String title = lstPedidos.size()+" Produtos";
        setTitle(title);
        
        adapter = new PedidosListAdapter(this, lstPedidos);
        listView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
    
}
