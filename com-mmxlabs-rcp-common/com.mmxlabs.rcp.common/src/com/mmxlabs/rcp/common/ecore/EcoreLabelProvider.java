/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * Simple subclass of {@link AdapterFactoryLabelProvider} with a default constructor to pass in our {@link EcoreComposedAdapterFactory} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class EcoreLabelProvider extends AdapterFactoryLabelProvider {

	public EcoreLabelProvider() {
		super(EcoreComposedAdapterFactory.getAdapterFactory());
	}
}
