/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public interface IJointModelEditorContribution {
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject);

	public void addPages(Composite parent);

	public void setLocked(final boolean locked);

	public void dispose();

	/**
	 * This method returns true if the content from this {@link IJointModelEditorContribution} is able to do something with this {@link IStatus} object. For example, does the IStatus relate to an
	 * object that is rendered in one of the contributed pages?
	 * 
	 * @param status
	 * @return
	 */
	boolean canHandle(IStatus status);

	/**
	 * Handle this {@link IStatus}. For example open the page and select the row for object the IStatus relates to.
	 * 
	 * @param status
	 */
	void handle(IStatus status);
}
