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
import br.com.vilaverde.cronos.model.Departamento;
import br.com.vilaverde.cronos.model.Departamento;

public class DepartamentoAdapter extends BaseAdapter{

	private String CNT_LOG = "DepartamentoAdapter";
	
	private List<Departamento> listDepartamentos;
	
	// Classe utilizada para instanciar is objetos  do xml
	private LayoutInflater inflater;
	
	public DepartamentoAdapter(Context context, List<Departamento> plistDepartamentos) {
	        this.listDepartamentos = plistDepartamentos;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public void addItem(final Departamento item) {
        this.listDepartamentos.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }    
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDepartamentos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDepartamentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        try
        {
	          			
	       //Pega o registro da lista e trasnfere para o objeto Departamento
	       Departamento departamento = listDepartamentos.get(position);
	        
	       
           //O ViewHolder irá guardar a instâncias dos objetos do estado_row
           ViewHolder holder;
           
           //Quando o objeto convertView não for nulo nós não precisaremos inflar
           //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
           if (convertView == null) {

   	        	//Utiliza o XML Departamento_list_row para apresentar na tela
        	   	convertView = inflater.inflate(R.layout.departamentos_list_row, null);
   	        	
   	        	//Cria o Viewholder e guarda a instância dos objetos
   	        	holder = new ViewHolder();
   	        	holder.tvDepartamento   = (TextView)convertView.findViewById(R.id.departamentos_ListRow_tvDepartamento);
   	        	
   	        	convertView.setTag(holder);
   	        	
           } else {
               //pega o ViewHolder para ter um acesso rápido aos objetos do XML
               //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
               holder = (ViewHolder) convertView.getTag();
           }
           
           holder.tvDepartamento.setText(departamento.getDepartamento());
           			           
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
        public TextView tvDepartamento;
    }  
}