/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Markets Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpotMarketsProfile#isIncludeEnabledSpotMarkets <em>Include Enabled Spot Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpotMarketsProfile#getSpotMarkets <em>Spot Markets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpotMarketsProfile()
 * @model
 * @generated
 */
public interface SpotMarketsProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Include Enabled Spot Markets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include Enabled Spot Markets</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Enabled Spot Markets</em>' attribute.
	 * @see #setIncludeEnabledSpotMarkets(boolean)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpotMarketsProfile_IncludeEnabledSpotMarkets()
	 * @model
	 * @generated
	 */
	boolean isIncludeEnabledSpotMarkets();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SpotMarketsProfile#isIncludeEnabledSpotMarkets <em>Include Enabled Spot Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Enabled Spot Markets</em>' attribute.
	 * @see #isIncludeEnabledSpotMarkets()
	 * @generated
	 */
	void setIncludeEnabledSpotMarkets(boolean value);

	/**
	 * Returns the value of the '<em><b>Spot Markets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.SpotMarket}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Markets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Markets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpotMarketsProfile_SpotMarkets()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SpotMarket> getSpotMarkets();

} // SpotMarketsProfile
