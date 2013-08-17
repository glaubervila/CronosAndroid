package br.com.vilaverde.cronos.model;

public class Vendedor {

	private static final long serialVersionUID = 1L;
	
	private int id ;	
	private int grupo;
	private String nome = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGrupo() {
		return grupo;
	}
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
       return nome;
    }

	
}
