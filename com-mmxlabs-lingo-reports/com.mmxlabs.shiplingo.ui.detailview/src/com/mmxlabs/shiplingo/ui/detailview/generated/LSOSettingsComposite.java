/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.lso.LsoPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing LSOSettings instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class LSOSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public LSOSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public LSOSettingsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "L SOSettings", validate);
	}

	public LSOSettingsComposite(final Composite container, final int style) {
		this(container, style, "L SOSettings", true);
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
    createLSOSettingsFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of LSOSettings.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      OptimisationSettingsComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to LSOSettings
	 * @generated
	 */
	protected static void createLSOSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createNumberOfStepsEditor(composite, mainGroup);
    createThresholderSettingsEditor(composite, mainGroup);
    createMoveGeneratorSettingsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the numberOfSteps feature on LSOSettings
	 * @generated
	 */
	protected static void createNumberOfStepsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(LsoPackage.eINSTANCE.getLSOSettings_NumberOfSteps()),
      "Number Of Steps");
  }
		
	/**
	 * Create an editor for the thresholderSettings feature on LSOSettings
	 * @generated
	 */
	protected static void createThresholderSettingsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final ThresholderSettingsComposite sub = 
      new ThresholderSettingsComposite(composite, composite.getStyle(), 
        "Thresholder Settings", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), LsoPackage.eINSTANCE.getLSOSettings_ThresholderSettings()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the moveGeneratorSettings feature on LSOSettings
	 * @generated
	 */
	protected static void createMoveGeneratorSettingsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final MoveGeneratorSettingsComposite sub = 
      new MoveGeneratorSettingsComposite(composite, composite.getStyle(), 
        "Move Generator Settings", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), LsoPackage.eINSTANCE.getLSOSettings_MoveGeneratorSettings()));
    composite.addSubEditor(sub);
  }
}
