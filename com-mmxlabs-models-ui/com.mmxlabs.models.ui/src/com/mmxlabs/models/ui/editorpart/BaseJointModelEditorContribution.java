/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.core.runtime.IStatus;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * @author hinton
 * 
 */
public abstract class BaseJointModelEditorContribution<T extends UUIDObject> implements IJointModelEditorContribution {
	protected boolean locked;
	protected JointModelEditorPart editorPart;
	protected MMXRootObject rootObject;
	protected T modelObject;

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.rootObject = rootObject;
		this.modelObject = (T) modelObject;
	}

	@Override
	public void dispose() {
		this.editorPart = null;
		this.rootObject = null;
		this.modelObject = (T) null;
	}

	@Override
	public boolean canHandle(final IStatus status) {
		return false;
	}

	@Override
	public void handle(final IStatus status) {
	}
}
