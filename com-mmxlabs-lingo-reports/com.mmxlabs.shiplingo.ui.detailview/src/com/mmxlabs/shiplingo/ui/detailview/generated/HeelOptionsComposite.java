/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing HeelOptions instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class HeelOptionsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public HeelOptionsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public HeelOptionsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Heel Options", validate);
	}

	public HeelOptionsComposite(final Composite container, final int style) {
		this(container, style, "Heel Options", true);
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
    createHeelOptionsFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of HeelOptions.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to HeelOptions
	 * @generated
	 */
	protected static void createHeelOptionsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createHeelLimitEditor(composite, mainGroup);
    createHeelCVValueEditor(composite, mainGroup);
    createHeelUnitPriceEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the heelLimit feature on HeelOptions
	 * @generated
	 */
	protected static void createHeelLimitEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getHeelOptions_HeelLimit()),
      "Heel Limit");
  }
		
	/**
	 * Create an editor for the heelCVValue feature on HeelOptions
	 * @generated
	 */
	protected static void createHeelCVValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getHeelOptions_HeelCVValue()),
      "Heel CVValue");
  }
		
	/**
	 * Create an editor for the heelUnitPrice feature on HeelOptions
	 * @generated
	 */
	protected static void createHeelUnitPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getHeelOptions_HeelUnitPrice()),
      "Heel Unit Price");
  }
}
