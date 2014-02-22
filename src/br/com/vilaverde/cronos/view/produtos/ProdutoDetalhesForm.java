package br.com.vilaverde.cronos.view.produtos;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.PedidoHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.model.Produto;

public class ProdutoDetalhesForm extends Fragment {

	private String CNT_LOG = "ProdutosDetalhesForm";
	View view = null;
	Produto selectedProduto = null;
	
	private Context context = null;
	PedidoHelper pedidoHelper = null;
	Pedido pedidoAberto = null;
	PedidoProduto pedidoProduto = null;
	PedidoProdutosHelper pedidoProdutoHelper = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = this.getActivity().getApplicationContext();
		
	 	view = inflater.inflate(R.layout.produtos_detalhe_form, container, false);
		    
		
		Button adicionar = (Button) view.findViewById(R.id.bt_produto_detalhe_adicionar);
		adicionar.setOnClickListener(onAdicionar);

		// Recuperar o Pedido Aberto
		pedidoHelper = new PedidoHelper(context);
		pedidoAberto = pedidoHelper.getPedidoAberto(); 

		// Instanciar o Helper de pedidoProdutos
		 pedidoProdutoHelper = new PedidoProdutosHelper(context);
		
	    return view;
	}

	private OnClickListener onAdicionar = new OnClickListener() {
		public void onClick(View arg0) {
			adicionar();
		}
	};
	
	public void adicionar() {
		Log.v(CNT_LOG,"Adicionar Produto no Pedido Aberto");
		try {

			// Testar se tem pedido Aberto
			if (pedidoAberto != null){
				Log.v(CNT_LOG, "Tem pedido Aberto Pedido = "+pedidoAberto.getId());
				
				if (selectedProduto != null) {
							
					// Recuperar a Quantidade
					EditText vQuantidade = (EditText)view.findViewById(R.id.produto_detalhe_quantidade);
					Log.v(CNT_LOG,"TAMANHO DA QUANTIDEDE = "+vQuantidade.getText().length());
					if (vQuantidade.getText().length() > 0) {

						float quantidade = Float.parseFloat(vQuantidade.getText().toString());
						
						// Calcular Valor Total
						float valor_total = (float) (quantidade * selectedProduto.getPreco());
						
						Log.v(CNT_LOG, "Adicionando Produto = "+selectedProduto.getDescricao()+" Quantidade = "+quantidade);
						
						
						pedidoProduto = new PedidoProduto();
						pedidoProduto.setId_pedido(""+pedidoAberto.getId());
						pedidoProduto.setId_produto(""+selectedProduto.getId());
						pedidoProduto.setQuantidade(quantidade);
						pedidoProduto.setValor(selectedProduto.getPreco());
						pedidoProduto.setValor_total(valor_total);
						pedidoProduto.setObservacao("teste de observacao");
			
						if(pedidoProdutoHelper.inserir(pedidoProduto) > -1){
							Toast.makeText(context, "Produto Adicionado ao Pedido", Toast.LENGTH_LONG).show();					
							
							Log.v(CNT_LOG, "FEchar Activity");
							this.getActivity().finish();
						}
						else {
							Log.v(CNT_LOG,"Erro na Inclusao do Produto");
						}
					}
					else {
						Log.w(CNT_LOG, "Produto Sem Quantidade");
						new AlertDialog.Builder(this.getActivity())
				        .setTitle("Atenção")
				        .setMessage("Inclua uma Quantidade no Produto.")
				        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	dialog.dismiss();
				            }
				        })
				        .create().show();						
					}
				}
			}
			else {
				Log.w(CNT_LOG, "NÃO TEM Pedido Aberto");
				new AlertDialog.Builder(this.getActivity())
		        .setTitle("Atenção")
		        .setMessage("Não Há Pedido Aberto.")
		        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	dialog.dismiss();
		            }
		        })
		        .create().show();
			}
			
			

		}
		catch (Exception e) {
			Log.e(CNT_LOG, "adicionar - Error["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}

	public void setProduto(Produto produto) {
		Log.v(CNT_LOG, "setProduto("+produto.getId()+")");
		selectedProduto = produto; 
	}
	
}

