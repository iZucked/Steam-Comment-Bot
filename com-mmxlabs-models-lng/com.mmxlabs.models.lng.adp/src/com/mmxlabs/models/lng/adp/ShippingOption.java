/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.ShippingOption#getVesselAssignmentType <em>Vessel Assignment Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ShippingOption#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ShippingOption#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.ShippingOption#getMaxLadenIdleDays <em>Max Laden Idle Days</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getShippingOption()
 * @model
 * @generated
 */
public interface ShippingOption extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Assignment Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Assignment Type</em>' reference.
	 * @see #setVesselAssignmentType(VesselAssignmentType)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getShippingOption_VesselAssignmentType()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getVesselAssignmentType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ShippingOption#getVesselAssignmentType <em>Vessel Assignment Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Assignment Type</em>' reference.
	 * @see #getVesselAssignmentType()
	 * @generated
	 */
	void setVesselAssignmentType(VesselAssignmentType value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getShippingOption_SpotIndex()
	 * @model
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ShippingOption#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getShippingOption_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ShippingOption#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Max Laden Idle Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Laden Idle Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Laden Idle Days</em>' attribute.
	 * @see #setMaxLadenIdleDays(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getShippingOption_MaxLadenIdleDays()
	 * @model
	 * @generated
	 */
	int getMaxLadenIdleDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.ShippingOption#getMaxLadenIdleDays <em>Max Laden Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Laden Idle Days</em>' attribute.
	 * @see #getMaxLadenIdleDays()
	 * @generated
	 */
	void setMaxLadenIdleDays(int value);

} // ShippingOption
