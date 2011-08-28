package platzerworld.kegeln.common.sensor;

import platzerworld.kegeln.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * KegelverwaltungSensor
 * 
 * @author platzerg
 */
public class KegelverwaltungSensor extends Activity implements OnClickListener {

	private Button accelerometerBtn;
	private Button sensorListBtn;
	private Button bouncingBallBtn;
	private Button bubblesBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kegelverwaltung_sensor);
        
        accelerometerBtn = (Button) findViewById(R.id.accelerometer_btn);
        accelerometerBtn.setOnClickListener(this);
        
        sensorListBtn = (Button) findViewById(R.id.sensor_list_btn);
        sensorListBtn.setOnClickListener(this);
        
        bouncingBallBtn = (Button) findViewById(R.id.bouncing_ball_btn);
        bouncingBallBtn.setOnClickListener(this);
        
        bubblesBtn = (Button) findViewById(R.id.bubbles_btn);
        bubblesBtn.setOnClickListener(this);
    } 
    
    public void onClick(View v) {
    	if (v == sensorListBtn) {
    		startActivity(new Intent(KegelverwaltungSensor.this, SensorActivity.class));
    	} else if (v == accelerometerBtn) {
    		startActivity(new Intent(KegelverwaltungSensor.this, BeschleunigungActivity.class));
    	} else if (v == bouncingBallBtn) {
    		startActivity(new Intent(KegelverwaltungSensor.this, RollenderBallActivity.class));
    	} else if (v == bubblesBtn) {
    		startActivity(new Intent(KegelverwaltungSensor.this, SeifenblasenActivity.class));
    	}
    }
}
