/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.label;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IMemento;

@NonNullByDefault
public class EnumBasedLabellingOption<T extends @Nullable Enum<T>> extends AbstractMementoBasedLabellingOption {

	private final Supplier<T[]> valuesProvider;
	private @Nullable T chosenOption = null;

	public EnumBasedLabellingOption(final String mementoLabel, final IMemento memento, final Supplier<T[]> valuesProvider) {
		super(mementoLabel, memento);
		this.valuesProvider = valuesProvider;
		final Integer savedState = this.memento.getInteger(mementoLabel);
		if (savedState != null && savedState > -1) {
			T[] values = valuesProvider.get();
			if (savedState < values.length) {
				showing = true;
				chosenOption = values[savedState.intValue()];
			}
		}
	}

	public void choose(final @Nullable T choice) {
		final int mementoOption;
		chosenOption = choice;
		if (choice == null) {
			showing = false;
			mementoOption = -1;
		} else {
			showing = true;
			mementoOption = choice.ordinal();
		}
		memento.putInteger(mementoLabel, mementoOption);
	}
	
	@Override
	public void reset() {
		if (showing) {
			showing = false;
			chosenOption = null;
			this.memento.putInteger(mementoLabel, -1);
		}
	}

	public @Nullable T getChosenOption() {
		return chosenOption;
	}

}
