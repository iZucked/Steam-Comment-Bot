package com.mmxlabs.models.lng.input.editor;

public interface IAssignmentListener<R, T> {
	public void taskReassigned(final T task, final T beforeTask, final T afterTask,
			final R oldResource, final R newResource);
	public void taskUnassigned(final T task, final R oldResource);
}
