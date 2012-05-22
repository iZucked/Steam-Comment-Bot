

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getRoundTripMatrices <em>Round Trip Matrices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnalyticsModelImpl extends UUIDObjectImpl implements AnalyticsModel {
	/**
	 * The cached value of the '{@link #getRoundTripMatrices() <em>Round Trip Matrices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoundTripMatrices()
	 * @generated
	 * @ordered
	 */
	protected EList<UnitCostMatrix> roundTripMatrices;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalyticsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ANALYTICS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnitCostMatrix> getRoundTripMatrices() {
		if (roundTripMatrices == null) {
			roundTripMatrices = new EObjectContainmentEList<UnitCostMatrix>(UnitCostMatrix.class, this, AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES);
		}
		return roundTripMatrices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES:
				return ((InternalEList<?>)getRoundTripMatrices()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES:
				return getRoundTripMatrices();
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
			case AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES:
				getRoundTripMatrices().clear();
				getRoundTripMatrices().addAll((Collection<? extends UnitCostMatrix>)newValue);
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
			case AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES:
				getRoundTripMatrices().clear();
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
			case AnalyticsPackage.ANALYTICS_MODEL__ROUND_TRIP_MATRICES:
				return roundTripMatrices != null && !roundTripMatrices.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of AnalyticsModelImpl

// finish type fixing
