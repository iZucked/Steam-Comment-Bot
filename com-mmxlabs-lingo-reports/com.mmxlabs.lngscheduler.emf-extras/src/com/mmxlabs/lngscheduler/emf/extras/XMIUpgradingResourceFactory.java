/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Implementation of {@link UpgradingResourceFactory} wrapping a {@link XMIResourceFactoryImpl}.
 * 
 * @author Simon Goodall
 * 
 */
public final class XMIUpgradingResourceFactory extends UpgradingResourceFactory {

	public XMIUpgradingResourceFactory() {
		super(new XMIResourceFactoryImpl());
	}
}
