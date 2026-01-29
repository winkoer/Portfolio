# java_math

Dieses Repository enthält eine selbst implementierte Sammlung von mathematischen Funktionen mit Fokus auf Matrizen- und Vektorrechnung. Alle Funktionen werden ohne externe Bibliotheken umgesetzt und dienen dem besseren Verständnis der zugrunde liegenden mathematischen Konzepte.

Das Projekt ist als Lern- und Übungsprojekt gedacht, insbesondere für lineare Algebra auf Hochschulniveau.

## Inhalt

Implementiert sind grundlegende Operationen aus der linearen Algebra, darunter:

### Matrizen
- matrix_vektor_multiplikation
- matrix_multiplikation
- betrag
- betrag_matrize
- betrag_vektor
- transponieren_reverse
- matrix_addieren
- matrix_subtrahieren
- matrix_gleich
- matrix_ketten_multiplikation
- matrix_ketten_multiplikation_rec

### Vektoren
- Vektor_addition
- Skalarmultiplikation
- Skalarprodukt

## Motivation

Ziel dieses Projekts ist es, mathematische Funktionen nicht nur anzuwenden, sondern vollständig selbst zu implementieren. Dadurch soll ein tieferes Verständnis für lineare Algebra, Dimensionsabhängigkeiten und typische Fehlerquellen entstehen.

Das Projekt verzichtet bewusst auf vorhandene Mathematik-Bibliotheken.


## Beispiel

```java
double[][] a = {
    {1, -2, 3},
    {4, 0, -1}
};

double[] v = {2, -1, 3};

double[] result = Matrizen.matrix_vektor_multiplikation(a, v);
