package br.com.vilaverde.cronos.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.vilaverde.cronos.R;

public class ProdutosFragment  extends Fragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	
	   @Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	        if (savedInstanceState != null) {
	            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
	        }

	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.produtos_grid_view, container, false);
	      
	    }
	
}
