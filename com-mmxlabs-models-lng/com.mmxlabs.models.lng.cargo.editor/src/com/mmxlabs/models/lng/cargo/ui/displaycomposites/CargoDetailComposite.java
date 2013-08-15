/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * Extended {@link DefaultDetailComposite} implementation for {@link Cargo}s. This assumes the Cargo editor is split into two sections - top and bottom. By default all editors are in the top section
 * with the exceptions listed below. The {@link CargoTopLevelComposite} will create two instances of this {@link Composite} rather than the default single instance and alter the boolean top flag in
 * the constructor.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoDetailComposite extends DefaultDetailComposite {

	private final boolean top;

	public CargoDetailComposite(final Composite parent, final int style, final boolean top,  final FormToolkit toolkit) {
		super(parent, style, toolkit);

		this.top = top;
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the top
		boolean topElement = true;

		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CargoPackage.eINSTANCE.getCargo_AllowedVessels()) {
			topElement = false;
		}
		if (editor.getFeature() == AssignmentPackage.eINSTANCE.getElementAssignment_Assignment()) {
			topElement = false;
		}
		// Do not add elements if they are for the wrong section.
		if (top != topElement) {
			return;
		}

		super.addInlineEditor(editor);
	}
}
