/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Index Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer()
 * @model
 * @generated
 */
public interface NamedIndexContainer<Value> extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer_Data()
	 * @model containment="true"
	 * @generated
	 */
	Index<Value> getData();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(Index<Value> value);

} // NamedIndexContainer
