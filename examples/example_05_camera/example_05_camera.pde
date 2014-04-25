//
// example_05_camera.pde - capture image for p5_sumaho_player
//
//  please enable "enable_camera" in p5_sumaho_player settings.
//

// p5_sumaho_player camera capture url
String url = "http://172.20.200.11:8080/camera.jpg";

void setup() {
  size(480, 640);
}

void draw() {  
  PImage img = loadImage(url);
  if (img != null) {
    image(img, 0, 0, width, height);
  }
  else {
    delay(200);
    background(0, 0, 255);
  }
}
