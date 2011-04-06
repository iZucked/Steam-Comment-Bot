/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.contract.ContractPackage;
import scenario.contract.SalesContract;
import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sales Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.SalesContractImpl#getMarket <em>Market</em>}</li>
 *   <li>{@link scenario.contract.impl.SalesContractImpl#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 *   <li>{@link scenario.contract.impl.SalesContractImpl#getMarkup <em>Markup</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SalesContractImpl extends ContractImpl implements SalesContract {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected Market market;
	/**
	 * The default value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected static final float REGAS_EFFICIENCY_EDEFAULT = 0.0F;
	/**
	 * The cached value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected float regasEfficiency = REGAS_EFFICIENCY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarkup() <em>Markup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkup()
	 * @generated
	 * @ordered
	 */
	protected static final float MARKUP_EDEFAULT = 1.05F;
	/**
	 * The cached value of the '{@link #getMarkup() <em>Markup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkup()
	 * @generated
	 * @ordered
	 */
	protected float markup = MARKUP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.SALES_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Market getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (Market)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.SALES_CONTRACT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarket(Market newMarket) {
		Market oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.SALES_CONTRACT__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getRegasEfficiency() {
		return regasEfficiency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRegasEfficiency(float newRegasEfficiency) {
		float oldRegasEfficiency = regasEfficiency;
		regasEfficiency = newRegasEfficiency;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.SALES_CONTRACT__REGAS_EFFICIENCY, oldRegasEfficiency, regasEfficiency));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getMarkup() {
		return markup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarkup(float newMarkup) {
		float oldMarkup = markup;
		markup = newMarkup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.SALES_CONTRACT__MARKUP, oldMarkup, markup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.SALES_CONTRACT__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
			case ContractPackage.SALES_CONTRACT__REGAS_EFFICIENCY:
				return getRegasEfficiency();
			case ContractPackage.SALES_CONTRACT__MARKUP:
				return getMarkup();
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
			case ContractPackage.SALES_CONTRACT__MARKET:
				setMarket((Market)newValue);
				return;
			case ContractPackage.SALES_CONTRACT__REGAS_EFFICIENCY:
				setRegasEfficiency((Float)newValue);
				return;
			case ContractPackage.SALES_CONTRACT__MARKUP:
				setMarkup((Float)newValue);
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
			case ContractPackage.SALES_CONTRACT__MARKET:
				setMarket((Market)null);
				return;
			case ContractPackage.SALES_CONTRACT__REGAS_EFFICIENCY:
				setRegasEfficiency(REGAS_EFFICIENCY_EDEFAULT);
				return;
			case ContractPackage.SALES_CONTRACT__MARKUP:
				setMarkup(MARKUP_EDEFAULT);
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
			case ContractPackage.SALES_CONTRACT__MARKET:
				return market != null;
			case ContractPackage.SALES_CONTRACT__REGAS_EFFICIENCY:
				return regasEfficiency != REGAS_EFFICIENCY_EDEFAULT;
			case ContractPackage.SALES_CONTRACT__MARKUP:
				return markup != MARKUP_EDEFAULT;
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
		result.append(" (regasEfficiency: ");
		result.append(regasEfficiency);
		result.append(", markup: ");
		result.append(markup);
		result.append(')');
		return result.toString();
	}

} //SalesContractImpl
