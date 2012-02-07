/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.port;

import scenario.NamedObject;
import scenario.UUIDObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Canal</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.port.PortPackage#getCanal()
 * @model
 * @generated
 */
public interface Canal extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Distance Model</b></em>' containment reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance Model</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Distance Model</em>' containment reference.
	 * @see #setDistanceModel(DistanceModel)
	 * @see scenario.port.PortPackage#getCanal_DistanceModel()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	DistanceModel getDistanceModel();

	/**
	 * Sets the value of the '{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Distance Model</em>' containment reference.
	 * @see #getDistanceModel()
	 * @generated
	 */
	void setDistanceModel(DistanceModel value);

} // Canal
