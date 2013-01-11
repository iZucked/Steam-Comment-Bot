/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedContracts <em>Restricted Contracts</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts <em>Restricted Ports</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #setMinQuantity(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_MinQuantity()
	 * @model required="true"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #setMaxQuantity(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_MaxQuantity()
	 * @model required="true"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Lists Are Permissive</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Lists Are Permissive</em>' attribute.
	 * @see #setRestrictedListsArePermissive(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedListsArePermissive()
	 * @model
	 * @generated
	 */
	boolean isRestrictedListsArePermissive();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restricted Lists Are Permissive</em>' attribute.
	 * @see #isRestrictedListsArePermissive()
	 * @generated
	 */
	void setRestrictedListsArePermissive(boolean value);

	/**
	 * Returns the value of the '<em><b>Restricted Contracts</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.Contract}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Contracts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Contracts</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedContracts()
	 * @model
	 * @generated
	 */
	EList<Contract> getRestrictedContracts();

	/**
	 * Returns the value of the '<em><b>Restricted Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restricted Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getContract_RestrictedPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getRestrictedPorts();

} // end of  Contract

// finish type fixing
