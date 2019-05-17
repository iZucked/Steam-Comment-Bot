package com.mmxlabs.rcp.common.json;

import org.eclipse.emf.ecore.EStructuralFeature;

public interface IJSONSerialisationConfigProvider {
	boolean isFeatureIgnored(EStructuralFeature feature);
}
