/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage
 * @generated
 */
public interface ActualsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ActualsFactory eINSTANCE = com.mmxlabs.models.lng.actuals.impl.ActualsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Load Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Load Actuals</em>'.
	 * @generated
	 */
	LoadActuals createLoadActuals();

	/**
	 * Returns a new object of class '<em>Discharge Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Discharge Actuals</em>'.
	 * @generated
	 */
	DischargeActuals createDischargeActuals();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ActualsPackage getActualsPackage();

} //ActualsFactory
