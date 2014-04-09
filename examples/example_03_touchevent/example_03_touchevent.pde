import p5_sumaho.*;

Sumaho sumaho;

Wave wave_press;
Wave wave_release;

SampleStopWatch stop_watch;

void setup() {
  size(480, 640);

  sumaho = new Sumaho(this, "192.168.1.101"); // p5_sumaho_player ip address
  sumaho.setPublishScale(0.5f);

  stop_watch = new SampleStopWatch();
  stop_watch.center.x = width / 2;
  stop_watch.center.y = height / 2;
  stop_watch.radius = width / 2 - 40;
    
  wave_press = new Wave(0, 255, 0);
  wave_release = new Wave(255, 128, 0);
}

void draw() {
  background(0, 0, 255);

  stop_watch.draw();
  
  wave_press.draw();
  wave_release.draw();

  if (mousePressed) {
    noStroke();
    fill(255, 255, 0);
    ellipse(mouseX, mouseY, 30, 30);
  }

  // publish to p5_sumaho_player  
  sumaho.publish();
}

void mousePressed() {
  wave_press.start(mouseX, mouseY);
}

void mouseReleased() {
  wave_release.start(mouseX, mouseY);
}

