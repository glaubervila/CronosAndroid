package br.com.vilaverde.cronos.view.clientes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.R.layout;
import br.com.vilaverde.cronos.R.menu;
import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.view.pedidos.PedidoAberto;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class ClienteSearch extends Activity implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener {

	private String CNT_LOG = "ClienteSearch";

	private static final int SELECIONAR = 1;
	
	private ClienteHelper helper;   
	private ClienteAdapter adapter; 
	private List<Cliente> lstClientes;
    ListView listView;

    // Pesquisa
    private SearchView mSearchView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cliente_search);
				
		// Crio o listview passando o id da listview no xml
        listView = (ListView) findViewById(R.id.cliente_search_list);

    	// Instanciar o Helper 
        helper = new ClienteHelper(this);
		
        // Recuperar o Array de Clientes no DB
        lstClientes = helper.getListaClientes();

        // Instanciar o Adapter e passar o array clientes
        adapter = new ClienteAdapter(this, lstClientes);
        
        // Adicionar o Adapter a ListView
        listView.setAdapter(adapter);
        
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				
				Cliente cliente = lstClientes.get(position);
				
				Log.v(CNT_LOG, "Cliente Nome ["+cliente.getNome()+"] Index ["+index+"] Posicao ["+position+"]");
							
				// Chamar a janela de Pedido Aberto
				Intent intent = new Intent();
				 
				// Passando como Parametro um Flag  1 para alterar
				//intent.putExtra("tipo", ALTERAR);
				// Passando o Objeto Cliente  
	            intent.putExtra("cliente", cliente);
	            
	            //chama a tela de Pedido
	            //startActivityForResult(intent,SELECIONADO);
    	        // Setando o Result como Ok
	            setResult(Activity.RESULT_OK, intent);
	            //startActivity(intent);
	            finish();
				//Toast.makeText(getApplicationContext(), "Você clicou no Cliente : " + lstClientes.get(position),Toast.LENGTH_SHORT).show();
				return false;
			}
        	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cliente_search, menu);
		
        // pesquisa
		mSearchView = (SearchView) menu.findItem(R.id.clientes_search_action_search).getActionView();
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
    	lstClientes.clear();
    	
    	lstClientes = helper.getClientes(query);

        adapter = new ClienteAdapter(this, lstClientes);
        listView.setAdapter(adapter);
        
    	adapter.notifyDataSetChanged();
    	
    }

    public void listarTodos(){
    	lstClientes.clear();
    	
        lstClientes = helper.getListaClientes();
        adapter = new ClienteAdapter(this, lstClientes);
        listView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
}
