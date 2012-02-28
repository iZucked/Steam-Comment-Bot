/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
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

} // end of  CharterOutEvent

// finish type fixing
