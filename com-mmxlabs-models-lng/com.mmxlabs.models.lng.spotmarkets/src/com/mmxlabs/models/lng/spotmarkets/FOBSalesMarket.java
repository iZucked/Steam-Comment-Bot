/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FOB Sales Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getOriginPorts <em>Origin Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBSalesMarket()
 * @model
 * @generated
 */
public interface FOBSalesMarket extends SpotMarket {
	/**
	 * Returns the value of the '<em><b>Origin Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBSalesMarket_OriginPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getOriginPorts();

} // end of  FOBSalesMarket

// finish type fixing
