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
	boolean is_dragged;
	int start_x;
	int start_y;
	
	public static final int drag_distance_threshold = 16;
	
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
			papplet.mousePressed = true;
			papplet.mouseButton = PApplet.LEFT;
			startDragCheck();
			papplet.mousePressed();
			break;
		case Event.TOUCH_MOVE:
			checkDrag();
			if (is_dragged == true) {
				papplet.mouseDragged();
			}
			papplet.mouseMoved();
			break;
		case Event.TOUCH_UP:
			papplet.mousePressed = false;
			papplet.mouseButton = 0;
			papplet.mouseReleased();
			if (is_dragged == false) {
				papplet.mouseClicked();
			}
			break;
		default:
			break;
		}
	}	
	
	void startDragCheck() {
		is_dragged = false;
		start_x = papplet.mouseX;
		start_y = papplet.mouseY;
	}
	
	void checkDrag() {
		if (is_dragged == true) return;
		
		int dx = papplet.mouseX - start_x;
		int dy = papplet.mouseY - start_y;
		
		double diff = Math.sqrt(dx * dx + dy * dy);
		if (drag_distance_threshold < diff) {
			is_dragged = true;
		}
	}
}
