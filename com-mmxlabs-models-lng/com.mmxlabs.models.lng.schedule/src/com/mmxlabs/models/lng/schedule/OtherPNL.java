/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other PNL</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.OtherPNL#getLateness <em>Lateness</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOtherPNL()
 * @model
 * @generated
 */
public interface OtherPNL extends ProfitAndLossContainer, CapacityViolationsHolder {

	/**
	 * Returns the value of the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lateness</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lateness</em>' containment reference.
	 * @see #setLateness(PortVisitLateness)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getOtherPNL_Lateness()
	 * @model containment="true"
	 * @generated
	 */
	PortVisitLateness getLateness();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.OtherPNL#getLateness <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lateness</em>' containment reference.
	 * @see #getLateness()
	 * @generated
	 */
	void setLateness(PortVisitLateness value);

} // OtherPNL
