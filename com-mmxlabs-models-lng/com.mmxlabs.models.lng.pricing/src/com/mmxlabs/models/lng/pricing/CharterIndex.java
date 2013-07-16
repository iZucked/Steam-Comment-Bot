/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Index</b></em>'.
 * @since 5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CharterIndex#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterIndex()
 * @model
 * @generated
 */
public interface CharterIndex extends NamedObject, UUIDObject {
	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterIndex_Data()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" containment="true"
	 * @generated
	 */
	Index<Integer> getData();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CharterIndex#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(Index<Integer> value);

} // CharterIndex
