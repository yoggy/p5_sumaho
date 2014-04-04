package p5_sumaho;


import p5_sumaho.utils.PImageUtils;
import processing.core.PApplet;
import processing.core.PImage;

import com.google.protobuf.ByteString;

public class Sumaho {
	PApplet papplet;
	int jpeg_quality = 90;
	TCPClient tcp_client;
	UDPEventServer udp_event_server;

	Sensor sensor;
	
	float publish_scale = 1.0f;
	PImage resize_img;
	
	public static final int default_sumaho_port = 23401;
	public static final int default_event_port = 23402;

	public Sumaho(PApplet papplet, String sumaho_port) {
		this(papplet, sumaho_port, default_sumaho_port, default_event_port);
	}

	public Sumaho(PApplet papplet, String sumaho_host, int sumaho_port, int event_port) {
		this.sensor = new Sensor(this);
		this.papplet = papplet;
		
		this.tcp_client = new TCPClient(sumaho_host, sumaho_port);
		this.tcp_client.start();
		
		this.udp_event_server = new UDPEventServer(papplet, event_port, sensor);
		this.udp_event_server.start();
	}

	public boolean isConnect() {
		return getLastPublishStatus();
	}
	
	public Sensor getSensor() {
		return this.sensor;
	}

	public int getJpegQuality() {
		return jpeg_quality;
	}

	public void setJpegQuality(int jpeg_quality) {
		this.jpeg_quality = jpeg_quality;
	}

	public int getPublishIntervalTime() {
		return tcp_client.getPublishIntervalTime();
	}

	public void setPublishIntrvalTime(int ms) {
		tcp_client.setPublishIntrvalTime(ms);
	}

	public float getPublishScale() {
		return this.publish_scale;
	}

	public void setPublishScale(float scale) {
		this.publish_scale = scale;
	}

	public boolean getLastPublishStatus() {
		return tcp_client.getLastPublishStatus();
	}

	public float getPublishFps() {
		return tcp_client.getPublishFps();
	}

	public float getPublishBps() {
		return tcp_client.getPublishBps();
	}

	public String getPublishBpsStr() {
		return tcp_client.getPublishBpsStr();
	}

	public void publish() {
		// create jpeg byte array
		PImage img = papplet.get();
		img.updatePixels();
		
		resize_img = img;
		if (publish_scale != 1.0f) {
			if (resize_img == null || resize_img.width != img.width
					|| resize_img.height != img.height) {
				resize_img = new PImage(img.width, img.height);
				resize_img.copy(0, 0, img.width, img.height, 0, 0, img.width, img.height);
			}
			resize_img.resize((int) (img.width * publish_scale),
					(int) (img.height * publish_scale));
		}

		byte[] jpeg_data = PImageUtils
				.toJpegByteArray(resize_img, jpeg_quality);

		// serialize to Payload
		Payload.Image.Builder builder = Payload.Image.newBuilder();
		builder.setJpeg(ByteString.copyFrom(jpeg_data));
		Payload.Image image = builder.build();
		byte[] payload = image.toByteArray();
		
		builder.clear();
		builder = null;

		// set payload
		tcp_client.setPayload(payload);
	}

}
