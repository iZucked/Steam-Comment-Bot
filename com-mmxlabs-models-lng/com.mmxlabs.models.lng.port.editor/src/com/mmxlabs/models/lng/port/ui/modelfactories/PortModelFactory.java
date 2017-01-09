/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 * Extended {@link DefaultModelFactory} to create a default {@link Location} when a new {@link Port} is created.
 * 
 * @author Simon Goodall
 * 
 */
public class PortModelFactory extends DefaultModelFactory {

	public PortModelFactory() {
		super();
	}

	@Override
	protected void postprocess(final EObject top) {
		super.postprocess(top);

		if (top instanceof Port) {
			final Port port = (Port) top;
			final Location location = PortFactory.eINSTANCE.createLocation();

			port.setLocation(location);
		}
	}

}