package br.com.vilaverde.cronos.view.clientes;

import java.util.List;
import br.com.vilaverde.cronos.R;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.model.Cliente;


public class ClientesList extends Activity  implements SearchView.OnQueryTextListener,
SearchView.OnCloseListener {

	private String CNT_LOG = "ClientesList";
	@SuppressWarnings("unused")
	private static final int INCLUIR = 0;
	private static final int ALTERAR = 1;
	private static final int EXCLUIR = 2;

	
	
	private ClienteHelper helper;   //instância responsável pela persistência dos dados
	private ClienteAdapter adapter; //Adapter responsável por apresentar os clientes na tela
	
	private List<Cliente> lstClientes = null; //lista de clientes cadastrados no BD
    ListView listView;
    
    int Posicao = 0;    //determinar a posição do contato dentro da lista lstContatos
    
    
    // Pesquisa
    private SearchView mSearchView;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		// Digo qual é o xml de layout
		setContentView(R.layout.clientes_list);
		
		
		// Crio o listview passando o id da listview no xml
        listView = (ListView) findViewById(R.id.clientes_listview);

    	// Instanciar o Helper 
        helper = new ClienteHelper(this);
        
        // Recuperar o Array de Clientes no DB
        lstClientes = helper.getListaClientes();
        //lstClientes = helper.Consultar();

        // Instanciar o Adapter e passar o array clientes
        adapter = new ClienteAdapter(this, lstClientes);
        
        // Adicionar o Adapter a ListView
        listView.setAdapter(adapter);
        
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView arg0, View view, int position, long index) {
//            	Toast.makeText(getApplicationContext(), "Você clicou no Cliente : " + lstClientes.get(position).toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        });        

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				
				Cliente cliente = lstClientes.get(position);
				
				Log.v(CNT_LOG, "Cliente Nome ["+cliente.getNome()+"] Index ["+index+"] Posicao ["+position+"]");
				
				// Guardar a Posicao do item para coloca ele no mesmo lugar depois de atualizar
				Posicao = position;
				
				// Chamar o Formulario e carregar o registro para edicao
				Intent intent = new Intent(getApplicationContext(), ClientesForm.class);
				 
				// Passando como Parametro um Flag  1 para alterar
				intent.putExtra("tipo", ALTERAR);
				// Passando o Objeto Cliente que vai ser Editado  
	            intent.putExtra("cliente", cliente);
	            
	            //chama a tela de alteração
	            startActivityForResult(intent, ALTERAR); 
				
				//Toast.makeText(getApplicationContext(), "Você clicou no Cliente : " + lstClientes.get(position),Toast.LENGTH_SHORT).show();
				return false;
			}
        	
		});
	}
	
    //Rotina executada quando finalizar a Activity ClienteForm
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        Cliente cliente = null;
         
        try
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK)
            {
                //obtem dados inseridos/alterados na Activity ContatoUI
                cliente = (Cliente)data.getExtras().getSerializable("cliente");
                
                int tipo = data.getExtras().getInt("tipo");
                 
                //o valor do requestCode foi definido na função startActivityForResult
                if (requestCode == INCLUIR)
                {
                    //verifica se digitou algo no nome do Cliente
                    if (!cliente.getNome().equals(""))
                    {
                    	Log.e(CNT_LOG, "onActivityResult - Inserir Nome [ "+cliente.getNome()+"]");
                    	
                        //insere o contato no Banco de Dados SQLite
                		long result = helper.inserir(cliente);
                        if (result > 0 ){
                            //insere o contato na lista de contatos em memória
                            lstClientes.add(cliente);
                        }
                        else {
                        	// Falha ao Inserir o Registro
                        	// Tratar melhor esse erro
                    		Toast.makeText(this, "Falha ao Inserir o Registro!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else if (requestCode == ALTERAR){

                     //atualiza o contato no Banco de Dados SQLite
                    helper.Alterar(cliente);
                     
                    //atualiza o contato na lista de contatos em memória
                    lstClientes.set(Posicao, cliente);
                    
                }else if (requestCode == EXCLUIR){
                	Log.e(CNT_LOG, "onActivityResult - Excluir");
                }
                
                // Se o botao de excluir for no formulario usar outro if para tratar pelo tipo 
                if (tipo == EXCLUIR){
                	Log.e(CNT_LOG, "onActivityResult - Excluir");
                	
            	 	// Usar o Helper e executar o metodo delete passando o objeto cliente
                    helper.Excluir(cliente);

                    // Remover da Lista
                    lstClientes.remove(cliente);
                    
            		Toast.makeText(this, "Registro Excluido!", Toast.LENGTH_SHORT).show();
                	
                }
                
                //método responsável pela atualiza da lista de dados na tela
                adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e) {
        	Log.e(CNT_LOG, "onActivityResult - Error["+e.getMessage()+"]");
        }       
    }    

	
    private void InserirContato(){
        try
        {
            //a variável "tipo" tem a função de definir o comportamento da Activity
            //ClienteForm, agora a variável tipo está definida com o valor "0" para
            //informar que será uma inclusão
             
            Intent intent = new Intent(this, ClientesForm.class);
            // Setando o Parametro Incluir Flag 0
            intent.putExtra("tipo", INCLUIR);
        	//chama a tela e incusão
            startActivityForResult(intent, INCLUIR);
        }
        catch (Exception e) {
        	Log.e(CNT_LOG, "save - Error["+e.getMessage()+"]");
        }           
    }    
    
    
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.clientes_list, menu);
		
	        
        // pesquisa
		mSearchView = (SearchView) menu.findItem(R.id.clientes_list_action_search).getActionView();
        setupSearchView();
		
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	        case R.id.menu_cliente_add:
	            // app icon in action bar clicked;          
	    		//startActivity(new Intent(this,ClientesForm.class));
	        	InserirContato();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
