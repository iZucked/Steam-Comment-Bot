/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing CharterOut instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy
 * for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class CharterOutComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public CharterOutComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public CharterOutComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Charter Out", validate);
	}

	public CharterOutComposite(final Composite container, final int style) {
		this(container, style, "Charter Out", true);
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
		createCharterOutFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of CharterOut.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		VesselEventComposite.createFields(composite, mainGroup);
		HeelOptionsComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to CharterOut
	 * 
	 * @generated
	 */
	protected static void createCharterOutFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createEndPortEditor(composite, mainGroup);
		createDailyCharterOutPriceEditor(composite, mainGroup);
		createRepositioningFeeEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the endPort feature on CharterOut
	 * 
	 * @generated
	 */
	protected static void createEndPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getCharterOut_EndPort()), "End Port");
	}

	/**
	 * Create an editor for the dailyCharterOutPrice feature on CharterOut
	 * 
	 * @generated
	 */
	protected static void createDailyCharterOutPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getCharterOut_DailyCharterOutPrice()), "Daily Charter Out Price");
	}

	/**
	 * Create an editor for the repositioningFee feature on CharterOut
	 * 
	 * @generated
	 */
	protected static void createRepositioningFeeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getCharterOut_RepositioningFee()), "Repositioning Fee");
	}
}
