/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.port;

import com.mmxlabs.models.lng.types.APort;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getCapabilities <em>Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends APort {

	/**
	 * Returns the value of the '<em><b>Capabilities</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.PortCapability}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.PortCapability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capabilities</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capabilities</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.PortCapability
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_Capabilities()
	 * @model
	 * @generated
	 */
	EList<PortCapability> getCapabilities();
} // Port
