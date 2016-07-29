/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class BaseLegalEntityTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;

	public BaseLegalEntityTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			// used for children in "middle" composite
			@Override
			public Object createTopLayoutData(MMXRootObject root, EObject value, EObject detail) {
				return new GridData(GridData.FILL_BOTH);
			}
		});
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof Cargo) {
			final CargoType cargoType = ((Cargo) object).getCargoType();
			groupName += " Type: " + cargoType.getName();
		}

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(object.eClass()).createSublevelComposite(g, object.eClass(), dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		// Initialise middle composite
		middle = toolkit.createComposite(this);

		createChildComposites(root, object, eClass, middle);
		// We know there are n slots, so n columns
		middle.setLayout(new GridLayout(childObjects.size(), true));
		middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel.display(dialogContext, root, object, range, dbc);

		final Iterator<IDisplayComposite> children = childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, true));
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) || ref == CommercialPackage.eINSTANCE.getBaseLegalEntity_ShippingBook() || ref == CommercialPackage.eINSTANCE.getBaseLegalEntity_TradingBook();
	}

	@Override
	protected void createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		if (value != null) {
			final Group g2 = new Group(parent, SWT.NONE);
			if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK) {
				g2.setText("Shipping Book");
			} else if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
				g2.setText("Trading Book");
			} else if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK) {
				g2.setText("Upstream Book");
			} else {
				// Unmangle the feature name (to avoid forking this class for client specific books - See FB)
				final String groupName = EditorUtils.unmangle(ref.getName());
				g2.setText(groupName);
			}
			g2.setLayout(new FillLayout());
			g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));
			g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			toolkit.adapt(g2);

			final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass()).createSublevelComposite(g2, value.eClass(),
					dialogContext, toolkit);

			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childReferences.add(ref);
			childComposites.add(sub);
			childObjects.add(value);
		}
	}
}
