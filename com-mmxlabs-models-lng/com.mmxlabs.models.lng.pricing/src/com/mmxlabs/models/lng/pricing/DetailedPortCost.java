

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Detailed Port Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DetailedPortCost#getVessels <em>Vessels</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDetailedPortCost()
 * @model
 * @generated
 */
public interface DetailedPortCost extends PortCostDefinition {
	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDetailedPortCost_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

} // end of  DetailedPortCost

// finish type fixing
