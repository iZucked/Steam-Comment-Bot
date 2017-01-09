/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.util.emfpath.EMFPath;

public class EObjectTableViewerColumnProvider extends ColumnLabelProvider {
	/**
	 * 
	 */
	private final EObjectTableViewer eObjectTableViewer;
	private final ICellRenderer renderer;
	private final EMFPath path;
	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	private final Color lockedColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	public EObjectTableViewerColumnProvider(EObjectTableViewer eObjectTableViewer, ICellRenderer renderer, EMFPath path) {
		this.eObjectTableViewer = eObjectTableViewer;
		this.renderer = renderer;
		this.path = path;
	}

	@Override
	public Color getForeground(final Object element) {

		if (this.eObjectTableViewer.lockedForEditing) {
			return lockedColour;
		}

		if (this.eObjectTableViewer.delegateColourProvider != null) {
			final Color colour = this.eObjectTableViewer.delegateColourProvider.getForeground(element);
			if (colour != null) {
				return colour;
			}
		}

		if (element instanceof EObject) {
			final EObject eObject = (EObject) element;
			final Object o = path == null ? eObject : path.get(eObject);
			if (renderer != null && renderer.isValueUnset(o)) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
			}
		}

		return super.getForeground(element);
	}

	@Override
	public Color getBackground(final Object element) {
		if (this.eObjectTableViewer.getValidationSupport().getValidationErrors().containsKey(element)) {
			final IStatus s = this.eObjectTableViewer.getValidationSupport().getValidationErrors().get(element);
			if (s.getSeverity() == IStatus.ERROR) {
				return errorColour;
			} else if (s.getSeverity() == IStatus.WARNING) {
				return warningColour;
			}
		}

		if (this.eObjectTableViewer.delegateColourProvider != null) {
			final Color colour = this.eObjectTableViewer.delegateColourProvider.getBackground(element);
			if (colour != null) {
				return colour;
			}
		}

		return super.getBackground(element);
	}

	@Override
	public String getText(final Object element) {
		return renderer.render(path.get((EObject) element));
	}

	@Override
	public String getToolTipText(final Object element) {
		if (this.eObjectTableViewer.getValidationSupport().getValidationErrors().containsKey(element)) {
			final IStatus s = this.eObjectTableViewer.getValidationSupport().getValidationErrors().get(element);
			if (!s.isOK()) {
				return getMessages(s);
			}
		}

		if (this.eObjectTableViewer.delegateToolTipProvider != null) {
			final String toolTip = this.eObjectTableViewer.delegateToolTipProvider.getToolTipText(element);
			if (toolTip != null) {
				return toolTip;
			}
		}

		return super.getToolTipText(element);
	}

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		if (this.eObjectTableViewer.delegateToolTipProvider != null) {
			return this.eObjectTableViewer.delegateToolTipProvider.getToolTipDisplayDelayTime(object);
		}
		return super.getToolTipDisplayDelayTime(object);
	}

	@Override
	public Image getToolTipImage(Object object) {
		if (this.eObjectTableViewer.delegateToolTipProvider != null) {
			final Image im = this.eObjectTableViewer.delegateToolTipProvider.getToolTipImage(object);
			if (im != null) {
				return im;
			}
		}
		return super.getToolTipImage(object);
	}

	@Override
	public Point getToolTipShift(Object object) {
		if (this.eObjectTableViewer.delegateToolTipProvider != null) {
			Point p = this.eObjectTableViewer.delegateToolTipProvider.getToolTipShift(object);
			if (p != null) {
				return p;
			}
		}
		return super.getToolTipShift(object);
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		if (this.eObjectTableViewer.delegateToolTipProvider != null) {
			return this.eObjectTableViewer.delegateToolTipProvider.getToolTipTimeDisplayed(object);
		}
		return super.getToolTipTimeDisplayed(object);
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