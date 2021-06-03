/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Repositioning Fee Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer#getTerms <em>Terms</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSimpleRepositioningFeeContainer()
 * @model
 * @generated
 */
public interface SimpleRepositioningFeeContainer extends IRepositioningFee {
	/**
	 * Returns the value of the '<em><b>Terms</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terms</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSimpleRepositioningFeeContainer_Terms()
	 * @model containment="true"
	 * @generated
	 */
	EList<RepositioningFeeTerm> getTerms();

} // SimpleRepositioningFeeContainer
