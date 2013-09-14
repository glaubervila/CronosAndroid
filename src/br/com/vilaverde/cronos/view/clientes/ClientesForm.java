package br.com.vilaverde.cronos.view.clientes;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import br.com.vilaverde.cronos.R;

import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Estado;

public class ClientesForm extends Activity {

	private static final int INCLUIR = 0;
	private static final int ALTERAR = 1;
	private static final int EXCLUIR = 2;

	
	private String CNT_LOG = "ClientesForm";
	
	Cliente cliente = new Cliente();
	private ClienteHelper helper;   //instância responsável pela persistência dos dados
	
	// Declaracao dos campos
	EditText etNome, etCpfCnpj, etRgIe, etTelFixo, etTelMovel, etEmail, etLogradouro, etNumero, etBairro, etCidade, etCep, etObservacao;
	Spinner  spUf;
	RadioButton rbPessoaFisica;
	RadioButton rbPessoaJuridica;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes_form);

    	// Instanciar o Helper 
        helper = new ClienteHelper(this);
		
		try {
            
			// Recupero os parametro passados pelo Bunble da Intent
            final Bundle data = (Bundle) getIntent().getExtras();
        
            int tipo = data.getInt("tipo");
            
            if (tipo == INCLUIR)
            {
   	         	Log.v(CNT_LOG, "Incluir");
                //quando for incluir um contato criamos uma nova instância
                cliente = new Cliente();
                
            }else{
            	Log.v(CNT_LOG, "Alterar");
                //quando for alterar o contato carregamos a classe que veio por Bundle 
                cliente = (Cliente)data.getSerializable("cliente");
            }
            

            // Criacao dos campos
            etNome     = (EditText)findViewById(R.id.cliente_et_nome);
            etCpfCnpj  = (EditText)findViewById(R.id.cliente_et_cpfcnpj);
            etRgIe     = (EditText)findViewById(R.id.cliente_et_rgie);
            etTelFixo  = (EditText)findViewById(R.id.cliente_et_telefone_fixo);
            etTelMovel = (EditText)findViewById(R.id.cliente_et_telefone_movel);
            etEmail    = (EditText)findViewById(R.id.cliente_et_email);
            etLogradouro = (EditText)findViewById(R.id.cliente_et_logradouro);
            etNumero    = (EditText)findViewById(R.id.cliente_et_numero);
            etBairro    = (EditText)findViewById(R.id.cliente_et_bairro);
            etCidade    = (EditText)findViewById(R.id.cliente_et_cidade);
            spUf        = (Spinner) findViewById(R.id.clientes_sp_uf);
            etCep       = (EditText)findViewById(R.id.cliente_et_cep);
            etObservacao= (EditText)findViewById(R.id.cliente_et_observacao);
            
            rbPessoaFisica  = (RadioButton) findViewById(R.id.cliente_rb_pessoa_fisica);
            rbPessoaJuridica  = (RadioButton) findViewById(R.id.cliente_rb_pessoa_juridica);
            
                       
            // Setando os Valores
            etNome.setText(cliente.getNome());
            etTelFixo.setText(cliente.getTelefoneFixo());
            etTelMovel.setText(cliente.getTelefoneMovel());
            etEmail.setText(cliente.getEmail());
            etLogradouro.setText(cliente.getRua());
            etNumero.setText(cliente.getNumero());
            etBairro.setText(cliente.getBairro());
            etCidade.setText(cliente.getCidade());
            etCep.setText(cliente.getCep());
            etObservacao.setText(cliente.getObservacao());
            
            // Verifico o tipo 
            if (cliente.getTipo() == 1){
            	// Pessoa Fisica
            	// Radio Button Tipo
            	rbPessoaFisica.setChecked(true);
            	// Setando o Cpf
                etCpfCnpj.setText(cliente.getCpf());
                // Setando o Rg
                etRgIe.setText(cliente.getRg());
            }
            else {
            	// Pessoa Juridica
            	// Radio Button Tipo
            	rbPessoaJuridica.setChecked(true);
            	// Setando o Cnpj
                etCpfCnpj.setText(cliente.getCnpj());
                // Setando a Inscricao Estadual
                etRgIe.setText(cliente.getInscricao_estadual());
            	
            }

            
            // Setando  Uf
            // aqui criamos um array adapter que irá popular o spinner o Estado.getEstados()
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Estado.getEstados());
            // associo o adapter
            spUf.setAdapter(adapter);
            // setando valor padrao para o spinner de estados
            spUf.setSelection(cliente.getUf());

            
		 }catch (Exception e) {
			Log.e(CNT_LOG, "onCreate - Error["+e.getMessage()+"]");
	     }       
		
		Button salvar = (Button) findViewById(R.id.bt_clientes_salvar);
		salvar.setOnClickListener(onSave);

		Button cancelar = (Button) findViewById(R.id.bt_clientes_cancelar);
		cancelar.setOnClickListener(onCancel);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.clientes_form, menu);
		return true;
	}

	private OnClickListener onSave = new OnClickListener() {

		public void onClick(View arg0) {

			save();
		}

	};

	
	
	private OnClickListener onCancel = new OnClickListener() {

		public void onClick(View arg0) {

			cancel();
		}

	};


	public void onDelete() {

		// Perguntar se realmente quer excluir
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Confirme");
	    builder.setMessage("Deseja Realmente Excluir o Registro?");

	    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            // Do nothing but close the dialog

	        	Log.v(CNT_LOG, "onDelete - Clicou no YES");
	        	
	        	delete();
	        	
	            dialog.dismiss();
	        }

	    });

	    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Log.v(CNT_LOG, "onDelete - Clicou no No");
	            // Do nothing
	            dialog.dismiss();
	        }
	    });

	    AlertDialog alert = builder.create();
	    alert.show();

	}

	
	public void save() {
		
		 try
	        {
	            //Quando confirmar a inclusão ou alteração deve-se devolver
	            //o registro com os dados preenchidos na tela e informar
	            //o RESULT_OK e em seguida finalizar a Activity
	                      
	            Intent intent = new Intent();
	            
	            // Seta o Status Servidor para a Enviar
	        	cliente.setStatus_servidor("0");
	    		// Setando os valores na classe model
	    		cliente.setNome(etNome.getText().toString());
	    		cliente.setTelefoneFixo(etTelFixo.getText().toString());
	    		cliente.setTelefoneMovel(etTelMovel.getText().toString());
	    		cliente.setEmail(etEmail.getText().toString());
	    		cliente.setRua(etLogradouro.getText().toString());
	    		cliente.setNumero(etNumero.getText().toString());
	    		cliente.setBairro(etBairro.getText().toString());
	    		cliente.setCidade(etCidade.getText().toString());
	    		cliente.setUf(spUf.getSelectedItemPosition());
	    		cliente.setCep(etCep.getText().toString());
	    		cliente.setObservacao(etObservacao.getText().toString());
	    		
	    		// O campo tipo ja esta setado no evendo de select do radiobutton
	    		// Tratando pelo tipo
	    		if (cliente.getTipo() == 1){
	    			// Pessoa Fisica
	    			// Setando o Cpf
	    			cliente.setCpf(etCpfCnpj.getText().toString());    			
	    			// Setando o RG
	    			cliente.setRg(etRgIe.getText().toString());
	    		}
	    		else {
	    			// Pessoa Juridica
	    			// Setando o CNPJ
	    			cliente.setCnpj(etCpfCnpj.getText().toString());
	    			// Setando a Inscricao Estadual
	    			cliente.setInscricao_estadual(etRgIe.getText().toString());
	    		}
	    		
	    		
	    		// Passando o Objeto Cliente pela Buble Intent
	    		intent.putExtra("cliente", cliente);
	            
	            // Setando o Result como Ok 
	            setResult(Activity.RESULT_OK, intent);
	            
	            finish();
	        }catch (Exception e) {
				Log.e(CNT_LOG, "save - Error["+e.getMessage()+"]");
	        }             
		
		// Recuperando os EditText
//		EditText nome = (EditText) findViewById(R.id.cliente_fr_nome);
//		EditText cpfcnpj = (EditText) findViewById(R.id.cliente_fr_cpf_cnpj);
//		EditText rgie = (EditText) findViewById(R.id.cliente_fr_rg_ie);
//		EditText telefoneFixo = (EditText) findViewById(R.id.cliente_fr_telefone_fixo);
//		EditText telefoneMovel = (EditText) findViewById(R.id.cliente_fr_telefone_movel);

//		// Setando os valores na classe model
//		cliente.setNome(txtNome.getText().toString());
//		cliente.setCpf(txtCpfCnpj.getText().toString());
//		cliente.setRgie(rgie.getText().toString());
//		cliente.setTelefoneFixo(telefoneFixo.getText().toString());
//		cliente.setTelefoneMovel(telefoneMovel.getText().toString());

		
		// Mostrando Um toast so pra testar
//		Toast.makeText(
//				this,
//				"Voce CLicou em Salvar o Cliente "
//						+ cliente.getNome().toString(), Toast.LENGTH_SHORT)
//				.show();
		
		
		// O Registro Pode ser Salvo aki ou na Lista
		// Criando Instancia Helper
		//ClienteHelper helper = new ClienteHelper(ClientesForm.this);
		
		// Aqui seria o teste se e um insert ou um update
		//helper.inserir(cliente);
		

	}

	
	public void cancel(){
		
		try
		{
			//Quando for simplesmente cancelar a operação de inclusão
			//ou alteração deve-se apenas informar o RESULT_CANCELED
			//e em seguida finalizar a Activity
				             
			setResult(Activity.RESULT_CANCELED);
			
			finish();
			
        }catch (Exception e) {
			Log.e(CNT_LOG, "cancel - Error["+e.getMessage()+"]");	    
        }             
		
	}

	
	public void delete(){

		Log.v(CNT_LOG, "delete -  Registro [ "+cliente.getId()+" ]");

		Intent intent = new Intent();

		intent.putExtra("tipo", EXCLUIR);
		// Passando o Objeto Cliente pela Buble Intent
		intent.putExtra("cliente", cliente);

        // Setando o Result como Ok 
        setResult(Activity.RESULT_OK, intent);

			
        finish();
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case R.id.menu_cliente_save:
	
				save();
	
				return true;
				
			case R.id.menu_cliente_discard:
				onDelete();
				
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.cliente_rb_pessoa_fisica:
	            if (checked)
	                // Setando tipo de cliente 
	            	cliente.setTipo(1);
	            break;
	        case R.id.cliente_rb_pessoa_juridica:
	            if (checked)
	            	cliente.setTipo(2);
	            break;
	    }
	}
