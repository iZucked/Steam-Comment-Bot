/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

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
