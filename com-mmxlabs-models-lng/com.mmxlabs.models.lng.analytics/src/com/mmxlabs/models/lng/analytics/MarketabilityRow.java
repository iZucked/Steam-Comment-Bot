/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuySlotAllocation <em>Buy Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellSlotAllocation <em>Sell Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getNextSlotVisit <em>Next Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getLadenPanama <em>Laden Panama</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBallastPanama <em>Ballast Panama</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow()
 * @model
 * @generated
 */
public interface MarketabilityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Buy Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Option</em>' reference.
	 * @see #setBuyOption(BuyOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_BuyOption()
	 * @model
	 * @generated
	 */
	BuyOption getBuyOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuyOption <em>Buy Option</em>}' reference.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Option</em>' reference.
	 * @see #setSellOption(SellOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_SellOption()
	 * @model
	 * @generated
	 */
	SellOption getSellOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellOption <em>Sell Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Option</em>' reference.
	 * @see #getSellOption()
	 * @generated
	 */
	void setSellOption(SellOption value);

	/**
	 * Returns the value of the '<em><b>Shipping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping</em>' reference.
	 * @see #setShipping(ShippingOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_Shipping()
	 * @model
	 * @generated
	 */
	ShippingOption getShipping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getShipping <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping</em>' reference.
	 * @see #getShipping()
	 * @generated
	 */
	void setShipping(ShippingOption value);

	/**
	 * Returns the value of the '<em><b>Rhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MarketabilityResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_RhsResults()
	 * @model containment="true"
	 * @generated
	 */
	EList<MarketabilityResult> getRhsResults();

	/**
	 * Returns the value of the '<em><b>Buy Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Slot Allocation</em>' reference.
	 * @see #setBuySlotAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_BuySlotAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getBuySlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBuySlotAllocation <em>Buy Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buy Slot Allocation</em>' reference.
	 * @see #getBuySlotAllocation()
	 * @generated
	 */
	void setBuySlotAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Sell Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Slot Allocation</em>' reference.
	 * @see #setSellSlotAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_SellSlotAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getSellSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getSellSlotAllocation <em>Sell Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Slot Allocation</em>' reference.
	 * @see #getSellSlotAllocation()
	 * @generated
	 */
	void setSellSlotAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Next Slot Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Slot Visit</em>' reference.
	 * @see #setNextSlotVisit(SlotVisit)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_NextSlotVisit()
	 * @model
	 * @generated
	 */
	SlotVisit getNextSlotVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getNextSlotVisit <em>Next Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Slot Visit</em>' reference.
	 * @see #getNextSlotVisit()
	 * @generated
	 */
	void setNextSlotVisit(SlotVisit value);

	/**
	 * Returns the value of the '<em><b>Laden Panama</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Panama</em>' reference.
	 * @see #setLadenPanama(Journey)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_LadenPanama()
	 * @model
	 * @generated
	 */
	Journey getLadenPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getLadenPanama <em>Laden Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Panama</em>' reference.
	 * @see #getLadenPanama()
	 * @generated
	 */
	void setLadenPanama(Journey value);

	/**
	 * Returns the value of the '<em><b>Ballast Panama</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Panama</em>' reference.
	 * @see #setBallastPanama(Journey)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityRow_BallastPanama()
	 * @model
	 * @generated
	 */
	Journey getBallastPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityRow#getBallastPanama <em>Ballast Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Panama</em>' reference.
	 * @see #getBallastPanama()
	 * @generated
	 */
	void setBallastPanama(Journey value);

} // MarketabilityRow
