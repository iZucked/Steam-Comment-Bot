package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.editors.VesselClassCostEditor;

/**
 * A composite containing a form for editing VesselClass instances. The EClass
 * hierarchy is implemented by the static methods at the bottom of the class,
 * and is not mirrored in the java class hierarchy for the composites, because
 * ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class VesselClassComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public VesselClassComposite(final Composite container, final int style,
			final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public VesselClassComposite(final Composite container, final int style,
			final boolean validate) {
		this(container, style, "Vessel Class", validate);
	}

	public VesselClassComposite(final Composite container, final int style) {
		this(container, style, "Vessel Class", true);
	}

	/**
	 * Create the main contents
	 * 
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
	 * @generated NO notes
	 */
	protected static void createFields(final AbstractDetailComposite composite,
			final Composite mainGroup) {
		createFieldsFromSupers(composite, mainGroup);
		createVesselClassFields(composite, mainGroup);
		AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of VesselClass.
	 * 
	 * @generated NO notes
	 */
	protected static void createFieldsFromSupers(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		NamedObjectComposite.createFields(composite, mainGroup);
//		AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to VesselClass
	 * 
	 * @generated
	 */
	protected static void createVesselClassFields(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		createCapacityEditor(composite, mainGroup);
		createMinSpeedEditor(composite, mainGroup);
		createMaxSpeedEditor(composite, mainGroup);
		createMinHeelVolumeEditor(composite, mainGroup);
		createFillCapacityEditor(composite, mainGroup);
		createSpotCharterCountEditor(composite, mainGroup);
		createDailyCharterInPriceEditor(composite, mainGroup);
		createDailyCharterOutPriceEditor(composite, mainGroup);
		createLadenAttributesEditor(composite, mainGroup);
		createBallastAttributesEditor(composite, mainGroup);
		createBaseFuelEditor(composite, mainGroup);
		createPortExclusionsEditor(composite, mainGroup);
		createInaccessiblePortsEditor(composite, mainGroup);
		createCanalCostsEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the capacity feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createCapacityEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup,
				composite.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_Capacity()), "Capacity");
	}

	/**
	 * Create an editor for the minSpeed feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createMinSpeedEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup,
				composite.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_MinSpeed()), "Min Speed");
	}

	/**
	 * Create an editor for the maxSpeed feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createMaxSpeedEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup,
				composite.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_MaxSpeed()), "Max Speed");
	}

	/**
	 * Create an editor for the minHeelVolume feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createMinHeelVolumeEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_MinHeelVolume()), "Min Heel Volume");
	}

	/**
	 * Create an editor for the fillCapacity feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createFillCapacityEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_FillCapacity()), "Fill Capacity");
	}

	/**
	 * Create an editor for the spotCharterCount feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createSpotCharterCountEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_SpotCharterCount()),
				"Spot Charter Count");
	}

	/**
	 * Create an editor for the dailyCharterInPrice feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createDailyCharterInPriceEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_DailyCharterInPrice()),
				"Daily Charter In Price");
	}

	/**
	 * Create an editor for the dailyCharterOutPrice feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createDailyCharterOutPriceEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_DailyCharterOutPrice()),
				"Daily Charter Out Price");
	}

	/**
	 * Create an editor for the ladenAttributes feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createLadenAttributesEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		final VesselStateAttributesComposite sub = new VesselStateAttributesComposite(
				composite, composite.getStyle(), "Laden Attributes", false);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sub.setPath(new CompiledEMFPath(composite.getInputPath(),
				FleetPackage.eINSTANCE.getVesselClass_LadenAttributes()));
		composite.addSubEditor(sub);
	}

	/**
	 * Create an editor for the ballastAttributes feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createBallastAttributesEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		final VesselStateAttributesComposite sub = new VesselStateAttributesComposite(
				composite, composite.getStyle(), "Ballast Attributes", false);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sub.setPath(new CompiledEMFPath(composite.getInputPath(),
				FleetPackage.eINSTANCE.getVesselClass_BallastAttributes()));
		composite.addSubEditor(sub);
	}

	/**
	 * Create an editor for the baseFuel feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createBaseFuelEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup,
				composite.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_BaseFuel()), "Base Fuel");
	}

	/**
	 * Create an editor for the portExclusions feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createPortExclusionsEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_PortExclusions()), "Port Exclusions");
	}

	/**
	 * Create an editor for the inaccessiblePorts feature on VesselClass
	 * 
	 * @generated
	 */
	protected static void createInaccessiblePortsEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite
				.createEditor(FleetPackage.eINSTANCE
						.getVesselClass_InaccessiblePorts()),
				"Inaccessible Ports");
	}

	/**
	 * Create an editor for the canalCosts feature on VesselClass
	 * 
	 * @generated NO custom editor
	 */
	protected static void createCanalCostsEditor(
			final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(
				mainGroup,
				new VesselClassCostEditor(composite.getInputPath(),
						FleetPackage.eINSTANCE.getVesselClass_CanalCosts(),
						composite.getCommandProcessor(), composite
								.getEditingDomain(), composite
								.getValueProviderProvider()),
				"Canal Costs");
	}
}
