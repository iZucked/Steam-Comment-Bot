/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market Allocation</b></em>'.
 * @since 5.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getMarket <em>Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation <em>Slot Allocation</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMarketAllocation()
 * @model
 * @generated
 */
public interface MarketAllocation extends ProfitAndLossContainer {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMarketAllocation_Slot()
	 * @model required="true"
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(SpotMarket)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMarketAllocation_Market()
	 * @model required="true"
	 * @generated
	 */
	SpotMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(SpotMarket value);

	/**
	 * Returns the value of the '<em><b>Slot Allocation</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation <em>Market Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocation</em>' reference.
	 * @see #setSlotAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getMarketAllocation_SlotAllocation()
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation
	 * @model opposite="marketAllocation"
	 * @generated
	 */
	SlotAllocation getSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Allocation</em>' reference.
	 * @see #getSlotAllocation()
	 * @generated
	 */
	void setSlotAllocation(SlotAllocation value);

} // MarketAllocation
