package br.com.vilaverde.cronos.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.vilaverde.cronos.R;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Pedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id ;
	private int id_servidor = 0;		
	private String id_usuario = "";		
	private String id_cliente = "";		
	private int status = 1;    			
	private float qtd_itens = 0;			
	private float valor_total = 0;
	private float valor_pago = 0;
	private float desconto = 0;
	private int finalizadora = 1;
	private int parcelamento = 1;
	private int nfe = 1;
	private String dt_inclusao = "";
	private String dt_envio = "";
	private String observacao = "";
	private List<PedidoProduto> produtos = null;
	private String jsonString = "";
	private String cliente = "";
	
		
	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
       return "id";
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_servidor() {
		return id_servidor;
	}

	public void setId_servidor(int id_servidor) {
		this.id_servidor = id_servidor;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}





	public String getId_cliente() {
		return id_cliente;
	}





	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}





	public int getStatus() {
		return status;
	}





	public void setStatus(int status) {
		this.status = status;
	}





	public float getQtd_itens() {
		return qtd_itens;
	}





	public void setQtd_itens(float qtd_itens) {
		this.qtd_itens = qtd_itens;
	}





	public float getValor_total() {
		//String teste = NumberFormat.getCurrencyInstance().format(valor_total);
		//return Float.parseFloat(teste);
		return valor_total;
	}





	public void setValor_total(float valor_total) {
		this.valor_total = valor_total;
	}





	public float getValor_pago() {
		return valor_pago;
	}

	public void setValor_pago(float valor_pago) {
		this.valor_pago = valor_pago;
	}

	public float getDesconto() {
		return desconto;
	}

	public void setDesconto(float desconto) {
		this.desconto = desconto;
	}

	public int getFinalizadora() {
		return finalizadora;
	}





	public void setFinalizadora(int finalizadora) {
		this.finalizadora = finalizadora;
	}





	public int getParcelamento() {
		return parcelamento;
	}





	public void setParcelamento(int parcelamento) {
		this.parcelamento = parcelamento;
	}





	public int getNfe() {
		return nfe;
	}





	public void setNfe(int nfe) {
		this.nfe = nfe;
	}





	public String getDt_inclusao() {
		return dt_inclusao;
	}





	public void setDt_inclusao(String dt_inclusao) {
		this.dt_inclusao = dt_inclusao;
	}





	public String getDt_envio() {
		return dt_envio;
	}





	public void setDt_envio(String dt_envio) {
		this.dt_envio = dt_envio;
	}





	public String getObservacao() {
		return observacao;
	}





	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}





	public List<PedidoProduto> getProdutos() {
		return produtos;
	}





	public void setProdutos(List<PedidoProduto> produtos) {
		this.produtos = produtos;
	}



	public String getStringStatus() {
		/*
		 * -1 - Erro (Enviado com Erro)
		 * 0 - Aberto
		 * 1 - Fechado (À Enviar)
		 * 2 - Enviado
		 * 9 - Enviado COM ERRO
		 */ 
		String strStatus = "";
		
		switch( status ) {
            case -1:
            	strStatus =  "Erro";
            	break;
            case 0:
            	strStatus =  "Aberto";
            	break;
            case 1:
            	strStatus =  "Fechado";
            	break;
            case 2:
            	strStatus =  "Enviado";
            	break;
            case 9:
            	strStatus =  "Enviado Com Erro";
            	break;
		}
		return strStatus;
	}
	
	public int getStatusColor() {
		/*
		 * -1 - Erro (Enviado com Erro)
		 * 0 - Aberto
		 * 1 - Fechado (À Enviar)
		 * 2 - Enviado
		 * 9 - Enviado COM ERRO
		 */ 
		int color = -1;
		
		switch( status ) {
            case -1:
            	// Error - Vermelho
            	color = Color.parseColor("#FF0000");
            	break;
            case 0:
            	// Aberto - Azul Claro
            	color = Color.parseColor("#00BFFF");
            	break;
            case 1:
            	// Fechado - Orange
            	color = Color.parseColor("#FFA500");
            	break;
            case 2:
            	// Enviado - LawnGreen
            	color = Color.parseColor("#7CFC00");
            	break;
            case 9:
            	// Error - Brown1
            	color = Color.parseColor("#FF4040");
            	break;
		}
		return color;
	}

	
	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getJsonString() {
		return jsonString;
	}

	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}


}
