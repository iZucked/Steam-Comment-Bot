/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Entity Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BaseEntityBook#getTaxRates <em>Tax Rates</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseEntityBook()
 * @model abstract="true"
 * @generated
 */
public interface BaseEntityBook extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Tax Rates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.TaxRate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tax Rates</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tax Rates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBaseEntityBook_TaxRates()
	 * @model containment="true"
	 * @generated
	 */
	EList<TaxRate> getTaxRates();

} // BaseEntityBook
