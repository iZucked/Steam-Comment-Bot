/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Basic Slot PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraShippingPNL <em>Extra Shipping PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getAdditionalPNL <em>Additional PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue <em>Misc Costs Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraUpsidePNL <em>Extra Upside PNL</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails()
 * @model
 * @generated
 */
public interface BasicSlotPNLDetails extends GeneralPNLDetails {
	/**
	 * Returns the value of the '<em><b>Extra Shipping PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Shipping PNL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Shipping PNL</em>' attribute.
	 * @see #setExtraShippingPNL(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_ExtraShippingPNL()
	 * @model
	 * @generated
	 */
	int getExtraShippingPNL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraShippingPNL <em>Extra Shipping PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extra Shipping PNL</em>' attribute.
	 * @see #getExtraShippingPNL()
	 * @generated
	 */
	void setExtraShippingPNL(int value);

	/**
	 * Returns the value of the '<em><b>Additional PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Additional PNL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Additional PNL</em>' attribute.
	 * @see #setAdditionalPNL(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_AdditionalPNL()
	 * @model
	 * @generated
	 */
	int getAdditionalPNL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getAdditionalPNL <em>Additional PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Additional PNL</em>' attribute.
	 * @see #getAdditionalPNL()
	 * @generated
	 */
	void setAdditionalPNL(int value);

	/**
	 * Returns the value of the '<em><b>Cancellation Fees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Fees</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Fees</em>' attribute.
	 * @see #isSetCancellationFees()
	 * @see #unsetCancellationFees()
	 * @see #setCancellationFees(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_CancellationFees()
	 * @model unsettable="true"
	 * @generated
	 */
	int getCancellationFees();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Fees</em>' attribute.
	 * @see #isSetCancellationFees()
	 * @see #unsetCancellationFees()
	 * @see #getCancellationFees()
	 * @generated
	 */
	void setCancellationFees(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCancellationFees()
	 * @see #getCancellationFees()
	 * @see #setCancellationFees(int)
	 * @generated
	 */
	void unsetCancellationFees();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cancellation Fees</em>' attribute is set.
	 * @see #unsetCancellationFees()
	 * @see #getCancellationFees()
	 * @see #setCancellationFees(int)
	 * @generated
	 */
	boolean isSetCancellationFees();

	/**
	 * Returns the value of the '<em><b>Hedging Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hedging Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hedging Value</em>' attribute.
	 * @see #isSetHedgingValue()
	 * @see #unsetHedgingValue()
	 * @see #setHedgingValue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_HedgingValue()
	 * @model unsettable="true"
	 * @generated
	 */
	int getHedgingValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hedging Value</em>' attribute.
	 * @see #isSetHedgingValue()
	 * @see #unsetHedgingValue()
	 * @see #getHedgingValue()
	 * @generated
	 */
	void setHedgingValue(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetHedgingValue()
	 * @see #getHedgingValue()
	 * @see #setHedgingValue(int)
	 * @generated
	 */
	void unsetHedgingValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Hedging Value</em>' attribute is set.
	 * @see #unsetHedgingValue()
	 * @see #getHedgingValue()
	 * @see #setHedgingValue(int)
	 * @generated
	 */
	boolean isSetHedgingValue();

	/**
	 * Returns the value of the '<em><b>Misc Costs Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Misc Costs Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Misc Costs Value</em>' attribute.
	 * @see #isSetMiscCostsValue()
	 * @see #unsetMiscCostsValue()
	 * @see #setMiscCostsValue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_MiscCostsValue()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMiscCostsValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue <em>Misc Costs Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Misc Costs Value</em>' attribute.
	 * @see #isSetMiscCostsValue()
	 * @see #unsetMiscCostsValue()
	 * @see #getMiscCostsValue()
	 * @generated
	 */
	void setMiscCostsValue(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue <em>Misc Costs Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMiscCostsValue()
	 * @see #getMiscCostsValue()
	 * @see #setMiscCostsValue(int)
	 * @generated
	 */
	void unsetMiscCostsValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue <em>Misc Costs Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Misc Costs Value</em>' attribute is set.
	 * @see #unsetMiscCostsValue()
	 * @see #getMiscCostsValue()
	 * @see #setMiscCostsValue(int)
	 * @generated
	 */
	boolean isSetMiscCostsValue();

	/**
	 * Returns the value of the '<em><b>Extra Upside PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Upside PNL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Upside PNL</em>' attribute.
	 * @see #setExtraUpsidePNL(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getBasicSlotPNLDetails_ExtraUpsidePNL()
	 * @model
	 * @generated
	 */
	int getExtraUpsidePNL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraUpsidePNL <em>Extra Upside PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extra Upside PNL</em>' attribute.
	 * @see #getExtraUpsidePNL()
	 * @generated
	 */
	void setExtraUpsidePNL(int value);

} // BasicSlotPNLDetails
