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
  
  // publish to p5_smaho_player  
  sumaho.publish();
}

