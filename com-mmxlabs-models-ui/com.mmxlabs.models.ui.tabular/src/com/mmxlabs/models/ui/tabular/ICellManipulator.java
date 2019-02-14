/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Refactoring IFeatureManipulator.
 * 
 * @author hinton
 * 
 */
public interface ICellManipulator {

	/**
	 * To be called before setValue. This can be used to link up setting containment if needed on edit
	 * 
	 * @param parent
	 * @param object
	 */
	void setParent(Object parent, Object object);

	void setValue(Object object, Object value);

	CellEditor getCellEditor(Composite parent, Object object);

	Object getValue(Object object);

	boolean canEdit(Object object);

	void setExtraCommandsHook(IExtraCommandsHook extraCommandsHook);

	interface IExtraCommandsHook {
		void applyExtraCommands(EditingDomain ed, CompoundCommand cmd, Object parent, Object object, Object value);
	}
}
