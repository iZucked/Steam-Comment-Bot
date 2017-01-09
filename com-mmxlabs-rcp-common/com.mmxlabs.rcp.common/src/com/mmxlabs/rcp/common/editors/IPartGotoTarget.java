/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.editors;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;

/**
 * The {@link IPartGotoTarget} interface is intended to be implemented by {@link IEditorPart}s or {@link IViewPart}s as a way of switching focus to the selected object - making in the current editing
 * focal point. For example this could be switching tabs and selecting the object in a table. This could be performed by a {@link ISelectionListener}, but that does not guarantee the correct editor
 * tab is selected.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPartGotoTarget {

	void gotoTarget(Object object);
}
