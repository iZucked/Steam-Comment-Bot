/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;

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
			FleetFactory theFleetFactory = (FleetFactory)EPackage.Registry.INSTANCE.getEFactory(FleetPackage.eNS_URI);
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
			case FleetPackage.FLEET_MODEL: return createFleetModel();
			case FleetPackage.BASE_FUEL: return createBaseFuel();
			case FleetPackage.VESSEL: return createVessel();
			case FleetPackage.VESSEL_CLASS: return createVesselClass();
			case FleetPackage.VESSEL_GROUP: return createVesselGroup();
			case FleetPackage.HEEL_OPTIONS: return createHeelOptions();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES: return createVesselStateAttributes();
			case FleetPackage.FUEL_CONSUMPTION: return createFuelConsumption();
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS: return createVesselClassRouteParameters();
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
	public FuelConsumption createFuelConsumption() {
		FuelConsumptionImpl fuelConsumption = new FuelConsumptionImpl();
		return fuelConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClassRouteParameters createVesselClassRouteParameters() {
		VesselClassRouteParametersImpl vesselClassRouteParameters = new VesselClassRouteParametersImpl();
		return vesselClassRouteParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselGroup createVesselGroup() {
		VesselGroupImpl vesselGroup = new VesselGroupImpl();
		return vesselGroup;
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
