/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * This provides a layout to an IDisplayComposite
 * 
 * @author hinton
 *
 */
public interface IDisplayCompositeLayoutProvider {
	boolean showLabelFor(MMXRootObject root, EObject value, IInlineEditor editor);

	/**
	 * Returns a "top level" editor layout which may include a number of specific "detail" layouts. These "detail" layouts correspond to data sub-components which themselves require a separate GUI
	 * editing sub-component
	 * 
	 * @param root
	 *            The root object for the entire data model
	 * @param object
	 *            The object being edited
	 * @param numDetailLayouts
	 *            The number of GUI sub-components involved in the editor
	 * 
	 * @return A Swing Layout object for the top GUI level in the editor component
	 */
	Layout createTopLevelLayout(MMXRootObject root, final EObject object, int numDetailLayouts);

	/**
	 * Returns a "detail" editor layout specifying how to lay out fields added to a specific GUI sub-component
	 * 
	 * @param root
	 *            The root object for the entire data model
	 * @param subObject
	 *            The data sub-component which has been marked for editing in its own GUI sub-component
	 * @return
	 */
	Layout createDetailLayout(MMXRootObject root, EObject subObject);

	/**
	 * Returns SWT layout data for the GUI control which edits a particular field of a particular object, in a GUI sub-component whose layout is controlled by createDetailLayout.
	 * 
	 * @param root
	 * @param value
	 * @param editor
	 * @param control
	 * @return
	 */
	Object createEditorLayoutData(MMXRootObject root, EObject value, IInlineEditor editor, Control control);

	/**
	 * Returns SWT layout data for the GUI label attached to a particular field editor.
	 * 
	 * @param root
	 * @param value
	 * @param editor
	 * @param control
	 * @param label
	 * @return
	 */
	Object createLabelLayoutData(MMXRootObject root, EObject value, final IInlineEditor editor, Control control, Label label);

	/**
	 * Returns SWT layout data for the editor GUI sub-component which represents an entire data sub-component, within a top-level component whose layout is controlled by createTopLevelLayout.
	 * 
	 * @param root
	 * @param value
	 * @param editor
	 * @param control
	 * @param label
	 * @return
	 */
	Object createTopLayoutData(MMXRootObject root, EObject value, EObject detail);
}