//	@Override
//	public void onBackPressed() {
//		Log.v("ClientesForm", "Clicou no Botão Voltar");
//		
//		Toast.makeText(this, "Voce Clicou no Botao Voltar Ainda tem que implementar a pergunta se deseja sair", Toast.LENGTH_SHORT).show();
//		
//		
////		  Timer myTimer = new Timer();
////		    myTimer.schedule(new TimerTask() {          
////		        @Override
////		        public void run() {
////		            TimerMethod();
////		        }
////
////		    }, 0, 1000);
//
//		
//		
//		return;
//	}

//    private void TimerMethod()
//    {
//    	Log.v("ClientesForm", "Entrou no Timer ");
//        //This method is called directly by the timer
//        //and runs in the same thread as the timer.
//
//        //We call the method that will work with the UI
//        //through the runOnUiThread method.
//    	//this.runOnUiThread(Timer_Tick);
//    }

	
	
//	ou 
	
	
//    thread=  new Thread(){
//        @Override
//        public void run(){
//            try {
//                synchronized(this){
//                    wait(3000);
//                }
//            }
//            catch(InterruptedException ex){                    
//            }
//
//            // TODO              
//        }
//    };
//
//    thread.start();     
//    
//    
//    @Override
//    public boolean onTouchEvent(MotionEvent evt)
//    {
//        if(evt.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            synchronized(thread){
//                thread.notifyAll();
//            }
//        }
//        return true;
//    }    
	
	
}
