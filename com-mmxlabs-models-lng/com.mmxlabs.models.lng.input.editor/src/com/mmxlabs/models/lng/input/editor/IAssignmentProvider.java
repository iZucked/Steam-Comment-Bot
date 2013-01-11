/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.editor;

import java.util.List;

public interface IAssignmentProvider<R, T> {
	public List<T> getAssignedObjects(final R resource);
	
	public boolean canStartEdit();

	public void finishEdit();
}
