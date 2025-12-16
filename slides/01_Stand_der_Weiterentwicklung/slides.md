---
marp: true
theme: default
header: Neuerungen seit Java 11
footer: Alexander Erben
paginate: true
---
<style>
img[alt~="center"] {
  display: block;
  margin: 0 auto;
}
section > h2 {
  position: absolute;
  top: 110px;
  left: 80px;
}
</style>

# Java seit Version 11 - Stand der Weiterentwicklung

---

## Historie

![center](./images/01_Historie.drawio.png)

---

## JDK Enhancement Proposal (JEP)

Das JEP‐Verfahren ist das strukturierte Vorgehen, um neue Features, Refactorings oder Tooling‐Anpassungen in das JDK einzubringen.

<hr>

![](./images/01_JEP_startpage.png)
[Link](https://openjdk.org/jeps/0)

---

## JDK Enhancement Proposal (JEP)

Eine JEP beschreibt:
* Motivation und Problemstellung
* Design‐Ansatz inklusive Diskussion alternativer Ideen
* Auswirkungen auf Kompatibilität
* Performance und Sicherheit
* Tests, Referenzimplementierung und Interaktion mit Spezifikationen

---

## JEP-Prozess

![center](./images/01_JEP-Prozess.drawio.png)

---

## JEP-Prozess

* JEPs können zwischen Releases verschoben oder verworfen werden.
* Features erst dann einkalkulieren, wenn eine JEP tatsächlich Targeted oder Completed ist.
* JEP‐Texte als Referenz für Architektur‐ und Coding‐Guidelines nutzen (z.B. Pattern Matching, Records).
* Frühzeitig Preview‐ oder Early‐Access‐Builds testen, wenn eine JEP den eigenen Code betrifft.

---
## Incubating- vs. Preview-Features

![center](images/01_Incubating_Preview.drawio.png)

---

## Beispiel: JEP 321

![center height:400](images/01_jep321.png)
