#include <Ethernet.h>

#include <LiquidCrystal_I2C.h>

#include "HX711.h"

#include <stdlib.h>

// HX711 circuit wiring
const int LOADCELL_DOUT_PIN = 3;
const int LOADCELL_SCK_PIN = 2;
long max_measure = 0;

//Funzionamento dei pulsanti
bool autoritenuta = false;
bool start = true;
bool stop = false;

char command;

int startButton = 8;
int stopButton = 7;
int p1;
int p2;


int contatore = 0;
unsigned long tempo = millis();

char testo_csv[] = "";
String prefisso = String("arduino");
String txt = String(".csv");


HX711 scale;
LiquidCrystal_I2C lcd(0x27, 20, 4);


void setup(){
  Serial.begin(921600);
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0, 0);
  lcd.clear();
  lcd.print("PRONTO PER ACQUISIRE");

  pinMode(startButton,INPUT);
  pinMode(stopButton,INPUT);


}

void loop() {

  p1 = digitalRead(startButton);
  p2 = digitalRead(stopButton);

  command = Serial.read();

  if(start && (p1 == 1 || command=='1')) {
    autoritenuta = true;
    start = false;
    stop = true;
    contatore = contatore + 1;
    tempo = millis();
    Serial.print("\nAVVIO\n");
  }

  if (stop && (p2 == 1 || command=='0')) {
    autoritenuta = false;
    start = true;
    stop = false;
    Serial.print("STOP\n");
    lcd.clear();
    lcd.print("TEST n." + String(contatore) + " SALVATO");
    lcd.setCursor(0, 2);
    lcd.print("PRONTO PER ACQUISIRE");
  }

  if (autoritenuta)
    if (scale.is_ready()) {
      float reading = ((scale.read() / 1000) - 13) / 44.2;
      if (max_measure < reading) {
        max_measure = reading;
      }
      unsigned long intervallo = millis() - tempo;

      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("FORZA [kg]:");
      lcd.setCursor(0, 1);
      lcd.print(reading);
      lcd.setCursor(0, 2);
      lcd.print("TEMPO [microS]:");
      lcd.setCursor(0, 3);
      lcd.print(intervallo);

      Serial.print(reading);
      Serial.print(",");
      Serial.print(intervallo);
      Serial.print("\n");
    } else {
      unsigned long intervallo = millis() - tempo;
      Serial.print("HX711 not found.");
      Serial.print(",");
      Serial.print(intervallo);
      Serial.print("\n");
      lcd.clear();
      lcd.print("Hx711 not found");
    }

  delay(50);
}