/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Market Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getMarkets <em>Markets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketGroup()
 * @model
 * @generated
 */
public interface SpotMarketGroup extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Availability</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Availability</em>' containment reference.
	 * @see #setAvailability(SpotAvailability)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketGroup_Availability()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotAvailability getAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getAvailability <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Availability</em>' containment reference.
	 * @see #getAvailability()
	 * @generated
	 */
	void setAvailability(SpotAvailability value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.spotmarkets.SpotType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotType
	 * @see #setType(SpotType)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketGroup_Type()
	 * @model required="true"
	 * @generated
	 */
	SpotType getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotType
	 * @see #getType()
	 * @generated
	 */
	void setType(SpotType value);

	/**
	 * Returns the value of the '<em><b>Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.SpotMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Markets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarketGroup_Markets()
	 * @model containment="true"
	 * @generated
	 */
	EList<SpotMarket> getMarkets();

} // end of  SpotMarketGroup

// finish type fixing
