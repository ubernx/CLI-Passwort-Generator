Habe aus Eigeninteresse einen kleinen CLI-Passwortgenerator programmiert, um zu testen,
wie schnell Java als Programmiersprache an sich sein kann, wenn man abstrakte Konzepte wie Objektprogrammierung so stark wie möglich reduziert.
Statt referenzielle Datenstrukturen zu verwenden, beschränkte ich es explizit auf primitive Datenstrukturen.

Bezüglich der Funktionalität der Utilite:
Vor der Nutzung den Quellcode als .jar kompilieren und beim Starten einfach die Datei selbst aufrufen über den Terminal oder einen Launcher.

Gegeben sind folgende Commands:

- /exit: Schließt das Programm
- /generate: Leitet den Anwender zur anderen Abfragen
	> Passwortlänge;
	> Anzahl der Passwörter;
	> Modus 1 (Groß- und Kleinbuchstaben, Zahlen);
	> Modus 2 (Modus 1 + Sonderzeichen);
 	> Dateinausgabe (J/N); 
