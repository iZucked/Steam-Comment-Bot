/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.emf.ecore.EObject;

public interface EObjectNameGetter {
	public String getName(EObject eObject);
}
