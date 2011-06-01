/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.optimiser.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.impl.NamedObjectImpl;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Objective</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.ObjectiveImpl#getWeight <em>Weight</em>}</li>
 *   <li>{@link scenario.optimiser.impl.ObjectiveImpl#getDiscountCurve <em>Discount Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectiveImpl extends NamedObjectImpl implements Objective {
	/**
	 * The default value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected static final double WEIGHT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected double weight = WEIGHT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDiscountCurve() <em>Discount Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountCurve()
	 * @generated
	 * @ordered
	 */
	protected DiscountCurve discountCurve;

	/**
	 * This is true if the Discount Curve reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean discountCurveESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ObjectiveImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OBJECTIVE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getWeight() {
		return weight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWeight(double newWeight) {
		double oldWeight = weight;
		weight = newWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OBJECTIVE__WEIGHT, oldWeight, weight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscountCurve getDiscountCurve() {
		if (discountCurve != null && discountCurve.eIsProxy()) {
			InternalEObject oldDiscountCurve = (InternalEObject)discountCurve;
			discountCurve = (DiscountCurve)eResolveProxy(oldDiscountCurve);
			if (discountCurve != oldDiscountCurve) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE, oldDiscountCurve, discountCurve));
			}
		}
		return discountCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscountCurve basicGetDiscountCurve() {
		return discountCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiscountCurve(DiscountCurve newDiscountCurve) {
		DiscountCurve oldDiscountCurve = discountCurve;
		discountCurve = newDiscountCurve;
		boolean oldDiscountCurveESet = discountCurveESet;
		discountCurveESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE, oldDiscountCurve, discountCurve, !oldDiscountCurveESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDiscountCurve() {
		DiscountCurve oldDiscountCurve = discountCurve;
		boolean oldDiscountCurveESet = discountCurveESet;
		discountCurve = null;
		discountCurveESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE, oldDiscountCurve, null, oldDiscountCurveESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDiscountCurve() {
		return discountCurveESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OptimiserPackage.OBJECTIVE__WEIGHT:
				return getWeight();
			case OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE:
				if (resolve) return getDiscountCurve();
				return basicGetDiscountCurve();
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
			case OptimiserPackage.OBJECTIVE__WEIGHT:
				setWeight((Double)newValue);
				return;
			case OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE:
				setDiscountCurve((DiscountCurve)newValue);
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
			case OptimiserPackage.OBJECTIVE__WEIGHT:
				setWeight(WEIGHT_EDEFAULT);
				return;
			case OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE:
				unsetDiscountCurve();
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
			case OptimiserPackage.OBJECTIVE__WEIGHT:
				return weight != WEIGHT_EDEFAULT;
			case OptimiserPackage.OBJECTIVE__DISCOUNT_CURVE:
				return isSetDiscountCurve();
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
		result.append(" (weight: ");
		result.append(weight);
		result.append(')');
		return result.toString();
	}

} //ObjectiveImpl
