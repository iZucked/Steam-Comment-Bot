/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Displays a text box for rendering the result of an {@link EOperation}
 * 
 * @author Simon Goodall
 * 
 */
public class BasicOperationRenderer implements ICellManipulator, ICellRenderer {

	private static final Logger log = LoggerFactory.getLogger(BasicOperationRenderer.class);

	protected final EOperation operation;
	protected final EditingDomain editingDomain;

	public BasicOperationRenderer(final EOperation operation, final EditingDomain editingDomain) {
		super();
		this.operation = operation;
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		if (object instanceof EObject) {
			return renderSetValue(object, getValue(object));
		}
		return null;
	}
	
	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? "" : setValue.toString();
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		throw new UnsupportedOperationException();
	}

	public void runSetCommand(final Object object, final Object value) {
		throw new UnsupportedOperationException();
	}

	public void doSetValue(final Object object, final Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue(final Object object) {
		return reallyGetValue(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	private Object reallyGetValue(final Object object) {
		
		if (object == null) {
			return null;
		}
		Object result = null;
		try {
			result = ((EObject) object).eInvoke(operation, new BasicEList<Object>());
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		if ((result == null) && (operation.getEType() == EcorePackage.eINSTANCE.getEString())) {
			return "";
		} else {
			return result;
		}
	}

	@Override
	public boolean canEdit(final Object object) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}
}
