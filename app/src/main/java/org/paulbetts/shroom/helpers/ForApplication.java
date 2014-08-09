package org.paulbetts.shroom.helpers;

// Cribbed from https://github.com/square/dagger/blob/master/examples/android-simple/src/main/java/com/example/dagger/simple/ForApplication.java

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface ForApplication {
}
