package br.com.vilaverde.cronos;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import br.com.vilaverde.cronos.dao.DepartamentosHelper;
import br.com.vilaverde.cronos.dao.ProdutosHelper;
import br.com.vilaverde.cronos.dummy.DummyContent;
import br.com.vilaverde.cronos.model.Departamento;
import br.com.vilaverde.cronos.model.Produto;
import br.com.vilaverde.cronos.view.produtos.ProdutosDetalhe;

/**
 * A fragment representing a single Produto detail screen. This fragment is
 * either contained in a {@link ProdutoListActivity} in two-pane mode (on
 * tablets) or a {@link ProdutoDetailActivity} on handsets.
 */
public class ProdutoDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	//private DummyContent.DummyItem mItem;

	DepartamentosHelper helperDepartamento = null;
	
	Departamento departamento = null;
	
	ProdutosHelper helperProdutos = null;
	
	List<Produto> produtos = null;

	private GridView gridView;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProdutoDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
//			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
//					ARG_ITEM_ID));
			
			String argItemId = getArguments().getString(ARG_ITEM_ID);
		    Log.v("ProdutoDetail","Departamento ID: "+ argItemId);
			
//	        helperDepartamento = new DepartamentosHelper(this.getActivity());
//	        departamento = helperDepartamento.getDepartamento(Integer.parseInt(argItemId));
		    Log.v("ProdutoFragment", "-----------------------------------------");
		    helperProdutos = new ProdutosHelper(this.getActivity());
			produtos = helperProdutos.ListProdutosByDepartamentos(Integer.parseInt(argItemId));
		    
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_produto_detail,
					container, false);
			
		if (produtos != null){
			Log.v("ProdutosDetail", "TEM PRODUTOS: "+produtos.size() );
		}
		
		gridView = (GridView) rootView.findViewById(R.id.produtos_gridview);			
		ProdutoDetailAdapter adapter = new ProdutoDetailAdapter(this.getActivity(), produtos);
		gridView.setAdapter(adapter);
		
	 	gridView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
        		
    			Produto produto = produtos.get(position);
        		
    			ArrayList<Produto> aProdutos = (ArrayList<Produto>) produtos;
    			
				Intent intent = new Intent(view.getContext(), ProdutosDetalhe.class);
				intent.putExtra("position", position);
				intent.putExtra("codigo", produto.getCodigo());
				intent.putExtra("image_path", produto.getImage_path());
				intent.putExtra("produto",produto);
				intent.putExtra("lstProdutos", aProdutos);
				startActivity(intent);
				
			}
        });
		
		return rootView;
	}
}
