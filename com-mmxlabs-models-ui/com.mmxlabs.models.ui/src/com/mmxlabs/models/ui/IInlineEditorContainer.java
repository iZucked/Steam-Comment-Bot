/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * 
 * @author hinton
 *
 */
public interface IInlineEditorContainer {
	/**
	 * Add the given inline editor to this composite
	 * 
	 * @param editor the editor to add
	 */
	public void addInlineEditor(final IInlineEditor editor);
}
