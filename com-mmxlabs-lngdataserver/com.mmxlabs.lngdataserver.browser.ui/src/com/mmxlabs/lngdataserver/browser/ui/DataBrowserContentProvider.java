/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class DataBrowserContentProvider extends AdapterFactoryContentProvider {

	public DataBrowserContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	public DataBrowserContentProvider() {
		super(null);
	}
}
