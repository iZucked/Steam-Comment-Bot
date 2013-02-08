

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.UnitCostLine;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Provisional Cargo</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl#getBuy <em>Buy</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl#getSell <em>Sell</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl#getCostLine <em>Cost Line</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ProvisionalCargoImpl#getVessel <em>Vessel</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProvisionalCargoImpl extends MMXObjectImpl implements ProvisionalCargo {
	/**
	 * The cached value of the '{@link #getBuy() <em>Buy</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuy()
	 * @generated
	 * @ordered
	 */
	protected BuyOpportunity buy;

	/**
	 * The cached value of the '{@link #getSell() <em>Sell</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSell()
	 * @generated
	 * @ordered
	 */
	protected SellOpportunity sell;

	/**
	 * The cached value of the '{@link #getCostLine() <em>Cost Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCostLine()
	 * @generated
	 * @ordered
	 */
	protected UnitCostLine costLine;

	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProvisionalCargoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.PROVISIONAL_CARGO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuyOpportunity getBuy() {
		return buy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBuy(BuyOpportunity newBuy, NotificationChain msgs) {
		BuyOpportunity oldBuy = buy;
		buy = newBuy;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__BUY, oldBuy, newBuy);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBuy(BuyOpportunity newBuy) {
		if (newBuy != buy) {
			NotificationChain msgs = null;
			if (buy != null)
				msgs = ((InternalEObject)buy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__BUY, null, msgs);
			if (newBuy != null)
				msgs = ((InternalEObject)newBuy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__BUY, null, msgs);
			msgs = basicSetBuy(newBuy, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__BUY, newBuy, newBuy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellOpportunity getSell() {
		return sell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSell(SellOpportunity newSell, NotificationChain msgs) {
		SellOpportunity oldSell = sell;
		sell = newSell;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__SELL, oldSell, newSell);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSell(SellOpportunity newSell) {
		if (newSell != sell) {
			NotificationChain msgs = null;
			if (sell != null)
				msgs = ((InternalEObject)sell).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__SELL, null, msgs);
			if (newSell != null)
				msgs = ((InternalEObject)newSell).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__SELL, null, msgs);
			msgs = basicSetSell(newSell, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__SELL, newSell, newSell));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitCostLine getCostLine() {
		return costLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCostLine(UnitCostLine newCostLine, NotificationChain msgs) {
		UnitCostLine oldCostLine = costLine;
		costLine = newCostLine;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE, oldCostLine, newCostLine);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCostLine(UnitCostLine newCostLine) {
		if (newCostLine != costLine) {
			NotificationChain msgs = null;
			if (costLine != null)
				msgs = ((InternalEObject)costLine).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE, null, msgs);
			if (newCostLine != null)
				msgs = ((InternalEObject)newCostLine).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE, null, msgs);
			msgs = basicSetCostLine(newCostLine, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE, newCostLine, newCostLine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.PROVISIONAL_CARGO__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PROVISIONAL_CARGO__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
				return basicSetBuy(null, msgs);
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
				return basicSetSell(null, msgs);
			case AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE:
				return basicSetCostLine(null, msgs);
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
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
				return getBuy();
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
				return getSell();
			case AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE:
				return getCostLine();
			case AnalyticsPackage.PROVISIONAL_CARGO__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
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
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
				setBuy((BuyOpportunity)newValue);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
				setSell((SellOpportunity)newValue);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE:
				setCostLine((UnitCostLine)newValue);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__VESSEL:
				setVessel((Vessel)newValue);
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
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
				setBuy((BuyOpportunity)null);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
				setSell((SellOpportunity)null);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE:
				setCostLine((UnitCostLine)null);
				return;
			case AnalyticsPackage.PROVISIONAL_CARGO__VESSEL:
				setVessel((Vessel)null);
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
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
				return buy != null;
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
				return sell != null;
			case AnalyticsPackage.PROVISIONAL_CARGO__COST_LINE:
				return costLine != null;
			case AnalyticsPackage.PROVISIONAL_CARGO__VESSEL:
				return vessel != null;
		}
		return super.eIsSet(featureID);
	}

} // end of ProvisionalCargoImpl

// finish type fixing
