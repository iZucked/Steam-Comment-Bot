/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.PenaltyType;
import com.mmxlabs.models.lng.actuals.ReturnActuals;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ActualsFactoryImpl extends EFactoryImpl implements ActualsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ActualsFactory init() {
		try {
			ActualsFactory theActualsFactory = (ActualsFactory)EPackage.Registry.INSTANCE.getEFactory(ActualsPackage.eNS_URI);
			if (theActualsFactory != null) {
				return theActualsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ActualsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActualsFactoryImpl() {
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
			case ActualsPackage.ACTUALS_MODEL: return createActualsModel();
			case ActualsPackage.CARGO_ACTUALS: return createCargoActuals();
			case ActualsPackage.LOAD_ACTUALS: return createLoadActuals();
			case ActualsPackage.DISCHARGE_ACTUALS: return createDischargeActuals();
			case ActualsPackage.RETURN_ACTUALS: return createReturnActuals();
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
			case ActualsPackage.PENALTY_TYPE:
				return createPenaltyTypeFromString(eDataType, initialValue);
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
			case ActualsPackage.PENALTY_TYPE:
				return convertPenaltyTypeToString(eDataType, instanceValue);
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
	public ActualsModel createActualsModel() {
		ActualsModelImpl actualsModel = new ActualsModelImpl();
		return actualsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoActuals createCargoActuals() {
		CargoActualsImpl cargoActuals = new CargoActualsImpl();
		return cargoActuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LoadActuals createLoadActuals() {
		LoadActualsImpl loadActuals = new LoadActualsImpl();
		return loadActuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DischargeActuals createDischargeActuals() {
		DischargeActualsImpl dischargeActuals = new DischargeActualsImpl();
		return dischargeActuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ReturnActuals createReturnActuals() {
		ReturnActualsImpl returnActuals = new ReturnActualsImpl();
		return returnActuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PenaltyType createPenaltyTypeFromString(EDataType eDataType, String initialValue) {
		PenaltyType result = PenaltyType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPenaltyTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActualsPackage getActualsPackage() {
		return (ActualsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ActualsPackage getPackage() {
		return ActualsPackage.eINSTANCE;
	}

} //ActualsFactoryImpl
