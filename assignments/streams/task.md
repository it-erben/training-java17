# Aufgabe
- Implementiere alle Methoden in `MovieRatingsService`, so wie sie im Javadoc spezifiziert sind. 
- Schreibe auch Tests um zu prüfen, ob deine Implementierungen funktionieren.

# Bonusaufgabe (nicht einfach!)
- Ergänze die Klasse mit einer generischen `getRatingsSortedByField` - Methode, welche es erlaubt, nach jedem Feld in `MovieRating` zu filtern. Man soll sie auf folgende Weise verwenden können:
```java
public static void main(String[]args) {
    new MovieRatingsService(MovieRatingsDataset.getInstance())
        .getRatingsSortedByField(MovieRating::name, true) // true steht für ascending
        .getRatingsSortedByField(MovieRating::rating, false) // false steht für descending
        .forEach(System.out::println);  
}
```