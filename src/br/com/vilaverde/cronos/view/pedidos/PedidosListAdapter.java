package br.com.vilaverde.cronos.view.pedidos;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.model.Pedido;
import br.com.vilaverde.cronos.model.Produto;

public class PedidosListAdapter extends BaseAdapter{

	private String CNT_LOG = "PedidosListAdapter";
	
	private List<Pedido> lstPedidos;
	
	private Context context = null;
	
	// Classe utilizada para instanciar is objetos  do xml
	private LayoutInflater inflater;
	
	public PedidosListAdapter(Context context, List<Pedido> plstPedidos) {
	        this.lstPedidos = plstPedidos;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        
	        this.context = context;
    }
	
	public void addItem(final Pedido item) {
        this.lstPedidos.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }    
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstPedidos.size();
	}

	@Override
	public Object getItem(int position) {
		return lstPedidos.get(position);
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
	       Pedido pedido = lstPedidos.get(position);
	         
	       
	       
           //O ViewHolder irá guardar a instâncias dos objetos do estado_row
           ViewHolder holder;
           
           //Quando o objeto convertView não for nulo nós não precisaremos inflar
           //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
           if (convertView == null) {

   	        	//Utiliza o XML produto_list_row para apresentar na tela
        	   	convertView = inflater.inflate(R.layout.pedidos_list_row, null);
   	        	
   	        	//Cria o Viewholder e guarda a instância dos objetos
   	        	holder = new ViewHolder();
   	        	holder.tvStatus  = (TextView)convertView.findViewById(R.id.pedidos_listrow_tvStatus);
   	        	holder.tvData    = (TextView)convertView.findViewById(R.id.pedidos_listrow_tvData);
   	        	holder.tvCliente = (TextView)convertView.findViewById(R.id.pedidos_listrow_tvCliente);
 	        	holder.tvTotal   = (TextView)convertView.findViewById(R.id.pedidos_listrow_tvTotal);
   	        	
   	        	convertView.setTag(holder);
   	        	
           } else {
               //pega o ViewHolder para ter um acesso rápido aos objetos do XML
               //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
               holder = (ViewHolder) convertView.getTag();
           }
           
          
           holder.tvStatus.setText(pedido.getStringStatus());
//           holder.tvStatus.setTextColor(pedido.getStatusColor());
           holder.tvStatus.setBackgroundColor(pedido.getStatusColor());
                     
           holder.tvData.setText(pedido.getDt_inclusao());
           
           String strTotal = NumberFormat.getCurrencyInstance().format(pedido.getValor_pago());
           holder.tvCliente.setText(pedido.getCliente());
           holder.tvTotal.setText(strTotal);
           
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
    	public TextView tvStatus;
    	public TextView tvCliente;
        public TextView tvData;
        public TextView tvTotal;
    }  
}
