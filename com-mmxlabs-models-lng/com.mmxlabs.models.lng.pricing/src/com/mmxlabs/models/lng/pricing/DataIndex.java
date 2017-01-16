/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DataIndex#getPoints <em>Points</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDataIndex()
 * @model
 * @generated
 */
public interface DataIndex<Value> extends Index<Value> {
	/**
	 * Returns the value of the '<em><b>Points</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.IndexPoint}&lt;Value>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Points</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Points</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDataIndex_Points()
	 * @model containment="true"
	 * @generated
	 */
	EList<IndexPoint<Value>> getPoints();

} // end of  DataIndex

// finish type fixing
