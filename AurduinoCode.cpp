#include <Servo.h>

const int TRIG_PIN = 9;
const int ECHO_PIN = 10;
const int SERVO_PIN = 11;

const int MAX_DISTANCE = 40;   
const long TIMEOUT_US = 5800;   

Servo radarServo;
int angle = 0;
int direction = 1; 

void setup() {
  Serial.begin(9600);
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  radarServo.attach(SERVO_PIN);
  radarServo.write(0);
  delay(1000);
}

void loop() {
  for (angle = 15; angle <= 165; angle++) {
    radarServo.write(angle);
    delay(30);
    int dist = getDistance();
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
  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);

  long duration = pulseIn(ECHO_PIN, HIGH, TIMEOUT_US);
  
  int distance = duration * 0.034 / 2;

  if (distance <= 0 || distance > MAX_DISTANCE) {
    return MAX_DISTANCE + 1; 
  }

  return distance;
}
