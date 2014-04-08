import p5_sumaho.*;

Sumaho sumaho;

void setup() {
  size(480, 640);

  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip a
}

void draw() {
  background(0, 0, 255);

  PVector g = sumaho.getSensor().getGravity(); // accelerometer sensor value
  PVector m = sumaho.getSensor().getMagneticField(); // matnetic field sensor value
  float l = sumaho.getSensor().getLight();     // ligith sensor value
  float p = sumaho.getSensor().getProximity(); // proximity sensor value

  text("sumaho.isConnect()=" + sumaho.isConnect(), 10, 20);  
  text(String.format("gravity=(%.2f, %.2f, %.2f)", g.x, g.y, g.z), 10, 40);
  text(String.format("magnetic field=(%.2f, %.2f, %.2f)", m.x, m.y, m.z), 10, 60);
  text("light sensor=" + l, 10, 80);
  text("proximity sensor=" + p, 10, 100);

  // publish to p5_smaho_player  
  sumaho.publish();
}

