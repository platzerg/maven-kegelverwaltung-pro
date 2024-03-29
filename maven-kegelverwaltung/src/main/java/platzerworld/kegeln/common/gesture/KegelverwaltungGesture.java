package platzerworld.kegeln.common.gesture;

import java.util.ArrayList;

import platzerworld.kegeln.R;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class KegelverwaltungGesture extends Activity implements OnGesturePerformedListener {
	private GestureLibrary gestureLib;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.kegelverwaltung_gestures, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.spells);
		if (!gestureLib.load()) {
			finish();
		}
		setContentView(gestureOverlayView);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 1.0) {
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
