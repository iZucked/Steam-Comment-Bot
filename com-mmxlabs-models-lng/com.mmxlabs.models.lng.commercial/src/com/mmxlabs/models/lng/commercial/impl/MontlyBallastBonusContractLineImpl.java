/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MontlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NextPortType;

import java.math.BigDecimal;

import java.time.YearMonth;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Montly Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MontlyBallastBonusContractLineImpl#getMonth <em>Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MontlyBallastBonusContractLineImpl#getBallastBonusTo <em>Ballast Bonus To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MontlyBallastBonusContractLineImpl#getBallastBonusPctFuel <em>Ballast Bonus Pct Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.MontlyBallastBonusContractLineImpl#getBallastBonusPctCharter <em>Ballast Bonus Pct Charter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MontlyBallastBonusContractLineImpl extends BallastBonusContractLineImpl implements MontlyBallastBonusContractLine {
	/**
	 * The default value of the '{@link #getMonth() <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMonth()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth MONTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMonth() <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMonth()
	 * @generated
	 * @ordered
	 */
	protected YearMonth month = MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastBonusTo() <em>Ballast Bonus To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusTo()
	 * @generated
	 * @ordered
	 */
	protected static final NextPortType BALLAST_BONUS_TO_EDEFAULT = NextPortType.UNKNOWN;

	/**
	 * The cached value of the '{@link #getBallastBonusTo() <em>Ballast Bonus To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusTo()
	 * @generated
	 * @ordered
	 */
	protected NextPortType ballastBonusTo = BALLAST_BONUS_TO_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastBonusPctFuel() <em>Ballast Bonus Pct Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusPctFuel()
	 * @generated
	 * @ordered
	 */
	protected static final BigDecimal BALLAST_BONUS_PCT_FUEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBallastBonusPctFuel() <em>Ballast Bonus Pct Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusPctFuel()
	 * @generated
	 * @ordered
	 */
	protected BigDecimal ballastBonusPctFuel = BALLAST_BONUS_PCT_FUEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastBonusPctCharter() <em>Ballast Bonus Pct Charter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusPctCharter()
	 * @generated
	 * @ordered
	 */
	protected static final BigDecimal BALLAST_BONUS_PCT_CHARTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBallastBonusPctCharter() <em>Ballast Bonus Pct Charter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusPctCharter()
	 * @generated
	 * @ordered
	 */
	protected BigDecimal ballastBonusPctCharter = BALLAST_BONUS_PCT_CHARTER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MontlyBallastBonusContractLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.MONTLY_BALLAST_BONUS_CONTRACT_LINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getMonth() {
		return month;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMonth(YearMonth newMonth) {
		YearMonth oldMonth = month;
		month = newMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH, oldMonth, month));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NextPortType getBallastBonusTo() {
		return ballastBonusTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastBonusTo(NextPortType newBallastBonusTo) {
		NextPortType oldBallastBonusTo = ballastBonusTo;
		ballastBonusTo = newBallastBonusTo == null ? BALLAST_BONUS_TO_EDEFAULT : newBallastBonusTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO, oldBallastBonusTo, ballastBonusTo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigDecimal getBallastBonusPctFuel() {
		return ballastBonusPctFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastBonusPctFuel(BigDecimal newBallastBonusPctFuel) {
		BigDecimal oldBallastBonusPctFuel = ballastBonusPctFuel;
		ballastBonusPctFuel = newBallastBonusPctFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL, oldBallastBonusPctFuel, ballastBonusPctFuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigDecimal getBallastBonusPctCharter() {
		return ballastBonusPctCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastBonusPctCharter(BigDecimal newBallastBonusPctCharter) {
		BigDecimal oldBallastBonusPctCharter = ballastBonusPctCharter;
		ballastBonusPctCharter = newBallastBonusPctCharter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER, oldBallastBonusPctCharter, ballastBonusPctCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH:
				return getMonth();
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO:
				return getBallastBonusTo();
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL:
				return getBallastBonusPctFuel();
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER:
				return getBallastBonusPctCharter();
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
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH:
				setMonth((YearMonth)newValue);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO:
				setBallastBonusTo((NextPortType)newValue);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL:
				setBallastBonusPctFuel((BigDecimal)newValue);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER:
				setBallastBonusPctCharter((BigDecimal)newValue);
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
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH:
				setMonth(MONTH_EDEFAULT);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO:
				setBallastBonusTo(BALLAST_BONUS_TO_EDEFAULT);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL:
				setBallastBonusPctFuel(BALLAST_BONUS_PCT_FUEL_EDEFAULT);
				return;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER:
				setBallastBonusPctCharter(BALLAST_BONUS_PCT_CHARTER_EDEFAULT);
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
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH:
				return MONTH_EDEFAULT == null ? month != null : !MONTH_EDEFAULT.equals(month);
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO:
				return ballastBonusTo != BALLAST_BONUS_TO_EDEFAULT;
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL:
				return BALLAST_BONUS_PCT_FUEL_EDEFAULT == null ? ballastBonusPctFuel != null : !BALLAST_BONUS_PCT_FUEL_EDEFAULT.equals(ballastBonusPctFuel);
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER:
				return BALLAST_BONUS_PCT_CHARTER_EDEFAULT == null ? ballastBonusPctCharter != null : !BALLAST_BONUS_PCT_CHARTER_EDEFAULT.equals(ballastBonusPctCharter);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (month: ");
		result.append(month);
		result.append(", ballastBonusTo: ");
		result.append(ballastBonusTo);
		result.append(", ballastBonusPctFuel: ");
		result.append(ballastBonusPctFuel);
		result.append(", ballastBonusPctCharter: ");
		result.append(ballastBonusPctCharter);
		result.append(')');
		return result.toString();
	}

} //MontlyBallastBonusContractLineImpl
