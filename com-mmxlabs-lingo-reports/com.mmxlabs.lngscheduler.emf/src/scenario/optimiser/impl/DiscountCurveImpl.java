/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.impl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.impl.NamedObjectImpl;
import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discount Curve</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.DiscountCurveImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link scenario.optimiser.impl.DiscountCurveImpl#getDiscounts <em>Discounts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiscountCurveImpl extends NamedObjectImpl implements DiscountCurve {
	/**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected Date startDate = START_DATE_EDEFAULT;

	/**
	 * This is true if the Start Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startDateESet;

	/**
	 * The cached value of the '{@link #getDiscounts() <em>Discounts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscounts()
	 * @generated
	 * @ordered
	 */
	protected EList<Discount> discounts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiscountCurveImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.DISCOUNT_CURVE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Discount> getDiscounts() {
		if (discounts == null) {
			discounts = new EObjectContainmentEList.Resolving<Discount>(Discount.class, this, OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS);
		}
		return discounts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartDate(Date newStartDate) {
		Date oldStartDate = startDate;
		startDate = newStartDate;
		boolean oldStartDateESet = startDateESet;
		startDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.DISCOUNT_CURVE__START_DATE, oldStartDate, startDate, !oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStartDate() {
		Date oldStartDate = startDate;
		boolean oldStartDateESet = startDateESet;
		startDate = START_DATE_EDEFAULT;
		startDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.DISCOUNT_CURVE__START_DATE, oldStartDate, START_DATE_EDEFAULT, oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStartDate() {
		return startDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS:
				return ((InternalEList<?>)getDiscounts()).basicRemove(otherEnd, msgs);
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
			case OptimiserPackage.DISCOUNT_CURVE__START_DATE:
				return getStartDate();
			case OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS:
				return getDiscounts();
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
			case OptimiserPackage.DISCOUNT_CURVE__START_DATE:
				setStartDate((Date)newValue);
				return;
			case OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS:
				getDiscounts().clear();
				getDiscounts().addAll((Collection<? extends Discount>)newValue);
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
			case OptimiserPackage.DISCOUNT_CURVE__START_DATE:
				unsetStartDate();
				return;
			case OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS:
				getDiscounts().clear();
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
			case OptimiserPackage.DISCOUNT_CURVE__START_DATE:
				return isSetStartDate();
			case OptimiserPackage.DISCOUNT_CURVE__DISCOUNTS:
				return discounts != null && !discounts.isEmpty();
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
		result.append(" (startDate: ");
		if (startDateESet) result.append(startDate); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //DiscountCurveImpl
