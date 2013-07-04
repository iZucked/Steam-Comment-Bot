/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.ecore.EObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Commodity Index</b></em>'.
 * @since 5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CommodityIndex#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCommodityIndex()
 * @model
 * @generated
 */
public interface CommodityIndex extends NamedObject, UUIDObject {
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCommodityIndex_Data()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EDoubleObject>" containment="true"
	 * @generated
	 */
	Index<Double> getData();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CommodityIndex#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(Index<Double> value);

} // CommodityIndex
