/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Result Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuySlotAllocation <em>Buy Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellSlotAllocation <em>Sell Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextSlotVisit <em>Next Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getLadenPanama <em>Laden Panama</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBallastPanama <em>Ballast Panama</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer()
 * @model
 * @generated
 */
public interface MarketabilityResultContainer extends EObject {

	/**
	 * Returns the value of the '<em><b>Rhs Results</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MarketabilityResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Results</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_RhsResults()
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_BuySlotAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getBuySlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuySlotAllocation <em>Buy Slot Allocation</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_SellSlotAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getSellSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellSlotAllocation <em>Sell Slot Allocation</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_NextSlotVisit()
	 * @model
	 * @generated
	 */
	SlotVisit getNextSlotVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextSlotVisit <em>Next Slot Visit</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_LadenPanama()
	 * @model
	 * @generated
	 */
	Journey getLadenPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getLadenPanama <em>Laden Panama</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_BallastPanama()
	 * @model
	 * @generated
	 */
	Journey getBallastPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBallastPanama <em>Ballast Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Panama</em>' reference.
	 * @see #getBallastPanama()
	 * @generated
	 */
	void setBallastPanama(Journey value);
} // MarketabilityResultContainer
