/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suez Canal Tug Band</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl#getTugs <em>Tugs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl#getBandStart <em>Band Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTugBandImpl#getBandEnd <em>Band End</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SuezCanalTugBandImpl extends EObjectImpl implements SuezCanalTugBand {
	/**
	 * The default value of the '{@link #getTugs() <em>Tugs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugs()
	 * @generated
	 * @ordered
	 */
	protected static final int TUGS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTugs() <em>Tugs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugs()
	 * @generated
	 * @ordered
	 */
	protected int tugs = TUGS_EDEFAULT;

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
	protected SuezCanalTugBandImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SUEZ_CANAL_TUG_BAND;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTugs() {
		return tugs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTugs(int newTugs) {
		int oldTugs = tugs;
		tugs = newTugs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TUG_BAND__TUGS, oldTugs, tugs));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START, oldBandStart, bandStart, !oldBandStartESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START, oldBandStart, BAND_START_EDEFAULT, oldBandStartESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END, oldBandEnd, bandEnd, !oldBandEndESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END, oldBandEnd, BAND_END_EDEFAULT, oldBandEndESet));
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
			case PricingPackage.SUEZ_CANAL_TUG_BAND__TUGS:
				return getTugs();
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START:
				return getBandStart();
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END:
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
			case PricingPackage.SUEZ_CANAL_TUG_BAND__TUGS:
				setTugs((Integer)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START:
				setBandStart((Integer)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END:
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
			case PricingPackage.SUEZ_CANAL_TUG_BAND__TUGS:
				setTugs(TUGS_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START:
				unsetBandStart();
				return;
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END:
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
			case PricingPackage.SUEZ_CANAL_TUG_BAND__TUGS:
				return tugs != TUGS_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_START:
				return isSetBandStart();
			case PricingPackage.SUEZ_CANAL_TUG_BAND__BAND_END:
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
		result.append(" (tugs: ");
		result.append(tugs);
		result.append(", bandStart: ");
		if (bandStartESet) result.append(bandStart); else result.append("<unset>");
		result.append(", bandEnd: ");
		if (bandEndESet) result.append(bandEnd); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //SuezCanalTugBandImpl
