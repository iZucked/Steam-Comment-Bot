/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.lso.LsoPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing ThresholderSettings instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ThresholderSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ThresholderSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ThresholderSettingsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Thresholder Settings", validate);
	}

	public ThresholderSettingsComposite(final Composite container, final int style) {
		this(container, style, "Thresholder Settings", true);
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
    createThresholderSettingsFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of ThresholderSettings.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to ThresholderSettings
	 * @generated
	 */
	protected static void createThresholderSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createAlphaEditor(composite, mainGroup);
    createInitialAcceptanceRateEditor(composite, mainGroup);
    createEpochLengthEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the alpha feature on ThresholderSettings
	 * @generated
	 */
	protected static void createAlphaEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(LsoPackage.eINSTANCE.getThresholderSettings_Alpha()),
      "Alpha");
  }
		
	/**
	 * Create an editor for the initialAcceptanceRate feature on ThresholderSettings
	 * @generated
	 */
	protected static void createInitialAcceptanceRateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(LsoPackage.eINSTANCE.getThresholderSettings_InitialAcceptanceRate()),
      "Initial Acceptance Rate");
  }
		
	/**
	 * Create an editor for the epochLength feature on ThresholderSettings
	 * @generated
	 */
	protected static void createEpochLengthEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(LsoPackage.eINSTANCE.getThresholderSettings_EpochLength()),
      "Epoch Length");
  }
}
