/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FleetFactoryImpl extends EFactoryImpl implements FleetFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FleetFactory init() {
		try {
			FleetFactory theFleetFactory = (FleetFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/fleet/1/"); 
			if (theFleetFactory != null) {
				return theFleetFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FleetFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case FleetPackage.VESSEL: return createVessel();
			case FleetPackage.VESSEL_CLASS: return createVesselClass();
			case FleetPackage.FLEET_MODEL: return createFleetModel();
			case FleetPackage.BASE_FUEL: return createBaseFuel();
			case FleetPackage.DRY_DOCK_EVENT: return createDryDockEvent();
			case FleetPackage.CHARTER_OUT_EVENT: return createCharterOutEvent();
			case FleetPackage.HEEL_OPTIONS: return createHeelOptions();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES: return createVesselStateAttributes();
			case FleetPackage.VESSEL_AVAILABLILITY: return createVesselAvailablility();
			case FleetPackage.FUEL_CONSUMPTION: return createFuelConsumption();
			case FleetPackage.MAINTENANCE_EVENT: return createMaintenanceEvent();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel createVessel() {
		VesselImpl vessel = new VesselImpl();
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass createVesselClass() {
		VesselClassImpl vesselClass = new VesselClassImpl();
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetModel createFleetModel() {
		FleetModelImpl fleetModel = new FleetModelImpl();
		return fleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel createBaseFuel() {
		BaseFuelImpl baseFuel = new BaseFuelImpl();
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DryDockEvent createDryDockEvent() {
		DryDockEventImpl dryDockEvent = new DryDockEventImpl();
		return dryDockEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOutEvent createCharterOutEvent() {
		CharterOutEventImpl charterOutEvent = new CharterOutEventImpl();
		return charterOutEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HeelOptions createHeelOptions() {
		HeelOptionsImpl heelOptions = new HeelOptionsImpl();
		return heelOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes createVesselStateAttributes() {
		VesselStateAttributesImpl vesselStateAttributes = new VesselStateAttributesImpl();
		return vesselStateAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailablility createVesselAvailablility() {
		VesselAvailablilityImpl vesselAvailablility = new VesselAvailablilityImpl();
		return vesselAvailablility;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelConsumption createFuelConsumption() {
		FuelConsumptionImpl fuelConsumption = new FuelConsumptionImpl();
		return fuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MaintenanceEvent createMaintenanceEvent() {
		MaintenanceEventImpl maintenanceEvent = new MaintenanceEventImpl();
		return maintenanceEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetPackage getFleetPackage() {
		return (FleetPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FleetPackage getPackage() {
		return FleetPackage.eINSTANCE;
	}

} //FleetFactoryImpl
