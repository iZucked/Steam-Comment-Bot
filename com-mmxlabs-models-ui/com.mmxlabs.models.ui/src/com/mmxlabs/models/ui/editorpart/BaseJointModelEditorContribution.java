/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

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
	public void init(JointModelEditorPart editorPart, MMXRootObject rootObject, UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.rootObject = rootObject;
		this.modelObject = (T) modelObject;
	}
	
	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
		if (locked) lock();
		else unlock();
	}
	
	protected abstract void lock();
	
	protected abstract void unlock();
}
