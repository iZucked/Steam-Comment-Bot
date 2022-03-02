/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Origin Port Repositioning Fee Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getOriginPortRepositioningFeeTerm()
 * @model
 * @generated
 */
public interface OriginPortRepositioningFeeTerm extends RepositioningFeeTerm, NotionalJourneyTerm {

	/**
	 * Returns the value of the '<em><b>Origin Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Port</em>' reference.
	 * @see #setOriginPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getOriginPortRepositioningFeeTerm_OriginPort()
	 * @model
	 * @generated
	 */
	Port getOriginPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm#getOriginPort <em>Origin Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Port</em>' reference.
	 * @see #getOriginPort()
	 * @generated
	 */
	void setOriginPort(Port value);
} // OriginPortRepositioningFeeTerm
