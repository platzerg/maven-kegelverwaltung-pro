package platzerworld.kegeln.common.sensor;

/**
 * SensorInfo
 * 
 * @author platzerg
 */
public class SensorInfo {
	public final String name;
	public final boolean supported;
	
	public SensorInfo(String name, boolean supported) {
		this.name = name;
		this.supported = supported;
	}	
}
