/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * {@link ViewerComparator} implementation to sort {@link IStatus} objects by severity then message.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidationStatusComparator extends ViewerComparator {

	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {

		if (e1 instanceof GroupedValidationStatusContentProvider.Node && e2 instanceof GroupedValidationStatusContentProvider.Node) {

			final GroupedValidationStatusContentProvider.Node s1 = (GroupedValidationStatusContentProvider.Node) e1;
			final GroupedValidationStatusContentProvider.Node s2 = (GroupedValidationStatusContentProvider.Node) e2;

			int c = s1.group.getPriority() - s2.group.getPriority();
			if (c == 0) {
				c = s1.desc.compareTo(s2.desc);
			}

			if (c != 0) {
				return c;
			}
		}
		if (e1 instanceof IStatus && e2 instanceof IStatus) {

			final IStatus s1 = (IStatus) e1;
			final IStatus s2 = (IStatus) e2;

			int c = s2.getSeverity() - s1.getSeverity();
			if (c == 0) {
				c = s1.getMessage().compareTo(s2.getMessage());
			}

			if (c != 0) {
				return c;
			}

		}

		return super.compare(viewer, e1, e2);
	}

}
