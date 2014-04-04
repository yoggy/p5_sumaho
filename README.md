p5_sumaho
=========

p5_sumaho is Android remote control framework library for Processsing.
![system abstract](fig01.png)

see also p5_sumaho_player
* https://github.com/yoggy/p5_sumaho_player

How to use
=========

example_01_publish
--------
<pre>
import p5_sumaho.*;

Sumaho sumaho;

void setup() {
  size(480, 640);

  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
  sumaho.setPublishScale(0.5f);
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

  // publish to p5_smaho_player  
  sumaho.publish();
}
</pre>

example_02_touchevent
--------
<pre>
import p5_sumaho.*;

Sumaho sumaho;
boolean mouse_press;

void setup() {
  size(480, 640);
  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
  sumaho.setPublishScale(0.5f);
}

void draw() {
  background(0, 0, 255);
  
  if (mouse_press) {
    fill(255, 255, 0);
    ellipse(mouseX, mouseY, 50, 50);
  }
  
  // publish to p5_smaho_player  
  sumaho.publish();
}

void mousePressed() {
  mouse_press = true;
}

void mouseReleased() {
  mouse_press = false;
}
</pre>
