/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCrewBonus <em>Crew Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getActuals <em>Actuals</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals()
 * @model
 * @generated
 */
public interface CargoActuals extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #setBaseFuelPrice(float)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_BaseFuelPrice()
	 * @model
	 * @generated
	 */
	float getBaseFuelPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #getBaseFuelPrice()
	 * @generated
	 */
	void setBaseFuelPrice(float value);

	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(float)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_Volume()
	 * @model
	 * @generated
	 */
	float getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(float value);

	/**
	 * Returns the value of the '<em><b>Insurance Premium</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insurance Premium</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insurance Premium</em>' attribute.
	 * @see #setInsurancePremium(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_InsurancePremium()
	 * @model
	 * @generated
	 */
	int getInsurancePremium();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insurance Premium</em>' attribute.
	 * @see #getInsurancePremium()
	 * @generated
	 */
	void setInsurancePremium(int value);

	/**
	 * Returns the value of the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Crew Bonus</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Crew Bonus</em>' attribute.
	 * @see #setCrewBonus(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_CrewBonus()
	 * @model
	 * @generated
	 */
	int getCrewBonus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCrewBonus <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Crew Bonus</em>' attribute.
	 * @see #getCrewBonus()
	 * @generated
	 */
	void setCrewBonus(int value);

	/**
	 * Returns the value of the '<em><b>Actuals</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.actuals.SlotActuals}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actuals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actuals</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_Actuals()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SlotActuals> getActuals();

} // CargoActuals
