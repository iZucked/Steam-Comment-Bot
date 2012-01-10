/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FuelMixture instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FuelMixtureComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FuelMixtureComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FuelMixtureComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fuel Mixture", validate);
	}

	public FuelMixtureComposite(final Composite container, final int style) {
		this(container, style, "Fuel Mixture", true);
	}

	/**
	 * Create the main contents
	 * @generated
	 */
	protected void createContents(final Composite group) {
    final Composite mainGroup;

    if (group == null) {
      mainGroup = createGroup(this, mainGroupTitle);
    } else {
      mainGroup = group;
    }
    
    super.createContents(mainGroup);		

    createFields(this, mainGroup);
  }

	/**
	 * @generated
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFieldsFromSupers(composite, mainGroup);
    createFuelMixtureFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FuelMixture.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to FuelMixture
	 * @generated
	 */
	protected static void createFuelMixtureFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFuelUsageEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the fuelUsage feature on FuelMixture
	 * @generated
	 */
	protected static void createFuelUsageEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelMixture_FuelUsage()),
      "Fuel Usage");
  }
}
