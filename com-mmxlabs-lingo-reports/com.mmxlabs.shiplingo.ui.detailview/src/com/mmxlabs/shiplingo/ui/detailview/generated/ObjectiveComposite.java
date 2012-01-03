/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Objective instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ObjectiveComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ObjectiveComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ObjectiveComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Objective", validate);
	}

	public ObjectiveComposite(final Composite container, final int style) {
		this(container, style, "Objective", true);
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
    createObjectiveFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Objective.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to Objective
	 * @generated
	 */
	protected static void createObjectiveFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createWeightEditor(composite, mainGroup);
    createDiscountCurveEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the weight feature on Objective
	 * @generated
	 */
	protected static void createWeightEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getObjective_Weight()),
      "Weight");
  }
		
	/**
	 * Create an editor for the discountCurve feature on Objective
	 * @generated
	 */
	protected static void createDiscountCurveEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final DiscountCurveComposite sub = 
      new DiscountCurveComposite(composite, composite.getStyle(), 
        "Discount Curve", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), OptimiserPackage.eINSTANCE.getObjective_DiscountCurve()));
    composite.addSubEditor(sub);
  }
}
