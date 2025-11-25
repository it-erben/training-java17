package tech.erben.java17.jdeprscan;

public class LegacyThreadController {

    public void stopNow(Thread worker) {
        worker.stop();
    }

    public void pause(Thread worker) {
        worker.suspend();
    }

    public void resume(Thread worker) {
        worker.resume();
    }
}
