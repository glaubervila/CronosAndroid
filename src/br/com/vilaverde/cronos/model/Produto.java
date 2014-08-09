package br.com.vilaverde.cronos.model;

import java.io.Serializable;
import java.text.NumberFormat;

public class Produto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id ;
	private int status;
	private int codigo;
	private int categoria_id;
	private String descricao_curta = "";
	private String descricao = "";
	private int quantidade;
	private double preco;
	private String image_name;
	private String image_path;
	private String image_size;
	private long image_id;
	private int image_status;
	
	public long getImage_id() {
		return image_id;
	}
	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	public String getImage_size() {
		return image_size;
	}
	public void setImage_size(String image_size) {
		this.image_size = image_size;
	}
	public int getImage_status() {
		return image_status;
	}
	public void setImage_status(int image_status) {
		this.image_status = image_status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getCategoria_id() {
		return categoria_id;
	}
	public void setCategoria_id(int categoria_id) {
		this.categoria_id = categoria_id;
	}
	public String getDescricao_curta() {
		return descricao_curta;
	}
	public void setDescricao_curta(String descricao_curta) {
		this.descricao_curta = descricao_curta;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getPreco() {
		return preco;
	}
	public String getStrPreco(){
	    String strPreco = NumberFormat.getCurrencyInstance().format(preco);
	    return strPreco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	
	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
       return descricao;
    }
	
}
