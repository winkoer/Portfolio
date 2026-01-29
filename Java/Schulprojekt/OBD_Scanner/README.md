# OBD-II Auslesegerät (Android App)

Schulprojekt – Seminarkurs Technisches Gymnasium Waldshut (TGI 12)  
Autor: Azamat Winkler  
Jahr: ca. 2022/2023

Eine einfache Android-Anwendung zum Auslesen von Echtzeit-Fahrzeugdaten über einen günstigen **Bluetooth OBD-II Adapter** (ELM327-kompatibel).

## Ziel des Projekts

- Einfaches, verständliches Dashboard für jedermann (keine komplizierte Profi-Software)
- Live-Anzeige wichtiger Parameter:  
  • Motordrehzahl (RPM)  
  • Geschwindigkeit  
  • Kühlwassertemperatur  
  • Ansauglufttemperatur  
  • Motorbelastung (Engine Load)
- Visuelle Warnungen bei kritischen Werten (rot/grün)
- Hoch-/Runterschalt-Empfehlung (einfache Logik)
- Zwei Screens (Tacho + Zusatzwerte) per Swipe wechselbar

## Features

- Bluetooth-Verbindung zu Standard-OBD-II-Adaptern (PIN: meist 1234)
- Eigener Thread für stabile, verzögerungsarme Datenabfrage (~100 ms)
- Analoge Tacho-Darstellung mit rotierender Nadel (Sin/Cos-Berechnung)
- Farbige Warnhinweise:
  - Rot bei Überlast (>90 %), hohen Drehzahlen (>3500 U/min), Überhitzung (>110 °C Wasser / >50 °C Luft)
  - Grün bei Betriebstemperatur (>70 °C Wasser)
- Umschalt-Empfehlung (LED-Indikatoren im Tacho)

## Technologie-Stack

- **Sprache**: Java (Android)
- **IDE**: Android Studio
- **Kommunikation**: Bluetooth Classic (Socket)
- **OBD-II-Befehle**: Standard-PID-Abfragen (01 0C, 01 0D, 01 05, …)
- **UI**: Custom Canvas-Zeichnung (Tacho), ViewPager/Swipe für Screens

## Verlauf & Meilensteine

1. Java-Desktop-Version mit simuliertem Auto (Socket 192.xxx.xxx.xxx)
2. Performance-Optimierung (Multi-Threading → 100 ms Delay)
3. Umstieg auf Android + reales Bluetooth-OBD-Gerät
4. Zweiter Tacho + Warnfarben + Schaltempfehlung

## Einschränkungen / To-do

- Funktioniert nur mit Fahrzeugen, die die abgefragten PIDs unterstützen (älteres Auto → wenige Sensoren)
- Keine Fehlercodes (DTC) Auslesung (noch)
- Keine Cloud-Speicherung / Langzeit-Logging
- Keine fortgeschrittene Fehlerbehandlung (viele Fälle → Default 0)

## Wichtige Erkenntnisse

- Recherche ≈ 30 % der Gesamtzeit – lohnt sich aber enorm
- Multi-Threading ist bei UI + IO **Pflicht**
- Bluetooth-Programmierung unter Android ist anfangs sehr knifflig
- Default-PIN fast aller ELM327-Adapter: **1234**

  
## Lizenz

MIT License (falls du den Code irgendwann hochladen möchtest)
