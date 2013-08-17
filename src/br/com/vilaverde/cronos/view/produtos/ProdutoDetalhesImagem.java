package br.com.vilaverde.cronos.view.produtos;



import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProdutoDetalhesImagem extends Fragment {

	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		 	View view = inflater.inflate(R.layout.produtos_detalhe_imagem, container, false);
		    return view;		 
	 }
}
