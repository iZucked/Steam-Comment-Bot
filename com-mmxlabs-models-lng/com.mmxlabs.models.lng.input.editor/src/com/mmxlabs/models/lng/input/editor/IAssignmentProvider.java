/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.editor;

import java.util.List;

import com.mmxlabs.models.lng.input.editorpart.InputJointModelEditorContribution;

public interface IAssignmentProvider<R, T> {
	public List<T> getAssignedObjects(final R resource);
	
	public boolean canStartEdit();

	public void finishEdit();
}
