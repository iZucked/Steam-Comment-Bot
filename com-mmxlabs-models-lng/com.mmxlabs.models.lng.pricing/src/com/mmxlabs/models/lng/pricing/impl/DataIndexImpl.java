/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Index</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.DataIndexImpl#getPoints <em>Points</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataIndexImpl<Value> extends IndexImpl<Value> implements DataIndex<Value> {
	/**
	 * The cached value of the '{@link #getPoints() <em>Points</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPoints()
	 * @generated
	 * @ordered
	 */
	protected EList<IndexPoint<Value>> points;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataIndexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.DATA_INDEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IndexPoint<Value>> getPoints() {
		if (points == null) {
			points = new EObjectContainmentEList<IndexPoint<Value>>(IndexPoint.class, this, PricingPackage.DATA_INDEX__POINTS);
		}
		return points;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.DATA_INDEX__POINTS:
				return ((InternalEList<?>)getPoints()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.DATA_INDEX__POINTS:
				return getPoints();
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
			case PricingPackage.DATA_INDEX__POINTS:
				getPoints().clear();
				getPoints().addAll((Collection<? extends IndexPoint<Value>>)newValue);
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
			case PricingPackage.DATA_INDEX__POINTS:
				getPoints().clear();
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
			case PricingPackage.DATA_INDEX__POINTS:
				return points != null && !points.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	private List<IndexPoint<Value>> sortedPoints = null;
	
	private List<IndexPoint<Value>> getSortedPoints() {
		if (sortedPoints == null) {
			sortedPoints = new ArrayList<IndexPoint<Value>>(getPoints());
			Collections.sort(sortedPoints, new Comparator<IndexPoint<Value>>() {
				@Override
				public int compare(IndexPoint<Value> arg0,
						IndexPoint<Value> arg1) {
					return arg0.getDate().compareTo(arg1.getDate());
				}
			});
		}
		return sortedPoints;
	}
	
	@Override
	public Value getValueAfter(final Date date) {
		Value lastValue = null;
		for (final IndexPoint<Value> point : getSortedPoints()) {
			if (!point.getDate().after(date)) lastValue = point.getValue();
			else return lastValue;
		}
		return lastValue;
	}

	@Override
	public EList<Date> getDates() {
		final EList<Date> result = new BasicEList<Date>();
		for (final IndexPoint<Value> s : getSortedPoints())
			result.add(s.getDate());
		return result;
	}
} // end of DataIndexImpl

// finish type fixing
