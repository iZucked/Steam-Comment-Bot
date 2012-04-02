

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getRoundTripMatrices <em>Round Trip Matrices</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel()
 * @model
 * @generated
 */
public interface AnalyticsModel extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Round Trip Matrices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.UnitCostMatrix}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Round Trip Matrices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Round Trip Matrices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_RoundTripMatrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnitCostMatrix> getRoundTripMatrices();

} // end of  AnalyticsModel

// finish type fixing
