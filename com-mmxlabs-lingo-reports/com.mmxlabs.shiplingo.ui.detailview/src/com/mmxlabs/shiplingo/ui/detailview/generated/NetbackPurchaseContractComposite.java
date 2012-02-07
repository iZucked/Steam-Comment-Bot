/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing NetbackPurchaseContract instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java
 * class hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class NetbackPurchaseContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public NetbackPurchaseContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public NetbackPurchaseContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Netback Purchase Contract", validate);
	}

	public NetbackPurchaseContractComposite(final Composite container, final int style) {
		this(container, style, "Netback Purchase Contract", true);
	}

	/**
	 * Create the main contents
	 * 
	 * @generated
	 */
	@Override
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
		createNetbackPurchaseContractFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of NetbackPurchaseContract.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		PurchaseContractComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to NetbackPurchaseContract
	 * 
	 * @generated
	 */
	protected static void createNetbackPurchaseContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createLowerBoundEditor(composite, mainGroup);
		createBuyersMarginEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the lowerBound feature on NetbackPurchaseContract
	 * 
	 * @generated
	 */
	protected static void createLowerBoundEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getNetbackPurchaseContract_LowerBound()), "Lower Bound");
	}

	/**
	 * Create an editor for the buyersMargin feature on NetbackPurchaseContract
	 * 
	 * @generated
	 */
	protected static void createBuyersMarginEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getNetbackPurchaseContract_BuyersMargin()), "Buyers Margin");
	}
}
