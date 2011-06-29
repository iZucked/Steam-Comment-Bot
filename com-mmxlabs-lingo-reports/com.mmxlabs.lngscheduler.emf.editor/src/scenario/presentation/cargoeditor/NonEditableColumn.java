/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;

public abstract class NonEditableColumn implements ICellManipulator, ICellRenderer {

	@Override
	public Comparable getComparable(Object object) {
		return render(object);
	}

	@Override
	public void setValue(Object object, Object value) {

	}

	@Override
	public CellEditor getCellEditor(Composite parent, Object object) {
		return null;
	}

	@Override
	public Object getValue(Object object) {
		return null;
	}

	@Override
	public boolean canEdit(Object object) {
		return false;
	}
	
	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(
			Object object) {
		return Collections.emptySet();
	}
}
