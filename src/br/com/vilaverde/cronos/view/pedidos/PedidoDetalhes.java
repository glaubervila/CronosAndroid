package br.com.vilaverde.cronos.view.pedidos;

import java.util.zip.Inflater;

import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.R.layout;
import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.dao.PedidoProdutosHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Pedido;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PedidoDetalhes extends Fragment {
	
	private String CNT_LOG = "PedidoDetalhes";
	View view = null;
	ClienteHelper clienteHelper = null;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pedido_detalhes, container, false);
	    clienteHelper = new ClienteHelper(getActivity().getApplicationContext());
		 	
		return view;
	}
	
	 public void loadDetalhes(Pedido pedido) {
		Log.v(CNT_LOG,"loadDetalhes("+pedido.getId()+")");
		
		// Data Pedido
		
		// Quantidade Itens
        TextView tvQuantidade = (TextView) view.findViewById(R.id.pedido_qtd_itens);
        tvQuantidade.setText(""+(int)pedido.getQtd_itens());

		// Valor Total
        TextView tvTotal = (TextView) view.findViewById(R.id.pedido_valor_total);
        tvTotal.setText("R$ "+pedido.getValor_total());

		
		// Cliente
		// FIXME AKI TEM UMA CONVERSAO NO ID CLIENTE DE INT PARA STRING
		Cliente cliente = clienteHelper.getCliente(Integer.parseInt(pedido.getId_cliente()));
		
        TextView tvCliente = (TextView) view.findViewById(R.id.pedido_nome_cliente);
        tvCliente.setText(cliente.getNome());
        
	 }
}