/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getViolations <em>Violations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getLateness <em>Lateness</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getHeelCost <em>Heel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getHeelRevenue <em>Heel Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getHeelCostUnitPrice <em>Heel Cost Unit Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl#getHeelRevenueUnitPrice <em>Heel Revenue Unit Price</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortVisitImpl extends EventImpl implements PortVisit {
	/**
	 * The cached value of the '{@link #getViolations() <em>Violations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViolations()
	 * @generated
	 * @ordered
	 */
	protected EMap<CapacityViolationType, Long> violations;

	/**
	 * The default value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected int portCost = PORT_COST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLateness() <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLateness()
	 * @generated
	 * @ordered
	 */
	protected PortVisitLateness lateness;

	/**
	 * The default value of the '{@link #getHeelCost() <em>Heel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelCost() <em>Heel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCost()
	 * @generated
	 * @ordered
	 */
	protected int heelCost = HEEL_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelRevenue() <em>Heel Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_REVENUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelRevenue() <em>Heel Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenue()
	 * @generated
	 * @ordered
	 */
	protected int heelRevenue = HEEL_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelCostUnitPrice() <em>Heel Cost Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCostUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double HEEL_COST_UNIT_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getHeelCostUnitPrice() <em>Heel Cost Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCostUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected double heelCostUnitPrice = HEEL_COST_UNIT_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelRevenueUnitPrice() <em>Heel Revenue Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenueUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double HEEL_REVENUE_UNIT_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getHeelRevenueUnitPrice() <em>Heel Revenue Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenueUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected double heelRevenueUnitPrice = HEEL_REVENUE_UNIT_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.PORT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<CapacityViolationType, Long> getViolations() {
		if (violations == null) {
			violations = new EcoreEMap<CapacityViolationType,Long>(SchedulePackage.Literals.CAPACITY_MAP_ENTRY, CapacityMapEntryImpl.class, this, SchedulePackage.PORT_VISIT__VIOLATIONS);
		}
		return violations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortCost() {
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortCost(int newPortCost) {
		int oldPortCost = portCost;
		portCost = newPortCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__PORT_COST, oldPortCost, portCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortVisitLateness getLateness() {
		return lateness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLateness(PortVisitLateness newLateness, NotificationChain msgs) {
		PortVisitLateness oldLateness = lateness;
		lateness = newLateness;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__LATENESS, oldLateness, newLateness);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLateness(PortVisitLateness newLateness) {
		if (newLateness != lateness) {
			NotificationChain msgs = null;
			if (lateness != null)
				msgs = ((InternalEObject)lateness).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PORT_VISIT__LATENESS, null, msgs);
			if (newLateness != null)
				msgs = ((InternalEObject)newLateness).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PORT_VISIT__LATENESS, null, msgs);
			msgs = basicSetLateness(newLateness, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__LATENESS, newLateness, newLateness));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHeelCost() {
		return heelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHeelCost(int newHeelCost) {
		int oldHeelCost = heelCost;
		heelCost = newHeelCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__HEEL_COST, oldHeelCost, heelCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHeelRevenue() {
		return heelRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHeelRevenue(int newHeelRevenue) {
		int oldHeelRevenue = heelRevenue;
		heelRevenue = newHeelRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__HEEL_REVENUE, oldHeelRevenue, heelRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getHeelCostUnitPrice() {
		return heelCostUnitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHeelCostUnitPrice(double newHeelCostUnitPrice) {
		double oldHeelCostUnitPrice = heelCostUnitPrice;
		heelCostUnitPrice = newHeelCostUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__HEEL_COST_UNIT_PRICE, oldHeelCostUnitPrice, heelCostUnitPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getHeelRevenueUnitPrice() {
		return heelRevenueUnitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHeelRevenueUnitPrice(double newHeelRevenueUnitPrice) {
		double oldHeelRevenueUnitPrice = heelRevenueUnitPrice;
		heelRevenueUnitPrice = newHeelRevenueUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE, oldHeelRevenueUnitPrice, heelRevenueUnitPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.PORT_VISIT__VIOLATIONS:
				return ((InternalEList<?>)getViolations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.PORT_VISIT__LATENESS:
				return basicSetLateness(null, msgs);
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
			case SchedulePackage.PORT_VISIT__VIOLATIONS:
				if (coreType) return getViolations();
				else return getViolations().map();
			case SchedulePackage.PORT_VISIT__PORT_COST:
				return getPortCost();
			case SchedulePackage.PORT_VISIT__LATENESS:
				return getLateness();
			case SchedulePackage.PORT_VISIT__HEEL_COST:
				return getHeelCost();
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE:
				return getHeelRevenue();
			case SchedulePackage.PORT_VISIT__HEEL_COST_UNIT_PRICE:
				return getHeelCostUnitPrice();
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE:
				return getHeelRevenueUnitPrice();
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
			case SchedulePackage.PORT_VISIT__VIOLATIONS:
				((EStructuralFeature.Setting)getViolations()).set(newValue);
				return;
			case SchedulePackage.PORT_VISIT__PORT_COST:
				setPortCost((Integer)newValue);
				return;
			case SchedulePackage.PORT_VISIT__LATENESS:
				setLateness((PortVisitLateness)newValue);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_COST:
				setHeelCost((Integer)newValue);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE:
				setHeelRevenue((Integer)newValue);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_COST_UNIT_PRICE:
				setHeelCostUnitPrice((Double)newValue);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE:
				setHeelRevenueUnitPrice((Double)newValue);
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
			case SchedulePackage.PORT_VISIT__VIOLATIONS:
				getViolations().clear();
				return;
			case SchedulePackage.PORT_VISIT__PORT_COST:
				setPortCost(PORT_COST_EDEFAULT);
				return;
			case SchedulePackage.PORT_VISIT__LATENESS:
				setLateness((PortVisitLateness)null);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_COST:
				setHeelCost(HEEL_COST_EDEFAULT);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE:
				setHeelRevenue(HEEL_REVENUE_EDEFAULT);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_COST_UNIT_PRICE:
				setHeelCostUnitPrice(HEEL_COST_UNIT_PRICE_EDEFAULT);
				return;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE:
				setHeelRevenueUnitPrice(HEEL_REVENUE_UNIT_PRICE_EDEFAULT);
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
			case SchedulePackage.PORT_VISIT__VIOLATIONS:
				return violations != null && !violations.isEmpty();
			case SchedulePackage.PORT_VISIT__PORT_COST:
				return portCost != PORT_COST_EDEFAULT;
			case SchedulePackage.PORT_VISIT__LATENESS:
				return lateness != null;
			case SchedulePackage.PORT_VISIT__HEEL_COST:
				return heelCost != HEEL_COST_EDEFAULT;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE:
				return heelRevenue != HEEL_REVENUE_EDEFAULT;
			case SchedulePackage.PORT_VISIT__HEEL_COST_UNIT_PRICE:
				return heelCostUnitPrice != HEEL_COST_UNIT_PRICE_EDEFAULT;
			case SchedulePackage.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE:
				return heelRevenueUnitPrice != HEEL_REVENUE_UNIT_PRICE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == CapacityViolationsHolder.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.PORT_VISIT__VIOLATIONS: return SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == CapacityViolationsHolder.class) {
			switch (baseFeatureID) {
				case SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS: return SchedulePackage.PORT_VISIT__VIOLATIONS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (portCost: ");
		result.append(portCost);
		result.append(", heelCost: ");
		result.append(heelCost);
		result.append(", heelRevenue: ");
		result.append(heelRevenue);
		result.append(", heelCostUnitPrice: ");
		result.append(heelCostUnitPrice);
		result.append(", heelRevenueUnitPrice: ");
		result.append(heelRevenueUnitPrice);
		result.append(')');
		return result.toString();
	}

} // end of PortVisitImpl

// finish type fixing
