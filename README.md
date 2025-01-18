# **Hallo und herzlich willkommen zu unserer Wetter-App!** 🌦️

Schön, dass du hier bist! Diese kleine, aber feine Anwendung hilft dir dabei, das aktuelle Wetter in Städten weltweit zu entdecken. Je nachdem, was die API so hergibt!

---

## **Was kann die App?**

- **Stadtwetter suchen:**  
  Gib einfach den Namen einer Stadt in das Textfeld ein und klicke auf **"Suche Stadt"**. Schon bekommst du Infos wie:
  - Aktueller Wetterzustand (z. B. Sonnig, Wolkig)
  - Temperatur
  - Luftfeuchtigkeit
  - UV-Index
  - Windgeschwindigkeit  

- **Temperatur wechseln:**  
  Celsius oder Fahrenheit? Du entscheidest! Mit einem Klick auf **"Toggle Temperature Unit"** kannst du zwischen den beiden Einheiten wechseln.  

- **Winddetails anzeigen:**  
  Möchtest du wissen, wie stark der Wind weht oder aus welcher Richtung er kommt? Der Button **"Toggle Wind Info"** zeigt dir abwechselnd die Geschwindigkeit und die Richtung an.

- **Die heißeste Stadt finden:**  
  Interessiert, wo es aktuell am wärmsten ist? Mit **"Show City with the Highest Temperature"** durchforstet die App eine Liste von Städten und zeigt dir die heißeste an.  

- **Speichern und Laden von Wetterdaten:**  
  Die App speichert automatisch die zuletzt abgerufenen Wetterdaten in einer Datei namens `weather_data.json`. Beim nächsten Start werden die Daten geladen, damit du sofort die letzten Ergebnisse siehst.
  Diese findest du im deinem aktellen Projektverzeichnis.

---

## **So startest du die App**

1. **Voraussetzungen:**  
   - Stelle sicher, dass Java 17 (oder neuer) installiert ist.  
   - Die JavaFX-Bibliotheken müssen verfügbar sein (falls du mit einer IDE arbeitest, wie IntelliJ, ist das oft schon integriert).  

2. **Loslegen:**  
   - Öffne die Datei **`HelloApplication.java`** und starte sie.  
   - Es öffnet sich ein übersichtliches Fenster mit Eingabefeld, Buttons und Wetteranzeige.

3. **Bedienung:**  
   - Stadt eingeben, auf **"Suche Stadt"** klicken – fertig!  
   - Nutze die Buttons, um zwischen Temperaturanzeigen oder Winddetails zu wechseln oder die heißeste Stadt anzuzeigen.

---

## **Ein Blick hinter die Kulissen**

- **JavaFX** sorgt für die benutzerfreundliche Oberfläche.  
- **Multithreading** stellt sicher, dass die Wetterabfragen im Hintergrund laufen und die App nicht einfriert.  
- **Datei-IO** speichert die Wetterdaten als JSON-Datei, damit du sie später wieder abrufen kannst.  
- **Netzwerk**-Funktionalität (in Arbeit).  

---


### **Und jetzt – viel Spaß mit der Wetter-App!** ☀️🌧️🌪️  

