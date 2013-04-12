/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo <em>Relocate To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHeelOptions <em>Heel Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHireRate <em>Hire Rate</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCharterOutEvent()
 * @model
 * @generated
 */
public interface CharterOutEvent extends VesselEvent {
	/**
	 * Returns the value of the '<em><b>Relocate To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relocate To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relocate To</em>' reference.
	 * @see #isSetRelocateTo()
	 * @see #unsetRelocateTo()
	 * @see #setRelocateTo(Port)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCharterOutEvent_RelocateTo()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Port getRelocateTo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo <em>Relocate To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relocate To</em>' reference.
	 * @see #isSetRelocateTo()
	 * @see #unsetRelocateTo()
	 * @see #getRelocateTo()
	 * @generated
	 */
	void setRelocateTo(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo <em>Relocate To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRelocateTo()
	 * @see #getRelocateTo()
	 * @see #setRelocateTo(Port)
	 * @generated
	 */
	void unsetRelocateTo();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo <em>Relocate To</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Relocate To</em>' reference is set.
	 * @see #unsetRelocateTo()
	 * @see #getRelocateTo()
	 * @see #setRelocateTo(Port)
	 * @generated
	 */
	boolean isSetRelocateTo();

	/**
	 * Returns the value of the '<em><b>Heel Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel Options</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Options</em>' containment reference.
	 * @see #setHeelOptions(HeelOptions)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCharterOutEvent_HeelOptions()
	 * @model containment="true" required="true"
	 * @generated
	 */
	HeelOptions getHeelOptions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHeelOptions <em>Heel Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Options</em>' containment reference.
	 * @see #getHeelOptions()
	 * @generated
	 */
	void setHeelOptions(HeelOptions value);

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCharterOutEvent_RepositioningFee()
	 * @model required="true"
	 * @generated
	 */
	int getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(int value);

	/**
	 * Returns the value of the '<em><b>Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Rate</em>' attribute.
	 * @see #setHireRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getCharterOutEvent_HireRate()
	 * @model required="true"
	 * @generated
	 */
	int getHireRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHireRate <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Rate</em>' attribute.
	 * @see #getHireRate()
	 * @generated
	 */
	void setHireRate(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	Port getEndPort();

} // end of  CharterOutEvent

// finish type fixing
