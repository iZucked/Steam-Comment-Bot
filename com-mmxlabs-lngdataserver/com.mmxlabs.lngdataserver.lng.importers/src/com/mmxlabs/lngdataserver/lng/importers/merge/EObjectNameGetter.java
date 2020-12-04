package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.emf.ecore.EObject;

public interface EObjectNameGetter {
	public String getName(EObject eObject);
}
