/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capability Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.CapabilityGroup#getCapability <em>Capability</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getCapabilityGroup()
 * @model
 * @generated
 */
public interface CapabilityGroup extends APortSet<Port> {
	/**
	 * Returns the value of the '<em><b>Capability</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.PortCapability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capability</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capability</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see #setCapability(PortCapability)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getCapabilityGroup_Capability()
	 * @model required="true"
	 * @generated
	 */
	PortCapability getCapability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.CapabilityGroup#getCapability <em>Capability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capability</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see #getCapability()
	 * @generated
	 */
	void setCapability(PortCapability value);

} // end of  CapabilityGroup

// finish type fixing
