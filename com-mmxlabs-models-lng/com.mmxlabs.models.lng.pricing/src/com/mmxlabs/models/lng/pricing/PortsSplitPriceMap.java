/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ports Split Price Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH1 <em>Index H1</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH2 <em>Index H2</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitPriceMap()
 * @model
 * @generated
 */
public interface PortsSplitPriceMap extends MMXObject {
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitPriceMap_Ports()
	 * @model required="true"
	 * @generated
	 */
	EList<APortSet<Port>> getPorts();

	/**
	 * Returns the value of the '<em><b>Index H1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index H1</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index H1</em>' reference.
	 * @see #setIndexH1(CommodityIndex)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitPriceMap_IndexH1()
	 * @model required="true"
	 * @generated
	 */
	CommodityIndex getIndexH1();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH1 <em>Index H1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index H1</em>' reference.
	 * @see #getIndexH1()
	 * @generated
	 */
	void setIndexH1(CommodityIndex value);

	/**
	 * Returns the value of the '<em><b>Index H2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index H2</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index H2</em>' reference.
	 * @see #setIndexH2(CommodityIndex)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitPriceMap_IndexH2()
	 * @model required="true"
	 * @generated
	 */
	CommodityIndex getIndexH2();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortsSplitPriceMap#getIndexH2 <em>Index H2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index H2</em>' reference.
	 * @see #getIndexH2()
	 * @generated
	 */
	void setIndexH2(CommodityIndex value);

} // PortsSplitPriceMap
