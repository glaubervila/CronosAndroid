package br.com.vilaverde.cronos.view.produtos;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.DepartamentosHelper;
import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.model.Departamento;
import br.com.vilaverde.cronos.model.Produto;

public class ProdutosFragment extends Fragment {
    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    
    
	private static String CNT_LOG = "ListaProdutos";
	private View view = null;
	private GridView gridView = null;
	private ProdutosImageAdapter adapter = null;
	
	private ArrayList<Produto> lstProdutos = null;
	
	private ProdutosHelper produtosHelper = null;
	
	private String path = "%Produtos%";
    private String image_default = "%Produtos/000000%";
    private long image_default_id = 0;
    private Bitmap image_default_bitmap = null;
	
	
//	int total = 0;
//	long ids[];
//	Uri[] urls = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }      

        // Crio a view e a GridView
        view = inflater.inflate(R.layout.lista_produtos_gridview,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.lista_produtos_gridview);
        
        // Crio o Helper Produtos
		produtosHelper = new ProdutosHelper(this.getActivity().getBaseContext()) ;

    
//		Cursor cursor = view.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//    		null, android.provider.MediaStore.Images.Media.DATA + " like ?", 
//    		new String[] {image_default},
//    		null);
//		cursor.moveToFirst();
//		
//		Log.v(CNT_LOG,"DefaultImageID ="+cursor.getString(cursor.getColumnIndex("_id")) );
//		image_default_id = cursor.getLong(cursor.getColumnIndex("_id"));
		
		// Setando o Bitmap da imagem default      			
//		image_default_bitmap = MediaStore.Images.Thumbnails.getThumbnail(
//                    view.getContext().getContentResolver(), image_default_id,
//                    MediaStore.Images.Thumbnails.MINI_KIND, null);
		
		image_default_bitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera);

		
