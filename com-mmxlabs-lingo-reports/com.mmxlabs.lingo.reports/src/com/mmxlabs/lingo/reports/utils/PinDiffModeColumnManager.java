package com.mmxlabs.lingo.reports.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.ColumnHandler;
import com.mmxlabs.lingo.reports.views.EMFReportView;

/**
 * Helper class for managing columns that appear when in Pin/Diff mode
 * 
 * @author berkan
 */
public class PinDiffModeColumnManager {
	private final Map<String, EObject> pinnedObjects = new LinkedHashMap<>();

	private EMFReportView parentView;

	public PinDiffModeColumnManager(EMFReportView parentView) {
		this.parentView = parentView;
	}

	public Map<String, EObject> getPinnedObjects() {
		return pinnedObjects;
	}

	public final boolean pinnedObjectsContains(EObject obj) {
		return pinnedObjects.containsValue(obj);
	}

	public EObject getPinnedObjectWithTheSameKeyAsThisObject(EObject eObj) {
		return pinnedObjects.get(parentView.getElementKey(eObj));
	}
}
