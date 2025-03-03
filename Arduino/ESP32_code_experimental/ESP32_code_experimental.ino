#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "config.h"

String serverUrl = String("http://") + SERVER_IP + ":" + SERVER_PORT + "/device/data";

StaticJsonDocument<200> doc;
String jsonString;

void setup() {
    Serial.begin(115200);

    pinMode(2, OUTPUT);

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to WiFi");

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("\nConnected to WiFi!");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());

    // Get ESP32's default MAC address
    Serial.print("ESP32 MAC Address: ");
    Serial.println(WiFi.macAddress());

    doc["device"] = "ESP32";
    doc["message"] = "Hello World";
    doc["mac"] = WiFi.macAddress();
    doc["type"] = 1;

    Serial.println(serverUrl);

    serializeJson(doc, jsonString);
}

void loop() {
  for(int i = 0; i < 5; i++){
    if (WiFi.status() == WL_CONNECTED) {
        digitalWrite(2, HIGH);
        HTTPClient http;
        http.begin(serverUrl);
        http.addHeader("Content-Type", "application/json");

        // Recreate JSON before sending
        doc["device"] = "ESP32";
        doc["message"] = "Hello World";
        doc["mac"] = WiFi.macAddress();
        jsonString = "";
        serializeJson(doc, jsonString);

        int httpResponseCode = http.POST(jsonString);

        Serial.print("Response code: ");
        Serial.println(httpResponseCode);
        http.end();
        digitalWrite(2, LOW);
    }
    delay(10000);  // Send data every 10 seconds
  }
}

