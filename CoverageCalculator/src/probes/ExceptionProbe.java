package probes;

public class ExceptionProbe extends Probe {

    private final Exception e;

    ExceptionProbe(Probe probe, Exception e) {
        super(probe.getId(), probe.getMethodSignature());
        this.e = e;
    }

    public Exception getException() {
        return e;
    }
}