//		cursor.close();
		
		// Executar a query
		lstProdutos = (ArrayList<Produto>) produtosHelper.ListProdutosByDepartamentos(0);
		
        // Instanciar o Adapter e passar a listProdutos
	 	adapter = new ProdutosImageAdapter(view.getContext(), lstProdutos); // uses the view to get the context instead of getActivity().
        
        // Adicionar o Adapter a ListView       
	 	gridView.setAdapter(adapter);    
        
	 	gridView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
        		
    			Produto produto = lstProdutos.get(position);
        		
    			
        		Log.v(CNT_LOG,"onItemClick");
        		
				Intent intent = new Intent(view.getContext(), ProdutosDetalhe.class);
				intent.putExtra("position", position);
				intent.putExtra("codigo", produto.getCodigo());
				intent.putExtra("image_path", produto.getImage_path());
				intent.putExtra("produto",produto);
				intent.putExtra("lstProdutos",lstProdutos);
				startActivity(intent);
				
			}
        });
	 	
	 	
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {
    	// Ao Escolher um departamento carregar as imagens
        mCurrentPosition = position;
        this.getProdutos(position);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
        
    
    public void getProdutos(int position){
    	Log.v(CNT_LOG, "Departamento Id = "+position);
    	
        DepartamentosHelper helper = new DepartamentosHelper(getActivity());
		// Recuperar o Array de Departamentos no DB
        List<Departamento> lstDepartamentos = helper.getDepartamentos();
    	Departamento departamento = lstDepartamentos.get(position);
    	
    	// Limpar a lista de produtos
    	lstProdutos.clear();

    	// Pesquisar pelo departamento e adicionar a list
    	int departamento_id = departamento.getId();
		lstProdutos.addAll(produtosHelper.ListProdutosByDepartamentos(departamento_id));

		//método responsável pela atualiza da lista de dados na tela
		adapter.notifyDataSetChanged();
    }
    
    // ------------- Image Adapter ---------------------
    
	// Classe ImageAdapter
	public class ProdutosImageAdapter extends BaseAdapter {

		// Guarda o Context da Activity
		private Context context;

		// Classe utilizada para instanciar is objetos  do xml
		private LayoutInflater inflater;
		
		// List a ser carregada no grid
		private List<Produto> listProdutos;
		
		// Guarda a posicao de todas as imageView carregadas 
		private HashMap<Integer, ImageView> imageViews;
		
		public ProdutosImageAdapter(Context context, List<Produto> plistProdutos) {
	        super();
	        // Setando o context
	        this.context = context;
	        
	        // Setando o Inflater
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        
	        // Setando o list de produtos
	        this.listProdutos = plistProdutos;
	        
	        // instanciando o hash map para imageview
	        imageViews = new HashMap<Integer, ImageView>();
	    }
		
		public void addItem(final Produto item) {
	        this.listProdutos.add(item);
	        //Atualizar a lista caso seja adicionado algum item
	        notifyDataSetChanged();
	    }    

		
		@Override
		public int getCount() {
			// Retorna a quantidade total de registros no cursor
			return (int) this.listProdutos.size();
		}

		@Override
		public Object getItem(int position) {
			return listProdutos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// Classe recebe os objetos que serao retornados para o convertView
		public  class ViewHolder {
			public ImageView imageViewProduto;
			public TextView  txtViewDescricao;
			public TextView  txtViewPreco;
			public TextView  txtViewPosition;
		}
		
		
		public View getView(int position, View convertView, ViewGroup parent) {

	        try
	        {	          			
		       //Pega o registro da lista e trasnfere para o objeto 
		       Produto produto = listProdutos.get(position);
		        
		       //O ViewHolder irá guardar a instâncias dos objetos do estado_row
		       ViewHolder viewHolder;
	           
	           //Quando o objeto convertView não for nulo nós não precisaremos inflar
	           //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
	           if (convertView == null) {

	   	        	//Utiliza o XML _row para apresentar na tela
	        	   	convertView = inflater.inflate(R.layout.lista_produtos_gridview_row, null);

	   	        	//Cria o Viewholder e guarda a instância dos objetos
	   	        	viewHolder = new ViewHolder();

					// Setando no Holder o objeto imageview que esta no layout row
		            viewHolder.imageViewProduto = (ImageView) convertView.findViewById(R.id.lista_produtos_imageViewProdutos);
	   	        	
					// Setando no Holder o objeto textview que esta no layout row
					viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.lista_produtos_textViewDescricao);
					viewHolder.txtViewPreco = (TextView) convertView.findViewById(R.id.lista_produtos_textViewPreco);

					// Debug mostrar a posicao do item
					viewHolder.txtViewPosition = (TextView) convertView.findViewById(R.id.lista_produtos_textViewPosition);
					
					
	   	        	convertView.setTag(viewHolder);
	   	        	
	           } else {
	               //pega o ViewHolder para ter um acesso rápido aos objetos do XML
	               //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
	        	   viewHolder = (ViewHolder) convertView.getTag();
	           }
	           
 	           viewHolder.txtViewDescricao.setText(produto.getDescricao_curta());
	           viewHolder.txtViewPreco.setText("R$ "+ produto.getStrPreco());
	           
	           // Mostrar a Position de uma Imagem
	           //viewHolder.txtViewPosition.setText(""+position);
	           		
	           // Guardo a ImageView e a sua posicao para usar quando a imagem estiver carregada
	           imageViews.put(position, viewHolder.imageViewProduto);
	                    			           		
	           // Criando o Objeto Bundle com os parametros
	           Bundle bundle = new Bundle ();
	           bundle.putInt("id", produto.getId());
	           bundle.putInt("codigo", produto.getCodigo());
	           bundle.putInt("position", position);
	           bundle.putString("path", produto.getImage_path());
	           
	           long image_id = produto.getImage_id();
	           
	           if (image_id > 0) {
	        	   bundle.putLong("image_id", produto.getImage_id());
	        	   //Log.v(CNT_LOG,"DEBUG: "+produto.getImage_id());
	           }
	           else {	        	
	           		String path = produto.getImage_path();	        		
	           		Log.v(CNT_LOG,"Path: "+path);
	           		
    				image_id = produtosHelper.markImageUpdate(produto.getId(), path);
    				if (image_id > 0){
	       		        produto.setImage_id(image_id);        
		       		    bundle.putLong("image_id", image_id);
    				}
	       		    else {
	       		    	Log.w(CNT_LOG,"Nao encontrou no mediaStore");
	       		    }
	           		
//	           		Cursor c = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
//		                				new String[] { MediaStore.MediaColumns._ID },
//		                				MediaStore.MediaColumns.DATA + "=?", 
//		                				new String[] {path},
//		                				null);
//	           		
//	       		    if (c != null && c.moveToFirst()) {
//	       		        int id = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
//	       		        c.close();   
//	       		    }
//	       		    else {
//	       		    	Log.w(CNT_LOG,"Nao encontrou no mediaStore");
//	       		    }
//	       		    c.close();
	           }
	           
	           // Carregando a Imagem
	           new LoadImage().execute(bundle);
	           			
	           return convertView;           
	            
	       }catch (Exception e) {
	    	   	if (e.getMessage() != null){
	    	   		Log.e(CNT_LOG, "Erro getView - [ "+ e.getMessage()+ " ]");
	    	   	}
	       }
	       return convertView;
	                
		}
	
		private class LoadImage extends AsyncTask<Bundle, Void, Bundle> {

    		@Override
    		protected Bundle doInBackground(Bundle... bundle) {
    			//Log.w("LoadImage", "doInBackground");

    			// Recuperando os Parametros do Bundle
    			int id = bundle[0].getInt("id");
    			int position = bundle[0].getInt("position");
    			int codigo = bundle[0].getInt("codigo");
    			long image_id = bundle[0].getLong("image_id");
    			String path = bundle[0].getString("path");

    			//Log.v("LoadImage", "Carregando Codigo = "+codigo+" Position: "+position+" ID: " +image_id);
		
    			// Usando o Metodo fetchThumb passando o id da imagem original e recebendo o Thumbnail
    			Bitmap bitmap = fetchThumb(image_id);		         
    			
    			if (bitmap == null){
    				//Log.v(CNT_LOG, "Nao encrontrou image Id");
    				//Log.v(CNT_LOG,"Path: "+path);
	           	    				
    				// marcar a imagem para ser verificada
    				image_id = produtosHelper.markImageUpdate(id, path);
    				if (image_id > 0){
    					bitmap = fetchThumb(image_id);
    				}
    			}
    			
    			
    			// Crio outro Bundle agora acrescentando o bitmap
    			Bundle result = new Bundle();
    			result.putInt("position", position);
    			result.putInt("codigo", codigo);
    			result.putLong("image_id", image_id);
   				result.putParcelable("bitmap", bitmap);

    			// Retorno o bundle que agora vai para o metodo onPostExecute
    			return result;

    		}
    		
    	  					
			protected void onPostExecute(Bundle result) {
    			super.onPostExecute(result);
    			//Log.w("LoadImage", "onPostExecute");
    			// No Metodo onPostExecute verifico se retornou o bitmap
    			
    			//Log.v("LoadImage", "Thumbnail Position: "+result.getInt("position")+" ID: " +result.getLong("image_id")+ result.getParcelable("bitmap"));

    			// Recupero o imageView do array imageViews na posicao da imagem 
    			ImageView imageView = imageViews.get(result.getInt("position"));

    			
    			// Testando se a imageView tem bitmap
//    			Drawable drawable = (imageView.getDrawable());
//    			
//    			if (drawable == null){
//    				Log.e("LoadImage", "Imagem nao Carregada");    			
//    			}
//    			else {
//    				Log.w("LoadImage", "Imagem Carregada");
//    			
//    			}
    			
    			// Se tiver o Thumbnail seta a imagem no imageView
    			if (result.getParcelable("bitmap") != null){

    				// Coloco o bitmap na imageView 
	    			imageView.setImageBitmap((Bitmap) result.getParcelable("bitmap"));

    			}
    			else {
        			// Se nao tiver bitmap logar para verificar
    				//Log.e("LoadImage", "Imagem nao Carregada");
	    			imageView.setImageBitmap((Bitmap) image_default_bitmap);
	    			imageView.setScaleType(ScaleType.CENTER);
    			}
    		}
    		
    		
    		// Este Metodo recebe o id da imagem e retorna um bitmap em tamanho thumbnail
        	private Bitmap fetchThumb (long id) {
        		
        		Bitmap bitmap = null;
            			   
        		try {
                			
        			bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                            view.getContext().getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND, null);
        			
        		}
        		catch (Exception e) {
        			Log.e("fetchThumb", e.getMessage(), e);
        		}
         
    			return bitmap;
        	}

		}

	}    
}
