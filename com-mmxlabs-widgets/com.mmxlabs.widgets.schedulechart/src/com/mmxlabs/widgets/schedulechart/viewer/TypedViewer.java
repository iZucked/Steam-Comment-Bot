/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.viewer;

import org.eclipse.jface.viewers.Viewer;

public abstract class TypedViewer<T> extends Viewer {
	
	@Override
	public abstract T getInput();
	
	/**
	 * DON'T CALL OR OVERRIDE THIS METHOD, USE {@link typedInputChanged} INSTEAD.
	 */ 
	@Override
	public void setInput(Object input) {
		typedSetInput((T) input);
	}
	
	/**
	 * DON'T CALL OR OVERRIDE THIS METHOD, USE {@link typedInputChanged} INSTEAD.
	 */ 
	@Override
	public void inputChanged(Object input, Object oldInput) {
		typedInputChanged((T) input, (T) oldInput);
	}
	
	public T typedGetInput() {
		return getInput();
	}
	
	public abstract void typedSetInput(T input);

	public abstract void typedInputChanged(T input, T oldInput);
}
