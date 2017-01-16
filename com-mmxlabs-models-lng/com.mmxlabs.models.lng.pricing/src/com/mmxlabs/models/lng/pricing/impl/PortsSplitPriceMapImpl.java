/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PortsSplitPriceMap;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ports Split Price Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl#getIndexH1 <em>Index H1</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortsSplitPriceMapImpl#getIndexH2 <em>Index H2</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortsSplitPriceMapImpl extends MMXObjectImpl implements PortsSplitPriceMap {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> ports;

	/**
	 * The cached value of the '{@link #getIndexH1() <em>Index H1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexH1()
	 * @generated
	 * @ordered
	 */
	protected CommodityIndex indexH1;

	/**
	 * The cached value of the '{@link #getIndexH2() <em>Index H2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndexH2()
	 * @generated
	 * @ordered
	 */
	protected CommodityIndex indexH2;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortsSplitPriceMapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PORTS_SPLIT_PRICE_MAP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, PricingPackage.PORTS_SPLIT_PRICE_MAP__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex getIndexH1() {
		if (indexH1 != null && indexH1.eIsProxy()) {
			InternalEObject oldIndexH1 = (InternalEObject)indexH1;
			indexH1 = (CommodityIndex)eResolveProxy(oldIndexH1);
			if (indexH1 != oldIndexH1) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1, oldIndexH1, indexH1));
			}
		}
		return indexH1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex basicGetIndexH1() {
		return indexH1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndexH1(CommodityIndex newIndexH1) {
		CommodityIndex oldIndexH1 = indexH1;
		indexH1 = newIndexH1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1, oldIndexH1, indexH1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex getIndexH2() {
		if (indexH2 != null && indexH2.eIsProxy()) {
			InternalEObject oldIndexH2 = (InternalEObject)indexH2;
			indexH2 = (CommodityIndex)eResolveProxy(oldIndexH2);
			if (indexH2 != oldIndexH2) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2, oldIndexH2, indexH2));
			}
		}
		return indexH2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex basicGetIndexH2() {
		return indexH2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndexH2(CommodityIndex newIndexH2) {
		CommodityIndex oldIndexH2 = indexH2;
		indexH2 = newIndexH2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2, oldIndexH2, indexH2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__PORTS:
				return getPorts();
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1:
				if (resolve) return getIndexH1();
				return basicGetIndexH1();
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2:
				if (resolve) return getIndexH2();
				return basicGetIndexH2();
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
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1:
				setIndexH1((CommodityIndex)newValue);
				return;
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2:
				setIndexH2((CommodityIndex)newValue);
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
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__PORTS:
				getPorts().clear();
				return;
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1:
				setIndexH1((CommodityIndex)null);
				return;
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2:
				setIndexH2((CommodityIndex)null);
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
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__PORTS:
				return ports != null && !ports.isEmpty();
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H1:
				return indexH1 != null;
			case PricingPackage.PORTS_SPLIT_PRICE_MAP__INDEX_H2:
				return indexH2 != null;
		}
		return super.eIsSet(featureID);
	}

} //PortsSplitPriceMapImpl
