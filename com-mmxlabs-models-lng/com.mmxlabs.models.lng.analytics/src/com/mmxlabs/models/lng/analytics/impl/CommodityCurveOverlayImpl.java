/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Commodity Curve Overlay</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl#getReferenceCurve <em>Reference Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CommodityCurveOverlayImpl#getAlternativeCurves <em>Alternative Curves</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommodityCurveOverlayImpl extends UUIDObjectImpl implements CommodityCurveOverlay {
	/**
	 * The cached value of the '{@link #getReferenceCurve() <em>Reference Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceCurve()
	 * @generated
	 * @ordered
	 */
	protected CommodityCurve referenceCurve;

	/**
	 * The cached value of the '{@link #getAlternativeCurves() <em>Alternative Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlternativeCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<YearMonthPointContainer> alternativeCurves;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommodityCurveOverlayImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommodityCurve getReferenceCurve() {
		if (referenceCurve != null && referenceCurve.eIsProxy()) {
			InternalEObject oldReferenceCurve = (InternalEObject)referenceCurve;
			referenceCurve = (CommodityCurve)eResolveProxy(oldReferenceCurve);
			if (referenceCurve != oldReferenceCurve) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE, oldReferenceCurve, referenceCurve));
			}
		}
		return referenceCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityCurve basicGetReferenceCurve() {
		return referenceCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferenceCurve(CommodityCurve newReferenceCurve) {
		CommodityCurve oldReferenceCurve = referenceCurve;
		referenceCurve = newReferenceCurve;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE, oldReferenceCurve, referenceCurve));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<YearMonthPointContainer> getAlternativeCurves() {
		if (alternativeCurves == null) {
			alternativeCurves = new EObjectContainmentEList<YearMonthPointContainer>(YearMonthPointContainer.class, this, AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES);
		}
		return alternativeCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				return ((InternalEList<?>)getAlternativeCurves()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE:
				if (resolve) return getReferenceCurve();
				return basicGetReferenceCurve();
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				return getAlternativeCurves();
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
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE:
				setReferenceCurve((CommodityCurve)newValue);
				return;
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				getAlternativeCurves().clear();
				getAlternativeCurves().addAll((Collection<? extends YearMonthPointContainer>)newValue);
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
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE:
				setReferenceCurve((CommodityCurve)null);
				return;
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				getAlternativeCurves().clear();
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
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE:
				return referenceCurve != null;
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				return alternativeCurves != null && !alternativeCurves.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CommodityCurveOverlayImpl
