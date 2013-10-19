package br.com.vilaverde.cronos.view;
import android.app.ListFragment;
import android.os.Bundle;

public class DepartamentosFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.
        
        // We need to use a different list item layout for devices older than Honeycomb
        //int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                //android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
    	
        // Create an array adapter for the list view, using the Ipsum headlines array
        //setListAdapter(new ArrayAdapter<String>(getActivity(), layout, DepartamentosHelper.getDepartamentos()));
        
        //setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, DepartamentosHelper.getDepartamentos()));
        //setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.departamentos_fragment, DepartamentosHelper.getDepartamentos()));
        
    }

	
}
