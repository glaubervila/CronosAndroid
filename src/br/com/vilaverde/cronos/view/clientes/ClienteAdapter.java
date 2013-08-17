package br.com.vilaverde.cronos.view.clientes;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.model.Cliente;

public class ClienteAdapter extends BaseAdapter{

	private String CNT_LOG = "ClienteAdapter";
	
	private List<Cliente> listClientes;
	
	// Classe utilizada para instanciar is objetos  do xml
	private LayoutInflater inflater;
	
	public ClienteAdapter(Context context, List<Cliente> plistClientes) {
	        this.listClientes = plistClientes;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public void addItem(final Cliente item) {
        this.listClientes.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }    
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listClientes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listClientes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        try
        {
	          			
	       //Pega o registro da lista e trasnfere para o objeto cliente
	       Cliente cliente = listClientes.get(position);
	        
	       
           //O ViewHolder irá guardar a instâncias dos objetos do estado_row
           ViewHolder holder;
           
           //Quando o objeto convertView não for nulo nós não precisaremos inflar
           //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
           if (convertView == null) {

   	        	//Utiliza o XML cliente_list_row para apresentar na tela
        	   	convertView = inflater.inflate(R.layout.clientes_list_row, null);
   	        	
   	        	//Cria o Viewholder e guarda a instância dos objetos
   	        	holder = new ViewHolder();
   	        	holder.tvNome   = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvNome);
 	        	holder.tvCidade = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvCidade);
 	        	holder.tvBairro = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvBairro);
 	        	holder.tvEndereco = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvEndereco);
 	        	holder.tvTel    = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvTelefone);
 	        	holder.tvCel    = (TextView)convertView.findViewById(R.id.Cliente_ListRow_tvCelular);
   	        	
   	        	convertView.setTag(holder);
   	        	
           } else {
               //pega o ViewHolder para ter um acesso rápido aos objetos do XML
               //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
               holder = (ViewHolder) convertView.getTag();
           }
           
           holder.tvNome.setText(cliente.getNome());
           holder.tvCidade.setText(cliente.getCidade());
           holder.tvBairro.setText(cliente.getBairro());
           holder.tvEndereco.setText(cliente.getEndereco());
           holder.tvTel.setText(cliente.getTelefoneFixo());
           holder.tvCel.setText(cliente.getTelefoneMovel());
           			           
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
        public TextView tvNome;
        public TextView tvCidade;
        public TextView tvBairro;
        public TextView tvEndereco;
        public TextView tvTel;
        public TextView tvCel;

    }  

}
