package com.mmxlabs.models.ui.editors.impl;

import com.mmxlabs.models.ui.editors.IInlineEditor;

public class ReadOnlyInlineEditorWrapper extends IInlineEditorEnablementWrapper {

	public ReadOnlyInlineEditorWrapper(IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean isEnabled() {
		return false;
	}

}
