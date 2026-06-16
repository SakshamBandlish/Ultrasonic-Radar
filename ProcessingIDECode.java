import processing.serial.*;

Serial myPort;
String angle = "";
String distance = "";
String data = "";
String noObject;
float pixsDistance;
int iAngle, iDistance;
int index1 = 0;
PFont orcFont;

void setup() {
  size(1200, 700);
  smooth();

  // ⚠️ Change "COM3" to your Arduino's port
  // Windows: "COM3", "COM4", etc.
  // Mac/Linux: "/dev/ttyUSB0" or "/dev/cu.usbmodem..."
  myPort = new Serial(this, "COM3", 9600);
  myPort.bufferUntil('.');

  orcFont = createFont("OCRaExtended", 14, true);
}

void draw() {
  fill(98, 245, 31);
  textFont(orcFont);

  // Fading trail effect
  noStroke();
  fill(0, 4);
  rect(0, 0, width, height - height * 0.065);

  fill(98, 245, 31);
  drawRadar();
  drawLine();
  drawObject();
  drawText();
}

void serialEvent(Serial myPort) {
  data = myPort.readStringUntil('.');
  if (data != null) {
    data = trim(data);
    index1 = data.indexOf(",");
    if (index1 != -1) {
      angle    = data.substring(0, index1);
      distance = data.substring(index1 + 1);
      iAngle    = int(angle);
      iDistance = int(distance);
    }
  }
}

void drawRadar() {
  pushMatrix();
  translate(width / 2, height - height * 0.074);
  noFill();
  strokeWeight(2);
  stroke(98, 245, 31);

  // Concentric arcs
  arc(0, 0, width * 0.0833f, width * 0.0833f, PI, TWO_PI);
  arc(0, 0, width * 0.2083f, width * 0.2083f, PI, TWO_PI);
  arc(0, 0, width * 0.3472f, width * 0.3472f, PI, TWO_PI);
  arc(0, 0, width * 0.4861f, width * 0.4861f, PI, TWO_PI);
  arc(0, 0, width * 0.6250f, width * 0.6250f, PI, TWO_PI);

  // Angle lines
  line(-width / 2, 0, width / 2, 0);
  line(0, 0, (-width / 2) * cos(radians(30)), (-width / 2) * sin(radians(30)));
  line(0, 0, (-width / 2) * cos(radians(60)), (-width / 2) * sin(radians(60)));
  line(0, 0, (-width / 2) * cos(radians(90)), (-width / 2) * sin(radians(90)));
  line(0, 0, (-width / 2) * cos(radians(120)), (-width / 2) * sin(radians(120)));
  line(0, 0, (-width / 2) * cos(radians(150)), (-width / 2) * sin(radians(150)));
  line((-width / 2) * cos(radians(30)), 0, width / 2, 0);

  popMatrix();
}

void drawObject() {
  pushMatrix();
  translate(width / 2, height - height * 0.074);
  strokeWeight(9);
  stroke(255, 10, 10); // Red dot for detected object

  // Map 40cm max range to screen pixels
  pixsDistance = iDistance * ((height - height * 0.1666f) / 40.0f);

  // Only draw if within range
  if (iDistance < 40) {
    line(
      pixsDistance * cos(radians(iAngle)),
      -pixsDistance * sin(radians(iAngle)),
      (width / 2) * cos(radians(iAngle)),
      -(width / 2) * sin(radians(iAngle))
    );
  }

  popMatrix();
}

void drawLine() {
  pushMatrix();
  strokeWeight(9);
  stroke(30, 250, 60); // Green sweep line
  translate(width / 2, height - height * 0.074);
  line(
    0, 0,
    (height - height * 0.12f) * cos(radians(iAngle)),
    -(height - height * 0.12f) * sin(radians(iAngle))
  );
  popMatrix();
}

void drawText() {
  pushMatrix();
  fill(0, 0, 0);
  noStroke();
  rect(0, height - height * 0.0648f, width, height);
  fill(98, 245, 31);
  textSize(25);

  // Range labels
  text("10cm", width / 2 + width * 0.0416f - 10, height - height * 0.0833f);
  text("20cm", width / 2 + width * 0.1250f - 10, height - height * 0.0833f);
  text("30cm", width / 2 + width * 0.2083f - 10, height - height * 0.0833f);
  text("40cm", width / 2 + width * 0.2916f - 10, height - height * 0.0833f);

  // Angle labels
  textSize(20);
  text("30°",  width / 2 - width * 0.3472f, height - height * 0.0463f);
  text("60°",  width / 2 - width * 0.1736f, height - height * 0.0463f);
  text("90°",  width / 2 - 15,              height - height * 0.0463f);
  text("120°", width / 2 + width * 0.1388f, height - height * 0.0463f);
  text("150°", width / 2 + width * 0.2916f, height - height * 0.0463f);

  // Status display
  textSize(25);
  fill(98, 245, 60);
  if (iDistance >= 40) {
    noObject = "Out of Range";
    text("Angle: " + iAngle + "°  Distance: " + noObject, width * 0.02f, height - height * 0.0277f);
  } else {
    text("Angle: " + iAngle + "°  Distance: " + iDistance + " cm", width * 0.02f, height - height * 0.0277f);
  }

  popMatrix();
}
