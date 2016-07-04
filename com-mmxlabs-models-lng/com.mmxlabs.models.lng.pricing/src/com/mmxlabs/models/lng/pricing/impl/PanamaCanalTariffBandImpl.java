/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Panama Canal Tariff Band</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl#getLadenTariff <em>Laden Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl#getBallastTariff <em>Ballast Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl#getBallastRoundtripTariff <em>Ballast Roundtrip Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl#getBandStart <em>Band Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaCanalTariffBandImpl#getBandEnd <em>Band End</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PanamaCanalTariffBandImpl extends EObjectImpl implements PanamaCanalTariffBand {
	/**
	 * The default value of the '{@link #getLadenTariff() <em>Laden Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenTariff()
	 * @generated
	 * @ordered
	 */
	protected static final double LADEN_TARIFF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLadenTariff() <em>Laden Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenTariff()
	 * @generated
	 * @ordered
	 */
	protected double ladenTariff = LADEN_TARIFF_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastTariff() <em>Ballast Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastTariff()
	 * @generated
	 * @ordered
	 */
	protected static final double BALLAST_TARIFF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBallastTariff() <em>Ballast Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastTariff()
	 * @generated
	 * @ordered
	 */
	protected double ballastTariff = BALLAST_TARIFF_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastRoundtripTariff() <em>Ballast Roundtrip Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastRoundtripTariff()
	 * @generated
	 * @ordered
	 */
	protected static final double BALLAST_ROUNDTRIP_TARIFF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getBallastRoundtripTariff() <em>Ballast Roundtrip Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastRoundtripTariff()
	 * @generated
	 * @ordered
	 */
	protected double ballastRoundtripTariff = BALLAST_ROUNDTRIP_TARIFF_EDEFAULT;

	/**
	 * The default value of the '{@link #getBandStart() <em>Band Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBandStart()
	 * @generated
	 * @ordered
	 */
	protected static final int BAND_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBandStart() <em>Band Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBandStart()
	 * @generated
	 * @ordered
	 */
	protected int bandStart = BAND_START_EDEFAULT;

	/**
	 * This is true if the Band Start attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean bandStartESet;

	/**
	 * The default value of the '{@link #getBandEnd() <em>Band End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBandEnd()
	 * @generated
	 * @ordered
	 */
	protected static final int BAND_END_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBandEnd() <em>Band End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBandEnd()
	 * @generated
	 * @ordered
	 */
	protected int bandEnd = BAND_END_EDEFAULT;

	/**
	 * This is true if the Band End attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean bandEndESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaCanalTariffBandImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLadenTariff() {
		return ladenTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenTariff(double newLadenTariff) {
		double oldLadenTariff = ladenTariff;
		ladenTariff = newLadenTariff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF, oldLadenTariff, ladenTariff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBallastTariff() {
		return ballastTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastTariff(double newBallastTariff) {
		double oldBallastTariff = ballastTariff;
		ballastTariff = newBallastTariff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF, oldBallastTariff, ballastTariff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getBallastRoundtripTariff() {
		return ballastRoundtripTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastRoundtripTariff(double newBallastRoundtripTariff) {
		double oldBallastRoundtripTariff = ballastRoundtripTariff;
		ballastRoundtripTariff = newBallastRoundtripTariff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF, oldBallastRoundtripTariff, ballastRoundtripTariff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBandStart() {
		return bandStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBandStart(int newBandStart) {
		int oldBandStart = bandStart;
		bandStart = newBandStart;
		boolean oldBandStartESet = bandStartESet;
		bandStartESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START, oldBandStart, bandStart, !oldBandStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBandStart() {
		int oldBandStart = bandStart;
		boolean oldBandStartESet = bandStartESet;
		bandStart = BAND_START_EDEFAULT;
		bandStartESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START, oldBandStart, BAND_START_EDEFAULT, oldBandStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBandStart() {
		return bandStartESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBandEnd() {
		return bandEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBandEnd(int newBandEnd) {
		int oldBandEnd = bandEnd;
		bandEnd = newBandEnd;
		boolean oldBandEndESet = bandEndESet;
		bandEndESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END, oldBandEnd, bandEnd, !oldBandEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBandEnd() {
		int oldBandEnd = bandEnd;
		boolean oldBandEndESet = bandEndESet;
		bandEnd = BAND_END_EDEFAULT;
		bandEndESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END, oldBandEnd, BAND_END_EDEFAULT, oldBandEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBandEnd() {
		return bandEndESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF:
				return getLadenTariff();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF:
				return getBallastTariff();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF:
				return getBallastRoundtripTariff();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START:
				return getBandStart();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END:
				return getBandEnd();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF:
				setLadenTariff((Double)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF:
				setBallastTariff((Double)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF:
				setBallastRoundtripTariff((Double)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START:
				setBandStart((Integer)newValue);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END:
				setBandEnd((Integer)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF:
				setLadenTariff(LADEN_TARIFF_EDEFAULT);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF:
				setBallastTariff(BALLAST_TARIFF_EDEFAULT);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF:
				setBallastRoundtripTariff(BALLAST_ROUNDTRIP_TARIFF_EDEFAULT);
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START:
				unsetBandStart();
				return;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END:
				unsetBandEnd();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF:
				return ladenTariff != LADEN_TARIFF_EDEFAULT;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF:
				return ballastTariff != BALLAST_TARIFF_EDEFAULT;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF:
				return ballastRoundtripTariff != BALLAST_ROUNDTRIP_TARIFF_EDEFAULT;
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_START:
				return isSetBandStart();
			case PricingPackage.PANAMA_CANAL_TARIFF_BAND__BAND_END:
				return isSetBandEnd();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ladenTariff: ");
		result.append(ladenTariff);
		result.append(", ballastTariff: ");
		result.append(ballastTariff);
		result.append(", ballastRoundtripTariff: ");
		result.append(ballastRoundtripTariff);
		result.append(", bandStart: ");
		if (bandStartESet) result.append(bandStart); else result.append("<unset>");
		result.append(", bandEnd: ");
		if (bandEndESet) result.append(bandEnd); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //PanamaCanalTariffBandImpl
