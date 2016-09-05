/**
 */
package com.mmxlabs.models.lng.adp;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Interval Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getIntervalType <em>Interval Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getSpacing <em>Spacing</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoIntervalDistributionModel()
 * @model
 * @generated
 */
public interface CargoIntervalDistributionModel extends DistributionModel {
	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoIntervalDistributionModel_Quantity()
	 * @model
	 * @generated
	 */
	int getQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Interval Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.adp.IntervalType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interval Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #setIntervalType(IntervalType)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoIntervalDistributionModel_IntervalType()
	 * @model
	 * @generated
	 */
	IntervalType getIntervalType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getIntervalType <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #getIntervalType()
	 * @generated
	 */
	void setIntervalType(IntervalType value);

	/**
	 * Returns the value of the '<em><b>Spacing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spacing</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spacing</em>' attribute.
	 * @see #setSpacing(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getCargoIntervalDistributionModel_Spacing()
	 * @model
	 * @generated
	 */
	int getSpacing();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getSpacing <em>Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spacing</em>' attribute.
	 * @see #getSpacing()
	 * @generated
	 */
	void setSpacing(int value);

} // CargoIntervalDistributionModel
