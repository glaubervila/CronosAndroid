package br.com.vilaverde.cronos.model;

import java.io.Serializable;

public class Pedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id ;					
	private String id_usuario = "";		
	private String id_cliente = "";		
	private int status = 1;    			
	private float qtd_itens = 0;			
	private float valor_total = 0;		
	private int finalizadora = 1;
	private int parcelamento = 1;
	private int nfe = 1;
	private String dt_inclusao = "";
	private String dt_envio = "";
	private String observacao = "";
	
		



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
		return valor_total;
	}





	public void setValor_total(float valor_total) {
		this.valor_total = valor_total;
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


}
