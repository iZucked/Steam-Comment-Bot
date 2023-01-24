/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CommodityCurveOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import com.mmxlabs.models.lng.analytics.VesselEventOption;
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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getBuys <em>Buys</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getSells <em>Sells</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getShippingTemplates <em>Shipping Templates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getCommodityCurves <em>Commodity Curves</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractAnalysisModelImpl extends UUIDObjectImpl implements AbstractAnalysisModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBuys() <em>Buys</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuys()
	 * @generated
	 * @ordered
	 */
	protected EList<BuyOption> buys;

	/**
	 * The cached value of the '{@link #getSells() <em>Sells</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSells()
	 * @generated
	 * @ordered
	 */
	protected EList<SellOption> sells;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEventOption> vesselEvents;

	/**
	 * The cached value of the '{@link #getShippingTemplates() <em>Shipping Templates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingTemplates()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingOption> shippingTemplates;

	/**
	 * The cached value of the '{@link #getCommodityCurves() <em>Commodity Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommodityCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<CommodityCurveOption> commodityCurves;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractAnalysisModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BuyOption> getBuys() {
		if (buys == null) {
			buys = new EObjectContainmentEList<BuyOption>(BuyOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS);
		}
		return buys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SellOption> getSells() {
		if (sells == null) {
			sells = new EObjectContainmentEList<SellOption>(SellOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS);
		}
		return sells;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselEventOption> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList<VesselEventOption>(VesselEventOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ShippingOption> getShippingTemplates() {
		if (shippingTemplates == null) {
			shippingTemplates = new EObjectContainmentEList<ShippingOption>(ShippingOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
		}
		return shippingTemplates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CommodityCurveOption> getCommodityCurves() {
		if (commodityCurves == null) {
			commodityCurves = new EObjectContainmentEList<CommodityCurveOption>(CommodityCurveOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES);
		}
		return commodityCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return ((InternalEList<?>)getBuys()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return ((InternalEList<?>)getSells()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return ((InternalEList<?>)getShippingTemplates()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES:
				return ((InternalEList<?>)getCommodityCurves()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME:
				return getName();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return getBuys();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return getSells();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return getShippingTemplates();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES:
				return getCommodityCurves();
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME:
				setName((String)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				getBuys().addAll((Collection<? extends BuyOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				getSells().addAll((Collection<? extends SellOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEventOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
				getShippingTemplates().addAll((Collection<? extends ShippingOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES:
				getCommodityCurves().clear();
				getCommodityCurves().addAll((Collection<? extends CommodityCurveOption>)newValue);
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES:
				getCommodityCurves().clear();
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return buys != null && !buys.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return sells != null && !sells.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return shippingTemplates != null && !shippingTemplates.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES:
				return commodityCurves != null && !commodityCurves.isEmpty();
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__NAME;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //AbstractAnalysisModelImpl
