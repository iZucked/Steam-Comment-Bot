/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suez Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getBands <em>Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getTugBands <em>Tug Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getTugCost <em>Tug Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getMooringCost <em>Mooring Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getPilotageCost <em>Pilotage Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getDisbursements <em>Disbursements</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getDiscountFactor <em>Discount Factor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getSdrToUSD <em>Sdr To USD</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SuezCanalTariffImpl extends EObjectImpl implements SuezCanalTariff {
	/**
	 * The cached value of the '{@link #getBands() <em>Bands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBands()
	 * @generated
	 * @ordered
	 */
	protected EList<SuezCanalTariffBand> bands;

	/**
	 * The cached value of the '{@link #getTugBands() <em>Tug Bands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugBands()
	 * @generated
	 * @ordered
	 */
	protected EList<SuezCanalTugBand> tugBands;

	/**
	 * The default value of the '{@link #getTugCost() <em>Tug Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugCost()
	 * @generated
	 * @ordered
	 */
	protected static final double TUG_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTugCost() <em>Tug Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugCost()
	 * @generated
	 * @ordered
	 */
	protected double tugCost = TUG_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getMooringCost() <em>Mooring Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMooringCost()
	 * @generated
	 * @ordered
	 */
	protected static final double MOORING_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMooringCost() <em>Mooring Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMooringCost()
	 * @generated
	 * @ordered
	 */
	protected double mooringCost = MOORING_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getPilotageCost() <em>Pilotage Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotageCost()
	 * @generated
	 * @ordered
	 */
	protected static final double PILOTAGE_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPilotageCost() <em>Pilotage Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotageCost()
	 * @generated
	 * @ordered
	 */
	protected double pilotageCost = PILOTAGE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getDisbursements() <em>Disbursements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisbursements()
	 * @generated
	 * @ordered
	 */
	protected static final double DISBURSEMENTS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDisbursements() <em>Disbursements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisbursements()
	 * @generated
	 * @ordered
	 */
	protected double disbursements = DISBURSEMENTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected static final double DISCOUNT_FACTOR_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected double discountFactor = DISCOUNT_FACTOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getSdrToUSD() <em>Sdr To USD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSdrToUSD()
	 * @generated
	 * @ordered
	 */
	protected static final String SDR_TO_USD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSdrToUSD() <em>Sdr To USD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSdrToUSD()
	 * @generated
	 * @ordered
	 */
	protected String sdrToUSD = SDR_TO_USD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalTariffImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SUEZ_CANAL_TARIFF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SuezCanalTariffBand> getBands() {
		if (bands == null) {
			bands = new EObjectContainmentEList<SuezCanalTariffBand>(SuezCanalTariffBand.class, this, PricingPackage.SUEZ_CANAL_TARIFF__BANDS);
		}
		return bands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SuezCanalTugBand> getTugBands() {
		if (tugBands == null) {
			tugBands = new EObjectContainmentEList<SuezCanalTugBand>(SuezCanalTugBand.class, this, PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS);
		}
		return tugBands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTugCost() {
		return tugCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTugCost(double newTugCost) {
		double oldTugCost = tugCost;
		tugCost = newTugCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST, oldTugCost, tugCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMooringCost() {
		return mooringCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMooringCost(double newMooringCost) {
		double oldMooringCost = mooringCost;
		mooringCost = newMooringCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__MOORING_COST, oldMooringCost, mooringCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPilotageCost() {
		return pilotageCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPilotageCost(double newPilotageCost) {
		double oldPilotageCost = pilotageCost;
		pilotageCost = newPilotageCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__PILOTAGE_COST, oldPilotageCost, pilotageCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getDisbursements() {
		return disbursements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisbursements(double newDisbursements) {
		double oldDisbursements = disbursements;
		disbursements = newDisbursements;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__DISBURSEMENTS, oldDisbursements, disbursements));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getDiscountFactor() {
		return discountFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiscountFactor(double newDiscountFactor) {
		double oldDiscountFactor = discountFactor;
		discountFactor = newDiscountFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR, oldDiscountFactor, discountFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSdrToUSD() {
		return sdrToUSD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSdrToUSD(String newSdrToUSD) {
		String oldSdrToUSD = sdrToUSD;
		sdrToUSD = newSdrToUSD;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD, oldSdrToUSD, sdrToUSD));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return ((InternalEList<?>)getBands()).basicRemove(otherEnd, msgs);
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return ((InternalEList<?>)getTugBands()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return getBands();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return getTugBands();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				return getTugCost();
			case PricingPackage.SUEZ_CANAL_TARIFF__MOORING_COST:
				return getMooringCost();
			case PricingPackage.SUEZ_CANAL_TARIFF__PILOTAGE_COST:
				return getPilotageCost();
			case PricingPackage.SUEZ_CANAL_TARIFF__DISBURSEMENTS:
				return getDisbursements();
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				return getDiscountFactor();
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				return getSdrToUSD();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				getBands().clear();
				getBands().addAll((Collection<? extends SuezCanalTariffBand>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				getTugBands().clear();
				getTugBands().addAll((Collection<? extends SuezCanalTugBand>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				setTugCost((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__MOORING_COST:
				setMooringCost((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__PILOTAGE_COST:
				setPilotageCost((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISBURSEMENTS:
				setDisbursements((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				setDiscountFactor((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				setSdrToUSD((String)newValue);
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				getBands().clear();
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				getTugBands().clear();
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				setTugCost(TUG_COST_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__MOORING_COST:
				setMooringCost(MOORING_COST_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__PILOTAGE_COST:
				setPilotageCost(PILOTAGE_COST_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISBURSEMENTS:
				setDisbursements(DISBURSEMENTS_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				setDiscountFactor(DISCOUNT_FACTOR_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				setSdrToUSD(SDR_TO_USD_EDEFAULT);
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return bands != null && !bands.isEmpty();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return tugBands != null && !tugBands.isEmpty();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				return tugCost != TUG_COST_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__MOORING_COST:
				return mooringCost != MOORING_COST_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__PILOTAGE_COST:
				return pilotageCost != PILOTAGE_COST_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISBURSEMENTS:
				return disbursements != DISBURSEMENTS_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				return discountFactor != DISCOUNT_FACTOR_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				return SDR_TO_USD_EDEFAULT == null ? sdrToUSD != null : !SDR_TO_USD_EDEFAULT.equals(sdrToUSD);
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
		result.append(" (tugCost: ");
		result.append(tugCost);
		result.append(", mooringCost: ");
		result.append(mooringCost);
		result.append(", pilotageCost: ");
		result.append(pilotageCost);
		result.append(", disbursements: ");
		result.append(disbursements);
		result.append(", discountFactor: ");
		result.append(discountFactor);
		result.append(", sdrToUSD: ");
		result.append(sdrToUSD);
		result.append(')');
		return result.toString();
	}

} //SuezCanalTariffImpl
