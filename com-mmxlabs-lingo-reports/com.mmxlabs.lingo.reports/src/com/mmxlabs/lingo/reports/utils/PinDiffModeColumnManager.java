package com.mmxlabs.lingo.reports.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.EMFReportView;
import com.mmxlabs.lingo.reports.views.EMFReportView.IFormatter;

/**
 * Helper class for managing columns that appear when in Pin/Diff mode
 * 
 * @author berkan
 */
public class PinDiffModeColumnManager {
	private Map<String, IFormatter> columnRegister = new LinkedHashMap<String, IFormatter>();

	private final Map<String, EObject> pinnedObjects = new LinkedHashMap<String, EObject>();

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
	
	
	/**
	 * Register a column to be added to the table/view when in Pin/Diff mode
	 * 
	 * @param title
	 * @param formatter
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager addColumn(final String title, final IFormatter formatter) {
		columnRegister.put(title, formatter); 
		return this;
	}
			
	/**
	 * Conditionally add or remove all Pin/Diff mode columns to/from the table
	 * 
	 * @param add
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager addAllColumnsToTableIf(boolean add) {
		// BE: leaving the code in this form to be clear what it does
		
		if (add) {
			// first reset the table to its "unpinned" state
			removeAllColumnsFromTable();

			// then add all registered pin/diff columns
			for (final String title : columnRegister.keySet()) {
				addColumnToTable(title);
			}
		}
		else {
			// reset the table to its "unpinned" state
			removeAllColumnsFromTable();
		}
		
		return this;
	}

	/**
	 * Add to the table the column registered with title
	 * 
	 * @param title
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager addColumnToTable(String title) {
		IFormatter formatter = columnRegister.get(title);
		if (title != null)
			parentView.addColumn(title, formatter);
		return this;
	}
	
	/**
	 * Remove all registered columns from the table
	 * 
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager removeAllColumnsFromTable() {
		for (final String title : columnRegister.keySet()) {
			removeColumnFromTable(title);
		}			
		return this;
	}
	
	
	/**
	 * Remove from the table the column registered with the title
	 * 
	 * @param title
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager removeColumnFromTable(String title) {
		 parentView.removeColumn(title);
		 return this;
	}		

	/**
	 * Deregister, and remove from the table the column registered with the title
	 * 
	 * @param title
	 * @return this, for chaining
	 */
	public PinDiffModeColumnManager removeColumn(String title) {
		columnRegister.remove(title);
		removeColumnFromTable(title);
		return this;
	}
	
}
