/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import java.time.LocalDate;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextEvent <em>Next Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuyDate <em>Buy Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellDate <em>Sell Date</em>}</li>
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
	 * Returns the value of the '<em><b>Next Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Event</em>' containment reference.
	 * @see #setNextEvent(MarketabilityEvent)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_NextEvent()
	 * @model containment="true"
	 * @generated
	 */
	MarketabilityEvent getNextEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getNextEvent <em>Next Event</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Event</em>' containment reference.
	 * @see #getNextEvent()
	 * @generated
	 */
	void setNextEvent(MarketabilityEvent value);

	/**
	 * Returns the value of the '<em><b>Laden Panama</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Panama</em>' attribute.
	 * @see #setLadenPanama(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_LadenPanama()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getLadenPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getLadenPanama <em>Laden Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Panama</em>' attribute.
	 * @see #getLadenPanama()
	 * @generated
	 */
	void setLadenPanama(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Ballast Panama</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Panama</em>' attribute.
	 * @see #setBallastPanama(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_BallastPanama()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getBallastPanama();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBallastPanama <em>Ballast Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Panama</em>' attribute.
	 * @see #getBallastPanama()
	 * @generated
	 */
	void setBallastPanama(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Buy Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buy Date</em>' attribute.
	 * @see #setBuyDate(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_BuyDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getBuyDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getBuyDate <em>Buy Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buy Date</em>' attribute.
	 * @see #getBuyDate()
	 * @generated
	 */
	void setBuyDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Sell Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sell Date</em>' attribute.
	 * @see #setSellDate(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityResultContainer_SellDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSellDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityResultContainer#getSellDate <em>Sell Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sell Date</em>' attribute.
	 * @see #getSellDate()
	 * @generated
	 */
	void setSellDate(LocalDate value);
} // MarketabilityResultContainer
