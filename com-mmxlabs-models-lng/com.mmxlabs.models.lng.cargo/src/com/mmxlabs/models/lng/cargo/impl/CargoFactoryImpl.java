/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
			CargoFactory theCargoFactory = (CargoFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/cargo/1/"); 
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
			case CargoPackage.CARGO: return createCargo();
			case CargoPackage.LOAD_SLOT: return createLoadSlot();
			case CargoPackage.DISCHARGE_SLOT: return createDischargeSlot();
			case CargoPackage.CARGO_MODEL: return createCargoModel();
			case CargoPackage.SPOT_SLOT: return createSpotSlot();
			case CargoPackage.SPOT_LOAD_SLOT: return createSpotLoadSlot();
			case CargoPackage.SPOT_DISCHARGE_SLOT: return createSpotDischargeSlot();
			case CargoPackage.CARGO_GROUP: return createCargoGroup();
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
	public SpotSlot createSpotSlot() {
		SpotSlotImpl spotSlot = new SpotSlotImpl();
		return spotSlot;
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
