/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing ScheduleModel instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class
 * hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class ScheduleModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public ScheduleModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public ScheduleModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Schedule Model", validate);
	}

	public ScheduleModelComposite(final Composite container, final int style) {
		this(container, style, "Schedule Model", true);
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
		createScheduleModelFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of ScheduleModel.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to ScheduleModel
	 * 
	 * @generated
	 */
	protected static void createScheduleModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createSchedulesEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the schedules feature on ScheduleModel
	 * 
	 * @generated
	 */
	protected static void createSchedulesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(SchedulePackage.eINSTANCE.getScheduleModel_Schedules()), "Schedules");
	}
}
