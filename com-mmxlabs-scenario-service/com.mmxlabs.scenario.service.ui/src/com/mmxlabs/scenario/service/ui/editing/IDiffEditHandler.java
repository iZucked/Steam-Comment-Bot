package com.mmxlabs.scenario.service.ui.editing;

/**
 * A callback interface to notify a backend of various events from the editor world. For example what to do when the editor is "saved" or "disposed".
 * 
 * @author Simon Goodall
 * 
 */
public interface IDiffEditHandler {

	/**
	 * Notify that the editor is saving its content.
	 */
	void onEditorCancel();

	/**
	 * Notify that the editor is being disposed(). This probably means the diff scenario instance should be removed.
	 */
	void onEditorDisposed();

	/**
	 * Notify that some kind of apply button has been pressed. This probably means the current state should be replicated into the parent.
	 */
	void onEditorApply();

}
