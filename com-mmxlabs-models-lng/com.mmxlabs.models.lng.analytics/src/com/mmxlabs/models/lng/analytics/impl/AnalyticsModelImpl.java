/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getRoundTripMatrices <em>Round Trip Matrices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getSelectedMatrix <em>Selected Matrix</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getShippingCostPlans <em>Shipping Cost Plans</em>}</li>
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
	 * The cached value of the '{@link #getSelectedMatrix() <em>Selected Matrix</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedMatrix()
	 * @generated
	 * @ordered
	 */
	protected UnitCostMatrix selectedMatrix;

	/**
	 * The cached value of the '{@link #getShippingCostPlans() <em>Shipping Cost Plans</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getShippingCostPlans()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingCostPlan> shippingCostPlans;

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
	public UnitCostMatrix getSelectedMatrix() {
		if (selectedMatrix != null && selectedMatrix.eIsProxy()) {
			InternalEObject oldSelectedMatrix = (InternalEObject)selectedMatrix;
			selectedMatrix = (UnitCostMatrix)eResolveProxy(oldSelectedMatrix);
			if (selectedMatrix != oldSelectedMatrix) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX, oldSelectedMatrix, selectedMatrix));
			}
		}
		return selectedMatrix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitCostMatrix basicGetSelectedMatrix() {
		return selectedMatrix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectedMatrix(UnitCostMatrix newSelectedMatrix) {
		UnitCostMatrix oldSelectedMatrix = selectedMatrix;
		selectedMatrix = newSelectedMatrix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX, oldSelectedMatrix, selectedMatrix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ShippingCostPlan> getShippingCostPlans() {
		if (shippingCostPlans == null) {
			shippingCostPlans = new EObjectContainmentEList<ShippingCostPlan>(ShippingCostPlan.class, this, AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS);
		}
		return shippingCostPlans;
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
			case AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS:
				return ((InternalEList<?>)getShippingCostPlans()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX:
				if (resolve) return getSelectedMatrix();
				return basicGetSelectedMatrix();
			case AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS:
				return getShippingCostPlans();
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
			case AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX:
				setSelectedMatrix((UnitCostMatrix)newValue);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS:
				getShippingCostPlans().clear();
				getShippingCostPlans().addAll((Collection<? extends ShippingCostPlan>)newValue);
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
			case AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX:
				setSelectedMatrix((UnitCostMatrix)null);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS:
				getShippingCostPlans().clear();
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
			case AnalyticsPackage.ANALYTICS_MODEL__SELECTED_MATRIX:
				return selectedMatrix != null;
			case AnalyticsPackage.ANALYTICS_MODEL__SHIPPING_COST_PLANS:
				return shippingCostPlans != null && !shippingCostPlans.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of AnalyticsModelImpl

// finish type fixing
