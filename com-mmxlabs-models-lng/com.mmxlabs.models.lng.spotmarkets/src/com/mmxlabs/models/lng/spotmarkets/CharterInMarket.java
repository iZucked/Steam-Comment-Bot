/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInRate <em>Charter In Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterContract <em>Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isNominal <em>Nominal</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket()
 * @model
 * @generated
 */
public interface CharterInMarket extends SpotCharterMarket, VesselAssignmentType, NamedObject {
	/**
	 * Returns the value of the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Price</em>' reference.
	 * @see #setCharterInPrice(CharterIndex)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterInPrice()
	 * @model required="true"
	 * @generated
	 */
//	CharterIndex getCharterInPrice();

	/**
	 * Returns the value of the '<em><b>Nominal</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominal</em>' attribute.
	 * @see #setNominal(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_Nominal()
	 * @model default="true"
	 * @generated
	 */
	boolean isNominal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isNominal <em>Nominal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominal</em>' attribute.
	 * @see #isNominal()
	 * @generated
	 */
	void setNominal(boolean value);

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #setSpotCharterCount(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_SpotCharterCount()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

	/**
	 * Returns the value of the '<em><b>Override Inaccessible Routes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Override Inaccessible Routes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Override Inaccessible Routes</em>' attribute.
	 * @see #setOverrideInaccessibleRoutes(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_OverrideInaccessibleRoutes()
	 * @model
	 * @generated
	 */
	boolean isOverrideInaccessibleRoutes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Override Inaccessible Routes</em>' attribute.
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 */
	void setOverrideInaccessibleRoutes(boolean value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Routes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Routes</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_InaccessibleRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getInaccessibleRoutes();

	/**
	 * Returns the value of the '<em><b>Charter Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Contract</em>' reference.
	 * @see #isSetCharterContract()
	 * @see #unsetCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterContract()
	 * @model unsettable="true"
	 * @generated
	 */
	CharterContract getCharterContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterContract <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Contract</em>' reference.
	 * @see #isSetCharterContract()
	 * @see #unsetCharterContract()
	 * @see #getCharterContract()
	 * @generated
	 */
	void setCharterContract(CharterContract value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterContract <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterContract()
	 * @see #getCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @generated
	 */
	void unsetCharterContract();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterContract <em>Charter Contract</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter Contract</em>' reference is set.
	 * @see #unsetCharterContract()
	 * @see #getCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @generated
	 */
	boolean isSetCharterContract();

	/**
	 * Returns the value of the '<em><b>Charter In Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Rate</em>' attribute.
	 * @see #setCharterInRate(String)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterInRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getCharterInRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInRate <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Rate</em>' attribute.
	 * @see #getCharterInRate()
	 * @generated
	 */
	void setCharterInRate(String value);

} // CharterInMarket
