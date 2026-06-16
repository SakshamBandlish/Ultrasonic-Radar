# Ultrasonic Radar System

A real-time object detection radar built with an **Arduino UNO**, **HC-SR04 ultrasonic sensor**, and **SG90 servo motor**. The sensor sweeps 150° and detects objects within a 40cm range. Distance and angle data is sent via Serial to a **Processing IDE** application that renders a live animated radar display — just like a real sonar system.

---


## Components Used

| Component        | Specification          | Purpose                     |
|------------------|------------------------|-----------------------------|
| Arduino UNO      | ATmega328P             | Main microcontroller        |
| HC-SR04          | Range: 2cm–400cm       | Distance measurement        |
| SG90 Servo Motor | 180°, 5V               | Rotating the sensor         |
| USB Cable        | Type-A to Type-B       | Serial communication to PC  |
| Jumper Wires     | Male-Male, Male-Female | Circuit connections         |
| Breadboard       | 400 tie points         | Wiring base                 |
| PC / Laptop      | Processing IDE installed | Radar UI rendering        |

---

## Circuit Wiring

| Component  | Component Pin   | Arduino Pin |
|------------|-----------------|-------------|
| HC-SR04    | VCC             | 5V          |
| HC-SR04    | GND             | GND         |
| HC-SR04    | TRIG            | Pin 9       |
| HC-SR04    | ECHO            | Pin 10      |
| SG90 Servo | Red (VCC)       | 5V          |
| SG90 Servo | Brown (GND)     | GND         |
| SG90 Servo | Orange (Signal) | Pin 11      |

---

---

## How to Run

### Step 1 — Upload Arduino Code
1. Open `arduino/radar.ino` in the Arduino IDE.
2. Select **Board**: Arduino UNO and the correct **COM Port**.
3. Click **Upload**.

### Step 2 — Run Processing Sketch
1. Open `processing/radar_display.pde` in Processing IDE.
2. Update the `serial port` name in the code to match your system (e.g., `"COM3"` on Windows or `"/dev/ttyUSB0"` on Linux/Mac).
3. Click **Run**.

### Step 3 — Watch the Radar!
The radar window will open and start displaying live sweep + object detection. 

---

##  Known Bug Fixed — Garbage Distance Values

The sensor randomly returned impossibly large values (e.g., `1190 cm`) when the actual object was only 30 cm away.

**Root Causes:**
- `pulseIn()` with no timeout would wait up to 1 second for an echo that never arrives.
- No distance validation meant junk values were sent directly to Processing.

**Fix Applied:**
- Added a **timeout** to `pulseIn()` (5800 µs ≈ 40cm range).
- Added **bounds checking** — only values between 2cm and 40cm are transmitted.

---

## Known Limitations

| Limitation     | Details                                                    |
|----------------|------------------------------------------------------------|
| 40cm max range | Intentionally capped for clean UI — sensor can do 400cm    |
| Sweep speed    | 30ms/degree × 150 degrees ≈ 4.5 sec per full sweep         |
| Single axis    | Only sweeps horizontally — no vertical detection           |
| Servo jitter   | SG90 may vibrate slightly at some angles, causing ±1cm noise |

---


