package br.com.vilaverde.cronos.model;

import java.io.Serializable;

public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id ;						// 0
	private int id_servidor;    	// 1
	private String id_usuario = "";			// 1
	private int tipo = 1;    				// 2
	private String nome = "";				// 3
	private String cpf = "";				// 4
	private String cnpj = "";				// 5
	private String rg = "";					
	private String inscricao_estadual = "";
	private String telefoneFixo = "";
	private String telefoneMovel = "";
	private String email = "";
	private String status_servidor = "0";
	private String responsavel = "";
	private String dt_inclusao = "";
	private String observacao = "";
	private String rua = "";
	private String numero = "";
	private String bairro = "";
	private String cidade = "";
	private int uf = 18; // default Rj
	private String cep = "";
	private String complemento = "";
	
		

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


	public int getTipo() {
		return tipo;
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}

	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	
	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public String getRg() {
		return rg;
	}


	public void setRg(String rg) {
		this.rg = rg;
	}


	public String getInscricao_estadual() {
		return inscricao_estadual;
	}


	public void setInscricao_estadual(String inscricao_estadual) {
		this.inscricao_estadual = inscricao_estadual;
	}


	public String getTelefoneFixo() {
		return telefoneFixo;
	}


	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}


	public String getTelefoneMovel() {
		return telefoneMovel;
	}


	public void setTelefoneMovel(String telefoneMovel) {
		this.telefoneMovel = telefoneMovel;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getStatus_servidor() {
		return status_servidor;
	}


	public void setStatus_servidor(String status_servidor) {
		this.status_servidor = status_servidor;
	}


	public String getResponsavel() {
		return responsavel;
	}


	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}


	public String getDt_inclusao() {
		return dt_inclusao;
	}


	public void setDt_inclusao(String dt_inclusao) {
		this.dt_inclusao = dt_inclusao;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public String getRua() {
		return rua;
	}


	public void setRua(String rua) {
		this.rua = rua;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public int getUf() {
		return uf;
	}


	public void setUf(int uf) {
		this.uf = uf;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getEndereco() {
		
		String endereco = this.getRua() + ", " + this.getNumero();
		return endereco;
	}


	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
       return nome;
    }


}
