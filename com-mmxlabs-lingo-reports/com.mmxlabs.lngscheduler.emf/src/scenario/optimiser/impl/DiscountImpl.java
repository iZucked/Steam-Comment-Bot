/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.optimiser.Discount;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discount</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.DiscountImpl#getTime <em>Time</em>}</li>
 *   <li>{@link scenario.optimiser.impl.DiscountImpl#getDiscountFactor <em>Discount Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiscountImpl extends EObjectImpl implements Discount {
	/**
	 * The default value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected static final int TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected int time = TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected static final float DISCOUNT_FACTOR_EDEFAULT = 1.0F;

	/**
	 * The cached value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected float discountFactor = DISCOUNT_FACTOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiscountImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.DISCOUNT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTime() {
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTime(int newTime) {
		int oldTime = time;
		time = newTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.DISCOUNT__TIME, oldTime, time));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getDiscountFactor() {
		return discountFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiscountFactor(float newDiscountFactor) {
		float oldDiscountFactor = discountFactor;
		discountFactor = newDiscountFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.DISCOUNT__DISCOUNT_FACTOR, oldDiscountFactor, discountFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OptimiserPackage.DISCOUNT__TIME:
				return getTime();
			case OptimiserPackage.DISCOUNT__DISCOUNT_FACTOR:
				return getDiscountFactor();
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
			case OptimiserPackage.DISCOUNT__TIME:
				setTime((Integer)newValue);
				return;
			case OptimiserPackage.DISCOUNT__DISCOUNT_FACTOR:
				setDiscountFactor((Float)newValue);
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
			case OptimiserPackage.DISCOUNT__TIME:
				setTime(TIME_EDEFAULT);
				return;
			case OptimiserPackage.DISCOUNT__DISCOUNT_FACTOR:
				setDiscountFactor(DISCOUNT_FACTOR_EDEFAULT);
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
			case OptimiserPackage.DISCOUNT__TIME:
				return time != TIME_EDEFAULT;
			case OptimiserPackage.DISCOUNT__DISCOUNT_FACTOR:
				return discountFactor != DISCOUNT_FACTOR_EDEFAULT;
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
		result.append(" (time: ");
		result.append(time);
		result.append(", discountFactor: ");
		result.append(discountFactor);
		result.append(')');
		return result.toString();
	}

} //DiscountImpl
