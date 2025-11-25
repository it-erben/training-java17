package tech.erben.java17.jdeprscan;

import java.util.Observable;
import java.util.Observer;

public class LegacyNewsFeed extends Observable {

    public void subscribe(Observer observer) {
        addObserver(observer);
    }

    public void publish(String headline) {
        setChanged();
        notifyObservers(headline);
    }
}
