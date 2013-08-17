package br.com.vilaverde.cronos.view.pedidos;

import java.util.List;

import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.R.id;
import br.com.vilaverde.cronos.R.layout;
import br.com.vilaverde.cronos.R.menu;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.view.clientes.ClienteSearch;
import br.com.vilaverde.cronos.view.clientes.ClientesForm;
import br.com.vilaverde.cronos.view.clientes.ClientesList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class PedidoAberto extends Activity {
	
	private final static String CNT_LOG = "PedidoAberto";
	
	private static final int SELECIONAR = 1;
	
	public Cliente cliente = null;
	public Pedido pedido = null;
	public Pedido aberto = null;
	public PedidoHelper helper = null;
	
	private static FragmentManager fragmentManager = null;
	private PedidoDetalhes pedidoDetalhes = null;
	private PedidoProdutos pedidoProdutos = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.pedido);
		
		try {
			
			helper = new PedidoHelper(this);
			
			// verificar se tem algum pedido aberto
			aberto = helper.getPedidoAberto();

			List<Pedido> todos = helper.getPedidos();

			if (aberto != null){			
				Log.v(CNT_LOG, "Tem pedido ABERTO!. Pedido = "+aberto.getId());
				loadPedido(aberto);
			}
			else {
				Log.v(CNT_LOG, "NAO Tem pedido ABERTO!");
								
			    // Instanciar a Activity de Selecao de Cliente
				Intent intent = new Intent(getApplicationContext(), ClienteSearch.class);
				             
	            //chama a tela de alteração
	            startActivityForResult(intent,SELECIONAR); 

			}
		}
		catch (Exception error){
			Log.e("TESTE", error.getMessage());
	    	Log.e("TESTE", "Falha ao Inserir Pedido");
	    }
		
		
	}
	
    // Executado apos selecionar um cliente
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.v(CNT_LOG, "onActivityResult()");
        Cliente cliente = null;
         
        try {
            super.onActivityResult(requestCode, resultCode, data);
            
            if (resultCode == Activity.RESULT_OK) {
           	
                //obtem dados Selecionados na Activity ClienteSearch 
                cliente = (Cliente)data.getExtras().getSerializable("cliente");
           	
                //o valor do requestCode foi definido na função startActivityForResult
                if (requestCode == SELECIONAR) {
                	Log.v(CNT_LOG, "onActivityResult - Cliente Selecionado ["+cliente.getNome()+"]");
                	onSelectCliente(cliente);
                }
            }
        }
        catch (Exception e) {
        	Log.e(CNT_LOG, "onActivityResult - Error["+e.getMessage()+"]");
        }       
    }    

	private void onSelectCliente(Cliente cliente) {
		Log.v(CNT_LOG, "onSelectCliente("+cliente.getId()+")");
		
		// Montar um Novo Objeto Pedido
		pedido = new Pedido();

		// FIXME AKI TEM UMA CONVERSAO NO ID CLIENTE DE INT PARA STRING
		pedido.setId_cliente(String.valueOf(cliente.getId()));
		pedido.setId_usuario("10");
		pedido.setStatus(0); // Aberto
		pedido.setValor_total(0);
		
		helper.inserir(pedido);
		aberto =  helper.getPedidoAberto();
		if (aberto != null){
			loadPedido(aberto);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedido_aberto, menu);
		
		
		
		return true;
	} 
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	            
	        case R.id.menu_excluir_pedido:

	        	excluirPedidoAberto();
	            return true;	            
	
	        case R.id.menu_finalizar_pedido:

	        	finalizarPedido();
	            return true;	            
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void finalizarPedido() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();

	    builder.setTitle("Finalizar Pedido");
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View finalizarView = inflater.inflate(R.layout.finalizar_pedido_dialog, null);
	    builder.setView(finalizarView);
	    
	    // Recuperar os elementos da dialog
	    // Radio Group
    	final RadioGroup formaPagamento = (RadioGroup) finalizarView.findViewById(R.id.pedido_rg_forma_pagamento);
    	// Swicth
    	final Switch notaFiscal = (Switch) finalizarView.findViewById(R.id.pedido_sw_nota_fiscal);
	    final EditText observacoes = (EditText) finalizarView.findViewById(R.id.pedido_et_observacoes);
	    
	    builder.setPositiveButton("Fechar Pedido", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	        	Log.v(CNT_LOG, "finalizarPedido - Clicou no SIM");
	        	// Verificar se os campos estao preenchidos
	        	
        	
	        	// Forma de Pagamento 
	        	// Returns an integer which represents the selected radio button's ID
	        	int selected = formaPagamento.getCheckedRadioButtonId();
	        	// Gets a reference to our "selected" radio button
	        	RadioButton rb = (RadioButton) formaPagamento.findViewById(selected);
	        	int idx = formaPagamento.indexOfChild(rb);
	        	// Adiciono +1 por as finalizadoras sao 1,2,3
	        	aberto.setFinalizadora(idx+1);

	        	// Nota Fiscal Eletronica
	        	boolean isChecked = notaFiscal.isChecked();
	        	aberto.setNfe(isChecked ? 1 : 0);

	        	// Observacao
	        	aberto.setObservacao(observacoes.getText().toString());

	        	Log.v(CNT_LOG, "Finalizadora = "+aberto.getFinalizadora());
	        	Log.v(CNT_LOG, "Nota Fiscal  = "+aberto.getNfe());
	        	Log.v(CNT_LOG, "Observação  = "+aberto.getObservacao());
	        	
	        	fecharPedido();
	            dialog.dismiss();
	        }
	    });
	    
	    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	Log.v(CNT_LOG, "finalizarPedido - Clicou no SIM");
	            // Do nothing
	            dialog.dismiss();
	        }
	    });

	    AlertDialog alert = builder.create();
	    alert.show();			
	}

	
	protected void fecharPedido() {
		Log.v(CNT_LOG,"fecharPedido()");
		
		// Perguntar se realmente quer Fechar o Pedido
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Fechar Pedido?");    

	    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            // Do nothing but close the dialog
	        	Log.v(CNT_LOG, "fecharPedido() - Clicou no YES");	
	        	fechar();
	            dialog.dismiss();
	        }
	    });
	    
	    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Log.v(CNT_LOG, "fecharPedido() - Clicou no No");
	            // Do nothing
	            dialog.dismiss();
	        }
	    });

	    AlertDialog alert = builder.create();
	    alert.show();			
	}

	

	protected void fechar() {
		Log.v(CNT_LOG,"fechar()");
		
		if (this.helper.fechar()){
			this.finish();
		}
	}

	private void excluirPedidoAberto() {

		if (aberto != null){
			Log.v(CNT_LOG,"excluirPedidoAberto()");
			
			// Perguntar se realmente quer excluir
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle("Excluir Pedido?");
		    builder.setMessage("Deseja Realmente Excluir o Pedido Aberto?");

		    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing but close the dialog
		        	Log.v(CNT_LOG, "onDelete - Clicou no YES");
		        	delete();		        	
		            dialog.dismiss();
		        }
		    });
		    
		    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	Log.v(CNT_LOG, "onDelete - Clicou no No");
		            // Do nothing
		            dialog.dismiss();
		        }
		    });

		    AlertDialog alert = builder.create();
		    alert.show();			
		}
	}


	
	public void delete() {
		Log.v(CNT_LOG,"delete()");
		
		if (this.helper.Excluir(aberto)){
			this.finish();
		}
	}


	public void loadPedido(Pedido pedido){
		// Criar um Fragment Manager para recuperar os fragments
		fragmentManager = getFragmentManager();
		
		// Carregando os Detalhes do Pedido
		pedidoDetalhes = (PedidoDetalhes) fragmentManager.findFragmentById(R.id.fragment_pedido_detalhes);
		pedidoDetalhes.loadDetalhes(pedido);

		// Carregando os Produtos do Pedido
		pedidoProdutos = (PedidoProdutos) fragmentManager.findFragmentById(R.id.fragment_pedido_produtos);
		pedidoProdutos.loadProdutos(pedido);
	}
	
}
