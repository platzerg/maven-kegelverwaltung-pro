package platzerworld.kegeln.common.sensor;

import platzerworld.kegeln.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * SensorActivity
 * 
 * @author platzerg
 */
public class SensorActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors);
        
		ListView sensorList = (ListView) findViewById(R.id.sensor_list);
		sensorList.setAdapter(new SensorListAdapter(this));
    }
}