/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getMmBtu <em>Mm Btu</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals()
 * @model abstract="true"
 * @generated
 */
public interface SlotActuals extends EObject {
	/**
	 * Returns the value of the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>CV</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>CV</em>' attribute.
	 * @see #setCV(float)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_CV()
	 * @model
	 * @generated
	 */
	float getCV();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>CV</em>' attribute.
	 * @see #getCV()
	 * @generated
	 */
	void setCV(float value);

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
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Volume()
	 * @model
	 * @generated
	 */
	float getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(float value);

	/**
	 * Returns the value of the '<em><b>Mm Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mm Btu</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mm Btu</em>' attribute.
	 * @see #setMmBtu(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_MmBtu()
	 * @model
	 * @generated
	 */
	int getMmBtu();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getMmBtu <em>Mm Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mm Btu</em>' attribute.
	 * @see #getMmBtu()
	 * @generated
	 */
	void setMmBtu(int value);

	/**
	 * Returns the value of the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Charges</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Charges</em>' attribute.
	 * @see #setPortCharges(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_PortCharges()
	 * @model
	 * @generated
	 */
	int getPortCharges();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Charges</em>' attribute.
	 * @see #getPortCharges()
	 * @generated
	 */
	void setPortCharges(int value);

	/**
	 * Returns the value of the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Consumption</em>' attribute.
	 * @see #setBaseFuelConsumption(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_BaseFuelConsumption()
	 * @model
	 * @generated
	 */
	int getBaseFuelConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Consumption</em>' attribute.
	 * @see #getBaseFuelConsumption()
	 * @generated
	 */
	void setBaseFuelConsumption(int value);

} // SlotActuals
