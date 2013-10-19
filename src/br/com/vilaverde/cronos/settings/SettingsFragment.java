package br.com.vilaverde.cronos.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import br.com.vilaverde.cronos.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

}

