/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.datetime.DateTimePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.EmissionParameters;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FleetPackageImpl extends EPackageImpl implements FleetPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fleetModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseFuelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselStateAttributesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelConsumptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselRouteParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselGroupEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private FleetPackageImpl() {
		super(eNS_URI, FleetFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link FleetPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static FleetPackage init() {
		if (isInited) return (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredFleetPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		FleetPackageImpl theFleetPackage = registeredFleetPackage instanceof FleetPackageImpl ? (FleetPackageImpl)registeredFleetPackage : new FleetPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theFleetPackage.createPackageContents();

		// Initialize created meta-data
		theFleetPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theFleetPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(FleetPackage.eNS_URI, theFleetPackage);
		return theFleetPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVessel() {
		return vesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_ShortName() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_IMO() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_Type() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_Reference() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_InaccessiblePorts() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_InaccessibleRoutesOverride() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_Capacity() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_FillCapacity() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_LadenAttributes() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_BallastAttributes() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_MinSpeed() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_MaxSpeed() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_SafetyHeel() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_WarmingTime() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_PurgeTime() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_CoolingVolume() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_CoolingTime() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_PurgeVolume() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_RouteParametersOverride() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_RouteParameters() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_PilotLightRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_MinBaseFuelConsumption() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_HasReliqCapabilityOverride() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_HasReliqCapability() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(30);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_Notes() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(31);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_MmxId() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(32);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_ReferenceVessel() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(33);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_MmxReference() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(34);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_Marker() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(35);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_BaseFuelEmissionRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(36);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_BogEmissionRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(37);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_PilotLightEmissionRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(38);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_Scnt() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_InaccessiblePortsOverride() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVessel_InaccessibleRoutes() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_BaseFuel() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_InPortBaseFuel() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_PilotLightBaseFuel() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVessel_IdleBaseFuel() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFleetModel() {
		return fleetModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_Vessels() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_BaseFuels() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_VesselGroups() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_FleetVersionRecord() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_VesselGroupVersionRecord() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFleetModel_BunkerFuelsVersionRecord() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFleetModel_MMXVesselDBVersion() {
		return (EAttribute)fleetModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseFuel() {
		return baseFuelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseFuel_EquivalenceFactor() {
		return (EAttribute)baseFuelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselStateAttributes() {
		return vesselStateAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_NboRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_IdleNBORate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_IdleBaseRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_InPortBaseRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_FuelConsumptionOverride() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselStateAttributes_FuelConsumption() {
		return (EReference)vesselStateAttributesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_ServiceSpeed() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselStateAttributes_InPortNBORate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFuelConsumption() {
		return fuelConsumptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelConsumption_Speed() {
		return (EAttribute)fuelConsumptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFuelConsumption_Consumption() {
		return (EAttribute)fuelConsumptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselRouteParameters() {
		return vesselRouteParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_RouteOption() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_ExtraTransitTime() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_LadenConsumptionRate() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_LadenNBORate() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_BallastConsumptionRate() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVesselRouteParameters_BallastNBORate() {
		return (EAttribute)vesselRouteParametersEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVesselGroup() {
		return vesselGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVesselGroup_Vessels() {
		return (EReference)vesselGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetFactory getFleetFactory() {
		return (FleetFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		fleetModelEClass = createEClass(FLEET_MODEL);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSELS);
		createEReference(fleetModelEClass, FLEET_MODEL__BASE_FUELS);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_GROUPS);
		createEReference(fleetModelEClass, FLEET_MODEL__FLEET_VERSION_RECORD);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD);
		createEReference(fleetModelEClass, FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD);
		createEAttribute(fleetModelEClass, FLEET_MODEL__MMX_VESSEL_DB_VERSION);

		baseFuelEClass = createEClass(BASE_FUEL);
		createEAttribute(baseFuelEClass, BASE_FUEL__EQUIVALENCE_FACTOR);

		vesselEClass = createEClass(VESSEL);
		createEAttribute(vesselEClass, VESSEL__SHORT_NAME);
		createEAttribute(vesselEClass, VESSEL__IMO);
		createEAttribute(vesselEClass, VESSEL__TYPE);
		createEReference(vesselEClass, VESSEL__REFERENCE);
		createEAttribute(vesselEClass, VESSEL__CAPACITY);
		createEAttribute(vesselEClass, VESSEL__FILL_CAPACITY);
		createEAttribute(vesselEClass, VESSEL__SCNT);
		createEReference(vesselEClass, VESSEL__BASE_FUEL);
		createEReference(vesselEClass, VESSEL__IN_PORT_BASE_FUEL);
		createEReference(vesselEClass, VESSEL__PILOT_LIGHT_BASE_FUEL);
		createEReference(vesselEClass, VESSEL__IDLE_BASE_FUEL);
		createEAttribute(vesselEClass, VESSEL__PILOT_LIGHT_RATE);
		createEAttribute(vesselEClass, VESSEL__SAFETY_HEEL);
		createEAttribute(vesselEClass, VESSEL__WARMING_TIME);
		createEAttribute(vesselEClass, VESSEL__COOLING_VOLUME);
		createEAttribute(vesselEClass, VESSEL__COOLING_TIME);
		createEAttribute(vesselEClass, VESSEL__PURGE_VOLUME);
		createEAttribute(vesselEClass, VESSEL__PURGE_TIME);
		createEReference(vesselEClass, VESSEL__LADEN_ATTRIBUTES);
		createEReference(vesselEClass, VESSEL__BALLAST_ATTRIBUTES);
		createEAttribute(vesselEClass, VESSEL__MIN_SPEED);
		createEAttribute(vesselEClass, VESSEL__MAX_SPEED);
		createEAttribute(vesselEClass, VESSEL__INACCESSIBLE_PORTS_OVERRIDE);
		createEReference(vesselEClass, VESSEL__INACCESSIBLE_PORTS);
		createEAttribute(vesselEClass, VESSEL__INACCESSIBLE_ROUTES_OVERRIDE);
		createEAttribute(vesselEClass, VESSEL__INACCESSIBLE_ROUTES);
		createEAttribute(vesselEClass, VESSEL__ROUTE_PARAMETERS_OVERRIDE);
		createEReference(vesselEClass, VESSEL__ROUTE_PARAMETERS);
		createEAttribute(vesselEClass, VESSEL__MIN_BASE_FUEL_CONSUMPTION);
		createEAttribute(vesselEClass, VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE);
		createEAttribute(vesselEClass, VESSEL__HAS_RELIQ_CAPABILITY);
		createEAttribute(vesselEClass, VESSEL__NOTES);
		createEAttribute(vesselEClass, VESSEL__MMX_ID);
		createEAttribute(vesselEClass, VESSEL__REFERENCE_VESSEL);
		createEAttribute(vesselEClass, VESSEL__MMX_REFERENCE);
		createEAttribute(vesselEClass, VESSEL__MARKER);
		createEAttribute(vesselEClass, VESSEL__BASE_FUEL_EMISSION_RATE);
		createEAttribute(vesselEClass, VESSEL__BOG_EMISSION_RATE);
		createEAttribute(vesselEClass, VESSEL__PILOT_LIGHT_EMISSION_RATE);

		vesselGroupEClass = createEClass(VESSEL_GROUP);
		createEReference(vesselGroupEClass, VESSEL_GROUP__VESSELS);

		vesselStateAttributesEClass = createEClass(VESSEL_STATE_ATTRIBUTES);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE);
		createEReference(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE);

		fuelConsumptionEClass = createEClass(FUEL_CONSUMPTION);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__SPEED);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__CONSUMPTION);

		vesselRouteParametersEClass = createEClass(VESSEL_ROUTE_PARAMETERS);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__ROUTE_OPTION);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__LADEN_NBO_RATE);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE);
		createEAttribute(vesselRouteParametersEClass, VESSEL_ROUTE_PARAMETERS__BALLAST_NBO_RATE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		fleetModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseFuelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseFuelEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		EGenericType g1 = createEGenericType(theTypesPackage.getAVesselSet());
		EGenericType g2 = createEGenericType(this.getVessel());
		g1.getETypeArguments().add(g2);
		vesselEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(this.getVessel());
		g1.getETypeArguments().add(g2);
		vesselGroupEClass.getEGenericSuperTypes().add(g1);
		vesselStateAttributesEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselRouteParametersEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());

		// Initialize classes and features; add operations and parameters
		initEClass(fleetModelEClass, FleetModel.class, "FleetModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetModel_Vessels(), this.getVessel(), null, "vessels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_BaseFuels(), this.getBaseFuel(), null, "baseFuels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselGroups(), this.getVesselGroup(), null, "vesselGroups", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_FleetVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "fleetVersionRecord", null, 0, 1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselGroupVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "vesselGroupVersionRecord", null, 0, 1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_BunkerFuelsVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "bunkerFuelsVersionRecord", null, 0, 1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFleetModel_MMXVesselDBVersion(), ecorePackage.getEString(), "MMXVesselDBVersion", null, 0, 1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelEClass, BaseFuel.class, "BaseFuel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBaseFuel_EquivalenceFactor(), ecorePackage.getEDouble(), "equivalenceFactor", null, 1, 1, BaseFuel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEClass, Vessel.class, "Vessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVessel_ShortName(), ecorePackage.getEString(), "shortName", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_IMO(), ecorePackage.getEString(), "IMO", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Type(), ecorePackage.getEString(), "type", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_Reference(), this.getVessel(), null, "reference", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Capacity(), ecorePackage.getEInt(), "capacity", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_FillCapacity(), ecorePackage.getEDouble(), "fillCapacity", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Scnt(), ecorePackage.getEInt(), "scnt", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_BaseFuel(), this.getBaseFuel(), null, "baseFuel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_InPortBaseFuel(), this.getBaseFuel(), null, "inPortBaseFuel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_PilotLightBaseFuel(), this.getBaseFuel(), null, "pilotLightBaseFuel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_IdleBaseFuel(), this.getBaseFuel(), null, "idleBaseFuel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_PilotLightRate(), ecorePackage.getEDouble(), "pilotLightRate", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_SafetyHeel(), ecorePackage.getEInt(), "safetyHeel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_WarmingTime(), ecorePackage.getEInt(), "warmingTime", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_CoolingVolume(), ecorePackage.getEInt(), "coolingVolume", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_CoolingTime(), ecorePackage.getEInt(), "coolingTime", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_PurgeVolume(), ecorePackage.getEInt(), "purgeVolume", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_PurgeTime(), ecorePackage.getEInt(), "purgeTime", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_LadenAttributes(), this.getVesselStateAttributes(), null, "ladenAttributes", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_BallastAttributes(), this.getVesselStateAttributes(), null, "ballastAttributes", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_MinSpeed(), ecorePackage.getEDouble(), "minSpeed", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_MaxSpeed(), ecorePackage.getEDouble(), "maxSpeed", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_InaccessiblePortsOverride(), ecorePackage.getEBoolean(), "inaccessiblePortsOverride", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVessel_InaccessiblePorts(), g1, null, "inaccessiblePorts", null, 0, -1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_InaccessibleRoutesOverride(), ecorePackage.getEBoolean(), "inaccessibleRoutesOverride", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_InaccessibleRoutes(), thePortPackage.getRouteOption(), "inaccessibleRoutes", null, 0, -1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_RouteParametersOverride(), ecorePackage.getEBoolean(), "routeParametersOverride", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_RouteParameters(), this.getVesselRouteParameters(), null, "routeParameters", null, 0, -1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_MinBaseFuelConsumption(), ecorePackage.getEDouble(), "minBaseFuelConsumption", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_HasReliqCapabilityOverride(), ecorePackage.getEBoolean(), "hasReliqCapabilityOverride", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_HasReliqCapability(), ecorePackage.getEBoolean(), "hasReliqCapability", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_MmxId(), ecorePackage.getEString(), "mmxId", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_ReferenceVessel(), ecorePackage.getEBoolean(), "referenceVessel", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_MmxReference(), ecorePackage.getEBoolean(), "mmxReference", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Marker(), ecorePackage.getEBoolean(), "marker", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_BaseFuelEmissionRate(), ecorePackage.getEDouble(), "baseFuelEmissionRate", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_BogEmissionRate(), ecorePackage.getEDouble(), "bogEmissionRate", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_PilotLightEmissionRate(), ecorePackage.getEDouble(), "pilotLightEmissionRate", null, 0, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEString(), "getShortenedName", 0, 1, IS_UNIQUE, IS_ORDERED);

		EOperation op = addEOperation(vesselEClass, null, "getVesselOrDelegateInaccessiblePorts", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		addEOperation(vesselEClass, thePortPackage.getRouteOption(), "getVesselOrDelegateInaccessibleRoutes", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, this.getBaseFuel(), "getVesselOrDelegateBaseFuel", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, this.getBaseFuel(), "getVesselOrDelegateIdleBaseFuel", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, this.getBaseFuel(), "getVesselOrDelegatePilotLightBaseFuel", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, this.getBaseFuel(), "getVesselOrDelegateInPortBaseFuel", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateCapacity", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrDelegateFillCapacity", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrDelegateMinSpeed", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrDelegateMaxSpeed", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateSafetyHeel", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegatePurgeTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegatePurgeVolume", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateWarmingTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateCoolingTime", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateCoolingVolume", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, this.getVesselRouteParameters(), "getVesselOrDelegateRouteParameters", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrDelegatePilotLightRate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrDelegateMinBaseFuelConsumption", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEBoolean(), "getVesselOrDelegateHasReliqCapability", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrDelegateSCNT", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(vesselGroupEClass, VesselGroup.class, "VesselGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselGroup_Vessels(), this.getVessel(), null, "vessels", null, 0, -1, VesselGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselStateAttributesEClass, VesselStateAttributes.class, "VesselStateAttributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselStateAttributes_NboRate(), ecorePackage.getEDouble(), "nboRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleNBORate(), ecorePackage.getEDouble(), "idleNBORate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleBaseRate(), ecorePackage.getEDouble(), "idleBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_InPortBaseRate(), ecorePackage.getEDouble(), "inPortBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_FuelConsumptionOverride(), ecorePackage.getEBoolean(), "fuelConsumptionOverride", null, 0, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselStateAttributes_FuelConsumption(), this.getFuelConsumption(), null, "fuelConsumption", null, 0, -1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_ServiceSpeed(), ecorePackage.getEDouble(), "serviceSpeed", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_InPortNBORate(), ecorePackage.getEDouble(), "inPortNBORate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateNBORate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateIdleNBORate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateIdleBaseRate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateInPortBaseRate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateServiceSpeed", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, ecorePackage.getEDouble(), "getVesselOrDelegateInPortNBORate", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselStateAttributesEClass, this.getFuelConsumption(), "getVesselOrDelegateFuelConsumption", 1, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelConsumptionEClass, FuelConsumption.class, "FuelConsumption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelConsumption_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelConsumption_Consumption(), ecorePackage.getEDouble(), "consumption", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselRouteParametersEClass, VesselRouteParameters.class, "VesselRouteParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselRouteParameters_RouteOption(), thePortPackage.getRouteOption(), "routeOption", null, 0, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselRouteParameters_ExtraTransitTime(), ecorePackage.getEInt(), "extraTransitTime", null, 1, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselRouteParameters_LadenConsumptionRate(), ecorePackage.getEDouble(), "ladenConsumptionRate", null, 1, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselRouteParameters_LadenNBORate(), ecorePackage.getEDouble(), "ladenNBORate", null, 1, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselRouteParameters_BallastConsumptionRate(), ecorePackage.getEDouble(), "ballastConsumptionRate", null, 1, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselRouteParameters_BallastNBORate(), ecorePackage.getEDouble(), "ballastNBORate", null, 1, 1, VesselRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/featureOverride
		createFeatureOverrideAnnotations();
		// http://www.mmxlabs.com/models/ui/featureEnablement
		createFeatureEnablementAnnotations();
		// http://www.mmxlabs.com/models/validation
		createValidationAnnotations();
		// http://www.mmxlabs.com/models/csv
		createCsvAnnotations();
		// http://www.mmxlabs.com/models/featureOverrideByContainer
		createFeatureOverrideByContainerAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";
		addAnnotation
		  (getBaseFuel_EquivalenceFactor(),
		   source,
		   new String[] {
			   "unit", "mmBtu/mt",
			   "formatString", "##.###"
		   });
		addAnnotation
		  (getVessel_Capacity(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0"
		   });
		addAnnotation
		  (getVessel_FillCapacity(),
		   source,
		   new String[] {
			   "scale", "100",
			   "formatString", "##0.#",
			   "unit", "%",
			   "exportFormatString", "#.###"
		   });
		addAnnotation
		  (getVessel_PilotLightRate(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.##"
		   });
		addAnnotation
		  (getVessel_SafetyHeel(),
		   source,
		   new String[] {
			   "formatString", "###,##0",
			   "unit", "m\u00b3"
		   });
		addAnnotation
		  (getVessel_WarmingTime(),
		   source,
		   new String[] {
			   "unit", "hrs",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVessel_CoolingVolume(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0"
		   });
		addAnnotation
		  (getVessel_CoolingTime(),
		   source,
		   new String[] {
			   "unit", "hrs",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVessel_PurgeVolume(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0"
		   });
		addAnnotation
		  (getVessel_PurgeTime(),
		   source,
		   new String[] {
			   "unit", "hrs",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVessel_MinSpeed(),
		   source,
		   new String[] {
			   "unit", "kts",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getVessel_MaxSpeed(),
		   source,
		   new String[] {
			   "unit", "kts",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getVessel_MinBaseFuelConsumption(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVessel_BaseFuelEmissionRate(),
		   source,
		   new String[] {
			   "unit", "kg/MT",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVessel_BogEmissionRate(),
		   source,
		   new String[] {
			   "unit", "kg/m\u00b3",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVessel_PilotLightEmissionRate(),
		   source,
		   new String[] {
			   "unit", "kg/MT",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_NboRate(),
		   source,
		   new String[] {
			   "unit", "m\u00b3/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_IdleNBORate(),
		   source,
		   new String[] {
			   "unit", "m\u00b3/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_IdleBaseRate(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_InPortBaseRate(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_ServiceSpeed(),
		   source,
		   new String[] {
			   "unit", "kts",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getVesselStateAttributes_InPortNBORate(),
		   source,
		   new String[] {
			   "unit", "m\u00b3/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getFuelConsumption_Speed(),
		   source,
		   new String[] {
			   "unit", "kts",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getFuelConsumption_Consumption(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselRouteParameters_ExtraTransitTime(),
		   source,
		   new String[] {
			   "unit", "hours",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getVesselRouteParameters_LadenConsumptionRate(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselRouteParameters_LadenNBORate(),
		   source,
		   new String[] {
			   "unit", "m\u00b3/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselRouteParameters_BallastConsumptionRate(),
		   source,
		   new String[] {
			   "unit", "MT/day",
			   "formatString", "##0.###"
		   });
		addAnnotation
		  (getVesselRouteParameters_BallastNBORate(),
		   source,
		   new String[] {
			   "unit", "m\u00b3/day",
			   "formatString", "##0.###"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/featureOverride</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createFeatureOverrideAnnotations() {
		String source = "http://www.mmxlabs.com/models/featureOverride";
		addAnnotation
		  (vesselEClass,
		   source,
		   new String[] {
		   },
		   new URI[] {
			 URI.createURI(eNS_URI).appendFragment("//Vessel/reference")
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/featureEnablement</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createFeatureEnablementAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/featureEnablement";
		addAnnotation
		  (getVessel_CoolingTime(),
		   source,
		   new String[] {
			   "feature", "purge"
		   });
		addAnnotation
		  (getVessel_PurgeTime(),
		   source,
		   new String[] {
			   "feature", "purge"
		   });
		addAnnotation
		  (getVessel_HasReliqCapability(),
		   source,
		   new String[] {
			   "feature", "reliq-support"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/validation</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createValidationAnnotations() {
		String source = "http://www.mmxlabs.com/models/validation";
		addAnnotation
		  (getVessel_Notes(),
		   source,
		   new String[] {
			   "ignore", "true"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/csv</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createCsvAnnotations() {
		String source = "http://www.mmxlabs.com/models/csv";
		addAnnotation
		  (vesselGroupEClass,
		   source,
		   new String[] {
			   "namePrefix", "VesselGroup"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/featureOverrideByContainer</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createFeatureOverrideByContainerAnnotations() {
		String source = "http://www.mmxlabs.com/models/featureOverrideByContainer";
		addAnnotation
		  (vesselStateAttributesEClass,
		   source,
		   new String[] {
		   },
		   new URI[] {
			 URI.createURI(eNS_URI).appendFragment("//Vessel/reference")
		   });
	}

} //FleetPackageImpl
