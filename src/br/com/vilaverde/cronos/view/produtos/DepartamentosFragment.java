package br.com.vilaverde.cronos.view.produtos;



import java.util.List;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import br.com.vilaverde.cronos.R;
import br.com.vilaverde.cronos.dao.DepartamentosHelper;
import br.com.vilaverde.cronos.model.Departamento;


public class DepartamentosFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;

	private static String CNT_LOG = "Departamentos";
	private DepartamentosHelper helper;   //instância responsável pela persistência dos dados
	private DepartamentoAdapter adapter; //Adapter responsável por apresentar os clientes na tela
	
	private List<Departamento> lstDepartamentos = null; //lista de departamentos cadastrados no BD
    
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected 
         * @param departamento_id 
         * @param departamento */
        public void onArticleSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

    	// Instanciar o Helper 
        helper = new DepartamentosHelper(this.getActivity());
        
        // Recuperar o Array de Departamentos no DB
        lstDepartamentos = helper.getDepartamentos();
        Log.v(CNT_LOG, "CHEGOU AKI");
        // Instanciar o Adapter e passar o array clientes
        adapter = new DepartamentoAdapter(this.getActivity(), lstDepartamentos);
        
        setListAdapter(adapter);
        
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
    	Departamento departamento = lstDepartamentos.get(position);
    	int departamento_id = departamento.getId();
    	
		Log.v(CNT_LOG, "Position "+position+" DepartamentoID["+departamento_id+"]");
		
        mCallback.onArticleSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}