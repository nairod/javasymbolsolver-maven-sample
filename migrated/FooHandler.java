package com.yourorganization.maven_sample;

public class FooHandler {

    FooView view = new FooView();

    public FooHandler() {
        view.isBar();
    }

    public void anotherMethod() {
        IBarView barExt = view.getBarView(IBarView.class);
    }
}
