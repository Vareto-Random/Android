package outlook;
 
import com.example.shoppinglist.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class SettingsActivity extends PreferenceActivity {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.pref_settings_menu);
 
    }
}