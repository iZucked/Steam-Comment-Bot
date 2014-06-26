package com.mmxlabs.lingo.reports.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.EMFReportView;

/**
 * Helper class for managing columns that appear when in Pin/Diff mode
 * 
 * @author berkan
 */
public class PinDiffModeColumnManager {
	private final Map<String, EObject> pinnedObjects = new LinkedHashMap<>();
	private final Map<String, Set<EObject>> unpinnedObjects = new LinkedHashMap<>();

	// FIXME: turn into an interface
	private final EMFReportView parentView;

	public PinDiffModeColumnManager(final EMFReportView parentView) {
		this.parentView = parentView;
	}

	public final boolean pinnedObjectsContains(final EObject obj) {
		return pinnedObjects.containsValue(obj);
	}

	public EObject getPinnedObjectWithTheSameKeyAsThisObject(final EObject eObj) {
		return pinnedObjects.get(parentView.getElementKey(eObj));
	}

	public Set<EObject> getUnpinnedObjectWithTheSameKeyAsThisObject(final EObject eObj) {
		return unpinnedObjects.get(parentView.getElementKey(eObj));
	}

	public void setPinnedObject(final String key, final EObject eObj) {
		pinnedObjects.put(key, eObj);
	}

	public void addUnpinnedObject(final String key, final EObject eObj) {
		final Set<EObject> objects;
		if (unpinnedObjects.containsKey(key)) {
			objects = unpinnedObjects.get(key);
		} else {
			objects = new HashSet<>();
			unpinnedObjects.put(key, objects);
		}
		objects.add(eObj);
	}

	public void reset() {
		pinnedObjects.clear();
		unpinnedObjects.clear();
	}
}
