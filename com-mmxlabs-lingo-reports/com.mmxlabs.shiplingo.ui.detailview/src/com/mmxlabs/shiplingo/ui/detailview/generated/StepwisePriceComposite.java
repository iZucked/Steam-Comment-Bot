/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.market.MarketPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing StepwisePrice instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class StepwisePriceComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public StepwisePriceComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public StepwisePriceComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Stepwise Price", validate);
	}

	public StepwisePriceComposite(final Composite container, final int style) {
		this(container, style, "Stepwise Price", true);
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
    createStepwisePriceFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of StepwisePrice.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to StepwisePrice
	 * @generated
	 */
	protected static void createStepwisePriceFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createDateEditor(composite, mainGroup);
    createPriceFromDateEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the date feature on StepwisePrice
	 * @generated
	 */
	protected static void createDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(MarketPackage.eINSTANCE.getStepwisePrice_Date()),
      "Date");
  }
		
	/**
	 * Create an editor for the priceFromDate feature on StepwisePrice
	 * @generated
	 */
	protected static void createPriceFromDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(MarketPackage.eINSTANCE.getStepwisePrice_PriceFromDate()),
      "Price From Date");
  }
}
