package platzerworld.kegeln.gui.einstellung;

import java.util.ArrayList;
import java.util.List;

import platzerworld.kegeln.R;
import platzerworld.kegeln.klasse.db.KlasseSpeicher;
import platzerworld.kegeln.klasse.vo.KlasseVO;
import platzerworld.kegeln.mannschaft.db.MannschaftSpeicher;
import platzerworld.kegeln.mannschaft.vo.MannschaftVO;
import platzerworld.kegeln.spieler.db.SpielerSpeicher;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class EinstellungenBearbeiten extends PreferenceActivity {
	private KlasseSpeicher mKlasseSpeicher;
	private ArrayList<KlasseVO> alleKlassenVO;
	private String[] klasseKeyArray;
	private String[] klasseValueArray;
	
	private MannschaftSpeicher mMannschaftSpeicher;
	private ArrayList<MannschaftVO> alleMannschaftenVO;
	String[] mannschaftKeyArray;
	private String[] mannschaftValueArray;
	
	private ListPreference mannschaftListPreference;
	
	private static final int EINSTELLUNG_BEARBEITEN_ID = Menu.FIRST;
	private static final int ZURUECK_ID = Menu.FIRST + 1;
	private static final int AMANDO_BEENDEN_ID = Menu.FIRST + 2;
	 public static final String EINSTELLUNGEN_NAME = EinstellungenBearbeiten.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.einstellungen_bearbeiten);
		
		mKlasseSpeicher = new KlasseSpeicher(this);
		mMannschaftSpeicher = new MannschaftSpeicher(this);
		
		alleKlassenVO = (ArrayList<KlasseVO>) holeKlassen();
		klasseKeyArray = new String[alleKlassenVO.size()];
		klasseValueArray = new String[alleKlassenVO.size()];
		
		for(int i = 0; i < klasseKeyArray.length; i++){
			klasseKeyArray[i] = String.valueOf(alleKlassenVO.get(i).key);
			klasseValueArray[i] = alleKlassenVO.get(i).value;
		}
		
		
		
		ListPreference klasseListPreference = (ListPreference) findPreference("LIST_KLASSE_PREF");
		klasseListPreference.setOnPreferenceChangeListener(mKlassePreferenceChangedListener);
		klasseListPreference.setEntries(klasseValueArray);
		klasseListPreference.setEntryValues(klasseKeyArray);
		
		
		alleMannschaftenVO = (ArrayList<MannschaftVO>) holeMannschaften(1);
		mannschaftKeyArray = new String[alleMannschaftenVO.size()];
		mannschaftValueArray = new String[alleMannschaftenVO.size()];
		
		for(int i = 0; i < alleMannschaftenVO.size(); i++){
			mannschaftKeyArray[i] = String.valueOf(alleMannschaftenVO.get(i).key);
			mannschaftValueArray[i] = alleMannschaftenVO.get(i).value;
		}
		
		
		mannschaftListPreference = (ListPreference) findPreference("LIST_VEREIN_PREF");
		mannschaftListPreference.setOnPreferenceChangeListener(mMannschaftPreferenceChangedListener);
		mannschaftListPreference.setEntries(mannschaftValueArray);
		mannschaftListPreference.setEntryValues(mannschaftKeyArray);		
		
		
		
		CheckBoxPreference herrenCheckBoxPreference = (CheckBoxPreference) findPreference("CHECK_HERREN_PREF");
		herrenCheckBoxPreference.setChecked(true);
		
		EditTextPreference spielerEditTextPreference = (EditTextPreference) findPreference("EDIT_SPIELERNAME_PREF");
		spielerEditTextPreference.setDefaultValue("GPL");
		
		EditTextPreference vereinEditTextPreference = (EditTextPreference) findPreference("EDIT_VEREINSNAME_PREF");
		vereinEditTextPreference.setDefaultValue("KC-Ismaning");
		
	}
	
	@Override
	protected void onPause() {
		if(null != mKlasseSpeicher){
			mKlasseSpeicher.schliessen();
		}
		if(null != mMannschaftSpeicher){
			mMannschaftSpeicher.schliessen();
		}
		super.onPause();
	}
	
	
	@Override
	protected void onStart() {
		if(null == mKlasseSpeicher){
			mKlasseSpeicher = new KlasseSpeicher(this);
		}
		if(null == mMannschaftSpeicher){
			mMannschaftSpeicher = new MannschaftSpeicher(this);
		}
		super.onStart();
	}


	/**
	 * Bis Android 1.6: Listener für Klick-Event auf Schaltfläche 'Karte
	 * Anzeigen'.
	 */
	private final OnPreferenceClickListener mKlassePreferenceClickListener = new OnPreferenceClickListener() {
		public boolean onPreferenceClick(Preference preference) {

			Toast.makeText(EinstellungenBearbeiten.this, "mKlassePreferenceClickListener" +preference.getKey(), Toast.LENGTH_SHORT).show();
			
			return true;
		}
	};
	
	private final OnPreferenceChangeListener m2KlassePreferenceChangedListener = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			ListPreference pref = (ListPreference) preference;
			int idxKlasse = pref.findIndexOfValue((String)newValue);
			Toast.makeText(EinstellungenBearbeiten.this, "mKlassePreferenceChangedListener: " +newValue +" Index Klasse: " +idxKlasse, Toast.LENGTH_SHORT).show();
			refreshMannschaften(idxKlasse+1);
			return true;
		}
	};
	
	/**
	 * Bis Android 1.6: Listener für Klick-Event auf Schaltfläche 'Karte
	 * Anzeigen'.
	 */
	private final OnPreferenceClickListener mMannschaftPreferenceClickListener = new OnPreferenceClickListener() {
		public boolean onPreferenceClick(Preference preference) {

			Toast.makeText(EinstellungenBearbeiten.this, "mKlassePreferenceClickListener" +preference.getKey(), Toast.LENGTH_SHORT).show();
			
			return true;
		}
	};
	
	/**
	 * Bis Android 1.6: Listener für Klick-Event auf Schaltfläche 'Karte
	 * Anzeigen'.
	 */
	private final OnPreferenceChangeListener mKlassePreferenceChangedListener = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {		
			int idxKlasse = Integer.parseInt((String)newValue);
			
			Toast.makeText(EinstellungenBearbeiten.this, "mKlassePreferenceChangedListener: Klassen-Index: " +idxKlasse, Toast.LENGTH_SHORT).show();
			
			refreshMannschaften(idxKlasse);
		
			return true;
		}
	};
	
	/**
	 * Bis Android 1.6: Listener für Klick-Event auf Schaltfläche 'Karte
	 * Anzeigen'.
	 */
	private final OnPreferenceChangeListener mMannschaftPreferenceChangedListener = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			ListPreference pref = (ListPreference) preference;
			int idxMannschaft = pref.findIndexOfValue((String)newValue);
			
			Toast.makeText(EinstellungenBearbeiten.this, "mMannschaftPreferenceChangedListener: " +newValue +" Mannschaft-Index: " +idxMannschaft, Toast.LENGTH_SHORT).show();
			
			return true;
		}
	};
	
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    
	    //menu.add(0, EINSTELLUNG_BEARBEITEN_ID, Menu.NONE, "Bearbeiten");
	    //menu.add(0, ZURUECK_ID, Menu.NONE, "Zur�ck");
	    menu.add(0, AMANDO_BEENDEN_ID, Menu.NONE,"Beenden");


	    return super.onCreateOptionsMenu(menu);
	  }

	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case AMANDO_BEENDEN_ID:
	    	  SharedPreferences sharedPreferences = getAnwendungsEinstellungen(this);
	    	  final Editor e = sharedPreferences.edit();
	    	  boolean herren = sharedPreferences.getBoolean("CHECK_HERREN_PREF", true);
	    	  String klasse = sharedPreferences.getString("LIST_KLASSE_PREF", "Kreisklasse");
	    	  String verein = sharedPreferences.getString("LIST_VEREIN_PREF", "KC-Ismaning");
	    	  String spielername = sharedPreferences.getString("EDIT_SPIELERNAME_PREF", "Guenter Platzer");
		      finish();
		      return true;
	      default:
	        Log.w(EINSTELLUNGEN_NAME,"unbekannte Option gewaehlt: " + item);
	        return super.onOptionsItemSelected(item);
	    }
	  }
	  
	  private void refreshMannschaften(int klasseId){
		  mannschaftListPreference = (ListPreference) findPreference("LIST_VEREIN_PREF");
			
		  alleMannschaftenVO = (ArrayList<MannschaftVO>) holeMannschaften(klasseId);
		  mannschaftKeyArray = new String[alleMannschaftenVO.size()];
		  mannschaftValueArray = new String[alleMannschaftenVO.size()];
			
	      for(int i = 0; i < alleMannschaftenVO.size(); i++){
		   	  mannschaftKeyArray[i] = String.valueOf(alleMannschaftenVO.get(i).key);
			  mannschaftValueArray[i] = alleMannschaftenVO.get(i).value;
		 }
			
	    mannschaftListPreference.setEntries(mannschaftValueArray);
		mannschaftListPreference.setEntryValues(mannschaftKeyArray);	
		}
	  
	  /** 
	   * Liefert die Anwendungseinstellungen. 
	   * 
	   * @param ctx Context der Anwendung
	   * @return SharedPreferences-Objekt mit den 
	   *   Anwendungseinstellungen
	   */
	  public static final SharedPreferences getAnwendungsEinstellungen(final ContextWrapper ctx) {
	    return ctx.getSharedPreferences(ctx.getPackageName() + "_preferences", MODE_PRIVATE);
	  }
	  
	  private List<KlasseVO> holeKlassen() {
		  if(null == mKlasseSpeicher){
			  mKlasseSpeicher = new KlasseSpeicher(this);
		  }
		  return mKlasseSpeicher.ladeKlassenAsKlasseVO();
	  }
	  
	  private List<MannschaftVO> holeMannschaften(int klasseId) {
		  if(null == mMannschaftSpeicher){
			  mMannschaftSpeicher = new MannschaftSpeicher(this);
		  }
		  return mMannschaftSpeicher.ladeMannschaftenAsString(klasseId);
	  }
}