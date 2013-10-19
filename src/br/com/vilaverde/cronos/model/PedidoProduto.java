package br.com.vilaverde.cronos.model;

import java.io.Serializable;
import java.text.NumberFormat;

public class PedidoProduto implements Serializable {
	
	private int id ;
	private String id_pedido;
	private String id_produto;
	private String descricao;
	private double quantidade;
	private double valor;
	private double valor_total;
	private String observacao;
	
	private String strValor;
	private String strValorTotal;
	private String strQuantidade;
	
	public String getStrValor() {
		String strValor = NumberFormat.getInstance().format(valor);
		return strValor;
	}



	public String getStrValorTotal() {
		String strValorTotal = NumberFormat.getInstance().format(valor_total);
		return strValorTotal;
	}



	public String getStrQuantidade() {
		String strQuantidade = NumberFormat.getInstance().format(quantidade);
		return strQuantidade;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getId_pedido() {
		return id_pedido;
	}



	public void setId_pedido(String id_pedido) {
		this.id_pedido = id_pedido;
	}



	public String getId_produto() {
		return id_produto;
	}



	public void setId_produto(String id_produto) {
		this.id_produto = id_produto;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public double getQuantidade() {
		return quantidade;
	}



	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}



	public double getValor() {
		return valor;
	}



	public void setValor(double valor) {
		this.valor = valor;
	}



	public double getValor_total() {
		return valor_total;
	}



	public void setValor_total(double valor_total) {
		this.valor_total = valor_total;
	}



	public String getObservacao() {
		return observacao;
	}



	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}



	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
       return id_produto;
    }
	
}
