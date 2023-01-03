/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

public interface IInlineEditorFactory {
	IInlineEditor createEditor(EClass owner, ETypedElement feature);
}
