/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.ldd;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class CellRendererColumnLabelProvider extends ColumnLabelProvider {

	private final Map<Object, IStatus> validationErrors;
	private final GridTableViewer eObjectTableViewer;
	private final ICellRenderer renderer;
	private final EMFPath path;
	
	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	private final Color lockedColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	public CellRendererColumnLabelProvider(GridTableViewer eObjectTableViewer, ICellRenderer renderer, Map<Object, IStatus> validationErrors, EMFPath path) {
		this.eObjectTableViewer = eObjectTableViewer;
		this.renderer = renderer;
		this.validationErrors = validationErrors;
		this.path = path;
	}

	@Override
	public Color getForeground(final Object element) {

//		if (this.eObjectTableViewer.lockedForEditing) {
//			return lockedColour;
//		}

//		if (this.eObjectTableViewer.delegateColourProvider != null) {
//			final Color colour = this.eObjectTableViewer.delegateColourProvider.getForeground(element);
//			if (colour != null) {
//				return colour;
//			}
//		}

		return super.getForeground(element);
	}

	@Override
	public Color getBackground(final Object element) {
		if (this.validationErrors.containsKey(element)) {
			final IStatus s = this.validationErrors.get(element);
			if (s.getSeverity() == IStatus.ERROR) {
				return errorColour;
			} else if (s.getSeverity() == IStatus.WARNING) {
				return warningColour;
			}
		}

//		if (this.eObjectTableViewer.delegateColourProvider != null) {
//			final Color colour = this.eObjectTableViewer.delegateColourProvider.getBackground(element);
//			if (colour != null) {
//				return colour;
//			}
//		}

		return super.getBackground(element);
	}

	@Override
	public String getText(final Object element) {
		return renderer.render(path.get((EObject) element));
	}

	@Override
	public String getToolTipText(final Object element) {
		if (this.validationErrors.containsKey(element)) {
			final IStatus s = this.validationErrors.get(element);
			if (!s.isOK()) {
				return getMessages(s);
			}
		}
		return super.getToolTipText(element);
	}

	/**
	 * Extract message hierarchy and construct the tool tip message.
	 * 
	 * @param status
	 * @return
	 */
	private String getMessages(final IStatus status) {
		if (status.isMultiStatus()) {
			final StringBuilder sb = new StringBuilder();
			for (final IStatus s : status.getChildren()) {
				sb.append(getMessages(s));
				sb.append("\n");
			}
			return sb.toString();
		} else {
			return status.getMessage();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}