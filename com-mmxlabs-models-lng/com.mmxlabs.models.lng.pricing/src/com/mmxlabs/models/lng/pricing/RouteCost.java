/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.RouteCost#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.RouteCost#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.RouteCost#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.RouteCost#getBallastCost <em>Ballast Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getRouteCost()
 * @model
 * @generated
 */
public interface RouteCost extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Route Option</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Option</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #setRouteOption(RouteOption)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getRouteCost_RouteOption()
	 * @model
	 * @generated
	 */
	RouteOption getRouteOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.RouteCost#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #getRouteOption()
	 * @generated
	 */
	void setRouteOption(RouteOption value);

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getRouteCost_Vessels()
	 * @model required="true"
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getVessels();

	/**
	 * Returns the value of the '<em><b>Laden Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Cost</em>' attribute.
	 * @see #setLadenCost(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getRouteCost_LadenCost()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='##,###,##0'"
	 * @generated
	 */
	int getLadenCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.RouteCost#getLadenCost <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Cost</em>' attribute.
	 * @see #getLadenCost()
	 * @generated
	 */
	void setLadenCost(int value);

	/**
	 * Returns the value of the '<em><b>Ballast Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Cost</em>' attribute.
	 * @see #setBallastCost(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getRouteCost_BallastCost()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='##,###,##0'"
	 * @generated
	 */
	int getBallastCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.RouteCost#getBallastCost <em>Ballast Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Cost</em>' attribute.
	 * @see #getBallastCost()
	 * @generated
	 */
	void setBallastCost(int value);

} // end of  RouteCost

// finish type fixing
