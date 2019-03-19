/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.displaycomposites;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.editor.displaycomposites.SpotMarketsDetailComposite.DetailGroup;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class SpotMarketTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link IDisplayComposite} to contain elements for the bottom of the editor
	 */
	protected IDisplayComposite restrictionsLevel = null;
	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;

	public SpotMarketTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
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

		g.setText("Market");
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = new SpotMarketsDetailComposite(g, SWT.NONE, DetailGroup.GENERAL, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		// Initialise middle composite
		middle = toolkit.createComposite(this);
		createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, middle);

		// We know there are n slots, so n columns
		middle.setLayout(new GridLayout(1, true));
		middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite myComposite = new Composite(this, SWT.NONE);
		toolkit.adapt(myComposite);
		myComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));

		final Group g2 = new Group(myComposite, SWT.NONE);

		toolkit.adapt(g2);

		g2.setText("Restrictions");
		g2.setLayout(new FillLayout());
		g2.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		restrictionsLevel = new SpotMarketsDetailComposite(g2, SWT.NONE, DetailGroup.RESTRICTIONS, toolkit);
		restrictionsLevel.setCommandHandler(commandHandler);
		restrictionsLevel.setEditorWrapper(editorWrapper);

		// // Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(3, false));
		myComposite.setLayout(new GridLayout(1, false));

		topLevel.display(dialogContext, root, object, range, dbc);
		restrictionsLevel.display(dialogContext, root, object, range, dbc);
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		super.displayValidationStatus(status);
		restrictionsLevel.displayValidationStatus(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		if (restrictionsLevel != null) {
			restrictionsLevel.setEditorWrapper(wrapper);
		}
		super.setEditorWrapper(wrapper);
	}

	@Override
	protected ChildCompositeContainer createChildComposites(final MMXRootObject root, final EObject object, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childReferences = new ChildCompositeContainer();

		// Custom block - put these features in order first!
		Set<Object> seenObjects = new HashSet<>();
		if (object instanceof SpotMarket) {
			SpotMarket spotMarket = (SpotMarket) object;
			createChildArea(childReferences, root, object, parent, SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO, "Price", spotMarket.getPriceInfo());
			createChildArea(childReferences, root, object, parent, SpotMarketsPackage.Literals.SPOT_MARKET__AVAILABILITY, spotMarket.getAvailability());
			seenObjects.add(spotMarket.getPriceInfo());
			seenObjects.add(spotMarket.getAvailability());

		}

		// ... then default code for the rest
		for (final var ref : eClass.getEAllReferences()) {
			if (shouldDisplay(ref)) {
				if (ref.isMany()) {
					final List<?> values = (List<?>) object.eGet(ref);
					for (final var value : values) {
						if (value instanceof EObject && !seenObjects.contains(value)) {
							createChildArea(childReferences, root, object, parent, ref, (EObject) value);
						}
					}
				} else {
					final EObject value = (EObject) object.eGet(ref);
					if (!seenObjects.contains(value)) {
						createChildArea(childReferences, root, object, parent, ref, value);
					}
				}
			}
		}
		return childReferences;
	}
}
