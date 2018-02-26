package com.yourorganization.maven_sample;

public class FooView {
	public boolean isBar() {
		return true;
	}

	public FooExtension getExtension() {
		return new FooExtension();
	}

	public IBarView getBarView() {
		return new BarView();
	}
	
	public IBarView getBarExt() {
		return new BarView();
	}
}
