package platzerworld.kegeln.gui;

import platzerworld.kegeln.R;
import platzerworld.kegeln.common.ConstantsIF;
import platzerworld.kegeln.common.receiver.SMSBroadcastReceiver;
import platzerworld.kegeln.common.sound.SoundManager;
import platzerworld.kegeln.common.style.StyleManager;
import platzerworld.kegeln.gui.einstellung.EinstellungenBearbeiten;
import platzerworld.kegeln.gui.utilities.UtilitiesActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Startseite extends Activity implements ConstantsIF{
	
	private BroadcastReceiver SMSbr;
	private IntentFilter SMSIntentFilter;
	final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static final String TAG = "Startseite Kegelverwaltung";
	private boolean checkboxPreferenceHerren;
	private String listPreferenceKlasse;
	private String listPreferenceVerein;
	private String editSpielernameTextPreference;
	
	private Button mButtonErgebnisse;
	private Button mButtonLigaverwaltung;
	private Button mButtonTabellen;
	private Button mButtonSchnittlisten;
	private ImageView mImageViewAnimation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startseite);
		
		init();
		
		playSound(getCurrentFocus());

		SoundManager.getInstance().initSounds(this);
		SoundManager.getInstance().loadSounds();
		SoundManager.getInstance().playSound(1, 1);

		
		mImageViewAnimation.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));


		registerSMS();

	}
	
	@Override
	protected void onStart() {
		getPrefs();
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		cleanDatabase();
		super.onDestroy();
	}
	
	
	private void init(){
		initWidgets();
		initStyle();
		initListener();
		initContextMenu();
		initDatabase();
	}
	
	private void initStyle() {
		Typeface font = StyleManager.getInstance().init(this).getTypeface();
		
		TextView titeltext = (TextView) findViewById(R.id.txt_kegelverwaltung_titel);
		titeltext.setTypeface(font);
		
		mButtonErgebnisse.setTypeface(font);
		mButtonLigaverwaltung.setTypeface(font);
		mButtonTabellen.setTypeface(font);
		mButtonSchnittlisten.setTypeface(font);
	}
	
	private void initWidgets(){
		mButtonErgebnisse = (Button) findViewById(R.id.sf_ergebnisse);
		mButtonLigaverwaltung = (Button) findViewById(R.id.sf_ligaverwaltung);	
		mButtonTabellen = (Button) findViewById(R.id.sf_tabellen);	
		mButtonSchnittlisten = (Button) findViewById(R.id.sf_schnittlisten);		
		mImageViewAnimation = (ImageView) findViewById(R.id.imageView1);
	}
	
	private void initListener(){
		mButtonErgebnisse.setOnClickListener(mErgebnisseListener);		
		mButtonLigaverwaltung.setOnClickListener(mLigaverwaltungListener);
		mButtonTabellen.setOnClickListener(mTabellenListener);
		mButtonSchnittlisten.setOnClickListener(mSchnittlisteListener);
	}
	
	private void initContextMenu(){
	}
	
	private void initDatabase(){
		
	}
	
	private void cleanDatabase(){
		
	}


	private final OnClickListener mErgebnisseListener = new OnClickListener() {
		public void onClick(View v) {
			onClickErgebnisseAnzeigen(v);
		}
	};

	public void onClickErgebnisseAnzeigen(final View sfNormal) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Suche Spieler");
		alert.setMessage("Bitte geben Sie einen Spielernamen ein!");

		final EditText inputSpielerSuche = new EditText(this);
		alert.setView(inputSpielerSuche);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = inputSpielerSuche.getText().toString();
		  final Intent intent = new Intent(Startseite.this, ErgebnisseAnzeigen.class);
		  intent.putExtra(INTENT_EXTRA_SPIELER, value);
			startActivity(intent);
		  }
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	private final OnClickListener mLigaverwaltungListener = new OnClickListener() {
		public void onClick(View v) {
			onClickLigaverwaltungAnzeigen(v);
		}
	};

	public void onClickLigaverwaltungAnzeigen(final View sfNormal) {
		final Intent i = new Intent(this, LigaVerwalten.class);
		startActivity(i);
	}

	private final OnClickListener mTabellenListener = new OnClickListener() {
		public void onClick(View v) {
			onClickTabellenAnzeigen(v);
		}
	};

	public void onClickTabellenAnzeigen(final View sfNormal) {
		final Intent settingsActivity = new Intent(this, TabellenAnzeigen.class);
		startActivity(settingsActivity);
	}

	private final OnClickListener mSchnittlisteListener = new OnClickListener() {
		public void onClick(View v) {
			onClickSchnittlisteAnzeigen(v);
		}
	};

	public void onClickSchnittlisteAnzeigen(final View sfNormal) {
		final Intent settingsActivity = new Intent(this, SchnittlisteAnzeigen.class);
		startActivity(settingsActivity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kegelverwaltung_option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return applyMenuChoice(item);
	}

	private boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.opt_kegelverwaltung_einstellung:
			startEinstellungen();
			return true;
		case R.id.opt_kegelverwaltung_utilities:
			startUtilities();
			Log.d(TAG, "opt_kegelverwaltung_utilities");
			return true;
		case R.id.opt_kegelverwaltung_beenden:
			Log.d(TAG, "opt_kegelverwaltung_beenden");
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void startEinstellungen(){
		Log.d(TAG, "opt_kegelverwaltung_einstellung:");
		startActivity(new Intent(this, EinstellungenBearbeiten.class));
	}
	
	private void startUtilities(){
		final Intent settingsActivity = new Intent(this, UtilitiesActivity.class);
		startActivity(settingsActivity);
		
	}

	private void getPrefs() {
		// Get the xml/preferences.xml preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		checkboxPreferenceHerren = prefs.getBoolean("CHECK_HERREN_PREF", true);
		listPreferenceKlasse = prefs.getString("LIST_KLASSE_PREF", "Kreisklasse");
		listPreferenceVerein = prefs.getString("LIST_VEREIN_PREF", "KC-Ismaning");

		editSpielernameTextPreference = prefs.getString("EDIT_SPIELERNAME_PREF", "Guenter Platzer");
	
		Log.d(TAG, "getPrefs() CHECK_HERREN_PREF: Herren: " + checkboxPreferenceHerren);
		Log.d(TAG, "getPrefs() LIST_KLASSE_PREF: Klasse: " + listPreferenceKlasse);
		Log.d(TAG, "getPrefs() LIST_VEREIN_PREF: Verein: " + listPreferenceVerein);
		Log.d(TAG, "getPrefs() EDIT_SPIELERNAME_PREF: Spielername: " + editSpielernameTextPreference);

	}

	private void registerSMS() {
		// SMS RECEIVER
		SMSbr = new SMSBroadcastReceiver() ;
		// The BroadcastReceiver needs to be registered before use.
		SMSIntentFilter = new IntentFilter(SMS_RECEIVED);
		this.registerReceiver(SMSbr, SMSIntentFilter);

	}
	
	private boolean checkSMS() {
		// Sets the sms inbox's URI
		Uri uriSMS = Uri.parse("content://sms");
		Cursor c = getBaseContext().getContentResolver().query(uriSMS, null, "read = 0", null, null);
		// Checks the number of unread messages in the inbox
		if (c.getCount() == 0) {
			return false;
		} else
			return true;
	}

	public void playSound(View view) {
		// First parameter defines the number of channels which should be played
		// in parallel, last one currently not used
		SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		int soundID = soundPool.load(this, R.raw.metalhit, 1);

		// Getting the user sound settings
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		soundPool.play(soundID, volume, volume, 1, 0, 1f);
	}

	// Call this method to stop the animation
	public void stopAnimation() {
		AnimationDrawable animator = (AnimationDrawable) mImageViewAnimation.getBackground();
		animator.stop();
		mImageViewAnimation.setImageResource(R.drawable.icon);
	}

}