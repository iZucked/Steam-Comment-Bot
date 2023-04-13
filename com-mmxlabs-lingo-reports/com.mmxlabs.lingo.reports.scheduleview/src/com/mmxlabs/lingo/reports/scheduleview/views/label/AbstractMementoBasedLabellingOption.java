/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.label;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.ui.IMemento;

@NonNullByDefault
public abstract class AbstractMementoBasedLabellingOption implements ILabellingOption {

	protected final String mementoLabel;
	protected final IMemento memento;
	protected boolean showing = false;

	protected AbstractMementoBasedLabellingOption(final String mementoLabel, final IMemento memento) {
		this.mementoLabel = mementoLabel;
		this.memento = memento;
	}
	
	@Override
	public boolean isShowing() {
		return showing;
	}
}
