package br.com.vilaverde.cronos.view.pedidos;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.PedidoProduto;
import br.com.vilaverde.cronos.model.Produto;
import br.com.vilaverde.cronos.view.pedidos.PedidosListAdapter.ViewHolder;

public class PedidosProdutosAdapter extends BaseAdapter{

		private String CNT_LOG = "PedidosProdutosAdapter";
		
		private List<PedidoProduto> lstprodutos;
		
		private Context context = null;
		
		// Classe utilizada para instanciar is objetos  do xml
		private LayoutInflater inflater;
		
		public PedidosProdutosAdapter(Context context, List<PedidoProduto> produtos) {
		        this.lstprodutos = produtos;
		        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        
		        this.context = context;
	    }
		
		public void addItem(final PedidoProduto item) {
	        this.lstprodutos.add(item);
	        //Atualizar a lista caso seja adicionado algum item
	        notifyDataSetChanged();
	    }    
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lstprodutos.size();
		}

		@Override
		public Object getItem(int position) {
			return lstprodutos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
	        try
	        {
		          			
	        	Log.v(CNT_LOG, "Position: ["+position+"]");
	        	
		       //Pega o registro da lista e trasnfere para o objeto produto
		       PedidoProduto produto = lstprodutos.get(position);
		         
	           Log.v(CNT_LOG, "Produto: ["+produto.getId()+"]");		       
		       
	           //O ViewHolder irá guardar a instâncias dos objetos do estado_row
	           ViewHolder holder;
	           
	           //Quando o objeto convertView não for nulo nós não precisaremos inflar
	           //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
	           if (convertView == null) {
	        	   
	   	        	//Utiliza o XML produto_list_row para apresentar na tela
	        	   	convertView = inflater.inflate(R.layout.pedidos_produtos_row, null);
	   	        	
	   	        	//Cria o Viewholder e guarda a instância dos objetos
	   	        	holder = new ViewHolder();
	   	        	holder.tvCodigo  = (TextView)convertView.findViewById(R.id.pedidosprodutos_listrow_tvCodigo);
	   	        	holder.tvDescricao    = (TextView)convertView.findViewById(R.id.pedidosprodutos_listrow_tvDescricao);
	   	        	holder.tvQuantidade = (TextView)convertView.findViewById(R.id.pedidosprodutos_listrow_tvQuantidade);
	 	        	holder.tvValor   = (TextView)convertView.findViewById(R.id.pedidosprodutos_listrow_tvValor);
	 	        	holder.tvValorTotal   = (TextView)convertView.findViewById(R.id.pedidosprodutos_listrow_tvValorTotal);
	   	        	
	   	        	convertView.setTag(holder);
	   	        	
	           } else {
	               //pega o ViewHolder para ter um acesso rápido aos objetos do XML
	               //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
	               holder = (ViewHolder) convertView.getTag();
	           }
	           

	           holder.tvCodigo.setText(produto.getId_produto());	           

	           holder.tvDescricao.setText(produto.getDescricao());
	           holder.tvQuantidade.setText(produto.getStrQuantidade());
	           
	           String strValor = NumberFormat.getCurrencyInstance().format(produto.getValor());
	           holder.tvValor.setText(strValor);
	           String strValorTotal = NumberFormat.getCurrencyInstance().format(produto.getValor_total());
	           holder.tvValorTotal.setText(strValorTotal);
	           
	           return convertView;           
	            
	       }catch (Exception e) {
	    	   	if (e.getMessage() != null){
	    	   		Log.e(CNT_LOG, "Erro getView - [ "+ e.getMessage()+ " ]");
	    	   	}
	       }
	       return convertView;
	                
		}
		
		//Criada esta classe estática para guardar a referência dos objetos abaixo
	    static class ViewHolder {
	    	public TextView tvCodigo;
	    	public TextView tvDescricao;
	        public TextView tvQuantidade;
	        public TextView tvValor;
	        public TextView tvValorTotal;
	    }  
	}
