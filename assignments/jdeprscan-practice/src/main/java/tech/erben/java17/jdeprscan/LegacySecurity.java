package tech.erben.java17.jdeprscan;

public class LegacySecurity {

    public SecurityManager installBasicManager() {
        SecurityManager manager = new SecurityManager();
        System.setSecurityManager(manager);
        return manager;
    }

    public SecurityManager currentManager() {
        return System.getSecurityManager();
    }
}
