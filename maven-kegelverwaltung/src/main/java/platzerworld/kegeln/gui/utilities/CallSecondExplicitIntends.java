package platzerworld.kegeln.gui.utilities;

import platzerworld.kegeln.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CallSecondExplicitIntends extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.call_second_explicit_intents);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		String value1 = extras.getString("Value1");
		String value2 = extras.getString("Value2");
		if (value1 != null && value2 != null) {
			EditText text1 = (EditText) findViewById(R.id.EditText01);
			EditText text2 = (EditText) findViewById(R.id.EditText02);
			text1.setText(value1);
			text2.setText(value2);
		}
	}

	public void onClick(View view) {
		finish();
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("returnKey1", "Swinging on a star. ");
		data.putExtra("returnKey2", "You could be bettern then you are. ");
		setResult(RESULT_OK, data);
		super.finish();
	}
	}