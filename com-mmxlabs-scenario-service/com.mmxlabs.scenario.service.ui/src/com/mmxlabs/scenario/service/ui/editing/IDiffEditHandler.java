/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing;

/**
 * A callback interface to notify a backend of various events from the editor world. For example what to do when we wish to apply or cancel the diff edit changes. Typically this will involve removing
 * the diff/edit scenario - but this requires a bit of a dance with the editor.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDiffEditHandler {

	/**
	 * Notify that the cancel action is invoked but the editor is not closed.
	 */
	void onPreEditorCancel();

	/**
	 * Notify that the cancel action is invoked and the editor is now closed.
	 */
	void onEditorCancel();

	/**
	 * Notify that the apply action is invoked but the editor is not closed.
	 */
	void onPreEditorApply();

	/**
	 * Notify that some kind of apply button has been pressed and the editor is now closed. This probably means the current state should be replicated into the parent.
	 */
	void onEditorApply();

	/**
	 * Notify that the editor is being disposed(). On it's own this probably means we should assume "cancel" and that the diff scenario instance should be removed. However if
	 * {@link #onPreEditorApply()} or {@link #onPreEditorCancel()} has been called then is probably the result of the editor being closed and we should expect the corresponding call to
	 * {@link #onEditorApply()} or {@link #onEditorCancel()} and this should essentially be a no-op call.
	 */
	void onEditorDisposed();
}
