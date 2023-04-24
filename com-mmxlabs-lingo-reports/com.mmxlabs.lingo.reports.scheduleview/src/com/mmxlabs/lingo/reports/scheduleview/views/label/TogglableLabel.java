/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.label;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.ui.IMemento;

@NonNullByDefault
public class TogglableLabel extends AbstractMementoBasedLabellingOption {

	public TogglableLabel(final String mementoLabel, final IMemento memento) {
		super(mementoLabel, memento);
		final Boolean savedState = this.memento.getBoolean(mementoLabel);
		if (savedState != null) {
			showing = savedState.booleanValue();
		}
	}

	public void toggle() {
		showing = !showing;
		memento.putBoolean(mementoLabel, showing);
	}

	public void reset() {
		if (showing) {
			showing = false;
			this.memento.putBoolean(mementoLabel, showing);
		}
	}
}
