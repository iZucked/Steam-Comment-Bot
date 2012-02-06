package com.mmxlabs.models.ui;

import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

public class ComponentHelperRegistry {
	private static final String COMPONENT_HELPER_EXTENSION_ID="com.mmxlabs.models.ui.componenthelpers";
	private final IComponentHelper nullHelper = new IComponentHelper() {
		@Override
		public void addEditorsToComposite(IDetailComposite detailComposite) {}
	};
	private WeakHashMap<EClass, IComponentHelper> cache = new WeakHashMap<EClass, IComponentHelper>();
	public ComponentHelperRegistry() {
		
	}
	
	private int getMinimumGenerations(final EClass eClass, final String canonicalNameOfSuper) {
		if (eClass.getInstanceClass().getCanonicalName().equals(canonicalNameOfSuper)) {
			return 0;
		}
		int result = Integer.MAX_VALUE;
		for (final EClass superClass : eClass.getESuperTypes()) {
			final int d = getMinimumGenerations(superClass, canonicalNameOfSuper);
			if (d-1 < result-1) {
				result = d+1;
			}
		}
		return result;
	}
	
	public synchronized IComponentHelper getComponentHelper(final EClass modelClass) {
		IComponentHelper result = cache.get(modelClass);
		if (result == null) {
			IConfigurationElement bestElement = null;
			int distance = Integer.MAX_VALUE;
			top:for (final IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(COMPONENT_HELPER_EXTENSION_ID)) {
				for (final IConfigurationElement helperElement : element.getChildren("componentHelper")) {
					final String modelClassName = helperElement.getAttribute("modelClass");
					final String inheritable = helperElement.getAttribute("inheritable");
					if (modelClass.getInstanceClass().getCanonicalName().equals(modelClassName)) {
						// we are done
						bestElement = helperElement;
						break top;
					} else if (inheritable.equalsIgnoreCase("true")) {
						int thisDistance = getMinimumGenerations(modelClass, modelClassName);
						if (thisDistance < distance) {
							distance = thisDistance;
							bestElement = helperElement;
						}
					}
				}
			}
			
			if (bestElement != null) {
				try {
					result = (IComponentHelper) bestElement.createExecutableExtension("helperClass");
				} catch (CoreException e) {
				}
			}
			if (result == null) {
				cache.put(modelClass, nullHelper);
			} else {
				cache.put(modelClass, result);
			}
			return result;
		}
		return result == nullHelper ? null : result;
	}
}
