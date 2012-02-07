/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing CharterOutVisit instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class
 * hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class CharterOutVisitComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public CharterOutVisitComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public CharterOutVisitComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Charter Out Visit", validate);
	}

	public CharterOutVisitComposite(final Composite container, final int style) {
		this(container, style, "Charter Out Visit", true);
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
		createCharterOutVisitFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of CharterOutVisit.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		VesselEventVisitComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to CharterOutVisit
	 * 
	 * @generated
	 */
	protected static void createCharterOutVisitFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createCharterOutEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the charterOut feature on CharterOutVisit
	 * 
	 * @generated
	 */
	protected static void createCharterOutEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(EventsPackage.eINSTANCE.getCharterOutVisit_CharterOut()), "Charter Out");
	}
}
