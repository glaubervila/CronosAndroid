package br.com.vilaverde.cronos.view.clientes;



import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
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
import br.com.vilaverde.cronos.Mask;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.ValidarDocumento;
import br.com.vilaverde.cronos.dao.ClienteHelper;
import br.com.vilaverde.cronos.model.Cliente;
import br.com.vilaverde.cronos.model.Estado;
import br.com.vilaverde.cronos.model.Produto;

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
	
	ArrayList<EditText> obrigatorios;

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
            	// Mascara
            	// etCpfCnpj.addTextChangedListener(Mask.insert("###.###.###-##", etCpfCnpj));
            	
            	
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

            
          // Setando Campos Obrigatorios       
          obrigatorios = new ArrayList<EditText>();
          obrigatorios.add(etNome);
          obrigatorios.add(etCpfCnpj);
          obrigatorios.add(etRgIe);
          obrigatorios.add(etLogradouro);
          obrigatorios.add(etBairro);
            
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
			 	boolean flag = true; 
	            Intent intent = new Intent();
	            
	            // Validar os campos obrigatorios
//	            EditText primeiroInvalido = null;
//	            for (int i = 0; i < obrigatorios.size() ; i++) {
//					EditText campo = obrigatorios.get(i);
//					if (!validarCampoObrigatorio(campo)){
//						flag = false;
//						if (primeiroInvalido == null){
//							primeiroInvalido = campo;
//						}
//						//break; 
//					}
//				}
//	            if (primeiroInvalido != null){
//	            	primeiroInvalido.requestFocus();
//	            }

	            // Validar os campos obrigatorios
	            for (int i = 0; i < obrigatorios.size() ; i++) {
					EditText campo = obrigatorios.get(i);
					if (!validarCampoObrigatorio(campo)){
						flag = false;
						break; 
					}
				}
	            
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
	    			// validar o cpf
	    			if (ValidarDocumento.check(etCpfCnpj.getText().toString())){
		    			// Setando o Cpf
	    				cliente.setCpf(etCpfCnpj.getText().toString());
	    			}
	    			else {
	    				Log.v(CNT_LOG, "CPF INVALIDO");
	    				flag = false;	    				
	    				// retorna mensagem de erro
	    				etCpfCnpj.setError("CPF Inválido!");
	    				etCpfCnpj.requestFocus();
	    			}
	    			
	    			// Setando o RG
	    			cliente.setRg(etRgIe.getText().toString());
	    		}
	    		else {
	    			// Pessoa Juridica
	    			// Setando o CNPJ
	    			if (ValidarDocumento.check(etCpfCnpj.getText().toString())){
		    			cliente.setCnpj(etCpfCnpj.getText().toString());	    				
	    			}
	    			else {
	    				Log.v(CNT_LOG, "CNPJ INVALIDO");
	    				flag = false;
	    				// retorna mensagem de erro
	    				etCpfCnpj.setError("CNPJ Inválido!");
	    				etCpfCnpj.requestFocus();
	    			}

	    			// Setando a Inscricao Estadual
	    			cliente.setInscricao_estadual(etRgIe.getText().toString());
	    		}
	    		
	    		if (flag){
		    		// Passando o Objeto Cliente pela Buble Intent
		    		intent.putExtra("cliente", cliente);
		            
		            // Setando o Result como Ok 
		            setResult(Activity.RESULT_OK, intent);
		            
		            finish();
	    		}

	        }catch (Exception e) {
				Log.e(CNT_LOG, "save - Error["+e.getMessage()+"]");
	        }             
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
	
	public boolean validarCampoObrigatorio(EditText editText) {
        boolean retorno = true;
       
        if (editText.getText().toString().equals("")) {
            editText.setError("Campo Obrigatório!");
            editText.requestFocus();
            retorno = false;
        }
                 
        return retorno;
    }
}
