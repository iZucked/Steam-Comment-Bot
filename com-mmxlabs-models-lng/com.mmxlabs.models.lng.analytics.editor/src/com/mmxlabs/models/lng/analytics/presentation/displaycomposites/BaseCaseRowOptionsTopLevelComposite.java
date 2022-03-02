/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.commercial.ui.displaycomposites.ContractDetailComposite.ContractDetailGroup;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.models.ui.impl.FeatureFilteringDetailDisplayComposite;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class BaseCaseRowOptionsTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link IDisplayComposite} to contain elements for the bottom of the editor
	 */
	protected FeatureFilteringDetailDisplayComposite buySide = null;
	protected FeatureFilteringDetailDisplayComposite sellSide = null;

	public BaseCaseRowOptionsTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			// used for children in "middle" composite
			@Override
			public Object createTopLayoutData(MMXRootObject root, EObject value, EObject detail) {
				return new GridData(GridData.FILL_BOTH);
			}
		});
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);

		toolkit.adapt(g);

		g.setText("Buy/event");
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		final Group g2 = new Group(this, SWT.NONE);

		toolkit.adapt(g2);

		g2.setText("Sell");
		g2.setLayout(new FillLayout());
		g2.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Create the directly rather than go through the registry. True indicates this
		// is the top section. The bottom will be created later on
		buySide = new FeatureFilteringDetailDisplayComposite(g, SWT.NONE, toolkit);

		buySide.setCommandHandler(commandHandler);
		buySide.setEditorWrapper(editorWrapper);
		// Create the directly rather than go through the registry. True indicates this
		// is the top section. The bottom will be created later on
		sellSide = new FeatureFilteringDetailDisplayComposite(g2, SWT.NONE, toolkit);
		sellSide.setCommandHandler(commandHandler);
		sellSide.setEditorWrapper(editorWrapper);
		AnalyticsPackage.Literals.BASE_CASE_ROW_OPTIONS.getEAllStructuralFeatures().forEach(f -> {
			if (f.getName().toLowerCase().contains("laden") || f.getName().toLowerCase().contains("load")) {
				buySide.includeFeature(f);
			} else {
				sellSide.includeFeature(f);
			}
		});

		//
		// // Overrides default layout factory so we get a single column rather than
		// multiple columns and one row
		this.setLayout(new GridLayout(2, false));

		buySide.display(dialogContext, root, object, range, dbc);
		sellSide.display(dialogContext, root, object, range, dbc);

	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		super.displayValidationStatus(status);
		buySide.displayValidationStatus(status);
		sellSide.displayValidationStatus(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		if (buySide != null) {
			buySide.setEditorWrapper(wrapper);
		}
		if (sellSide != null) {
			sellSide.setEditorWrapper(wrapper);
		}
		super.setEditorWrapper(wrapper);
	}
}
