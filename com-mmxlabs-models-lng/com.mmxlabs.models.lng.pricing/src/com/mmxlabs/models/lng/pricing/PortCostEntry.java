/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.types.PortCapability;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Cost Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getActivity <em>Activity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getCost <em>Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostEntry()
 * @model
 * @generated
 */
public interface PortCostEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Activity</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.PortCapability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activity</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see #setActivity(PortCapability)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostEntry_Activity()
	 * @model required="true"
	 * @generated
	 */
	PortCapability getActivity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getActivity <em>Activity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activity</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see #getActivity()
	 * @generated
	 */
	void setActivity(PortCapability value);

	/**
	 * Returns the value of the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost</em>' attribute.
	 * @see #setCost(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCostEntry_Cost()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='##,###,##0'"
	 * @generated
	 */
	int getCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCostEntry#getCost <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cost</em>' attribute.
	 * @see #getCost()
	 * @generated
	 */
	void setCost(int value);

} // end of  PortCostEntry

// finish type fixing
