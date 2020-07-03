/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.update;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Simon Goodall
 * 
 */
public class UpdateItemLabelProvider extends ColumnLabelProvider {

	private final Image imgError;
	private final Image imgWarn;
	private final Image imgInfo;

	@Override
	public void dispose() {
		imgError.dispose();
		imgWarn.dispose();
		imgInfo.dispose();
	}

	private int colIdx;

	public UpdateItemLabelProvider() {
		this(0);
	}

	public UpdateItemLabelProvider(int colIdx) {
		this.colIdx = colIdx;

		imgError = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lngdataserver.lng.importers", "/icons/error.gif").createImage();
		imgWarn = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lngdataserver.lng.importers", "/icons/warning.gif").createImage();
		imgInfo = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lngdataserver.lng.importers", "/icons/information.gif").createImage();
	}

	@Override
	public Image getImage(final Object element) {

		if (colIdx == 0) {
			if (element instanceof UpdateError) {
				return imgError;
			}
			if (element instanceof UpdateWarning) {
				return imgWarn;
			}
		}

//		if (element instanceof GroupedValidationStatusContentProvider.Node) {
//			GroupedValidationStatusContentProvider.Node node = (GroupedValidationStatusContentProvider.Node) element;
//
//			int severity = IStatus.OK;
//			for (IStatus s : node.status) {
//				if (s.getSeverity() > severity) {
//					severity = s.getSeverity();
//				}
//			}
//
//			if (severity == IStatus.ERROR) {
//				return imgError;
//			} else if (severity == IStatus.WARNING) {
//				return imgWarn;
//			} else if (severity == IStatus.INFO) {
//				return imgInfo;
//			}
//		}
//		if (element instanceof IStatus) {
//			final int severity = ((IStatus) element).getSeverity();
//
//			if (severity == IStatus.ERROR) {
//				return imgError;
//			} else if (severity == IStatus.WARNING) {
//				return imgWarn;
//			} else if (severity == IStatus.INFO) {
//				return imgInfo;
//			}
//		}
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (colIdx == 1) {

			if (element instanceof UpdateWarning) {
				UpdateWarning updateWarning = (UpdateWarning) element;
				if (updateWarning.isHasQuickFix()) {
					return updateWarning.getQuickFixMsg();
				}
			}
			if (element instanceof UserUpdateStep) {
				UserUpdateStep step = (UserUpdateStep) element;
				if (step.isHasQuickFix()) {
					return step.getQuickFixMsg();
				}
			}

			return null;
		}

		if (element != null) {
			return element.toString();
		}
		return null;
	}
}
