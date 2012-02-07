/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Idle instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for
 * the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class IdleComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public IdleComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public IdleComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Idle", validate);
	}

	public IdleComposite(final Composite container, final int style) {
		this(container, style, "Idle", true);
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
		createIdleFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Idle.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		PortVisitComposite.createFields(composite, mainGroup);
		FuelMixtureComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Idle
	 * 
	 * @generated
	 */
	protected static void createIdleFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createVesselStateEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the vesselState feature on Idle
	 * 
	 * @generated
	 */
	protected static void createVesselStateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(EventsPackage.eINSTANCE.getIdle_VesselState()), "Vessel State");
	}
}
