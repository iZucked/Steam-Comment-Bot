/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Refactoring IFeatureManipulator.
 * 
 * @author hinton
 * 
 */
public interface ICellManipulator {

	void setValue(Object object, Object value);

	CellEditor getCellEditor(Composite parent, Object object);

	Object getValue(Object object);

	boolean canEdit(Object object);

}
