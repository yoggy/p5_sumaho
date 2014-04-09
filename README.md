p5_sumaho
=========

p5_sumaho is an Android smartphone remote control library for Processing.
You can easily create a prototype for the Android using Processing.

![system abstract](fig01.png)

Demo Movie
=========
  * http://youtu.be/RWt1vlE57-g
  * [![ScreenShot](https://farm8.staticflickr.com/7125/13623802903_857e5497bd_m.jpg)](http://youtu.be/RWt1vlE57-g)
  

How to use
=========

1. Install p5_sumaho_player on your Android smartphone.
  * [p5_sumaho_player](https://play.google.com/store/apps/details?id=net.sabamiso.p5_sumaho_player) (Google Play)

2. Setup the Wi-Fi configuration on your Android smartphone in order to connect to the PC through the Wi-Fi network.

3. Launch p5_sumaho_player on your Android smartphone.

4. Install p5_sumaho library on your Processing environment.
  * [download library from here](https://github.com/yoggy/p5_sumaho/archive/master.zip)
  * see also for manual installation... [How to Install a Contributed Library](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library) (processing.org)

5. Create a following sketch for Processing.
<pre>
  import p5_sumaho.*;
  
  Sumaho sumaho;
  
  void setup() {
    size(480, 640);
    
    sumaho = new Sumaho(this, "192.168.1.101");  // p5_sumaho_player IP Address
  
    colorMode(HSB, 100, 100, 100);
    background(66, 100, 100);
  }
  
  void draw() {
    // draw something ...
    fill(frameCount % 100, 100, 100);
    ellipse(mouseX, mouseY, 100, 100);
  
    // publish to p5_sumaho_player  
    sumaho.publish();
  }
</pre>

6. Run the sketch and move the mouse pointer on the sketch. p5_sumaho_player will display the same content on the screen of the Android smartphone.

7. Touch operation on the screen of the Android smartphone. Touch operation is sent to the sketch.



Examples
=========

example_01_publish
--------
<pre>
import p5_sumaho.*;

Sumaho sumaho;
  
void setup() {
  size(480, 640);
  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
}

void draw() {
  // draw something
  if (frameCount % 100 == 0) {
    background(random(255), random(255), random(255));
  }

  int x = (int)random(width);
  int y = (int)random(height);
  int r = (int)random(50) + 30;

  noStroke();
  fill(random(255), random(255), random(255));
  ellipse(x, y, r, r);

  // publish to p5_sumaho_player  
  sumaho.publish();
}
</pre>

example_02_touchevent
--------
<pre>
import p5_sumaho.*;

Sumaho sumaho;

void setup() {
  size(480, 640);
  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
}

void draw() {
  background(0, 0, 255);
  
  if (mousePressed) {
    fill(255, 255, 0);
    ellipse(mouseX, mouseY, 50, 50);
  }
  
  // publish to p5_sumaho_player  
  sumaho.publish();
}
</pre>


example_04_sensor
--------

<pre>
import p5_sumaho.*;

Sumaho sumaho;

void setup() {
  size(480, 640);

  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
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

  // publish to p5_sumaho_player  
  sumaho.publish();
}
</pre>

Libraries
========
p5_smaho uses the following libraries.

Protocol Buffers
* https://code.google.com/p/protobuf/

