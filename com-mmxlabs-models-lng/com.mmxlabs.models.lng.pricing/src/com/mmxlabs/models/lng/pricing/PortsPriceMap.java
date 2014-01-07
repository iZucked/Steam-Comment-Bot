/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.MMXObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ports Price Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsPriceMap#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsPriceMap#getIndex <em>Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsPriceMap()
 * @model
 * @generated
 */
public interface PortsPriceMap extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsPriceMap_Ports()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getPorts();

	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(CommodityIndex)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsPriceMap_Index()
	 * @model required="true"
	 * @generated
	 */
	CommodityIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortsPriceMap#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(CommodityIndex value);

} // PortsPriceMap
