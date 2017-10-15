package probes;

public class ExceptionProbe extends Probe {

    private Exception e;

    public ExceptionProbe(Probe probe, Exception e) {
        super(probe.getId(), probe.getType(), probe.getMethodSignature());
        this.e = e;
    }

    public Exception getException() {
        return e;
    }
}