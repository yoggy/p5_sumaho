
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

