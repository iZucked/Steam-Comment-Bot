/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editor;

public interface IAssignmentListener<R, T> {
	/**
	 * Called when the editor has moved a task somewhere
	 * @param task the task being moved
	 * @param beforeTask (maybe null) a task which the task has been moved before
	 * @param afterTask (maybe null) a task which the task ought to follow
	 * @param oldResource (maybe null) the resource where the task was before
	 * @param newResource the new resource for the task
	 */
	public void taskReassigned(final T task, final T beforeTask, final T afterTask,final R oldResource, final R newResource);
	
	/**
	 * Called when a task has been unassigned from any resource
	 * @param task
	 * @param oldResource the resource the task used to be on
	 */
	public void taskUnassigned(final T task, final R oldResource);
	
	/**
	 * Called when the user has double-clicked a task or similar
	 * @param task
	 */
	public void taskOpened(final T task);

	/**
	 * Called when the user has asked to delete a task
	 * @param task
	 */
	public void taskDeleted(final T task);
	
	/**
	 * Called when the user has asked to lock a task's assignment
	 * @param task
	 */
	public void taskLocked(final T task, final R resource);
	
	/**
	 * Called when the user has asked to unlock a task's assignment.
	 * @param task
	 * @param resource
	 */
	public void taskUnlocked(final T task, final R resource);
}
