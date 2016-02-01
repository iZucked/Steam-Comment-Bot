/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPriceInfo <em>Price Info</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPricingEvent <em>Pricing Event</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket()
 * @model abstract="true"
 * @generated
 */
public interface SpotMarket extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enabled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #setEnabled(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_Enabled()
	 * @model default="true"
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#isEnabled <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_Availability()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SpotAvailability getAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getAvailability <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Availability</em>' containment reference.
	 * @see #getAvailability()
	 * @generated
	 */
	void setAvailability(SpotAvailability value);

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #setMinQuantity(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_MinQuantity()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #setMaxQuantity(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_MaxQuantity()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#,###,##0'"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.VolumeUnits}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Limits Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #setVolumeLimitsUnit(VolumeUnits)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_VolumeLimitsUnit()
	 * @model required="true"
	 * @generated
	 */
	VolumeUnits getVolumeLimitsUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getVolumeLimitsUnit <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Limits Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 */
	void setVolumeLimitsUnit(VolumeUnits value);

	/**
	 * Returns the value of the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Info</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Info</em>' containment reference.
	 * @see #setPriceInfo(LNGPriceCalculatorParameters)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_PriceInfo()
	 * @model containment="true"
	 * @generated
	 */
	LNGPriceCalculatorParameters getPriceInfo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPriceInfo <em>Price Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Info</em>' containment reference.
	 * @see #getPriceInfo()
	 * @generated
	 */
	void setPriceInfo(LNGPriceCalculatorParameters value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Pricing Event</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.commercial.PricingEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pricing Event</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Event</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see #setPricingEvent(PricingEvent)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotMarket_PricingEvent()
	 * @model
	 * @generated
	 */
	PricingEvent getPricingEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotMarket#getPricingEvent <em>Pricing Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Event</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see #getPricingEvent()
	 * @generated
	 */
	void setPricingEvent(PricingEvent value);

} // end of  SpotMarket

// finish type fixing
