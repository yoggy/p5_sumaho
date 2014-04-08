package p5_sumaho.test;

import p5_sumaho.Sumaho;
import processing.core.PApplet;
import processing.core.PVector;

public class SumahoClientTestMain extends PApplet {
	private static final long serialVersionUID = 4258434090200713048L;

	Sumaho sumaho;
	SampleStopWatch stop_watch;
	Wave wave_press;
	Wave wave_release;
	
	public void setup() {
		size(480, 640);
		sumaho = new Sumaho(this, "172.20.200.11");
		sumaho.setPublishScale(0.5f);
		
		stop_watch = new SampleStopWatch();
		stop_watch.center.x = width / 2;
		stop_watch.center.y = height / 2;
		stop_watch.radius = width / 2 - 40;
	
		wave_press = new Wave(0, 255, 0);
		wave_release = new Wave(255, 128, 0);
	}

	public void draw() {
		background(0, 0, 64);

		stop_watch.draw();

		wave_press.draw();
		wave_release.draw();
		
		stroke(255, 255, 255);
		if (mousePressed) {
			noStroke();			
			fill(255, 255, 0);
			ellipse(mouseX, mouseY, 30, 30);
		}

		strokeWeight(2);

		fill(255);
		text("frameCount=" + frameCount, 10, 16);
		text("frameRate=" + frameRate, 10, 32);
		text("publishFPS=" + sumaho.getPublishFps(), 10, 48);
		text("publishBPS=" + sumaho.getPublishBpsStr(), 10, 64);
		text("lastPublishStatus=" + sumaho.getLastPublishStatus(), 10, 80);

		PVector g = sumaho.getSensor().getGravity();
		PVector m = sumaho.getSensor().getMagneticField();

		text(String.format("sensor.gravity=(%.2f, %.2f, %.2f)", g.x, g.y, g.z), 10, 100);
		text(String.format("sensor.magnetic_field=(%.2f, %.2f, %.2f)", m.x, m.y, m.z), 10, 116);
		text("sensor.light=" + sumaho.getSensor().getLight(), 10, 132);
		text("sensor.proximity=" + sumaho.getSensor().getProximity(), 10, 148);

		sumaho.publish();

		println("mouseButton=" + mouseButton);
	}
	
	public void mousePressed() {
		wave_press.start(mouseX, mouseY);
	}
	
	public void mouseReleased() {
		wave_release.start(mouseX, mouseY);
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "p5_sumaho.test.SumahoClientTestMain" });
	}

	class SampleStopWatch {
		public PVector center = new PVector();
		public int radius = 0;

		void draw_clock_line(float s, float e, float angle) {
			float vx = sin(angle * 2 * PI);
			float vy = -cos(angle * 2 * PI);
			float sp_x = center.x + s * radius * vx;
			float sp_y = center.y + s * radius * vy;
			float ep_x = center.x + e * radius * vx;
			float ep_y = center.y + e * radius * vy;
			line(sp_x, sp_y, ep_x, ep_y);
		}

		void draw() {
			stroke(255);
			noFill();

			strokeWeight(10);
			ellipse(center.x, center.y, radius * 2, radius * 2);
			strokeWeight(5);
			for (int i = 0; i < 12; ++i) {
				draw_clock_line(0.9f, 1.0f, i / 12.0f);
			}
			
			long t = System.currentTimeMillis();
			int m = (int)(t / 1000 / 60) % 60;
			int s = (int)(t / 1000)  % 60;
			int ss = (int)(t % 1000);

			float angle_m = m / 60.0f + s / 60.0f / 60.0f;
			strokeWeight(15);
			draw_clock_line(0.0f, 0.5f, angle_m);

			float angle_s = s / 60.0f + ss / 60.0f / 1000.0f;
			strokeWeight(10);
			draw_clock_line(0.0f, 0.7f, angle_s);
			
			strokeWeight(5);
			float angle_ss = ss / 1000.0f;
			draw_clock_line(0.0f, 0.80f, angle_ss);
		}
	}

	class Wave {
		int max_count = 90;
		int count = max_count;
		int x;
		int y;
		int r, g, b;
		
		public Wave(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public void start(int x, int y) {
			this.x = x;
			this.y = y;
			this.count = 0;
		}
		
		public void draw() {
			if (count == max_count) return;
			count ++;
			
			float p = count / (float)max_count;
			strokeWeight(10);
			stroke(r, g, b, 255 * (1.0f - p));
			noFill();

			ellipse(x, y, width / 2 * p, width / 2 * p);
		}
		
	}
}
