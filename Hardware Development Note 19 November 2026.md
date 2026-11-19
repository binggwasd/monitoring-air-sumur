# Hardware Development Note 
## 19 Novermber 2026

Housing untuk hardware didesain dalam Fusion (Formerly Fusion 360). Proses desain dimulai dengan pengukuran dimensi dari komponen-komponen, kemudian informasi tersebut akan digunakan untuk
memastikan bahwa housing yang akan didesain sesuai. 

## Desain dari housing Gateway
<img width="1148" height="757" alt="Screenshot 2026-11-19 142419" src="https://github.com/user-attachments/assets/e0d7eb2c-cf0e-4f2f-87e5-218313f23332" />
<img width="599" height="744" alt="image" src="https://github.com/user-attachments/assets/89a6dc50-2c3e-4422-841d-f266b0e80a56" />

Panjang: 180
Tinggi: 32.50
Lebar: 80<br>

Ketebalan housing: 2 Milimeter<br>

## Desain dari housing Buoy
<img width="731" height="603" alt="image" src="https://github.com/user-attachments/assets/b9190281-d672-4156-96a8-8683a147128b" />
<img width="769" height="582" alt="image" src="https://github.com/user-attachments/assets/7b67c829-0e15-4033-9138-39731ecc9ea9" />

Ketebalan housing: 2 Milimeter hingga 4 Milimeter<br>

## Implementasi 
<img width="1716" height="2292" alt="LRM_20261119_144215 jpg" src="https://github.com/user-attachments/assets/116a4a25-12dd-4fd8-b75b-66923e6a8dcf" />
Housing gateway berhasil diimplementasi.<br>
<img width="2230" height="1670" alt="LRM_20261119_144446 jpg" src="https://github.com/user-attachments/assets/cb822e32-74c8-40ec-bf4d-f592c16f5218" />
Housing buoy mengalami kegagalan pencetakan dan node sensor untuk sementara dipasang secara langsung.
<img width="431" height="284" alt="image" src="https://github.com/user-attachments/assets/121d7113-aa1f-4454-84f7-05d14bbf2847" /> <br>
Hasil pembacaan pada Arduino Uno.

## Kendala
1. 3D printer mengalami kendala saat pencetakan housing buoy yang membuat node sensor dipasang secara langsung untuk sementara agar dapat melihat jika node sensor berfungsi atau tidak.
2. Arduino Uno dalam kondisi fisik yang kurang baik karena slot untuk pin female dupont terutama pada input analog bersifat longgar yang membuat pembacaan sensor kurang konsisten dan sering kali tidak terdeteksi.
3. ESP32-C6 sebagai MCU pada Gateway mengalami kegagalan dimana MCU tersebut tidak dapat memberi output pada serial monitor. 

## Firmware V1
Gateway
```cpp
#include <ArduinoJson.h>

constexpr int RS485_RX = 2;
constexpr int RS485_TX = 3;

void setup() {
  Serial.begin(115200);
  Serial1.begin(9600, SERIAL_8N1, RS485_RX, RS485_TX);
  Serial1.setTimeout(200);
}

void loop() {
  if (!Serial1.available()) return;

  String line = Serial1.readStringUntil('\n');
  JsonDocument doc;
  DeserializationError err = deserializeJson(doc, line);
  if (err) {
    Serial.printf("Bad packet: %s\n", err.c_str());  
    return;
  }

  float turbV = doc["turb_v"];
  int   turbRaw = doc["turb_raw"];
  float tempC = doc["temp_c"] | NAN;   // NAN if the temp field was missing

  Serial.printf("Turbidity: %.2f V (raw %d), Temp: %.2f C\n", turbV, turbRaw, tempC);
  // to firebase
}
}
```

Buoy
```cpp
#include <SoftwareSerial.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <ArduinoJson.h>

#define ONE_WIRE_BUS 2
#define RS485_RX 10
#define RS485_TX 11

OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
SoftwareSerial rs485(RS485_RX, RS485_TX);

void setup() {
  Serial.begin(9600);   // USB debug
  rs485.begin(9600);    // to XY-485
  sensors.begin();
}

void loop() {
  int raw = analogRead(A5);
  float voltage = raw * (5.0 / 1023.0);

  sensors.requestTemperatures();
  float tempC = sensors.getTempCByIndex(0);

  JsonDocument doc;
  doc["turb_v"] = voltage;
  doc["turb_raw"] = raw;
  if (tempC != DEVICE_DISCONNECTED_C) doc["temp_c"] = tempC;  // omit if probe unplugged

  serializeJson(doc, rs485);
  rs485.println();              // newline = end of message

  serializeJson(doc, Serial);   // local debug copy
  Serial.println();

  delay(1000);
}
```
