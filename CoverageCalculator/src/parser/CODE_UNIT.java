package parser;

import java.io.Serializable;

/**
 * Enum corresponding to a level in the project hierarchy. Note that this does not correspond
 * to a specific type - e.g. a node of type CODE_UNIT.CLASS may be an interface or an enum.
 */
public enum CODE_UNIT implements Serializable {
    ROOT(0), PACKAGE(1), CLASS(2), METHOD(3);

    private final int depth;

    CODE_UNIT(int value) {
        this.depth = value;
    }

    /**
     * @return A value corresponding to the depth of the level within the project hierarchy.
     */
    public int getDepth() {
        return depth;
    }
}
