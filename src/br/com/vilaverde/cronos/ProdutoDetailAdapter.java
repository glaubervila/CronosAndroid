package br.com.vilaverde.cronos;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.List;

import br.com.vilaverde.cronos.model.Produto;
import br.com.vilaverde.cronos.view.produtos.ProdutosFragment.ProdutosImageAdapter.ViewHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProdutoDetailAdapter extends BaseAdapter {

	// Guarda o Context da Activity
	private Context context;

	// List a ser carregada no grid
	private List<Produto> produtos;
	
	// Classe utilizada para instanciar is objetos  do xml
	private LayoutInflater inflater;

	private Bitmap mPlaceHolderBitmap;

	
	public ProdutoDetailAdapter(Context context, List<Produto> produtos) {
		super();
	
		this.context = context;
		this.produtos = produtos;
			
        // Setando o Inflater
        inflater = LayoutInflater.from(context);
		
        // Setar PlaceHolder
        mPlaceHolderBitmap = null;
        //mPlaceHolderBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_camera);
        
	}
	
	@Override
	public int getCount() {
		return this.produtos.size();
	}

	@Override
	public Produto getItem(int arg0) {
		
		return this.produtos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return this.produtos.get(arg0).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		
       	//Cria o Viewholder e guarda a instância dos objetos
		ViewHolder holder = null;
		
		if (row == null){
			
			//Utiliza o XML _row para apresentar na tela
			row = inflater.inflate(R.layout.lista_produtos_gridview_row, null);
			
        	//Cria o Viewholder e guarda a instância dos objetos
        	holder = new ViewHolder();

			// Setando no Holder o objeto imageview que esta no layout row
            holder.imageViewProduto = (ImageView) row.findViewById(R.id.lista_produtos_imageViewProdutos);
            
			// Setando no Holder o objeto textview que esta no layout row
            holder.txtViewDescricao = (TextView) row.findViewById(R.id.lista_produtos_textViewDescricao);
            
            holder.txtViewPreco = (TextView) row.findViewById(R.id.lista_produtos_textViewPreco);

			// Debug mostrar a posicao do item
            holder.txtViewPosition = (TextView) row.findViewById(R.id.lista_produtos_textViewPosition);
            row.setTag(holder);
		}
		else {
            //pega o ViewHolder para ter um acesso rápido aos objetos do XML
            //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela
			holder = (ViewHolder) row.getTag();
		}
		
		Produto produto = this.getItem(position);
		
        holder.txtViewDescricao.setText(produto.getDescricao_curta());
          
        String strPreco = NumberFormat.getCurrencyInstance().format(produto.getPreco());
        holder.txtViewPreco.setText(strPreco);
        
        // Mostrar a Position de uma Imagem
        //holder.txtViewPosition.setText(""+position);		

        // TODO: USAR ESSE EXEMPLO PARA CARREGAR AS IMAGENS
        //http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
               
		// Ver se o Produto tem Image Id
        Integer image_id = Integer.parseInt(""+produto.getImage_id());
        
        if (image_id == 0){
        	Log.e("ProdutoAdapter", "NAO TEM IMAGE ID");        
    	}

        
        if (holder.imageViewProduto != null){
        	
        	loadBitmap(image_id, holder.imageViewProduto);
        }
        
		return row;
	}

	public void loadBitmap(Integer image_id, ImageView imageView) {

		if (cancelPotentialWork(image_id, imageView)) {
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(image_id);
	    }
	}
	
	public static boolean cancelPotentialWork(int data, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final int bitmapData = bitmapWorkerTask.data;
	        
	        // If bitmapData is not yet set or it differs from the new data
	        if (bitmapData == 0 || bitmapData != data) {
	            // Cancel previous task        	 
	            bitmapWorkerTask.cancel(true);
	            
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
	   if (imageView != null) {
	       final Drawable drawable = imageView.getDrawable();
	       if (drawable instanceof AsyncDrawable) {
	           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
	           return asyncDrawable.getBitmapWorkerTask();
	       }
	    }
	    return null;
	}			
	
	private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		
		private final WeakReference<ImageView> imageViewReference;		
		private int data = 0;
		
		public BitmapWorkerTask(ImageView imageViewProduto) {
			imageViewReference = new WeakReference<ImageView>(imageViewProduto);
		}
		
		protected Bitmap doInBackground(Integer... params) {

			// Recupera o bundle
			data = params[0];
					
			// Usando o Metodo fetchThumb passando o id da imagem original e recebendo o Thumbnail
			Bitmap bitmap = fetchThumb(data);		         
		
			return bitmap;	
		}

	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	            bitmap = null;
	        }

	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            final BitmapWorkerTask bitmapWorkerTask =
	                    getBitmapWorkerTask(imageView);
	            if (this == bitmapWorkerTask && imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
		
		// Este Metodo recebe o id da imagem e retorna um bitmap em tamanho thumbnail
    	private Bitmap fetchThumb (long id) {
    		
    		Bitmap bitmap = null;        			   
    		try {
    			bitmap = MediaStore.Images.Thumbnails.getThumbnail(
    					context.getContentResolver(), 
    					id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
    		}
    		catch (Exception e) {
    			Log.e("fetchThumb", e.getMessage(), e);
    		}
     
			return bitmap;
    	}
	}
	
	
	static class AsyncDrawable extends BitmapDrawable {
	    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

	    public AsyncDrawable(Resources res, Bitmap bitmap,
	            BitmapWorkerTask bitmapWorkerTask) {
	        super(res, bitmap);
	        bitmapWorkerTaskReference =
	            new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	    }

	    public BitmapWorkerTask getBitmapWorkerTask() {
	        return bitmapWorkerTaskReference.get();
	    }
	
	}
	
	// Classe recebe os objetos que serao retornados para o convertView
	public  class ViewHolder {
		public ImageView imageViewProduto;
		public TextView  txtViewDescricao;
		public TextView  txtViewPreco;
		public TextView  txtViewPosition;
	}
	
}
