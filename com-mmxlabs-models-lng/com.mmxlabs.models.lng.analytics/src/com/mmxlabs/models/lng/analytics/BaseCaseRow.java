/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Case Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getVesselEventOption <em>Vessel Event Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isOptionise <em>Optionise</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isFreeze <em>Freeze</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow()
 * @model
 * @generated
 */
public interface BaseCaseRow extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buy Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Option</em>' reference.
	 * @see #setBuyOption(BuyOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_BuyOption()
	 * @model
	 * @generated
	 */
	BuyOption getBuyOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getBuyOption <em>Buy Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buy Option</em>' reference.
	 * @see #getBuyOption()
	 * @generated
	 */
	void setBuyOption(BuyOption value);

	/**
	 * Returns the value of the '<em><b>Sell Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sell Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Option</em>' reference.
	 * @see #setSellOption(SellOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_SellOption()
	 * @model
	 * @generated
	 */
	SellOption getSellOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getSellOption <em>Sell Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Option</em>' reference.
	 * @see #getSellOption()
	 * @generated
	 */
	void setSellOption(SellOption value);

	/**
	 * Returns the value of the '<em><b>Vessel Event Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event Option</em>' reference.
	 * @see #setVesselEventOption(VesselEventOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_VesselEventOption()
	 * @model
	 * @generated
	 */
	VesselEventOption getVesselEventOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getVesselEventOption <em>Vessel Event Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event Option</em>' reference.
	 * @see #getVesselEventOption()
	 * @generated
	 */
	void setVesselEventOption(VesselEventOption value);

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_Shipping()
	 * @model
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getShipping <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Optionise</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optionise</em>' attribute.
	 * @see #setOptionise(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_Optionise()
	 * @model
	 * @generated
	 */
	boolean isOptionise();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isOptionise <em>Optionise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optionise</em>' attribute.
	 * @see #isOptionise()
	 * @generated
	 */
	void setOptionise(boolean value);

	/**
	 * Returns the value of the '<em><b>Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Options</em>' containment reference.
	 * @see #setOptions(BaseCaseRowOptions)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_Options()
	 * @model containment="true"
	 * @generated
	 */
	BaseCaseRowOptions getOptions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getOptions <em>Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Options</em>' containment reference.
	 * @see #getOptions()
	 * @generated
	 */
	void setOptions(BaseCaseRowOptions value);

	/**
	 * Returns the value of the '<em><b>Freeze</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Freeze</em>' attribute.
	 * @see #setFreeze(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_Freeze()
	 * @model
	 * @generated
	 */
	boolean isFreeze();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#isFreeze <em>Freeze</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Freeze</em>' attribute.
	 * @see #isFreeze()
	 * @generated
	 */
	void setFreeze(boolean value);

	/**
	 * Returns the value of the '<em><b>Group</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.analytics.BaseCaseRowGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' reference.
	 * @see #setGroup(BaseCaseRowGroup)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBaseCaseRow_Group()
	 * @see com.mmxlabs.models.lng.analytics.BaseCaseRowGroup#getRows
	 * @model opposite="rows"
	 * @generated
	 */
	BaseCaseRowGroup getGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BaseCaseRow#getGroup <em>Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' reference.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(BaseCaseRowGroup value);

} // BaseCaseRow
