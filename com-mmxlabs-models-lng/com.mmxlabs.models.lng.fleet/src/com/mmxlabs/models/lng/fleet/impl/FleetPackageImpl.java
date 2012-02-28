/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

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
	private EClass vesselClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventEClass = null;

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
	private EClass dryDockEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterOutEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass heelOptionsEClass = null;

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
	private EClass vesselAvailablilityEClass = null;

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
	private EClass maintenanceEventEClass = null;

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
		FleetPackageImpl theFleetPackage = (FleetPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof FleetPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new FleetPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		TypesPackage.eINSTANCE.eClass();

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
	public EClass getVessel() {
		return vesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVessel_VesselClass() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVessel_InaccessiblePorts() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVessel_StartHeel() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVessel_Availability() {
		return (EReference)vesselEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVessel_TimeCharterRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselClass() {
		return vesselClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClass_InaccessiblePorts() {
		return (EReference)vesselClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClass_BaseFuel() {
		return (EReference)vesselClassEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_Capacity() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_FillCapacity() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClass_LadenAttributes() {
		return (EReference)vesselClassEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClass_BallastAttributes() {
		return (EReference)vesselClassEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_MinSpeed() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_MaxSpeed() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_MinHeel() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_WarmingTime() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_CoolingTime() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_CoolingVolume() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselEvent() {
		return vesselEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_DurationInDays() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselEvent_AllowedVessels() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselEvent_Port() {
		return (EReference)vesselEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_StartAfter() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselEvent_StartBy() {
		return (EAttribute)vesselEventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFleetModel() {
		return fleetModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_Vessels() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_VesselClasses() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_VesselEvents() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_BaseFuels() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseFuel() {
		return baseFuelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDryDockEvent() {
		return dryDockEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterOutEvent() {
		return charterOutEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterOutEvent_RelocateTo() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCharterOutEvent_HeelOptions() {
		return (EReference)charterOutEventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHeelOptions() {
		return heelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeelOptions_VolumeAvailable() {
		return (EAttribute)heelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeelOptions_CvValue() {
		return (EAttribute)heelOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeelOptions_PricePerMMBTU() {
		return (EAttribute)heelOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselStateAttributes() {
		return vesselStateAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselStateAttributes_NboRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselStateAttributes_IdleNBORate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselStateAttributes_IdleBaseRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselStateAttributes_CanalNBORate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselStateAttributes_InPortBaseRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselStateAttributes_FuelConsumption() {
		return (EReference)vesselStateAttributesEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselAvailablility() {
		return vesselAvailablilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailablility_StartAt() {
		return (EReference)vesselAvailablilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailablility_StartAfter() {
		return (EAttribute)vesselAvailablilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailablility_StartBy() {
		return (EAttribute)vesselAvailablilityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailablility_EndAt() {
		return (EReference)vesselAvailablilityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailablility_EndAfter() {
		return (EAttribute)vesselAvailablilityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailablility_EndBy() {
		return (EAttribute)vesselAvailablilityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFuelConsumption() {
		return fuelConsumptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelConsumption_Speed() {
		return (EAttribute)fuelConsumptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelConsumption_Consumption() {
		return (EAttribute)fuelConsumptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMaintenanceEvent() {
		return maintenanceEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
		vesselEClass = createEClass(VESSEL);
		createEReference(vesselEClass, VESSEL__VESSEL_CLASS);
		createEReference(vesselEClass, VESSEL__INACCESSIBLE_PORTS);
		createEReference(vesselEClass, VESSEL__START_HEEL);
		createEReference(vesselEClass, VESSEL__AVAILABILITY);
		createEAttribute(vesselEClass, VESSEL__TIME_CHARTER_RATE);

		vesselClassEClass = createEClass(VESSEL_CLASS);
		createEReference(vesselClassEClass, VESSEL_CLASS__INACCESSIBLE_PORTS);
		createEReference(vesselClassEClass, VESSEL_CLASS__BASE_FUEL);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__CAPACITY);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__FILL_CAPACITY);
		createEReference(vesselClassEClass, VESSEL_CLASS__LADEN_ATTRIBUTES);
		createEReference(vesselClassEClass, VESSEL_CLASS__BALLAST_ATTRIBUTES);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__MIN_SPEED);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__MAX_SPEED);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__MIN_HEEL);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__WARMING_TIME);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__COOLING_TIME);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__COOLING_VOLUME);

		vesselEventEClass = createEClass(VESSEL_EVENT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__DURATION_IN_DAYS);
		createEReference(vesselEventEClass, VESSEL_EVENT__ALLOWED_VESSELS);
		createEReference(vesselEventEClass, VESSEL_EVENT__PORT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_AFTER);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_BY);

		fleetModelEClass = createEClass(FLEET_MODEL);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSELS);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_CLASSES);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_EVENTS);
		createEReference(fleetModelEClass, FLEET_MODEL__BASE_FUELS);

		baseFuelEClass = createEClass(BASE_FUEL);

		dryDockEventEClass = createEClass(DRY_DOCK_EVENT);

		charterOutEventEClass = createEClass(CHARTER_OUT_EVENT);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__RELOCATE_TO);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__HEEL_OPTIONS);

		heelOptionsEClass = createEClass(HEEL_OPTIONS);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__VOLUME_AVAILABLE);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__CV_VALUE);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__PRICE_PER_MMBTU);

		vesselStateAttributesEClass = createEClass(VESSEL_STATE_ATTRIBUTES);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__CANAL_NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
		createEReference(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);

		vesselAvailablilityEClass = createEClass(VESSEL_AVAILABLILITY);
		createEReference(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__START_AT);
		createEAttribute(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__START_AFTER);
		createEAttribute(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__START_BY);
		createEReference(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__END_AT);
		createEAttribute(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__END_AFTER);
		createEAttribute(vesselAvailablilityEClass, VESSEL_AVAILABLILITY__END_BY);

		fuelConsumptionEClass = createEClass(FUEL_CONSUMPTION);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__SPEED);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__CONSUMPTION);

		maintenanceEventEClass = createEClass(MAINTENANCE_EVENT);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		vesselEClass.getESuperTypes().add(theTypesPackage.getAVessel());
		vesselClassEClass.getESuperTypes().add(theTypesPackage.getAVesselClass());
		vesselEventEClass.getESuperTypes().add(theTypesPackage.getAVesselEvent());
		vesselEventEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		fleetModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		fleetModelEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		baseFuelEClass.getESuperTypes().add(theTypesPackage.getABaseFuel());
		dryDockEventEClass.getESuperTypes().add(this.getVesselEvent());
		charterOutEventEClass.getESuperTypes().add(this.getVesselEvent());
		heelOptionsEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselStateAttributesEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselAvailablilityEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		maintenanceEventEClass.getESuperTypes().add(this.getVesselEvent());

		// Initialize classes and features; add operations and parameters
		initEClass(vesselEClass, Vessel.class, "Vessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVessel_VesselClass(), this.getVesselClass(), null, "vesselClass", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_InaccessiblePorts(), theTypesPackage.getAPortSet(), null, "inaccessiblePorts", null, 0, -1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_StartHeel(), this.getHeelOptions(), null, "startHeel", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVessel_Availability(), this.getVesselAvailablility(), null, "availability", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_TimeCharterRate(), ecorePackage.getEInt(), "timeCharterRate", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselClassEClass, VesselClass.class, "VesselClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselClass_InaccessiblePorts(), theTypesPackage.getAPortSet(), null, "inaccessiblePorts", null, 0, -1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_BaseFuel(), this.getBaseFuel(), null, "baseFuel", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_Capacity(), ecorePackage.getEInt(), "capacity", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_FillCapacity(), ecorePackage.getEDouble(), "fillCapacity", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_LadenAttributes(), this.getVesselStateAttributes(), null, "ladenAttributes", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_BallastAttributes(), this.getVesselStateAttributes(), null, "ballastAttributes", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MinSpeed(), ecorePackage.getEDouble(), "minSpeed", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MaxSpeed(), ecorePackage.getEDouble(), "maxSpeed", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MinHeel(), ecorePackage.getEInt(), "minHeel", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_WarmingTime(), ecorePackage.getEInt(), "warmingTime", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_CoolingTime(), ecorePackage.getEInt(), "coolingTime", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_CoolingVolume(), ecorePackage.getEInt(), "coolingVolume", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(vesselClassEClass, theTypesPackage.getAVessel(), "collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theTypesPackage.getAVesselSet(), "marked", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(vesselEventEClass, VesselEvent.class, "VesselEvent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselEvent_DurationInDays(), ecorePackage.getEInt(), "durationInDays", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEvent_AllowedVessels(), theTypesPackage.getAVesselSet(), null, "allowedVessels", null, 0, -1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEvent_Port(), theTypesPackage.getAPort(), null, "port", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartAfter(), ecorePackage.getEDate(), "startAfter", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartBy(), ecorePackage.getEDate(), "startBy", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fleetModelEClass, FleetModel.class, "FleetModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetModel_Vessels(), this.getVessel(), null, "vessels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselClasses(), this.getVesselClass(), null, "vesselClasses", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselEvents(), this.getVesselEvent(), null, "vesselEvents", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_BaseFuels(), this.getBaseFuel(), null, "baseFuels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelEClass, BaseFuel.class, "BaseFuel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dryDockEventEClass, DryDockEvent.class, "DryDockEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(charterOutEventEClass, CharterOutEvent.class, "CharterOutEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterOutEvent_RelocateTo(), theTypesPackage.getAPort(), null, "relocateTo", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_HeelOptions(), this.getHeelOptions(), null, "heelOptions", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(heelOptionsEClass, HeelOptions.class, "HeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHeelOptions_VolumeAvailable(), ecorePackage.getEInt(), "volumeAvailable", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHeelOptions_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHeelOptions_PricePerMMBTU(), ecorePackage.getEDouble(), "pricePerMMBTU", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselStateAttributesEClass, VesselStateAttributes.class, "VesselStateAttributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselStateAttributes_NboRate(), ecorePackage.getEInt(), "nboRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleNBORate(), ecorePackage.getEInt(), "idleNBORate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleBaseRate(), ecorePackage.getEInt(), "idleBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_CanalNBORate(), ecorePackage.getEInt(), "canalNBORate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_InPortBaseRate(), ecorePackage.getEInt(), "inPortBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselStateAttributes_FuelConsumption(), this.getFuelConsumption(), null, "fuelConsumption", null, 0, -1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselAvailablilityEClass, VesselAvailablility.class, "VesselAvailablility", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselAvailablility_StartAt(), theTypesPackage.getAPortSet(), null, "startAt", null, 0, -1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailablility_StartAfter(), ecorePackage.getEDate(), "startAfter", null, 1, 1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailablility_StartBy(), ecorePackage.getEDate(), "startBy", null, 1, 1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailablility_EndAt(), theTypesPackage.getAPortSet(), null, "endAt", null, 0, -1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailablility_EndAfter(), ecorePackage.getEDate(), "endAfter", null, 1, 1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailablility_EndBy(), ecorePackage.getEDate(), "endBy", null, 1, 1, VesselAvailablility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fuelConsumptionEClass, FuelConsumption.class, "FuelConsumption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelConsumption_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelConsumption_Consumption(), ecorePackage.getEDouble(), "consumption", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(maintenanceEventEClass, MaintenanceEvent.class, "MaintenanceEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //FleetPackageImpl
