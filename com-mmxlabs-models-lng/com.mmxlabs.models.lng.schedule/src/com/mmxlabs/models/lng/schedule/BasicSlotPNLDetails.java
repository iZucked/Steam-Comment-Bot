/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Basic Slot PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails()
 * @model
 * @generated
 */
public interface BasicSlotPNLDetails extends GeneralPNLDetails {
	/**
	 * Returns the value of the '<em><b>Cancellation Fees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Fees</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Fees</em>' attribute.
	 * @see #isSetCancellationFees()
	 * @see #unsetCancellationFees()
	 * @see #setCancellationFees(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_CancellationFees()
	 * @model unsettable="true"
	 * @generated
	 */
	int getCancellationFees();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Fees</em>' attribute.
	 * @see #isSetCancellationFees()
	 * @see #unsetCancellationFees()
	 * @see #getCancellationFees()
	 * @generated
	 */
	void setCancellationFees(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCancellationFees()
	 * @see #getCancellationFees()
	 * @see #setCancellationFees(int)
	 * @generated
	 */
	void unsetCancellationFees();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cancellation Fees</em>' attribute is set.
	 * @see #unsetCancellationFees()
	 * @see #getCancellationFees()
	 * @see #setCancellationFees(int)
	 * @generated
	 */
	boolean isSetCancellationFees();

	/**
	 * Returns the value of the '<em><b>Hedging Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hedging Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hedging Value</em>' attribute.
	 * @see #isSetHedgingValue()
	 * @see #unsetHedgingValue()
	 * @see #setHedgingValue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_HedgingValue()
	 * @model unsettable="true"
	 * @generated
	 */
	int getHedgingValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hedging Value</em>' attribute.
	 * @see #isSetHedgingValue()
	 * @see #unsetHedgingValue()
	 * @see #getHedgingValue()
	 * @generated
	 */
	void setHedgingValue(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetHedgingValue()
	 * @see #getHedgingValue()
	 * @see #setHedgingValue(int)
	 * @generated
	 */
	void unsetHedgingValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Hedging Value</em>' attribute is set.
	 * @see #unsetHedgingValue()
	 * @see #getHedgingValue()
	 * @see #setHedgingValue(int)
	 * @generated
	 */
	boolean isSetHedgingValue();

} // BasicSlotPNLDetails
