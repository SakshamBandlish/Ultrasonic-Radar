#include <Servo.h>

// --- Pin Definitions ---
const int TRIG_PIN = 9;
const int ECHO_PIN = 10;
const int SERVO_PIN = 11;

// --- Settings ---
const int MAX_DISTANCE = 40;      // cm — intentional cap for clean UI
const long TIMEOUT_US = 5800;     // microseconds (~40cm round trip)

Servo radarServo;
int angle = 0;
int direction = 1; // 1 = forward, -1 = backward

void setup() {
  Serial.begin(9600);
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  radarServo.attach(SERVO_PIN);
  radarServo.write(0);
  delay(1000);
}

void loop() {
  // Sweep from 0 to 150 degrees and back
  for (angle = 15; angle <= 165; angle++) {
    radarServo.write(angle);
    delay(30);
    int dist = getDistance();
    // Send data: "angle,distance."
    Serial.print(angle);
    Serial.print(",");
    Serial.print(dist);
    Serial.print(".");
  }

  for (angle = 165; angle >= 15; angle--) {
    radarServo.write(angle);
    delay(30);
    int dist = getDistance();
    Serial.print(angle);
    Serial.print(",");
    Serial.print(dist);
    Serial.print(".");
  }
}

int getDistance() {
  // Send ultrasonic pulse
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);

  // Read echo with timeout to prevent garbage values
  long duration = pulseIn(ECHO_PIN, HIGH, TIMEOUT_US);

  // Convert to cm
  int distance = duration * 0.034 / 2;

  // Validate: discard out-of-range readings
  if (distance <= 0 || distance > MAX_DISTANCE) {
    return MAX_DISTANCE + 1; // Sentinel value = "nothing detected"
  }

  return distance;
}
