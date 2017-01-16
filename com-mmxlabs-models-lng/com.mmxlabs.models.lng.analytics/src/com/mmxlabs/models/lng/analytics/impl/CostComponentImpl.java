/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
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

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.FuelCost;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cost Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CostComponentImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CostComponentImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CostComponentImpl#getFuelCosts <em>Fuel Costs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CostComponentImpl extends EObjectImpl implements CostComponent {
	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected int hireCost = HIRE_COST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFuelCosts() <em>Fuel Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelCost> fuelCosts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CostComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.COST_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.COST_COMPONENT__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHireCost(int newHireCost) {
		int oldHireCost = hireCost;
		hireCost = newHireCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.COST_COMPONENT__HIRE_COST, oldHireCost, hireCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelCost> getFuelCosts() {
		if (fuelCosts == null) {
			fuelCosts = new EObjectContainmentEList<FuelCost>(FuelCost.class, this, AnalyticsPackage.COST_COMPONENT__FUEL_COSTS);
		}
		return fuelCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFuelCost() {
		int accumulator = 0;
		for (final FuelCost fc : getFuelCosts()) {
			accumulator += fc.getCost();
		}
		return accumulator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.COST_COMPONENT__FUEL_COSTS:
				return ((InternalEList<?>)getFuelCosts()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.COST_COMPONENT__DURATION:
				return getDuration();
			case AnalyticsPackage.COST_COMPONENT__HIRE_COST:
				return getHireCost();
			case AnalyticsPackage.COST_COMPONENT__FUEL_COSTS:
				return getFuelCosts();
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
			case AnalyticsPackage.COST_COMPONENT__DURATION:
				setDuration((Integer)newValue);
				return;
			case AnalyticsPackage.COST_COMPONENT__HIRE_COST:
				setHireCost((Integer)newValue);
				return;
			case AnalyticsPackage.COST_COMPONENT__FUEL_COSTS:
				getFuelCosts().clear();
				getFuelCosts().addAll((Collection<? extends FuelCost>)newValue);
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
			case AnalyticsPackage.COST_COMPONENT__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case AnalyticsPackage.COST_COMPONENT__HIRE_COST:
				setHireCost(HIRE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.COST_COMPONENT__FUEL_COSTS:
				getFuelCosts().clear();
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
			case AnalyticsPackage.COST_COMPONENT__DURATION:
				return duration != DURATION_EDEFAULT;
			case AnalyticsPackage.COST_COMPONENT__HIRE_COST:
				return hireCost != HIRE_COST_EDEFAULT;
			case AnalyticsPackage.COST_COMPONENT__FUEL_COSTS:
				return fuelCosts != null && !fuelCosts.isEmpty();
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
		result.append(" (duration: ");
		result.append(duration);
		result.append(", hireCost: ");
		result.append(hireCost);
		result.append(')');
		return result.toString();
	}

} // end of CostComponentImpl

// finish type fixing
