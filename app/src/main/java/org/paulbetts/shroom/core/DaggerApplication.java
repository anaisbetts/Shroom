package org.paulbetts.shroom.core;

import android.app.Application;

import org.paulbetts.shroom.AndroidModule;

import dagger.ObjectGraph;

/**
 * Created by paul on 8/3/14.
 */
public class DaggerApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(new AndroidModule(this));
    }

    public void inject(Object target){ graph.inject(target); }

    public ObjectGraph getApplicationGraph() { return graph; }
}
