/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorControlFactory;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * Customised {@link DetailCompositeDialog} to alter default {@link IDisplayCompositeLayoutProvider}.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoDetailComposite extends DefaultDetailComposite {

	private Composite topComposite;
	private Composite otherComposite;

	private final List<EStructuralFeature> topFeatures;
	private final List<EStructuralFeature> otherFeatures;
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();

	public CargoDetailComposite(final Composite parent, final int style, final boolean top, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		topFeatures = new ArrayList<>(3);
		otherFeatures = new LinkedList<>();

		topFeatures.add(MMXCorePackage.Literals.NAMED_OBJECT__NAME);
		topFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		topFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX);

		otherFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {
			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(1, false);
			}

			@Override
			public Object createTopLayoutData(final MMXRootObject root, final EObject value, final EObject detail) {
				return new GridData(GridData.FILL_HORIZONTAL);
			}
		};
	}

	@Override
	public IInlineEditor addInlineEditor(IInlineEditor editor) {

		editor = super.addInlineEditor(editor);
		if (editor != null) {
			final EStructuralFeature f = editor.getFeature();
			feature2Editor.put(f, editor);
			if (!topFeatures.contains(f) && !otherFeatures.contains(f)) {
				otherFeatures.add(f);
			}
		}

		return editor;
	}

	@Override
	public void createControls(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {

		topComposite = toolkit.createComposite(this);
		topComposite.setLayout(layoutProvider.createDetailLayout(root, object));

		otherComposite = toolkit.createComposite(this);
		otherComposite.setLayout(layoutProvider.createDetailLayout(root, object));

		EditorControlFactory.makeControls(dialogContext, root, object, topComposite, topFeatures.toArray(new EStructuralFeature[topFeatures.size()]), feature2Editor, dbc, layoutProvider, toolkit);

		EditorControlFactory.makeControls(dialogContext, root, object, otherComposite, otherFeatures.toArray(new EStructuralFeature[otherFeatures.size()]), feature2Editor, dbc, layoutProvider,
				toolkit);

	}
}
