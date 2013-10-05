package br.com.vilaverde.cronos.view.produtos;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.model.Produto;

public class ProdutosListAdapter extends BaseAdapter{

	private String CNT_LOG = "ProdutosListAdapter";
	
	private List<Produto> lstProdutos;
	
	// Classe utilizada para instanciar is objetos  do xml
	private LayoutInflater inflater;
	
	public ProdutosListAdapter(Context context, List<Produto> plstProdutos) {
	        this.lstProdutos = plstProdutos;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public void addItem(final Produto item) {
        this.lstProdutos.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }    
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstProdutos.size();
	}

	@Override
	public Object getItem(int position) {
		return lstProdutos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        try
        {
	          			
	       //Pega o registro da lista e trasnfere para o objeto produto
	       Produto produto = lstProdutos.get(position);
	        
	       
           //O ViewHolder ir� guardar a inst�ncias dos objetos do estado_row
           ViewHolder holder;
           
           //Quando o objeto convertView n�o for nulo n�s n�o precisaremos inflar
           //os objetos do XML, ele ser� nulo quando for a primeira vez que for carregado
           if (convertView == null) {

   	        	//Utiliza o XML produto_list_row para apresentar na tela
        	   	convertView = inflater.inflate(R.layout.produtos_list_row, null);
   	        	
   	        	//Cria o Viewholder e guarda a inst�ncia dos objetos
   	        	holder = new ViewHolder();
   	        	holder.tvDescricao   = (TextView)convertView.findViewById(R.id.produtos_listrow_tvDescricao);
 	        	holder.tvPreco = (TextView)convertView.findViewById(R.id.produtos_listrow_tvPreco);
   	        	
   	        	convertView.setTag(holder);
   	        	
           } else {
               //pega o ViewHolder para ter um acesso r�pido aos objetos do XML
               //ele sempre passar� por aqui quando,por exemplo, for efetuado uma rolagem na tela
               holder = (ViewHolder) convertView.getTag();
           }
           
           holder.tvDescricao.setText(produto.getDescricao());
           holder.tvPreco.setText("R$ "+produto.getPreco());
           
           return convertView;           
            
       }catch (Exception e) {
    	   	if (e.getMessage() != null){
    	   		Log.e(CNT_LOG, "Erro getView - [ "+ e.getMessage()+ " ]");
    	   	}
       }
       return convertView;
                
	}
	
	//Criada esta classe est�tica para guardar a refer�ncia dos objetos abaixo
    static class ViewHolder {
        public TextView tvDescricao;
        public TextView tvPreco;
    }  

}
