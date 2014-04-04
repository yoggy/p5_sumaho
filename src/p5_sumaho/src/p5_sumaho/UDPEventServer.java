package p5_sumaho;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import processing.core.PApplet;

public class UDPEventServer extends Thread {	
	PApplet papplet;
	Sensor sensor;
	int port;
	boolean mouse_pressed = false;
	
	DatagramSocket socket;
	
	public UDPEventServer(PApplet papplet, int port, Sensor sensor) {
		this.papplet = papplet;
		this.port = port;
		this.sensor = sensor;
	}
	
	public void run() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		while(true) {
			try {
				socket.receive(packet);
				packet.getLength();

				byte [] payload = packet.getData();
				ByteArrayInputStream bis = new ByteArrayInputStream(payload);

				Payload.EventType type = Payload.EventType.parseDelimitedFrom(bis);
				switch(type.getId()) {
				case Event.TYPE_TOUCH:
					processEventTouch(Payload.EventTouch.parseDelimitedFrom(bis));
					break;
				case Event.TYPE_GRAVITY:
					sensor.setGravity(Payload.EventGravity.parseDelimitedFrom(bis));
					break;
				case Event.TYPE_MAGNETIC_FIELD:
					sensor.setMagneticField(Payload.EventMagneticField.parseDelimitedFrom(bis));
					break;
				case Event.TYPE_LIGHT:
					sensor.setLight(Payload.EventLight.parseDelimitedFrom(bis));
					break;
				case Event.TYPE_PROXIMITY:
					sensor.setProximity(Payload.EventProximity.parseDelimitedFrom(bis));
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processEventTouch(Payload.EventTouch evt) {
		// ignore multi touch event...
		if (evt.getId() != 0) return;
		
		// convert to screen coordinate
		int type = evt.getType();
		int x = (int)(papplet.getWidth() * evt.getX());
		int y = (int)(papplet.getHeight() * evt.getY());

		papplet.mouseX = x;
		papplet.mouseY = y;
		
		// 
		switch(type) {
		case Event.TOUCH_DOWN:
			mouse_pressed = true;
			papplet.mousePressed();
			break;
		case Event.TOUCH_MOVE:
			if (mouse_pressed == true) {
				papplet.mouseDragged();
			}
			papplet.mouseMoved();
			break;
		case Event.TOUCH_UP:
			mouse_pressed = false;
			papplet.mouseReleased();
			break;
		default:
			break;
		}
	}	
}
