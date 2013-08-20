/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

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
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor;
	
	public CargoDetailComposite(final Composite parent, final int style, final boolean top, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();
		this.top = top;
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}

	
	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider(){
			@Override
			public Layout createDetailLayout(MMXRootObject root, EObject value) {
				return new GridLayout(4, false);
			}
			
			@Override
			public Object createTopLayoutData(MMXRootObject root, EObject value,
					EObject detail) {
				return new GridData(GridData.FILL_HORIZONTAL);
			}
		};
	}
	
	@Override
	public void addInlineEditor(final IInlineEditor editor) {
 
		// By default all elements are in the top
		boolean topElement = true;

		EStructuralFeature feature = editor.getFeature() ;
		// Here the exceptions are listed for the elements which should go into the bottom
//		if (feature == CargoPackage.eINSTANCE.getCargo_AllowedVessels()) {
////			topElement = false;
//		}
//		if (feature == AssignmentPackage.eINSTANCE.getElementAssignment_Assignment()) {
////			topElement = false;
//		}
//		// Do not add elements if they are for the wrong section.
//		if (top != topElement) {
//			return;
//		}

//		if(feature == AssignmentPackage.eINSTANCE.getElementAssignment_Assignment()){
//			System.out.println(feature.getName());
//		}
	
		feature2Editor.put(feature, editor);
		
		super.addInlineEditor(editor);
	}
	
	@Override
	public void createControls(MMXRootObject root, EObject object, final EMFDataBindingContext dbc) {
		// TODO Auto-generated method stub
		super.createControls(root, object, dbc);
//		for (final IInlineEditor editor : editors) {
//			final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(this, SWT.NONE) : null;
//			editor.setLabel(label);
//			final Control control = editor.createControl(this);
//			control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
//			control.setData(LABEL_CONTROL_KEY, label);
//			if (label != null) {
//				label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
//			}
//		}
	}
}
