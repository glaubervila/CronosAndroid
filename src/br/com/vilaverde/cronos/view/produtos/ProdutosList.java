package br.com.vilaverde.cronos.view.produtos;

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
import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.model.Produto;

public class ProdutosList extends Activity implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener {

	private String CNT_LOG = "ProdutosList";

	private static final int SELECIONAR = 1;
	
	private ProdutosHelper helper;   
	private ProdutosListAdapter adapter; 
	private List<Produto> lstProdutos;
    ListView listView;

    // Pesquisa
    private SearchView mSearchView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produtos_list);
				
		// Crio o listview passando o id da listview no xml
        listView = (ListView) findViewById(R.id.produtos_list);

    	// Instanciar o Helper 
        helper = new ProdutosHelper(this);
		
        // Recuperar o Array de Clientes no DB
        lstProdutos = helper.getListProdutos();
        String title = lstProdutos.size()+" Produtos";
        setTitle(title);

        
        // Instanciar o Adapter e passar o array clientes
        adapter = new ProdutosListAdapter(this, lstProdutos);
        
        // Adicionar o Adapter a ListView
        listView.setAdapter(adapter);
        
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				
				Produto produto = lstProdutos.get(position);
				
				
				// TODO: Janela Detalhe Produto precisa buscar seu proximo e seu anterior 
				// Isso aki foi uma correcao temporaria aparentemente nao pode passar uma lista muito grande de produtos
				// Pelo intent, e atuamente nem e mais necessario pq a janela fecha ao incluir um produto.
				// a solucao idal e colocar a tela de detalhes recuperando um registro seu proximo e seu antecessor
//				List<Produto> produtos = new ArrayList<Produto>();
				ArrayList<Produto> produtos = new ArrayList<Produto>();
				produtos.add(produto);
				
//				List<Produto> produtos = new ArrayList<Produto>();
//				produtos = (List<Produto>) helper.ListProdutosByDepartamentos(produto.getCategoria_id());

				
				ArrayList<Produto> arrayProdutos = (ArrayList<Produto>)produtos; 
							
				// Chamar a janela de Produtos Detalhes	  			        		
				Intent intent = new Intent(getApplicationContext(), ProdutosDetalhe.class);
				//intent.putExtra("position", position);
				intent.putExtra("position", 0);
				intent.putExtra("codigo", produto.getCodigo());
				intent.putExtra("image_path", produto.getImage_path());
				intent.putExtra("produto",produto);
				
				
				intent.putExtra("lstProdutos",arrayProdutos);
				
				Log.v(CNT_LOG, "Produto Descricao ["+produto.getDescricao_curta()+"] Index ["+index+"] Posicao ["+position+"]");
				
				startActivity(intent);

				return false;
			}
        	
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.produtos_search, menu);
		
        // pesquisa
		mSearchView = (SearchView) menu.findItem(R.id.produtos_search_action_search).getActionView();
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
    	lstProdutos.clear();
    	
    	lstProdutos = helper.getListProdutos(query);

        String title = lstProdutos.size()+" Produtos Encontrados";
        setTitle(title);
    	
        adapter = new ProdutosListAdapter(this, lstProdutos);
        listView.setAdapter(adapter);
        
    	adapter.notifyDataSetChanged();
    	
    }

    public void listarTodos(){
    	lstProdutos.clear();
    	
        lstProdutos = helper.getListProdutos();
        
        String title = lstProdutos.size()+" Produtos";
        setTitle(title);
        
        adapter = new ProdutosListAdapter(this, lstProdutos);
        listView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
    
}
