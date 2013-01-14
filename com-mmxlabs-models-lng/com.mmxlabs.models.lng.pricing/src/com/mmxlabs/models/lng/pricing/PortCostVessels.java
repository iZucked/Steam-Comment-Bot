/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Cost Vessels</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCostVessels#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCostVessels#getPortCostEntries <em>Port Cost Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostVessels()
 * @model
 * @generated
 */
public interface PortCostVessels extends MMXObject {
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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostVessels_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

	/**
	 * Returns the value of the '<em><b>Port Cost Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PortCostEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Cost Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Cost Entries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostVessels_PortCostEntries()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCostEntry> getPortCostEntries();

} // end of  PortCostVessels

// finish type fixing
