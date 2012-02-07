/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Discount instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy
 * for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class DiscountComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public DiscountComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public DiscountComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Discount", validate);
	}

	public DiscountComposite(final Composite container, final int style) {
		this(container, style, "Discount", true);
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
		createDiscountFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Discount.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to Discount
	 * 
	 * @generated
	 */
	protected static void createDiscountFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createTimeEditor(composite, mainGroup);
		createDiscountFactorEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the time feature on Discount
	 * 
	 * @generated
	 */
	protected static void createTimeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(OptimiserPackage.eINSTANCE.getDiscount_Time()), "Time");
	}

	/**
	 * Create an editor for the discountFactor feature on Discount
	 * 
	 * @generated
	 */
	protected static void createDiscountFactorEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(OptimiserPackage.eINSTANCE.getDiscount_DiscountFactor()), "Discount Factor");
	}
}
