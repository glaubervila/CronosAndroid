package br.com.vilaverde.cronos.view.pedidos;

import java.awt.font.TextAttribute;
import java.util.List;

import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.R.id;
import br.com.vilaverde.cronos.R.layout;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.model.Produto;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.TableRow;
import android.widget.TextView;

public class PedidoProdutos extends Fragment {
	
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

		
//		table.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int position, long index) {
//								
//				Log.v(CNT_LOG, "Cliente Nome ");
//							
//				return false;
//			}
//        	
//		});
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
			TextView valor = createCell(""+produto.getStrValor(),3, background,0);
			row.addView(valor);
			
			// Valor Total
			TextView valor_total = createCell(""+produto.getStrValorTotal(),4, background,0);
			row.addView(valor_total);
						
	 		table.addView(row);			       

		}



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
        
//        if ( !background ){ 
//        	textView.setBackgroundResource(R.drawable.cell_shape_impar);	
//		}
        

		return textView;
	}
}
