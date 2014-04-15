package com.lorol.rss.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.lorol.rss.R;

/*
 * This class is the activity which contains the preferences
 */
@SuppressWarnings("deprecation")
public class rssSettings extends PreferenceActivity {

    /*
     * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.ic_dashclock);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /*
     * A preference value change listener that updates the preference's summary 
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference prePreference, Object objValue) {

        	if (prePreference instanceof CheckBoxPreference) { 
                // Display default summary
            
            } else {

            	prePreference.setSummary(objValue.toString());

            }

            return true;

        }

    };

    /*
     * Binds a preference's summary to its value. More specifically, when the 
     * preference's value is changed, its summary is updated to reflect the value.
     */
    private static void bindPreferenceSummaryToValue(Preference prePreference) {

        prePreference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        
        if (prePreference instanceof CheckBoxPreference) { 
            sBindPreferenceSummaryToValueListener.onPreferenceChange(prePreference,
                    PreferenceManager
                    .getDefaultSharedPreferences(prePreference.getContext())
            		.getBoolean(prePreference.getKey(), false));
        
        } else {
        
        	sBindPreferenceSummaryToValueListener.onPreferenceChange(prePreference,
        			PreferenceManager
        			.getDefaultSharedPreferences(prePreference.getContext())
        			.getString(prePreference.getKey(), ""));
        }
    }
    
    /*
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference("scron"));
        bindPreferenceSummaryToValue(findPreference("showon"));
        bindPreferenceSummaryToValue(findPreference("pars"));
        bindPreferenceSummaryToValue(findPreference("rsstitle"));
        bindPreferenceSummaryToValue(findPreference("rssurl"));
        bindPreferenceSummaryToValue(findPreference("pattern"));
        bindPreferenceSummaryToValue(findPreference("toms"));
        bindPreferenceSummaryToValue(findPreference("rvr"));
        bindPreferenceSummaryToValue(findPreference("nums"));
        bindPreferenceSummaryToValue(findPreference("show_indexes"));
    }

}