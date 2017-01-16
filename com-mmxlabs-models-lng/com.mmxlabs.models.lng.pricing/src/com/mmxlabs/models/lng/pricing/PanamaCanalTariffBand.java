/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Panama Canal Tariff Band</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getLadenTariff <em>Laden Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastTariff <em>Ballast Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastRoundtripTariff <em>Ballast Roundtrip Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart <em>Band Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd <em>Band End</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand()
 * @model
 * @generated
 */
public interface PanamaCanalTariffBand extends EObject {
	/**
	 * Returns the value of the '<em><b>Laden Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Tariff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Tariff</em>' attribute.
	 * @see #setLadenTariff(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand_LadenTariff()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='#0.###'"
	 * @generated
	 */
	double getLadenTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getLadenTariff <em>Laden Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Tariff</em>' attribute.
	 * @see #getLadenTariff()
	 * @generated
	 */
	void setLadenTariff(double value);

	/**
	 * Returns the value of the '<em><b>Ballast Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Tariff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Tariff</em>' attribute.
	 * @see #setBallastTariff(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand_BallastTariff()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='#0.###'"
	 * @generated
	 */
	double getBallastTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastTariff <em>Ballast Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Tariff</em>' attribute.
	 * @see #getBallastTariff()
	 * @generated
	 */
	void setBallastTariff(double value);

	/**
	 * Returns the value of the '<em><b>Ballast Roundtrip Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Roundtrip Tariff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Roundtrip Tariff</em>' attribute.
	 * @see #setBallastRoundtripTariff(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand_BallastRoundtripTariff()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='#0.###'"
	 * @generated
	 */
	double getBallastRoundtripTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBallastRoundtripTariff <em>Ballast Roundtrip Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Roundtrip Tariff</em>' attribute.
	 * @see #getBallastRoundtripTariff()
	 * @generated
	 */
	void setBallastRoundtripTariff(double value);

	/**
	 * Returns the value of the '<em><b>Band Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Band Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Band Start</em>' attribute.
	 * @see #isSetBandStart()
	 * @see #unsetBandStart()
	 * @see #setBandStart(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand_BandStart()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitSuffix='m3\r\n' formatString='##,###,##0'"
	 * @generated
	 */
	int getBandStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart <em>Band Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Band Start</em>' attribute.
	 * @see #isSetBandStart()
	 * @see #unsetBandStart()
	 * @see #getBandStart()
	 * @generated
	 */
	void setBandStart(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart <em>Band Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBandStart()
	 * @see #getBandStart()
	 * @see #setBandStart(int)
	 * @generated
	 */
	void unsetBandStart();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandStart <em>Band Start</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Band Start</em>' attribute is set.
	 * @see #unsetBandStart()
	 * @see #getBandStart()
	 * @see #setBandStart(int)
	 * @generated
	 */
	boolean isSetBandStart();

	/**
	 * Returns the value of the '<em><b>Band End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Band End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Band End</em>' attribute.
	 * @see #isSetBandEnd()
	 * @see #unsetBandEnd()
	 * @see #setBandEnd(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariffBand_BandEnd()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unitSuffix='m3\r\n' formatString='##,###,##0'"
	 * @generated
	 */
	int getBandEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd <em>Band End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Band End</em>' attribute.
	 * @see #isSetBandEnd()
	 * @see #unsetBandEnd()
	 * @see #getBandEnd()
	 * @generated
	 */
	void setBandEnd(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd <em>Band End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBandEnd()
	 * @see #getBandEnd()
	 * @see #setBandEnd(int)
	 * @generated
	 */
	void unsetBandEnd();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand#getBandEnd <em>Band End</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Band End</em>' attribute is set.
	 * @see #unsetBandEnd()
	 * @see #getBandEnd()
	 * @see #setBandEnd(int)
	 * @generated
	 */
	boolean isSetBandEnd();

} // PanamaCanalTariffBand
