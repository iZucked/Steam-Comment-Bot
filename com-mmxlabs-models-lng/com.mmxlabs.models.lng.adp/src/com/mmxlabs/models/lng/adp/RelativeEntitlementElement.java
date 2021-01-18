/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relative Entitlement Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.RelativeEntitlementElement#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.RelativeEntitlementElement#getEntitlement <em>Entitlement</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getRelativeEntitlementElement()
 * @model
 * @generated
 */
public interface RelativeEntitlementElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getRelativeEntitlementElement_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.RelativeEntitlementElement#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Entitlement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entitlement</em>' attribute.
	 * @see #setEntitlement(double)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getRelativeEntitlementElement_Entitlement()
	 * @model
	 * @generated
	 */
	double getEntitlement();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.RelativeEntitlementElement#getEntitlement <em>Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entitlement</em>' attribute.
	 * @see #getEntitlement()
	 * @generated
	 */
	void setEntitlement(double value);

} // RelativeEntitlementElement
