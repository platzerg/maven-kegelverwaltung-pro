package platzerworld.kegeln.gui;

import platzerworld.kegeln.R;
import platzerworld.kegeln.common.ConstantsIF;
import platzerworld.kegeln.common.KeyValueVO;
import platzerworld.kegeln.common.style.StyleManager;
import platzerworld.kegeln.spieler.db.SpielerSpeicher;
import platzerworld.kegeln.spieler.vo.SpielerVO;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Zeigt die Liste der Geokontakte an.
 * 
 * @author Arno Becker, David Müller 2010 visionera gmbh
 * 
 */
public class SpielerAnlegen extends Activity implements ConstantsIF{

	private static final long serialVersionUID = 1L;

	/** Kuerzel fuers Logging. */
	private static final String TAG = ErgebnisseAnzeigen.class.getSimpleName();
	
	private Button mSpeichernButton;
	private Button mAbbruchButton;

	private SpielerSpeicher mSpielerSpeicher;
	
	private SpielerVO mSpielerVO;
	private KeyValueVO mannschaftVO;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.d(TAG, "onCreate(): entered...");
		setContentView(R.layout.spieler_anlegen);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		mannschaftVO = (KeyValueVO) extras.getSerializable(INTENT_EXTRA_MANNSCHAFT);
		
		init();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		cleanDatabase();
		super.onDestroy();
	}
	
	private void init(){
		initStyle();
		initWidgets();
		initListener();
		initContextMenu();
		initDatabase();
	}
	
	private void initStyle() {
		Typeface font = StyleManager.getInstance().init(this).getTypeface();
		TextView titeltext = (TextView) findViewById(R.id.txt_spieler_neuanlegen_titel);
		titeltext.setTypeface(font);
	}
	
	private void initWidgets(){
		mSpeichernButton = (Button) findViewById(R.id.sf_spieler_neuanlagen_ok);
		mAbbruchButton = (Button) findViewById(R.id.sf_spieler_neuanlagen_abbruch);
	}
	
	private void initListener(){		
		mSpeichernButton.setOnClickListener(mSpielerAnlegenOkListener);
		mAbbruchButton.setOnClickListener(mSpielerAnlegenAbbruchListener);
	}
	
	private void initContextMenu(){
	}
	
	private void initDatabase(){
	}
	
	private void cleanDatabase(){
	}
	
	private OnClickListener mSpielerAnlegenOkListener = new OnClickListener() {
	    public void onClick(View v) {	    	
	      speichern();
	    }
	};
	
	private OnClickListener mSpielerAnlegenAbbruchListener = new OnClickListener() {
	    public void onClick(View v) {
	      finish();
	    }
	};
	
	private void speichern(){
		EditText passnr = (EditText) findViewById(R.id.edt_spieler_neuanlegen_passnr);
		EditText name = (EditText) findViewById(R.id.edt_spieler_neuanlegen_name);
		EditText vorname = (EditText) findViewById(R.id.edt_spieler_neuanlegen_vorname);
		EditText gebDatum = (EditText) findViewById(R.id.edt_spieler_neuanlegen_gebdatum);
		
		EditText latitude = (EditText) findViewById(R.id.edt_spieler_neuanlegen_latitude);
		EditText longitude = (EditText) findViewById(R.id.edt_spieler_neuanlegen_longitude);
		
		mSpielerSpeicher = new SpielerSpeicher(this);		
		long mannschaftId = mannschaftVO.key;
		
		float latitudeInt = Float.parseFloat(latitude.getText().toString());
		float longitudeInt = Float.parseFloat(longitude.getText().toString());
		
		mSpielerVO = new SpielerVO(mannschaftId, Long.parseLong(passnr.getText().toString()), 
				name.getText().toString(), vorname.getText().toString(), gebDatum.getText().toString(),
				(int) (latitudeInt * 1E6), (int) (longitudeInt * 1E6));
		mSpielerVO.id = 0;
		mSpielerSpeicher.speichereSpieler(mSpielerVO);
		
		finish();
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra(INTENT_EXTRA_NEUER_SPIELER, mSpielerVO);
		setResult(RESULT_OK, data);
		super.finish();
	}

}
