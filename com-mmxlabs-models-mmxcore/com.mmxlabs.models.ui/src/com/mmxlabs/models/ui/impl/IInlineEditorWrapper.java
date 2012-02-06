/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.base;

import com.mmxlabs.shiplingo.ui.detailview.editors.IInlineEditor;

/**
 * Instances provide a single method to wrap an IInlineEditor, allowing its
 * control to be modified without the editor or the containing composite knowing
 * too much about it.
 * 
 * @author hinton
 */
public interface IInlineEditorWrapper {
	/**
	 * A wrapper which does nothing (returns its argument)
	 */
	final IInlineEditorWrapper IDENTITY = new IInlineEditorWrapper() {
		@Override
		public IInlineEditor wrap(final IInlineEditor editor) {
			return editor;
		}
	};

	public IInlineEditor wrap(final IInlineEditor editor);
}
