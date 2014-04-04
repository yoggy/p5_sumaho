package p5_sumaho;

import p5_sumaho.Payload.EventGravity;
import p5_sumaho.Payload.EventLight;
import p5_sumaho.Payload.EventMagneticField;
import p5_sumaho.Payload.EventProximity;
import processing.core.PVector;

public class Sensor {
	Sumaho sumaho;

	PVector gravity = new PVector();
	PVector magnetic_field = new PVector();
	float light;
	float proximity;
		
	public Sensor(Sumaho sumaho) {
		this.sumaho = sumaho;
	}

	public boolean isConnect() {
		return checkStatus();
	}
	
	protected void clear() {
		gravity = new PVector();
		magnetic_field = new PVector();
		proximity = 0.0f;
		light = 0.0f;
	}

	protected boolean checkStatus() {
		if (sumaho.isConnect() == false) {
			clear();
			return false;
		}
		return true;
	}

	public PVector getGravity() {
		if (!checkStatus()) {
			return new PVector();
		}		
		return new PVector(gravity.x, gravity.y, gravity.z);
	}
	
	protected void setGravity(EventGravity evt) {
		this.gravity.x = evt.getX();
		this.gravity.y = evt.getY();
		this.gravity.z = evt.getZ();
	}

	public PVector getMagneticField() {
		if (!checkStatus()) {
			return new PVector();
		}		
		return new PVector(magnetic_field.x, magnetic_field.y, magnetic_field.z);
	}

	protected void setMagneticField(EventMagneticField evt) {
		this.magnetic_field.x = evt.getX();
		this.magnetic_field.y = evt.getY();
		this.magnetic_field.z = evt.getZ();
	}

	 void setMagneticField(float x, float y, float z) {
	}

	public float getProximity() {
		if (!checkStatus()) {
			return 0.0f;
		}
		return proximity;
	}
	
	protected void setProximity(EventProximity evt) {
		this.proximity = evt.getVal();
	}

	public float getLight() {
		if (!checkStatus()) {
			return 0.0f;
		}
		return light;
	}
	
	protected void setLight(EventLight evt) {
		this.light = evt.getVal();
	}
}
