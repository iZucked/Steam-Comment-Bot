/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PeriodDistribution;

import java.time.YearMonth;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Period Distribution</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl#getMinCargoes <em>Min Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl#getMaxCargoes <em>Max Cargoes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PeriodDistributionImpl extends EObjectImpl implements PeriodDistribution {
	/**
	 * The cached value of the '{@link #getRange() <em>Range</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRange()
	 * @generated
	 * @ordered
	 */
	protected EList<YearMonth> range;

	/**
	 * The default value of the '{@link #getMinCargoes() <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinCargoes() <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCargoes()
	 * @generated
	 * @ordered
	 */
	protected int minCargoes = MIN_CARGOES_EDEFAULT;

	/**
	 * This is true if the Min Cargoes attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minCargoesESet;

	/**
	 * The default value of the '{@link #getMaxCargoes() <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxCargoes() <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCargoes()
	 * @generated
	 * @ordered
	 */
	protected int maxCargoes = MAX_CARGOES_EDEFAULT;

	/**
	 * This is true if the Max Cargoes attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxCargoesESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PeriodDistributionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.PERIOD_DISTRIBUTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<YearMonth> getRange() {
		if (range == null) {
			range = new EDataTypeUniqueEList<YearMonth>(YearMonth.class, this, ADPPackage.PERIOD_DISTRIBUTION__RANGE);
		}
		return range;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinCargoes() {
		return minCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCargoes(int newMinCargoes) {
		int oldMinCargoes = minCargoes;
		minCargoes = newMinCargoes;
		boolean oldMinCargoesESet = minCargoesESet;
		minCargoesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES, oldMinCargoes, minCargoes, !oldMinCargoesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinCargoes() {
		int oldMinCargoes = minCargoes;
		boolean oldMinCargoesESet = minCargoesESet;
		minCargoes = MIN_CARGOES_EDEFAULT;
		minCargoesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES, oldMinCargoes, MIN_CARGOES_EDEFAULT, oldMinCargoesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinCargoes() {
		return minCargoesESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxCargoes() {
		return maxCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxCargoes(int newMaxCargoes) {
		int oldMaxCargoes = maxCargoes;
		maxCargoes = newMaxCargoes;
		boolean oldMaxCargoesESet = maxCargoesESet;
		maxCargoesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES, oldMaxCargoes, maxCargoes, !oldMaxCargoesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMaxCargoes() {
		int oldMaxCargoes = maxCargoes;
		boolean oldMaxCargoesESet = maxCargoesESet;
		maxCargoes = MAX_CARGOES_EDEFAULT;
		maxCargoesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES, oldMaxCargoes, MAX_CARGOES_EDEFAULT, oldMaxCargoesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMaxCargoes() {
		return maxCargoesESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.PERIOD_DISTRIBUTION__RANGE:
				return getRange();
			case ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES:
				return getMinCargoes();
			case ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES:
				return getMaxCargoes();
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
			case ADPPackage.PERIOD_DISTRIBUTION__RANGE:
				getRange().clear();
				getRange().addAll((Collection<? extends YearMonth>)newValue);
				return;
			case ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES:
				setMinCargoes((Integer)newValue);
				return;
			case ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES:
				setMaxCargoes((Integer)newValue);
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
			case ADPPackage.PERIOD_DISTRIBUTION__RANGE:
				getRange().clear();
				return;
			case ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES:
				unsetMinCargoes();
				return;
			case ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES:
				unsetMaxCargoes();
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
			case ADPPackage.PERIOD_DISTRIBUTION__RANGE:
				return range != null && !range.isEmpty();
			case ADPPackage.PERIOD_DISTRIBUTION__MIN_CARGOES:
				return isSetMinCargoes();
			case ADPPackage.PERIOD_DISTRIBUTION__MAX_CARGOES:
				return isSetMaxCargoes();
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
		result.append(" (range: ");
		result.append(range);
		result.append(", minCargoes: ");
		if (minCargoesESet) result.append(minCargoes); else result.append("<unset>");
		result.append(", maxCargoes: ");
		if (maxCargoesESet) result.append(maxCargoes); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //PeriodDistributionImpl
