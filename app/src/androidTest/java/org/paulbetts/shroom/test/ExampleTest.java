package org.paulbetts.shroom.test;

import android.test.InstrumentationTestCase;

/**
 * Created by paul on 8/5/14.
 */
public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
}
