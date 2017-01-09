/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.components.EMFReportView;

/**
 * Helper class for managing columns that appear when in Pin/Diff mode
 * 
 * @author berkan
 */
public class PinDiffModeColumnManager {
	private final Map<String, EObject> pinnedObjects = new LinkedHashMap<>();
	private final Map<String, Set<EObject>> unpinnedObjects = new LinkedHashMap<>();

	private final Map<String, String> permutationGroupPrefix = new HashMap<>();
	private int nextPrefix = 1;

	private boolean showPinnedData = true;

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
		permutationGroupPrefix.clear();
		nextPrefix = 1;
	}

	public String getPermutationGroupPrefix(final String permutationGroup) {
		if (permutationGroupPrefix.containsKey(permutationGroup)) {
			return permutationGroupPrefix.get(permutationGroup);
		}
		final String prefix = Integer.toString(nextPrefix++);
		permutationGroupPrefix.put(permutationGroup, prefix);
		return prefix;
	}

	public boolean showPinnedData() {
		return showPinnedData;
	}

	public void setShowPinnedData(final boolean show) {
		this.showPinnedData = show;
	}
}
