/*
  R1 = R2 = 10k, divider factor 2.0.
*/

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <ESP8266HTTPClient.h>
#include <time.h>

const char* WIFI_SSID     = "Ante";
const char* WIFI_PASSWORD = "";           

const char* FIREBASE_HOST = "zavrsni-3f1ea-default-rtdb.europe-west1.firebasedatabase.app";
const char* DEVICE_ID     = "device_001";  

#define MQ2_PIN A0

const float DIVIDER_FACTOR = 2.0;   // (R1+R2)/R2, both 10k
const float SUPPLY_VOLTAGE = 5.0;

const float CLEAN_AIR_RATIO = 9.83; // Rs/R0 in clean air, from the datasheet

// ppm = a * (Rs/R0)^b, coefficients fitted to the MQ-2 datasheet curves
const float CO_A    = 36974.0,  CO_B    = -3.109;
const float SMOKE_A = 3616.1,   SMOKE_B = -2.675;

const float CO_THRESHOLD_PPM    = 5.0;   
const float SMOKE_THRESHOLD_PPM = 10.0;

const unsigned long WARMUP_MS           = 180000;  
const unsigned long CURRENT_INTERVAL_MS = 5000;    // write to current every 5 s
const unsigned long HISTORY_INTERVAL_MS = 60000;   // write to history every 60 s


float R0 = 0.0;              

float coBaseline = 0.0;
float smokeBaseline = 0.0;

unsigned long lastCurrentUpload = 0;
unsigned long lastHistoryUpload = 0;

uint64_t baseEpochMs = 0;    
unsigned long baseMillis = 0;  


float readRaw(int samples = 20) {
  long total = 0;
  for (int i = 0; i < samples; i++) {
    total += analogRead(MQ2_PIN);
    delay(5);
  }
  return (float)total / samples;
}
float readRs() {
  float raw = readRaw();
  float pinVoltage = raw * (3.3 / 1023.0);
  float sensorVoltage = pinVoltage * DIVIDER_FACTOR;

  if (sensorVoltage < 0.05) return -1.0;

  return (SUPPLY_VOLTAGE - sensorVoltage) / sensorVoltage;
}

float ppmFromRatio(float ratio, float a, float b) {
  if (ratio <= 0) return 0.0;
  float value = a * pow(ratio, b);
  if (value < 0) value = 0;
  if (value > 10000) value = 10000;
  return value;
}

float roundTo1(float v) {
  return roundf(v * 10.0) / 10.0;
}

void connectToWiFi() {
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);

  WiFi.mode(WIFI_STA);

  if (strlen(WIFI_PASSWORD) == 0) {
    WiFi.begin(WIFI_SSID);
  } else {
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  }

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  Serial.println();
  Serial.print("Connected. IP address: ");
  Serial.println(WiFi.localIP());
}


void syncTime() {
  Serial.print("Fetching network time");
  configTime(0, 0, "hr.pool.ntp.org", "pool.ntp.org");

  time_t now = time(nullptr);
  while (now < 1700000000) {   
    Serial.print(".");
    delay(500);
    now = time(nullptr);
  }

  baseEpochMs = (uint64_t)now * 1000ULL;
  baseMillis = millis();

  Serial.println();
  Serial.println("Time synchronised.");
}

uint64_t currentEpochMs() {
  return baseEpochMs + (uint64_t)(millis() - baseMillis);
}

void u64ToString(uint64_t value, char* out) {
  char buffer[21];
  int i = 0;
  if (value == 0) { out[0] = '0'; out[1] = '\0'; return; }
  while (value > 0) {
    buffer[i++] = '0' + (value % 10);
    value /= 10;
  }
  int j = 0;
  while (i > 0) out[j++] = buffer[--i];
  out[j] = '\0';
}

