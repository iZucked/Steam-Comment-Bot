/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing IndexPricePurchaseContract instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class IndexPricePurchaseContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public IndexPricePurchaseContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public IndexPricePurchaseContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Index Price Purchase Contract", validate);
	}

	public IndexPricePurchaseContractComposite(final Composite container, final int style) {
		this(container, style, "Index Price Purchase Contract", true);
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
    createIndexPricePurchaseContractFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of IndexPricePurchaseContract.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      SimplePurchaseContractComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to IndexPricePurchaseContract
	 * @generated
	 */
	protected static void createIndexPricePurchaseContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createIndexEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the index feature on IndexPricePurchaseContract
	 * @generated
	 */
	protected static void createIndexEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getIndexPricePurchaseContract_Index()),
      "Index");
  }
}
