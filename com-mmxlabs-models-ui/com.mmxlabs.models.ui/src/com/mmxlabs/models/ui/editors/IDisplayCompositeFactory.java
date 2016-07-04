/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * Interface for factories which will create composites for displaying model objects.
 * 
 * @author hinton
 * 
 */
public interface IDisplayCompositeFactory {
	/**
	 * This is for creating a composite that can be displayed in a properties view or editor dialog
	 * 
	 * @param eClass
	 * @param toolkit
	 * @return
	 */
	IDisplayComposite createToplevelComposite(Composite composite, EClass eClass, IDialogEditingContext context, FormToolkit toolkit);

	/**
	 * This is for creating a composite which can be displayed within a top level composite somewhere; it should just handle the direct fields on the eClass, not any contained classes.
	 * 
	 * @param eClass
	 * @return
	 */
	IDisplayComposite createSublevelComposite(Composite composite, EClass eClass, IDialogEditingContext context, FormToolkit toolkit);

	/**
	 * This is for asking what non-contained objects a composite can edit on this value.
	 * 
	 * @param root
	 * @param value
	 * @return
	 */
	List<EObject> getExternalEditingRange(MMXRootObject root, EObject value);
}
