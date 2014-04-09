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

