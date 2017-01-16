/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Legal Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl#getShippingBook <em>Shipping Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl#getTradingBook <em>Trading Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl#getUpstreamBook <em>Upstream Book</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class BaseLegalEntityImpl extends UUIDObjectImpl implements BaseLegalEntity {
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
	 * The cached value of the '{@link #getShippingBook() <em>Shipping Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingBook()
	 * @generated
	 * @ordered
	 */
	protected BaseEntityBook shippingBook;

	/**
	 * The cached value of the '{@link #getTradingBook() <em>Trading Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTradingBook()
	 * @generated
	 * @ordered
	 */
	protected BaseEntityBook tradingBook;

	/**
	 * The cached value of the '{@link #getUpstreamBook() <em>Upstream Book</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpstreamBook()
	 * @generated
	 * @ordered
	 */
	protected BaseEntityBook upstreamBook;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseLegalEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.BASE_LEGAL_ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseEntityBook getShippingBook() {
		return shippingBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShippingBook(BaseEntityBook newShippingBook, NotificationChain msgs) {
		BaseEntityBook oldShippingBook = shippingBook;
		shippingBook = newShippingBook;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK, oldShippingBook, newShippingBook);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShippingBook(BaseEntityBook newShippingBook) {
		if (newShippingBook != shippingBook) {
			NotificationChain msgs = null;
			if (shippingBook != null)
				msgs = ((InternalEObject)shippingBook).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK, null, msgs);
			if (newShippingBook != null)
				msgs = ((InternalEObject)newShippingBook).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK, null, msgs);
			msgs = basicSetShippingBook(newShippingBook, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK, newShippingBook, newShippingBook));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseEntityBook getTradingBook() {
		return tradingBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTradingBook(BaseEntityBook newTradingBook, NotificationChain msgs) {
		BaseEntityBook oldTradingBook = tradingBook;
		tradingBook = newTradingBook;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK, oldTradingBook, newTradingBook);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTradingBook(BaseEntityBook newTradingBook) {
		if (newTradingBook != tradingBook) {
			NotificationChain msgs = null;
			if (tradingBook != null)
				msgs = ((InternalEObject)tradingBook).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK, null, msgs);
			if (newTradingBook != null)
				msgs = ((InternalEObject)newTradingBook).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK, null, msgs);
			msgs = basicSetTradingBook(newTradingBook, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK, newTradingBook, newTradingBook));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseEntityBook getUpstreamBook() {
		return upstreamBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUpstreamBook(BaseEntityBook newUpstreamBook, NotificationChain msgs) {
		BaseEntityBook oldUpstreamBook = upstreamBook;
		upstreamBook = newUpstreamBook;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK, oldUpstreamBook, newUpstreamBook);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpstreamBook(BaseEntityBook newUpstreamBook) {
		if (newUpstreamBook != upstreamBook) {
			NotificationChain msgs = null;
			if (upstreamBook != null)
				msgs = ((InternalEObject)upstreamBook).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK, null, msgs);
			if (newUpstreamBook != null)
				msgs = ((InternalEObject)newUpstreamBook).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK, null, msgs);
			msgs = basicSetUpstreamBook(newUpstreamBook, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK, newUpstreamBook, newUpstreamBook));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK:
				return basicSetShippingBook(null, msgs);
			case CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK:
				return basicSetTradingBook(null, msgs);
			case CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK:
				return basicSetUpstreamBook(null, msgs);
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
			case CommercialPackage.BASE_LEGAL_ENTITY__NAME:
				return getName();
			case CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK:
				return getShippingBook();
			case CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK:
				return getTradingBook();
			case CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK:
				return getUpstreamBook();
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
			case CommercialPackage.BASE_LEGAL_ENTITY__NAME:
				setName((String)newValue);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK:
				setShippingBook((BaseEntityBook)newValue);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK:
				setTradingBook((BaseEntityBook)newValue);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK:
				setUpstreamBook((BaseEntityBook)newValue);
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
			case CommercialPackage.BASE_LEGAL_ENTITY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK:
				setShippingBook((BaseEntityBook)null);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK:
				setTradingBook((BaseEntityBook)null);
				return;
			case CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK:
				setUpstreamBook((BaseEntityBook)null);
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
			case CommercialPackage.BASE_LEGAL_ENTITY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CommercialPackage.BASE_LEGAL_ENTITY__SHIPPING_BOOK:
				return shippingBook != null;
			case CommercialPackage.BASE_LEGAL_ENTITY__TRADING_BOOK:
				return tradingBook != null;
			case CommercialPackage.BASE_LEGAL_ENTITY__UPSTREAM_BOOK:
				return upstreamBook != null;
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
				case CommercialPackage.BASE_LEGAL_ENTITY__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
				case MMXCorePackage.NAMED_OBJECT__NAME: return CommercialPackage.BASE_LEGAL_ENTITY__NAME;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //BaseLegalEntityImpl
