/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;
import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.lng.types.ALegalEntity;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Legal Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTaxRates <em>Tax Rates</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLegalEntity()
 * @model
 * @generated
 */
public interface LegalEntity extends UUIDObject, NamedObject {

	/**
	 * Returns the value of the '<em><b>Tax Rates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.TaxRate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Rates</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Rates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLegalEntity_TaxRates()
	 * @model containment="true"
	 * @generated
	 */
	EList<TaxRate> getTaxRates();
} // end of  LegalEntity

// finish type fixing
