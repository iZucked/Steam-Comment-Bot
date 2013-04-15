/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.VesselType;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	private EClass vesselAvailabilityEClass = null;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselClassRouteParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioFleetModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselTypeGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum vesselTypeEEnum = null;

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
		PortPackage.eINSTANCE.eClass();

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
	public EAttribute getVessel_TimeCharterRate() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVessel_Capacity() {
		return (EAttribute)vesselEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVessel_FillCapacity() {
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
	public EAttribute getVesselClass_CoolingVolume() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClass_RouteParameters() {
		return (EReference)vesselClassEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClass_PilotLightRate() {
		return (EAttribute)vesselClassEClass.getEStructuralFeatures().get(12);
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
	public EReference getFleetModel_BaseFuels() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_VesselGroups() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_SpecialVesselGroups() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFleetModel_ScenarioFleetModel() {
		return (EReference)fleetModelEClass.getEStructuralFeatures().get(5);
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
	public EAttribute getBaseFuel_EquivalenceFactor() {
		return (EAttribute)baseFuelEClass.getEStructuralFeatures().get(0);
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
	public EAttribute getCharterOutEvent_RepositioningFee() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCharterOutEvent_HireRate() {
		return (EAttribute)charterOutEventEClass.getEStructuralFeatures().get(3);
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
	public EAttribute getVesselStateAttributes_InPortBaseRate() {
		return (EAttribute)vesselStateAttributesEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselStateAttributes_FuelConsumption() {
		return (EReference)vesselStateAttributesEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselAvailability() {
		return vesselAvailabilityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_StartAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_StartAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_StartBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_EndAt() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_EndAfter() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselAvailability_EndBy() {
		return (EAttribute)vesselAvailabilityEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_Vessel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselAvailability_StartHeel() {
		return (EReference)vesselAvailabilityEClass.getEStructuralFeatures().get(7);
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
	public EClass getVesselClassRouteParameters() {
		return vesselClassRouteParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselClassRouteParameters_Route() {
		return (EReference)vesselClassRouteParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClassRouteParameters_ExtraTransitTime() {
		return (EAttribute)vesselClassRouteParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClassRouteParameters_LadenConsumptionRate() {
		return (EAttribute)vesselClassRouteParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClassRouteParameters_LadenNBORate() {
		return (EAttribute)vesselClassRouteParametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClassRouteParameters_BallastConsumptionRate() {
		return (EAttribute)vesselClassRouteParametersEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselClassRouteParameters_BallastNBORate() {
		return (EAttribute)vesselClassRouteParametersEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScenarioFleetModel() {
		return scenarioFleetModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioFleetModel_VesselAvailabilities() {
		return (EReference)scenarioFleetModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioFleetModel_VesselEvents() {
		return (EReference)scenarioFleetModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselGroup() {
		return vesselGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselGroup_Vessels() {
		return (EReference)vesselGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselTypeGroup() {
		return vesselTypeGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVesselTypeGroup_VesselType() {
		return (EAttribute)vesselTypeGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVesselType() {
		return vesselTypeEEnum;
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
		fleetModelEClass = createEClass(FLEET_MODEL);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSELS);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_CLASSES);
		createEReference(fleetModelEClass, FLEET_MODEL__BASE_FUELS);
		createEReference(fleetModelEClass, FLEET_MODEL__VESSEL_GROUPS);
		createEReference(fleetModelEClass, FLEET_MODEL__SPECIAL_VESSEL_GROUPS);
		createEReference(fleetModelEClass, FLEET_MODEL__SCENARIO_FLEET_MODEL);

		baseFuelEClass = createEClass(BASE_FUEL);
		createEAttribute(baseFuelEClass, BASE_FUEL__EQUIVALENCE_FACTOR);

		vesselEClass = createEClass(VESSEL);
		createEReference(vesselEClass, VESSEL__VESSEL_CLASS);
		createEReference(vesselEClass, VESSEL__INACCESSIBLE_PORTS);
		createEAttribute(vesselEClass, VESSEL__TIME_CHARTER_RATE);
		createEAttribute(vesselEClass, VESSEL__CAPACITY);
		createEAttribute(vesselEClass, VESSEL__FILL_CAPACITY);

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
		createEAttribute(vesselClassEClass, VESSEL_CLASS__COOLING_VOLUME);
		createEReference(vesselClassEClass, VESSEL_CLASS__ROUTE_PARAMETERS);
		createEAttribute(vesselClassEClass, VESSEL_CLASS__PILOT_LIGHT_RATE);

		vesselGroupEClass = createEClass(VESSEL_GROUP);
		createEReference(vesselGroupEClass, VESSEL_GROUP__VESSELS);

		vesselTypeGroupEClass = createEClass(VESSEL_TYPE_GROUP);
		createEAttribute(vesselTypeGroupEClass, VESSEL_TYPE_GROUP__VESSEL_TYPE);

		heelOptionsEClass = createEClass(HEEL_OPTIONS);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__VOLUME_AVAILABLE);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__CV_VALUE);
		createEAttribute(heelOptionsEClass, HEEL_OPTIONS__PRICE_PER_MMBTU);

		vesselStateAttributesEClass = createEClass(VESSEL_STATE_ATTRIBUTES);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE);
		createEAttribute(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
		createEReference(vesselStateAttributesEClass, VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);

		fuelConsumptionEClass = createEClass(FUEL_CONSUMPTION);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__SPEED);
		createEAttribute(fuelConsumptionEClass, FUEL_CONSUMPTION__CONSUMPTION);

		vesselClassRouteParametersEClass = createEClass(VESSEL_CLASS_ROUTE_PARAMETERS);
		createEReference(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE);
		createEAttribute(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME);
		createEAttribute(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE);
		createEAttribute(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE);
		createEAttribute(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE);
		createEAttribute(vesselClassRouteParametersEClass, VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE);

		scenarioFleetModelEClass = createEClass(SCENARIO_FLEET_MODEL);
		createEReference(scenarioFleetModelEClass, SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES);
		createEReference(scenarioFleetModelEClass, SCENARIO_FLEET_MODEL__VESSEL_EVENTS);

		vesselAvailabilityEClass = createEClass(VESSEL_AVAILABILITY);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_AT);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_AFTER);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_BY);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_AT);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_AFTER);
		createEAttribute(vesselAvailabilityEClass, VESSEL_AVAILABILITY__END_BY);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__VESSEL);
		createEReference(vesselAvailabilityEClass, VESSEL_AVAILABILITY__START_HEEL);

		vesselEventEClass = createEClass(VESSEL_EVENT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__DURATION_IN_DAYS);
		createEReference(vesselEventEClass, VESSEL_EVENT__ALLOWED_VESSELS);
		createEReference(vesselEventEClass, VESSEL_EVENT__PORT);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_AFTER);
		createEAttribute(vesselEventEClass, VESSEL_EVENT__START_BY);

		maintenanceEventEClass = createEClass(MAINTENANCE_EVENT);

		dryDockEventEClass = createEClass(DRY_DOCK_EVENT);

		charterOutEventEClass = createEClass(CHARTER_OUT_EVENT);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__RELOCATE_TO);
		createEReference(charterOutEventEClass, CHARTER_OUT_EVENT__HEEL_OPTIONS);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__REPOSITIONING_FEE);
		createEAttribute(charterOutEventEClass, CHARTER_OUT_EVENT__HIRE_RATE);

		// Create enums
		vesselTypeEEnum = createEEnum(VESSEL_TYPE);
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
		vesselClassEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(this.getVessel());
		g1.getETypeArguments().add(g2);
		vesselGroupEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(this.getVessel());
		g1.getETypeArguments().add(g2);
		vesselTypeGroupEClass.getEGenericSuperTypes().add(g1);
		heelOptionsEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselStateAttributesEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselClassRouteParametersEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		scenarioFleetModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		vesselAvailabilityEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		vesselEventEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		vesselEventEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		vesselEventEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		maintenanceEventEClass.getESuperTypes().add(this.getVesselEvent());
		dryDockEventEClass.getESuperTypes().add(this.getVesselEvent());
		charterOutEventEClass.getESuperTypes().add(this.getVesselEvent());

		// Initialize classes and features; add operations and parameters
		initEClass(fleetModelEClass, FleetModel.class, "FleetModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFleetModel_Vessels(), this.getVessel(), null, "vessels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselClasses(), this.getVesselClass(), null, "vesselClasses", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_BaseFuels(), this.getBaseFuel(), null, "baseFuels", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_VesselGroups(), this.getVesselGroup(), null, "vesselGroups", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_SpecialVesselGroups(), this.getVesselTypeGroup(), null, "specialVesselGroups", null, 0, -1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFleetModel_ScenarioFleetModel(), this.getScenarioFleetModel(), null, "scenarioFleetModel", null, 0, 1, FleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseFuelEClass, BaseFuel.class, "BaseFuel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBaseFuel_EquivalenceFactor(), ecorePackage.getEDouble(), "equivalenceFactor", null, 1, 1, BaseFuel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEClass, Vessel.class, "Vessel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVessel_VesselClass(), this.getVesselClass(), null, "vesselClass", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVessel_InaccessiblePorts(), g1, null, "inaccessiblePorts", null, 0, -1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_TimeCharterRate(), ecorePackage.getEInt(), "timeCharterRate", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_Capacity(), ecorePackage.getEInt(), "capacity", null, 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVessel_FillCapacity(), ecorePackage.getEDouble(), "fillCapacity", "1", 1, 1, Vessel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEInt(), "getVesselOrVesselClassCapacity", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(vesselEClass, ecorePackage.getEDouble(), "getVesselOrVesselClassFillCapacity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(vesselClassEClass, VesselClass.class, "VesselClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselClass_InaccessiblePorts(), g1, null, "inaccessiblePorts", null, 0, -1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_BaseFuel(), this.getBaseFuel(), null, "baseFuel", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_Capacity(), ecorePackage.getEInt(), "capacity", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_FillCapacity(), ecorePackage.getEDouble(), "fillCapacity", "1", 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_LadenAttributes(), this.getVesselStateAttributes(), null, "ladenAttributes", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_BallastAttributes(), this.getVesselStateAttributes(), null, "ballastAttributes", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MinSpeed(), ecorePackage.getEDouble(), "minSpeed", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MaxSpeed(), ecorePackage.getEDouble(), "maxSpeed", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_MinHeel(), ecorePackage.getEInt(), "minHeel", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_WarmingTime(), ecorePackage.getEInt(), "warmingTime", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_CoolingVolume(), ecorePackage.getEInt(), "coolingVolume", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselClass_RouteParameters(), this.getVesselClassRouteParameters(), null, "routeParameters", null, 0, -1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClass_PilotLightRate(), ecorePackage.getEInt(), "pilotLightRate", null, 1, 1, VesselClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselGroupEClass, VesselGroup.class, "VesselGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselGroup_Vessels(), this.getVessel(), null, "vessels", null, 0, -1, VesselGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselTypeGroupEClass, VesselTypeGroup.class, "VesselTypeGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselTypeGroup_VesselType(), this.getVesselType(), "vesselType", null, 1, 1, VesselTypeGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(heelOptionsEClass, HeelOptions.class, "HeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHeelOptions_VolumeAvailable(), ecorePackage.getEInt(), "volumeAvailable", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHeelOptions_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHeelOptions_PricePerMMBTU(), ecorePackage.getEDouble(), "pricePerMMBTU", null, 1, 1, HeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselStateAttributesEClass, VesselStateAttributes.class, "VesselStateAttributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselStateAttributes_NboRate(), ecorePackage.getEInt(), "nboRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleNBORate(), ecorePackage.getEInt(), "idleNBORate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_IdleBaseRate(), ecorePackage.getEInt(), "idleBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselStateAttributes_InPortBaseRate(), ecorePackage.getEInt(), "inPortBaseRate", null, 1, 1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselStateAttributes_FuelConsumption(), this.getFuelConsumption(), null, "fuelConsumption", null, 0, -1, VesselStateAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fuelConsumptionEClass, FuelConsumption.class, "FuelConsumption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelConsumption_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelConsumption_Consumption(), ecorePackage.getEInt(), "consumption", null, 1, 1, FuelConsumption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselClassRouteParametersEClass, VesselClassRouteParameters.class, "VesselClassRouteParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselClassRouteParameters_Route(), thePortPackage.getRoute(), null, "route", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClassRouteParameters_ExtraTransitTime(), ecorePackage.getEInt(), "extraTransitTime", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClassRouteParameters_LadenConsumptionRate(), ecorePackage.getEInt(), "ladenConsumptionRate", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClassRouteParameters_LadenNBORate(), ecorePackage.getEInt(), "ladenNBORate", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClassRouteParameters_BallastConsumptionRate(), ecorePackage.getEInt(), "ballastConsumptionRate", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselClassRouteParameters_BallastNBORate(), ecorePackage.getEInt(), "ballastNBORate", null, 1, 1, VesselClassRouteParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scenarioFleetModelEClass, ScenarioFleetModel.class, "ScenarioFleetModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScenarioFleetModel_VesselAvailabilities(), this.getVesselAvailability(), null, "vesselAvailabilities", null, 0, -1, ScenarioFleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioFleetModel_VesselEvents(), this.getVesselEvent(), null, "vesselEvents", null, 0, -1, ScenarioFleetModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselAvailabilityEClass, VesselAvailability.class, "VesselAvailability", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselAvailability_StartAt(), g1, null, "startAt", null, 0, -1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartAfter(), ecorePackage.getEDate(), "startAfter", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_StartBy(), ecorePackage.getEDate(), "startBy", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselAvailability_EndAt(), g1, null, "endAt", null, 0, -1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndAfter(), ecorePackage.getEDate(), "endAfter", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselAvailability_EndBy(), ecorePackage.getEDate(), "endBy", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_Vessel(), this.getVessel(), null, "vessel", null, 0, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselAvailability_StartHeel(), this.getHeelOptions(), null, "startHeel", null, 1, 1, VesselAvailability.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventEClass, VesselEvent.class, "VesselEvent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVesselEvent_DurationInDays(), ecorePackage.getEInt(), "durationInDays", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(this.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getVesselEvent_AllowedVessels(), g1, null, "allowedVessels", null, 0, -1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEvent_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartAfter(), ecorePackage.getEDate(), "startAfter", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVesselEvent_StartBy(), ecorePackage.getEDate(), "startBy", null, 1, 1, VesselEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(maintenanceEventEClass, MaintenanceEvent.class, "MaintenanceEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dryDockEventEClass, DryDockEvent.class, "DryDockEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(charterOutEventEClass, CharterOutEvent.class, "CharterOutEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharterOutEvent_RelocateTo(), thePortPackage.getPort(), null, "relocateTo", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCharterOutEvent_HeelOptions(), this.getHeelOptions(), null, "heelOptions", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_RepositioningFee(), ecorePackage.getEInt(), "repositioningFee", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCharterOutEvent_HireRate(), ecorePackage.getEInt(), "hireRate", null, 1, 1, CharterOutEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(charterOutEventEClass, thePortPackage.getPort(), "getEndPort", 1, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(vesselTypeEEnum, VesselType.class, "VesselType");
		addEEnumLiteral(vesselTypeEEnum, VesselType.OWNED);
		addEEnumLiteral(vesselTypeEEnum, VesselType.TIME_CHARTERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
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
		  (getVessel_Capacity(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });		
		addAnnotation
		  (getVessel_FillCapacity(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "###.#",
			 "unit", "%"
		   });		
		addAnnotation
		  (getVesselClass_Capacity(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });		
		addAnnotation
		  (getVesselClass_FillCapacity(), 
		   source, 
		   new String[] {
			 "scale", "100",
			 "formatString", "###.#",
			 "unit", "%"
		   });		
		addAnnotation
		  (getVesselClass_MinSpeed(), 
		   source, 
		   new String[] {
			 "unit", "kts"
		   });		
		addAnnotation
		  (getVesselClass_MaxSpeed(), 
		   source, 
		   new String[] {
			 "unit", "kts"
		   });		
		addAnnotation
		  (getVesselClass_MinHeel(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });		
		addAnnotation
		  (getVesselClass_WarmingTime(), 
		   source, 
		   new String[] {
			 "unit", "hrs"
		   });		
		addAnnotation
		  (getVesselClass_CoolingVolume(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });		
		addAnnotation
		  (getVesselClass_PilotLightRate(), 
		   source, 
		   new String[] {
			 "unit", "MT/d"
		   });		
		addAnnotation
		  (getHeelOptions_VolumeAvailable(), 
		   source, 
		   new String[] {
			 "unit", "m3"
		   });		
		addAnnotation
		  (getVesselStateAttributes_NboRate(), 
		   source, 
		   new String[] {
			 "unit", "m3/d"
		   });		
		addAnnotation
		  (getVesselStateAttributes_IdleNBORate(), 
		   source, 
		   new String[] {
			 "unit", "m3/d"
		   });		
		addAnnotation
		  (getVesselStateAttributes_IdleBaseRate(), 
		   source, 
		   new String[] {
			 "unit", "MT/d"
		   });		
		addAnnotation
		  (getVesselStateAttributes_InPortBaseRate(), 
		   source, 
		   new String[] {
			 "unit", "MT/d"
		   });
	}

} //FleetPackageImpl
