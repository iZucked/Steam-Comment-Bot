/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ContractTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link IDisplayComposite} to contain elements for the bottom of the editor
	 */
	protected IDisplayComposite bottomLevel = null;

	public ContractTopLevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range) {

		final EClass eClass = object.eClass();

		final TabFolder tabFolder = new TabFolder(this, SWT.TOP);
		// tabFolder.setLayout(new FillLayout());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalIndent = 0;
		gd.horizontalIndent = 0;
		tabFolder.setLayoutData(gd);
		final TabItem mainTabPage = new TabItem(tabFolder, SWT.None);
		mainTabPage.setText("Contract");//EditorUtils.unmangle(eClass.getName()));
		final Composite mainTabComposite = new Composite(tabFolder, SWT.NONE);
		mainTabPage.setControl(mainTabComposite);
		// g.setText(EditorUtils.unmangle(eClass.getName()));
		mainTabComposite.setLayout(new FillLayout());
		mainTabComposite.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = new ContractDetailComposite(mainTabComposite, SWT.NONE, true);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		final TabItem priceInfoTabPage = new TabItem(tabFolder, SWT.None);
		priceInfoTabPage.setText("Pricing");
		final Composite priceInfoTabComposite = new Composite(tabFolder, SWT.NONE);
		priceInfoTabPage.setControl(priceInfoTabComposite);
		priceInfoTabComposite.setLayout(new GridLayout(1, true));
		createChildComposites(root, object, eClass, priceInfoTabComposite);

		final TabItem constraintTabPage = new TabItem(tabFolder, SWT.None);
		constraintTabPage.setText("Restrictions");
		// final Group g = new Group(tabFolder, SWT.NONE);
		final Composite constraintTabComposite = new Composite(tabFolder, SWT.NONE);
		constraintTabPage.setControl(constraintTabComposite);

		// Additional Group for the bottom section
		// g2.setText("Additional Constraints");
		constraintTabComposite.setLayout(new FillLayout());
		constraintTabComposite.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		// Create the directly rather than go through the registry. True indicates this is the bottom section.
		bottomLevel = new ContractDetailComposite(constraintTabComposite, SWT.NONE, false);
		bottomLevel.setCommandHandler(commandHandler);
		bottomLevel.setEditorWrapper(editorWrapper);

		topLevel.display(location, root, object, range);
		bottomLevel.display(location, root, object, range);


		final Iterator<EReference> refs = childReferences.iterator();
		final Iterator<IDisplayComposite> children = childComposites.iterator();

		while (refs.hasNext()) {
			children.next().display(location, root, (EObject) object.eGet(refs.next()), range);
		}

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, true));
	}

	// @Override
	// protected boolean shouldDisplay(final EReference ref) {
	// return super.shouldDisplay(ref) || ref == CargoPackage.eINSTANCE.getCargo_LoadSlot() || ref == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	// }

	@Override
	public void displayValidationStatus(final IStatus status) {
		bottomLevel.displayValidationStatus(status);
		super.displayValidationStatus(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		if (bottomLevel != null) {
			bottomLevel.setEditorWrapper(wrapper);
		}
		super.setEditorWrapper(wrapper);
	}
}
