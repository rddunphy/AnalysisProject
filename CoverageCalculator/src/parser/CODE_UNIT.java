package parser;

import java.io.Serializable;

public enum CODE_UNIT implements Serializable {
    ROOT(0), PACKAGE(1), CLASS(2), METHOD(3);

    private int depth;

    CODE_UNIT(int value) {
        this.depth = value;
    }

    public int getDepth() {
        return depth;
    }
}
