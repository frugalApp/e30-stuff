#include <SoftwareSerial.h>

char command[128] = "Q";
int panicCounter = 0;
SoftwareSerial bluetooth(8, 9);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  bluetooth.begin(115200);
  bluetooth.print("$$$");
  delay(100);
  bluetooth.println("U,9600,N");
  bluetooth.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  getInput();
  
  if (compareStringArrays(command, "1234")) {
    unlock();
    command[0] = '\0';
  }
  else if (compareStringArrays(command, "2345")) {
    lock();
    command[0] = '\0';
  }
  else if (compareStringArrays(command, "3456")) {
    if (panicCounter == 0) {
      panicCounter = 10;
    } else {
      panicCounter = 0;
      pinMode(6, INPUT);
      pinMode(7, INPUT);
    }
    command[0] = '\0';
  }

  panic();
  
  delay(100);
}

bool compareStringArrays(char* a, char* b) {
  int count = 0;
  while (a[count] != '\0' && b[count] != '\0') {
    if (a[count] != b[count++]) {
      return false;
    }
  }
  return a[count] == b[count];
}

void getInput() {
  int count = 0;
  while (bluetooth.available() && count < 4) {
    command[count++] = (char)bluetooth.read();
    bluetooth.print(command[count-1]);
  }
  if(count)  bluetooth.print("\n");
  command[count] = '\0';
}

void unlock() {
    pinMode(4, OUTPUT);
    pinMode(6, OUTPUT);
    pinMode(7, OUTPUT);
    delay(50);
    pinMode(7, INPUT);
    delay(200);
    pinMode(4, INPUT);
    delay(150);
    pinMode(6, INPUT);
    delay(400);
    pinMode(6, OUTPUT);
    delay(400);
    pinMode(6, INPUT);
}

void lock() {
    pinMode(5, OUTPUT);
    pinMode(6, OUTPUT);
    delay(250);
    pinMode(5, INPUT);
    delay(150);
    pinMode(6, INPUT);
    delay(400);
    pinMode(6, OUTPUT);
    delay(400);
    pinMode(6, INPUT);
}

void panic() {
  if (panicCounter > 0) {
    if (panicCounter / 2 * 2 == panicCounter) {
      pinMode(6, OUTPUT);
      pinMode(7, OUTPUT);
    } else {
      pinMode(6, INPUT);
      pinMode(7, INPUT);
    }
    delay(650);
    panicCounter--;
  }
}

