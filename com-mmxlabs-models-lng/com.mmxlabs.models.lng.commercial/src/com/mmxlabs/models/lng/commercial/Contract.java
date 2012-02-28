/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.port.Port;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getAllowedPorts <em>Allowed Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getPreferredPort <em>Preferred Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract()
 * @model
 * @generated
 */
public interface Contract extends AContract {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(LegalEntity)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_Entity()
	 * @model required="true"
	 * @generated
	 */
	LegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(LegalEntity value);

	/**
	 * Returns the value of the '<em><b>Allowed Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_AllowedPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getAllowedPorts();

	/**
	 * Returns the value of the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preferred Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preferred Port</em>' reference.
	 * @see #setPreferredPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_PreferredPort()
	 * @model required="true"
	 * @generated
	 */
	Port getPreferredPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getPreferredPort <em>Preferred Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Preferred Port</em>' reference.
	 * @see #getPreferredPort()
	 * @generated
	 */
	void setPreferredPort(Port value);

} // end of  Contract

// finish type fixing
