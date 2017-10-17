package calc.subpkg;

public enum SOME_ENUM {

    VALUE1(0), VALUE2(1), VALUE3(2);

    private int v;

    SOME_ENUM(int v) {
        this.v = v;
    }

    public SOME_ENUM getNext(SOME_ENUM e) {
        if (e == VALUE1) {
            return VALUE2;
        } else if (e == VALUE2) {
            return VALUE3;
        }
        return VALUE1;
    }
}
