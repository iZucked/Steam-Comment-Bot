/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Date Point Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DatePointContainer#getPoints <em>Points</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDatePointContainer()
 * @model
 * @generated
 */
public interface DatePointContainer extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Points</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.DatePoint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Points</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Points</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDatePointContainer_Points()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<DatePoint> getPoints();

} // DatePointContainer