void calibrate() {
  Serial.print("Calibrating in clean air");

  float total = 0;
  int valid = 0;
  for (int i = 0; i < 25; i++) {
    float rs = readRs();
    if (rs > 0) { total += rs; valid++; }
    Serial.print(".");
    delay(200);
  }
  Serial.println();

  if (valid == 0) {
    Serial.println("ERROR: no valid sensor readings. Check the wiring.");
    R0 = 1.0;
    return;
  }

  R0 = (total / valid) / CLEAN_AIR_RATIO;

  coBaseline    = ppmFromRatio(CLEAN_AIR_RATIO, CO_A, CO_B);
  smokeBaseline = ppmFromRatio(CLEAN_AIR_RATIO, SMOKE_A, SMOKE_B);

  Serial.print("R0 = ");
  Serial.println(R0, 3);
  Serial.print("Clean air baseline: CO ");
  Serial.print(coBaseline, 1);
  Serial.print(" ppm, smoke ");
  Serial.print(smokeBaseline, 1);
  Serial.println(" ppm (subtracted from every reading)");
}

bool uploadToFirebase(const String& path, const String& payload) {
  WiFiClientSecure client;
  client.setInsecure();
  client.setBufferSizes(1024, 1024); 

  HTTPClient http;
  String url = "https://" + String(FIREBASE_HOST) + path;

  if (!http.begin(client, url)) {
    Serial.println("ERROR: could not open the connection.");
    return false;
  }

  http.addHeader("Content-Type", "application/json");
  int code = http.PUT(payload);
  http.end();

  if (code == HTTP_CODE_OK) return true;

  Serial.print("ERROR while uploading, HTTP code: ");
  Serial.println(code);
  return false;
}

void setup() {
  Serial.begin(115200);
  delay(100);
  pinMode(MQ2_PIN, INPUT);

  Serial.println();
  Serial.println("=== FireGuard sensor node ===");
  Serial.print("Warming up the sensor for ");
  Serial.print(WARMUP_MS / 1000);
  Serial.println(" seconds...");

  unsigned long start = millis();
  while (millis() - start < WARMUP_MS) {
    Serial.print(".");
    delay(1000);
  }
  Serial.println();

  calibrate();
  connectToWiFi();
  syncTime();

  Serial.println("Starting uploads.");
  Serial.println();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Connection lost, reconnecting...");
    connectToWiFi();
  }

  unsigned long now = millis();
  if (now - lastCurrentUpload < CURRENT_INTERVAL_MS) return;
  lastCurrentUpload = now;

  float rs = readRs();
  if (rs < 0) {
    Serial.println("Invalid sensor reading, skipping this cycle.");
    return;
  }

  float ratio = rs / R0;

  float coRaw = ppmFromRatio(ratio, CO_A, CO_B) - coBaseline;
  float smokeRaw = ppmFromRatio(ratio, SMOKE_A, SMOKE_B) - smokeBaseline;
  if (coRaw < 0) coRaw = 0;
  if (smokeRaw < 0) smokeRaw = 0;

  float coLevel = roundTo1(coRaw);
  float smokeLevel = roundTo1(smokeRaw);

  float temperature = 0.0;

  bool safe = (coLevel <= CO_THRESHOLD_PPM) && (smokeLevel <= SMOKE_THRESHOLD_PPM);

  char timestamp[21];
  u64ToString(currentEpochMs(), timestamp);

  String payload = "{";
  payload += "\"coLevel\":" + String(coLevel, 1) + ",";
  payload += "\"smokeLevel\":" + String(smokeLevel, 1) + ",";
  payload += "\"temperature\":" + String(temperature, 1) + ",";
  payload += "\"isSystemSafe\":" + String(safe ? "true" : "false") + ",";
  payload += "\"timestamp\":" + String(timestamp);
  payload += "}";

  Serial.print("Rs/R0 = ");
  Serial.print(ratio, 2);
  Serial.print(" | CO = ");
  Serial.print(coLevel, 1);
  Serial.print(" ppm | smoke = ");
  Serial.print(smokeLevel, 1);
  Serial.print(" ppm | state: ");
  Serial.println(safe ? "safe" : "DANGER");

  String currentPath = "/sensors/" + String(DEVICE_ID) + "/current.json";
  uploadToFirebase(currentPath, payload);

  if (now - lastHistoryUpload >= HISTORY_INTERVAL_MS) {
    lastHistoryUpload = now;
    String historyPath = "/sensors/" + String(DEVICE_ID) + "/history/" + String(timestamp) + ".json";
    uploadToFirebase(historyPath, payload);
    Serial.println("   -> record added to history");
  }
}
