package fixtures.r3;

import org.jspecify.annotations.Nullable;

public class BadAboveLocal {
    public void baz() {
        @Nullable
        String s = null;
    }
}
