package com.mmxlabs.lngscheduler.emf.extras.plugin;

/**
 * <copyright>
 *
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.Resource.Factory.Descriptor;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mmxlabs.lngscheduler.emf.extras.UpgradingResourceFactory;

/**
 * The main plugin class to be used in the desktop.
 */
public class ExtrasPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static ExtrasPlugin plugin;

	/**
	 * The constructor.
	 */
	public ExtrasPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// register upgrading resource factory
		final Object current = Factory.Registry.INSTANCE
				.getExtensionToFactoryMap().get(
						Resource.Factory.Registry.DEFAULT_EXTENSION);
		final Factory delegate;
		if (current instanceof Descriptor) {
			delegate = ((Descriptor) current).createFactory();
		} else {
			delegate = (Factory) current;
		}

		// this only works once per runtime.
//		Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
//				Resource.Factory.Registry.DEFAULT_EXTENSION,
//				new UpgradingResourceFactory(delegate));


		Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("scenario",
				new UpgradingResourceFactory(new XMIResourceFactoryImpl()));
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static ExtrasPlugin getDefault() {
		return plugin;
	}
}
