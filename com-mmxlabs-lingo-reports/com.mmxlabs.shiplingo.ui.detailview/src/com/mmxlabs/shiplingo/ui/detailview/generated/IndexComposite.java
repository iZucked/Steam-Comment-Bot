/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.market.MarketPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Index instances
 * 
 * @generated
 */
public class IndexComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public IndexComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public IndexComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Index", validate);
	}

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public IndexComposite(final Composite container, final int style) {
		this(container, style, "Index", true);
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
		createIndexFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Index.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		NamedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Index
	 * 
	 * @generated
	 */
	protected static void createIndexFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createPriceCurveEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the priceCurve feature on Index
	 * 
	 * @generated
	 */
	protected static void createPriceCurveEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		final StepwisePriceCurveComposite sub = new StepwisePriceCurveComposite(composite, composite.getStyle(), "Price Curve", false);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sub.setPath(new CompiledEMFPath(composite.getInputPath(), MarketPackage.eINSTANCE.getIndex_PriceCurve()));
		composite.addSubEditor(sub);
	}
}
