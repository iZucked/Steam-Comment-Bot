package com.mmxlabs.rcp.common.editors;

import org.eclipse.ui.IEditorPart;

/**
 * The {@link IEditorGotoTarget} interface is intended to be implemented by {@link IEditorPart}s as a way of switching focus to the selected object - making in the current editing focal point. For
 * example this could be switching tabs and selecting the object in a table.
 * 
 * @author Simon Goodall
 * @since 2.1
 * 
 */
public interface IEditorGotoTarget {

	void gotoTarget(Object object);
}
