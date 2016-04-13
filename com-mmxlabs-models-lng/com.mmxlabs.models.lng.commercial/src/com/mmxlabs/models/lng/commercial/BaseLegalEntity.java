/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Legal Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getShippingBook <em>Shipping Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getTradingBook <em>Trading Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getUpstreamBook <em>Upstream Book</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseLegalEntity()
 * @model abstract="true"
 * @generated
 */
public interface BaseLegalEntity extends UUIDObject, NamedObject {

	/**
	 * Returns the value of the '<em><b>Shipping Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Book</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Book</em>' containment reference.
	 * @see #setShippingBook(BaseEntityBook)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseLegalEntity_ShippingBook()
	 * @model containment="true"
	 * @generated
	 */
	BaseEntityBook getShippingBook();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getShippingBook <em>Shipping Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Book</em>' containment reference.
	 * @see #getShippingBook()
	 * @generated
	 */
	void setShippingBook(BaseEntityBook value);

	/**
	 * Returns the value of the '<em><b>Trading Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trading Book</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trading Book</em>' containment reference.
	 * @see #setTradingBook(BaseEntityBook)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseLegalEntity_TradingBook()
	 * @model containment="true"
	 * @generated
	 */
	BaseEntityBook getTradingBook();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getTradingBook <em>Trading Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trading Book</em>' containment reference.
	 * @see #getTradingBook()
	 * @generated
	 */
	void setTradingBook(BaseEntityBook value);

	/**
	 * Returns the value of the '<em><b>Upstream Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Upstream Book</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upstream Book</em>' containment reference.
	 * @see #setUpstreamBook(BaseEntityBook)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseLegalEntity_UpstreamBook()
	 * @model containment="true"
	 * @generated
	 */
	BaseEntityBook getUpstreamBook();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getUpstreamBook <em>Upstream Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upstream Book</em>' containment reference.
	 * @see #getUpstreamBook()
	 * @generated
	 */
	void setUpstreamBook(BaseEntityBook value);
} // BaseLegalEntity
