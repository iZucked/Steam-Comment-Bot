/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * A container for IInlineEditors
 * 
 * @author hinton
 * 
 */
public interface IInlineEditorContainer {
	/**
	 * Add the given {@link IInlineEditor} to this composite. Returns a potentially "wrapped" version of the {@link IInlineEditor} or null if the container rejected the {@link IInlineEditor} editor.
	 * 
	 * @param editor
	 *            the editor to add
	 */
	@Nullable
	IInlineEditor addInlineEditor(final IInlineEditor editor);
}
