/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Vessel instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for
 * the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class VesselComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public VesselComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public VesselComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel", validate);
	}

	public VesselComposite(final Composite container, final int style) {
		this(container, style, "Vessel", true);
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
	 * @generated NO
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createFieldsFromSupers(composite, mainGroup);
		createVesselFields(composite, mainGroup);
		AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Vessel.
	 * 
	 * @generated NO
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		NamedObjectComposite.createFields(composite, mainGroup);
		// AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Vessel
	 * 
	 * @generated
	 */
	protected static void createVesselFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createTimeCharteredEditor(composite, mainGroup);
		createDailyCharterOutPriceEditor(composite, mainGroup);
		createClassEditor(composite, mainGroup);
		createStartRequirementEditor(composite, mainGroup);
		createEndRequirementEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the timeChartered feature on Vessel
	 * 
	 * @generated
	 */
	protected static void createTimeCharteredEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVessel_TimeChartered()), "Time Chartered");
	}

	/**
	 * Create an editor for the dailyCharterOutPrice feature on Vessel
	 * 
	 * @generated
	 */
	protected static void createDailyCharterOutPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVessel_DailyCharterOutPrice()), "Daily Charter Out Price");
	}

	/**
	 * Create an editor for the class feature on Vessel
	 * 
	 * @generated
	 */
	protected static void createClassEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVessel_Class()), "Class");
	}

	/**
	 * Create an editor for the startRequirement feature on Vessel
	 * 
	 * @generated
	 */
	protected static void createStartRequirementEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		final PortTimeAndHeelComposite sub = new PortTimeAndHeelComposite(composite, composite.getStyle(), "Start Requirement", false);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sub.setPath(new CompiledEMFPath(composite.getInputPath(), FleetPackage.eINSTANCE.getVessel_StartRequirement()));
		composite.addSubEditor(sub);
	}

	/**
	 * Create an editor for the endRequirement feature on Vessel
	 * 
	 * @generated
	 */
	protected static void createEndRequirementEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		final PortAndTimeComposite sub = new PortAndTimeComposite(composite, composite.getStyle(), "End Requirement", false);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sub.setPath(new CompiledEMFPath(composite.getInputPath(), FleetPackage.eINSTANCE.getVessel_EndRequirement()));
		composite.addSubEditor(sub);
	}
}
