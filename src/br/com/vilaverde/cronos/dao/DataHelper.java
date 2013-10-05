package br.com.vilaverde.cronos.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper{

	private final String CNT_LOG = "DataHelper";
	
	protected static  String NOME_BANCO = "cronos.db";
	
	private final static String TABELA = "pedidos";
	protected static int VERSAO_SCHEMA = 66;
	
	public Boolean REMOTE = false;
		
	protected SQLiteDatabase db;
	
	
	// Dados para Acesso ao Servidor
	// Endereco IP do Servidor para conexoes locais ex: 192.168.0.1 
	static String SERVER_DOMAIN_LOCAL = "192.168.0.200";
	//static String SERVER_DOMAIN_LOCAL = "10.0.0.102";
	
	// Endereco DNS do Servidor para conexoes remotas ex: magui.servehttp.com:6980
	static String SERVER_DOMAIN_REMOTE = "magui.servehttp.com:6980";		

	// Caminho para o arquivo que vai processar os requests ex: /cronos/main.php
	static String SERVER_PATH = "/cronos/main.php";

	// String Completa com o Host do Servidor LOCAL ex:"http://192.168.0.3/cronos/main.php"
	static String SERVER_HOST_LOCAL = "";
	
	// String Completa com o Host do Servidor REMOTO ex:"magui.servehttp.com:6980/cronos/main.php"
	static String SERVER_HOST_REMOTE = "";
	
	// Path Padrao para imagens 
	static String PATH_IMAGES = "Produtos";

	public String getPathImages(){
		return this.PATH_IMAGES;
	}

	
	public String getServerHostLocal(){
		// Setando o Caminho para conexoes Locais
		this.SERVER_HOST_LOCAL = "http://" + this.SERVER_DOMAIN_LOCAL + this.SERVER_PATH;

		return this.SERVER_HOST_LOCAL;
	}

	public String getServerHostRemote(){
		// Setando o Caminho para conexoes Remotas		
		this.SERVER_HOST_REMOTE = "http://" + this.SERVER_DOMAIN_REMOTE + this.SERVER_PATH;

		return this.SERVER_HOST_REMOTE;
	}
	

	public DataHelper(Context context) {
		
		super(context, NOME_BANCO , null, VERSAO_SCHEMA);

	}

	
	//@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		//this.onCreateTableClientes(db);
		//this.onCreateTableProdutos(db);
		//this.onCreateTablePedidos(db);
		//this.onCreateTablePedidoProdutos(db);
//		this.onCreateTableVendedor(db);
		this.onCreateTableDepartamentos(db);	
	}

	//@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		//this.onUpgradeTableClientes(db, oldVersion, newVersion);
		//this.onUpgradeTableProdutos(db, oldVersion, newVersion);
		//this.onUpgradeTablePedidos(db, oldVersion, newVersion);
		//this.onUpgradeTablePedidoProdutos(db, oldVersion, newVersion);
		//this.onUpgradeTableVendedor(db, oldVersion, newVersion);
		this.onUpgradeTableDepartamentos(db, oldVersion, newVersion);
		
	}

	public DataHelper Open() {
		
        db = this.getWritableDatabase();
        
        Log.i(CNT_LOG, "Conexao Aberta");
        
        return this;
    }
     
    public void Close() {
        if (db.isOpen()) {
        	
            db.close();
            
	        Log.i(CNT_LOG, "Conexao Fechada");
        }
    }
    
    public long Insert(String tabela, ContentValues values) {
	    long linhasInseridas = 0;
	         
	    this.Open();
	    try {
	        Log.i(CNT_LOG, "Inserindo registro. Tabela [ "+tabela+" ]");
	         
	        linhasInseridas = db.insert(tabela, null, values);
	         
	        Log.i(CNT_LOG, "Linhas inseridas: " + String.valueOf(linhasInseridas));
	        
	    } finally {
	        this.Close();
	    }
	     
	    return linhasInseridas;
	}
    
	public void onCreateTableClientes(SQLiteDatabase db) {
		Log.v(CNT_LOG, "Criando a Tabela [ clientes ]");
		
		String sql = "CREATE TABLE IF NOT EXISTS clientes"+
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" id_servidor INTEGER," +
					" id_usuario TEXT," +
					" tipo TEXT," +
					" nome TEXT," +
					" cpf TEXT," +
					" cnpj TEXT," +
					" rg TEXT," +
					" inscricao_estadual TEXT," +
					" telefone_fixo TEXT," +
					" telefone_movel TEXT," +
					" email TEXT," +
					" status_servidor TEXT," +
					" responsavel TEXT," +
					" dt_inclusao TEXT," +
					" observacao TEXT," +
					" rua TEXT," +
					" numero TEXT," +
					" bairro TEXT," +
					" cidade TEXT," +
					" uf TEXT," +
					" cep TEXT," +
					" complemento TEXT" +	
				");";
		
		db.execSQL(sql);
		
		Log.v(CNT_LOG, "Tabela [ clientes ] Criada com Sucesso!");
	}
		
	public void onUpgradeTableClientes(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table [clientes]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS clientes");
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [ clientes ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreate(db);
	}

//    private void createTableCliente(){
//		Log.v(CNT_LOG, "Criando a Tabela [ clientes ]");
//		
//		String sql = "CREATE TABLE IF NOT EXISTS clientes"+
//				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//					" id_usuario TEXT," +
//					" tipo TEXT," +
//					" nome TEXT," +
//					" cpf TEXT," +
//					" cnpj TEXT," +
//					" rg TEXT," +
//					" inscricao_estadual TEXT," +
//					" telefone_fixo TEXT," +
//					" telefone_movel TEXT," +
//					" email TEXT," +
//					" status_servidor TEXT," +
//					" responsavel TEXT," +
//					" dt_inclusao TEXT," +
//					" observacao TEXT," +
//					" rua TEXT," +
//					" numero TEXT," +
//					" bairro TEXT," +
//					" cidade TEXT," +
//					" uf TEXT," +
//					" cep TEXT," +
//					" complemento TEXT" +	
//				");";
//		
//		db.execSQL(sql);
//		
//		Log.v(CNT_LOG, "Tabela [ clientes ] Criada com Sucesso!");
//    }
//
    private void onCreateTableProdutos(SQLiteDatabase db){
		Log.v(CNT_LOG, "Criando a Tabela [ produtos ]");
		
		String sql = "CREATE TABLE IF NOT EXISTS produtos"+
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			" status INTEGER," +
			" codigo INTEGER," +
			" categoria_id INTEGER," +
			" descricao_curta TEXT," +
			" descricao TEXT," +
			" quantidade INTEGER," +
			" preco FLOAT," +
			" image_name TEXT," +
			" image_path TEXT," +
			" image_size TEXT," +
			" image_id INTEGER," +
			" image_status INTEGER" +
		");";
		
		db.execSQL(sql);
		
		Log.v(CNT_LOG, "Tabela [ produtos ] Criada com Sucesso!");
    }
       
	public void onUpgradeTableProdutos(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table [ produtos ]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS produtos");
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [ produtos ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreateTableProdutos(db);
	}
	
	
	public void onCreateTablePedidos(SQLiteDatabase db) {
		Log.v(CNT_LOG, "Criando a Tabela [ pedidos ]");

		String sql = "CREATE TABLE IF NOT EXISTS pedidos"+
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" id_servidor INTEGER," +
					" id_usuario TEXT," +
					" id_cliente TEXT," +
					" status INTEGER," +
					" qtd_itens REAL," +
					" valor_total REAL," +
					" finalizadora INTEGER," +
					" parcelamento INTEGER," +
					" nfe INTEGER," +
					" dt_inclusao TEXT," +
					" dt_envio TEXT," +
					" observacao TEXT" +
				");";
		
		db.execSQL(sql);
		
		Log.v(CNT_LOG, "Tabela [ pedidos ] Criada com Sucesso!");
	}


	public void onUpgradeTablePedidos(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Table [ pedidos ] OldVersion ["+oldVersion+"] NewVersion ["+newVersion+"]");
		try {
			//Log.v(CNT_LOG, "onUprade - Drop Table [ pedidos ]");
			db.execSQL("DROP TABLE IF EXISTS pedidos");
//			String sql = "DROP TABLE IF EXISTS pedidos";
//			if (newVersion == 58) {
//				sql = "ALTER TABLE pedidos ADD COLUMN id_servidor INTEGER;";
//			}
//			Log.v(CNT_LOG, sql);
//			db.execSQL(sql);
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [ pedidos ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreateTablePedidos(db);
	}
	
	public void onCreateTablePedidoProdutos(SQLiteDatabase db) {
	Log.v(CNT_LOG, "Criando a Tabela [ pedido_produtos ]");

	// Criacao da Tabela Pedido
	String sql = "CREATE TABLE IF NOT EXISTS pedido_produtos"+
			"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" id_pedido TEXT," +
				" id_produto TEXT," +
				" quantidade REAL," +
				" valor REAL," +
				" valor_total REAL," +
				" observacao TEXT" +
			");";
	
	db.execSQL(sql);
	Log.v(CNT_LOG, "Tabela [ pedido_produtos ] Criada com Sucesso!");	

}


	public void onUpgradeTablePedidoProdutos(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table [ pedido_produtos ]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS pedido_produtos");
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [" +TABELA+" ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreateTablePedidoProdutos(db);
	}
	
	public void onCreateTableVendedor(SQLiteDatabase db) {
		Log.v(CNT_LOG, "Criando a Tabela [ vendedor ]");
		
		String sql = "CREATE TABLE IF NOT EXISTS vendedor"+
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" nome TEXT," +
					" grupo INTEGER" +
				");";
		
		db.execSQL(sql);
		
		Log.v(CNT_LOG, "Tabela [ vendedor ] Criada com Sucesso!");
	}
		
	public void onUpgradeTableVendedor(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table [vendedor]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS vendedor");
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [ vendedor ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreate(db);
	}

	public void onCreateTableDepartamentos(SQLiteDatabase db) {
		Log.v(CNT_LOG, "Criando a Tabela [ departamentos ]");
		
		String sql = "CREATE TABLE IF NOT EXISTS departamentos"+
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" departamento TEXT" +
				");";
		
		db.execSQL(sql);
		
		Log.v(CNT_LOG, "Tabela [ departamentos ] Criada com Sucesso!");
	}
		
	public void onUpgradeTableDepartamentos(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(CNT_LOG, "onUprade - Drop Table [departamentos]");
		
		try {
			db.execSQL("DROP TABLE IF EXISTS departamentos");
		}
		catch (Exception error){
			Log.e(CNT_LOG, "Falha ao Excluir Tabela [ departamentos ] ERROR ["+error.getMessage()+"]");
		}
		
		this.onCreate(db);
	}
	
	
	// ---------------------------------------< Classe Result >-----------------------------------------
	public class Result {

		public int count = 0;
		public int erros = 0;
		public String msg = "";
		public boolean success = true;
	}
}
