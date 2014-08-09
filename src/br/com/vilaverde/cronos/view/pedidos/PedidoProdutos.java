package br.com.vilaverde.cronos.view.pedidos;

import java.text.NumberFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;

public class PedidoProdutos extends Fragment {
	
	int AlreadySelctedRow = 0;
	private String CNT_LOG = "PedidoProdutos";
	View view = null;
	private Context context = null;
	ViewGroup container = null;

	Pedido pedido = null;
	List<PedidoProduto> produtos = null;

	PedidoHelper pedidoHelper = null;
	PedidoProdutosHelper pedidoProdutoHelper = null;
	
	TableLayout table = null;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Setando o context 
		context = this.getActivity().getApplicationContext();
		// Setando a View para o fragment
		view = inflater.inflate(R.layout.pedido_produtos, container, false);
	
		this.container = container;

		// Instancias Helpers
		pedidoHelper = new PedidoHelper(context);
		pedidoProdutoHelper = new PedidoProdutosHelper(context);

		// Recuperar o Table Layout
		table = (TableLayout) view.findViewById(R.id.table_pedido_produtos);

		return view;
	}
	 
	public void loadProdutos(Pedido selectedPedido) {
		Log.v(CNT_LOG, "loadProdutos("+selectedPedido.getId()+")");
		pedido = selectedPedido;
		
		if (pedido != null){
					
			// Buscar os Produtos desse pedido
			produtos = pedidoProdutoHelper.getProdutos(pedido);		
			if (produtos.size() > 0) {
				listProdutos();
			}
		}
	}
	
	public void listProdutos(){
		Log.v(CNT_LOG, "listProdutos()");
		
		for (int i=0; i < produtos.size(); i++){
			PedidoProduto produto = produtos.get(i);
			
			// Criando uma Table Row
			TableRow row = new TableRow(context);

			//int id = Integer.parseInt(produto.getId_produto());
			// Colocar o index como id da row
			int index = i;
			row.setId(index);
			
			
			Log.v(CNT_LOG,"ROW [ "+i+" ] Produto ["+produto.getId_produto()+"]");
			
			
			boolean background =  ( i % 2 ) == 0;  

			// Codigo
			TextView codigo = createCell(produto.getId_produto(),0, background,0);
			row.addView(codigo);
			
			// Descricao
			TextView descricao = createCell(produto.getDescricao(),1, background,1);
			row.addView(descricao);
			
			// Quantidade
			TextView quantidade = createCell(""+produto.getStrQuantidade(),2, background,0);
			row.addView(quantidade);
			
			// Valor
	        float vValor = (float) produto.getValor();
			String strValor = NumberFormat.getCurrencyInstance().format(vValor);
			//TextView valor = createCell(""+produto.getStrValor(),3, background,0);
			TextView valor = createCell(strValor,3, background,0);
			row.addView(valor);
			
			// Valor Total
	        float vValorTotal = (float) produto.getValor_total();
			String strValorTotal = NumberFormat.getCurrencyInstance().format(vValorTotal);
			//TextView valor_total = createCell(""+produto.getStrValorTotal(),4, background,0);
			TextView valor_total = createCell(strValorTotal,4, background,0);
			row.addView(valor_total);



	        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = layoutInflater.inflate(R.layout.cell_button, container );
	        
			// Button Edit
			ImageButton btnEdit = (ImageButton) view.findViewById(R.id.cellButton);
			btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_light));

			LayoutParams cellLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
			cellLayout.column = 5;
			btnEdit.setLayoutParams(cellLayout);
			
			btnEdit.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                final TableRow parent = (TableRow) v.getParent();
	                Log.v(CNT_LOG, "EDITAR PRODUTO");
	                int index = parent.getId();
                
	                quantidadeDialog(index);
	                //table.removeView(parent);
	            }			
	        });       
	        row.addView(btnEdit);
	       
	        
	        // Button Delete
	        LayoutInflater layoutInflater2 =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view2 = layoutInflater.inflate(R.layout.cell_button, container );        			
			ImageButton btnDelete = (ImageButton) view2.findViewById(R.id.cellButton);		
			btnDelete.setImageDrawable(getResources().getDrawable(R.drawable.ic_discard_light));
			LayoutParams cellLayout2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
			cellLayout2.column = 6;
			btnDelete.setLayoutParams(cellLayout2);
			btnDelete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                final TableRow parent = (TableRow) v.getParent();

	                int index = parent.getId();
	                
	                onDelete(index, parent);
	            }			
	        });
			row.addView(btnDelete);
			
	 		table.addView(row);			       

		}
	}

	public void quantidadeDialog( final int index) {
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    builder.setTitle("Quantidade");

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View promptView = inflater.inflate(R.layout.quantidade_prompt, null); 
	    builder.setView(promptView);
	    
	    final EditText input = (EditText) promptView.findViewById(R.id.quantidade_prompt_edtxt);
	    
	    // Add action buttons
	    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int id) {	        	   
	               // Alterar quantidade do produto
	        	   if (input.getText().toString().length() > 0){

		        	   float quantidade = Float.parseFloat(input.getText().toString());
		        	   
		        	   Log.v(CNT_LOG, "ALTERAR O PRODUTO. Index: "+index+ " Quantidade: "+quantidade);
		        	   
	        	       alterarQuantidade(index, quantidade);
	        	   }
	        	   
	           }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
           }
        });      
	    builder.create();
	    builder.show();
	}

	public void onDelete(final int index, final TableRow row){
		// Perguntar se realmente quer excluir
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	
	    builder.setTitle("Confirme");
	    builder.setMessage("Deseja Realmente Excluir o Registro?");
	
	    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
	
	        public void onClick(DialogInterface dialog, int which) {
        	
	        	deletarProduto(index, row);
	        	        	
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
	

	public  TextView createCell(String value, int colIndex, Boolean background, int weight ){
		
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.cell, container );        
        TextView textView = (TextView) view.findViewById(R.id.cell);
        
        // Value
        textView.setText(value);
 
        // Table Layout Params
        android.widget.TableRow.LayoutParams cellLayout = new android.widget.TableRow.LayoutParams();
        cellLayout.column = colIndex;
        cellLayout.weight = weight;
             
        textView.setLayoutParams(cellLayout);
        
		return textView;
	}
	
	public void alterarQuantidade(int index, float quantidade){

		PedidoProduto produto = produtos.get(index);
		double quantidade_old = produto.getQuantidade();
		
		PedidoProdutosHelper helper = new PedidoProdutosHelper(context);
		if (quantidade != quantidade_old){
			// Se a quantidade for diferente
			Log.v(CNT_LOG, "Alterando "+produto.getDescricao()+" Quantidade: "+quantidade);
			// Atualiza o record
			produto.setQuantidade(quantidade);
			// Tenta fazer o update
			if(helper.alterar(produto) != -1){
				// Limpa a tabela
				table.removeAllViews();
				// Recarrega a tabela
				loadProdutos(pedido);
				// Produto Alterado com sucesso
				Toast.makeText(context, "Produto Alterado", Toast.LENGTH_SHORT).show();
				// TODO: Altualizar o detalhe do Pedido
			}
		}
		else {
			Log.v(CNT_LOG, "Quantidade Igual nao Faz Nada");
		}	
	}
	
	public void deletarProduto(int index, TableRow row){

		PedidoProduto produto = produtos.get(index);
		
		PedidoProdutosHelper helper = new PedidoProdutosHelper(context);

		Log.v(CNT_LOG, "Excluir "+produto.getDescricao());

		if(helper.deleteProduto(produto)){
		
			// Remove a row da tabela
            table.removeView(row);
				
            // Produto Removido com sucesso
			Toast.makeText(context, "Produto Removido", Toast.LENGTH_SHORT).show();
			// TODO: Altualizar o detalhe do Pedido
		}	
	}	
}
