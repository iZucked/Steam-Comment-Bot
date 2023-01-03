/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;

@NonNullByDefault
public interface ISelectedScenariosServiceListener {

	/**
	 * Notify listener there is a new set of data to process. The block flag can be set to true to request synchronous update or left false to process asynchronously and return quickly.
	 */
	void selectedDataProviderChanged(ISelectedDataProvider selectedDataProvider, boolean block);

	default void selectedObjectChanged(@Nullable MPart source, ISelection selection) {
		// Do nothing by default
	}

	default void diffOptionChanged(EDiffOption option, Object oldValue, Object newValue) {
		// Do nothing by default
	}
}
