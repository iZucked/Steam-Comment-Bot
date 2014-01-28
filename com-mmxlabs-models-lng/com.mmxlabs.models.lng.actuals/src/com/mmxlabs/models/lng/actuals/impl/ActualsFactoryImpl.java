/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.*;

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
			case ActualsPackage.LOAD_ACTUALS: return createLoadActuals();
			case ActualsPackage.DISCHARGE_ACTUALS: return createDischargeActuals();
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
