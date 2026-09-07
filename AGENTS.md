# Arbeitsregeln

Ton, Schreibweise und Commit-Regeln stehen in der Nutzer-Konfiguration
(`~/.claude/CLAUDE.md`, Abschnitte "Schreibweise in deutschen Texten" und
"Arbeitsregeln in Repos"). Hier steht nur, was in diesem Repo dazukommt oder
abweicht.

## Folien

Dreizehn Marp-Decks unter `slides/<NN_Thema>/slides.md`, ein Sprachfeature je
Deck.

- **Unpersönlich.** Anders als die Aufgabenstellungen sprechen die Folien
  niemanden an: kein "du", kein "Sie", kein "ihr". Über 2000 Zeilen kommt kein
  einziges "du" vor. Sachverhalte stehen als Aussage da ("Records sind implizit
  final"), Vorgänge im Passiv oder mit "lässt sich". Diesen Stil beibehalten. Die
  Regel gegen Leseransprache gilt hier ohne Ausnahme.
- `header` nennt die JDK-Version, in der das Feature kam, dann das Thema:
  `Java 17: Records`, `Java 15: Textblöcke`, `Java 11-17: Weitere Neuerungen`.
  Die Version gehört zur Aussage des Decks; beim Verschieben eines Themas
  mitprüfen.
- `footer: Alexander Erben`, `paginate: true`.
- Titelfolie ist `# Thema` ohne Modulnummer.
- Folientitel als `## Thema: Aspekt`. Das Deck wiederholt seinen eigenen Namen
  als Präfix (`## Records: Kanonischer Konstruktor`). Etwa ein Fünftel der
  Folien nutzt das; innerhalb eines Decks einheitlich bleiben.
- Aufzählungen mit `*`, nicht `-`.
- Ein Feature wird zuerst als Kurzdefinition genannt, dann am kleinsten
  lauffähigen Beispiel gezeigt, dann in seinen Regeln aufgelistet.
- Wo der Compiler etwas erzeugt, steht das erzeugte Ergebnis als Java-Code
  daneben, eingeleitet mit `// Der Compiler erzeugt ungefähr:`.
- Erwartete Ausgaben als Zeilenkommentar hinter der Anweisung
  (`System.out.println(p.firstName()); // "Ada"`).

## Aufgabenstellungen

Jedes Verzeichnis unter `assignments/` ist ein eigenes Maven-Modul mit einer
`README.md`. Sie sind Lehrmaterial und heben die Pronomen- und
Leseransprache-Regel auf.

- **Geduzt.** "führt dich heran", "deine Aufgabe", "Forme ... um", "Ersetze".
- Erster Absatz nennt die Übungsdomäne und den Ausgangszustand des
  mitgelieferten Codes ("Der vorhandene Code nutzt noch klassische, mutable
  POJOs").
- `## Was liegt bereit?` listet die vorbereiteten Klassen mit ihrem Pfad im
  Package-Baum und einem Halbsatz, was sie heute tun.
- Danach die Aufgaben als nummerierte Etappen mit fettem Titel, darunter
  eingerückte Stichpunkte mit dem, was konkret zu ändern ist.
- Jede Etappe nennt die Typnamen und Methodennamen, die entstehen sollen
  (`withEmail`, `withAddedModule(String)`), aber keinen fertigen Code.
- Lernziele, wo vorhanden, als `## Lernziele` am Anfang.
- Bonusaufgaben als eigener Abschnitt am Ende, mit dem Schwierigkeitsgrad im
  Titel (`## Bonusaufgabe (nicht einfach!)`).

## Vor dem Abschluss

- `pre-commit run --all-files` und `mvn -q verify` im Wurzelverzeichnis laufen
  lassen.

## Aufbau dieses Repos

Kurs zu den Sprachneuerungen zwischen Java 8 und Java 21. Wurzel-POM
`java17-course` mit drei Reaktor-Modulen:

- `assignments/`: Übungsprojekte mit unfertigem Ausgangscode.
- `solutions/`: die zugehörigen Lösungen, Namensschema
  `<assignment>-solution`.
- `trainer/`: Beispiele, die nur der Trainer vorführt: die beiden
  Jigsaw-Module, JPA-mit-Records, ein Jakarta-Workshop.

`slides/01_...` bis `slides/13_...` sind vom Reaktor unabhängig und laufen über
die `pdf-publisher`-Komponente.

Ein neues Assignment braucht drei Eintragungen: das Verzeichnis, den
`<module>`-Eintrag in `assignments/pom.xml` und die Lösung samt Eintrag in
`solutions/pom.xml`.

## Fallstricke dieses Repos

- **`assignments/jdeprscan-practice` fehlt in `assignments/pom.xml`.** Neun
  Module stehen zehn Verzeichnissen gegenüber; dieses Assignment wird vom
  Reaktor nicht gebaut. Ein grünes `mvn verify` sagt nichts über seinen
  Zustand aus.
- **`assignments/workshop-photo-pdf` hat keine Lösung**: die anderen
  Workshop-Assignments haben eine. Und `solutions/workshop-insurance-solutions`
  bricht das Namensschema mit einem Plural-`s`.
- **Fast kein Assignment hat Tests.** Im gesamten `assignments/`-Baum liegen
  drei Testklassen. Die Abnahme läuft über das Ausführen des jeweiligen
  Playground-Mains, nicht über einen grünen Testlauf.
- **Der Parent ist `spring-boot-starter-parent` 4.0.2 mit `java.version` 21**,
  obwohl der Kurs "Java 17" heißt. Beim Anlegen eines Moduls keine eigene
  Compiler-Version setzen, sonst weicht es vom Rest ab.
- **`slides/04_Textblöcke` trägt einen Umlaut im Pfad.** Nicht umbenennen, ohne
  alle Verweise mitzuziehen.
- **In den Folien stehen geschützte Bindestriche (U+2011)**, 30 Stück, etwa in
  "Accessor‑Methoden". Eine Suche nach dem normalen `-` findet sie nicht.
  Beim Kopieren von Folientext in Code oder Suchmuster darauf achten.
- **Die Gliederung der Aufgabenstellungen ist uneinheitlich**: mal
  `## Aufgaben in Etappen`, mal `## Hands-on-Aufgaben`, mal `## Was zu tun
  ist`. Beim Bearbeiten das Schema der jeweiligen Datei fortführen.
- **Die CI hat eine `deploy`-Stage, aber keinen Job darin.** Es gibt keine
  `training-deploy`-Komponente; dieses Repo wird nicht als Website deployt.
- **Die CI läuft auf zwei Plattformen.** `.gitlab-ci.yml` bindet die
  GitLab-Komponenten ein, `.github/workflows/ci.yml` ruft `lint.yml`,
  `slides.yml`, `maven.yml`, `release.yml` und `pages.yml` aus
  `it-erben/ci`. Die PDFs gehen
  dort auf GitHub Pages.
