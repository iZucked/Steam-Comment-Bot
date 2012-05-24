package com.mmxlabs.models.lng.input.editor;

import java.util.List;

public interface IAssignmentProvider<R, T> {
	public List<T> getAssignedObjects(final R resource);
}
