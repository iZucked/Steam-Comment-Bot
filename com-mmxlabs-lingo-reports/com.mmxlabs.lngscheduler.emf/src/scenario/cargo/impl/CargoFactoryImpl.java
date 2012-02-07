/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoModel;
import scenario.cargo.CargoPackage;
import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class CargoFactoryImpl extends EFactoryImpl implements CargoFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static CargoFactory init() {
		try {
			final CargoFactory theCargoFactory = (CargoFactory) EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/cargo");
			if (theCargoFactory != null) {
				return theCargoFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CargoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CargoFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case CargoPackage.CARGO_MODEL:
			return createCargoModel();
		case CargoPackage.CARGO:
			return createCargo();
		case CargoPackage.SLOT:
			return createSlot();
		case CargoPackage.LOAD_SLOT:
			return createLoadSlot();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case CargoPackage.CARGO_TYPE:
			return createCargoTypeFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case CargoPackage.CARGO_TYPE:
			return convertCargoTypeToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CargoModel createCargoModel() {
		final CargoModelImpl cargoModel = new CargoModelImpl();
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Cargo createCargo() {
		final CargoImpl cargo = new CargoImpl();
		return cargo;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Slot createSlot() {
		final SlotImpl slot = new SlotImpl();
		return slot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public LoadSlot createLoadSlot() {
		final LoadSlotImpl loadSlot = new LoadSlotImpl();
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CargoType createCargoTypeFromString(final EDataType eDataType, final String initialValue) {
		final CargoType result = CargoType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertCargoTypeToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CargoPackage getCargoPackage() {
		return (CargoPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CargoPackage getPackage() {
		return CargoPackage.eINSTANCE;
	}

} // CargoFactoryImpl
