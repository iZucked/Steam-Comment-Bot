/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoFactoryImpl extends EFactoryImpl implements CargoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CargoFactory init() {
		try {
			CargoFactory theCargoFactory = (CargoFactory)EPackage.Registry.INSTANCE.getEFactory(CargoPackage.eNS_URI);
			if (theCargoFactory != null) {
				return theCargoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CargoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoFactoryImpl() {
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
			case CargoPackage.CARGO_MODEL: return createCargoModel();
			case CargoPackage.CARGO: return createCargo();
			case CargoPackage.LOAD_SLOT: return createLoadSlot();
			case CargoPackage.DISCHARGE_SLOT: return createDischargeSlot();
			case CargoPackage.SPOT_LOAD_SLOT: return createSpotLoadSlot();
			case CargoPackage.SPOT_DISCHARGE_SLOT: return createSpotDischargeSlot();
			case CargoPackage.CARGO_GROUP: return createCargoGroup();
			case CargoPackage.VESSEL_AVAILABILITY: return createVesselAvailability();
			case CargoPackage.MAINTENANCE_EVENT: return createMaintenanceEvent();
			case CargoPackage.DRY_DOCK_EVENT: return createDryDockEvent();
			case CargoPackage.CHARTER_OUT_EVENT: return createCharterOutEvent();
			case CargoPackage.VESSEL_TYPE_GROUP: return createVesselTypeGroup();
			case CargoPackage.END_HEEL_OPTIONS: return createEndHeelOptions();
			case CargoPackage.START_HEEL_OPTIONS: return createStartHeelOptions();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case CargoPackage.CARGO_TYPE:
				return createCargoTypeFromString(eDataType, initialValue);
			case CargoPackage.VESSEL_TYPE:
				return createVesselTypeFromString(eDataType, initialValue);
			case CargoPackage.EVESSEL_TANK_STATE:
				return createEVesselTankStateFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case CargoPackage.CARGO_TYPE:
				return convertCargoTypeToString(eDataType, instanceValue);
			case CargoPackage.VESSEL_TYPE:
				return convertVesselTypeToString(eDataType, instanceValue);
			case CargoPackage.EVESSEL_TANK_STATE:
				return convertEVesselTankStateToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo createCargo() {
		CargoImpl cargo = new CargoImpl();
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot createLoadSlot() {
		LoadSlotImpl loadSlot = new LoadSlotImpl();
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot createDischargeSlot() {
		DischargeSlotImpl dischargeSlot = new DischargeSlotImpl();
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoModel createCargoModel() {
		CargoModelImpl cargoModel = new CargoModelImpl();
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotLoadSlot createSpotLoadSlot() {
		SpotLoadSlotImpl spotLoadSlot = new SpotLoadSlotImpl();
		return spotLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotDischargeSlot createSpotDischargeSlot() {
		SpotDischargeSlotImpl spotDischargeSlot = new SpotDischargeSlotImpl();
		return spotDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoGroup createCargoGroup() {
		CargoGroupImpl cargoGroup = new CargoGroupImpl();
		return cargoGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability createVesselAvailability() {
		VesselAvailabilityImpl vesselAvailability = new VesselAvailabilityImpl();
		return vesselAvailability;
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
	public VesselTypeGroup createVesselTypeGroup() {
		VesselTypeGroupImpl vesselTypeGroup = new VesselTypeGroupImpl();
		return vesselTypeGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndHeelOptions createEndHeelOptions() {
		EndHeelOptionsImpl endHeelOptions = new EndHeelOptionsImpl();
		return endHeelOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartHeelOptions createStartHeelOptions() {
		StartHeelOptionsImpl startHeelOptions = new StartHeelOptionsImpl();
		return startHeelOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoType createCargoTypeFromString(EDataType eDataType, String initialValue) {
		CargoType result = CargoType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCargoTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselType createVesselTypeFromString(EDataType eDataType, String initialValue) {
		VesselType result = VesselType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVesselTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EVesselTankState createEVesselTankStateFromString(EDataType eDataType, String initialValue) {
		EVesselTankState result = EVesselTankState.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEVesselTankStateToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoPackage getCargoPackage() {
		return (CargoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CargoPackage getPackage() {
		return CargoPackage.eINSTANCE;
	}

} //CargoFactoryImpl
