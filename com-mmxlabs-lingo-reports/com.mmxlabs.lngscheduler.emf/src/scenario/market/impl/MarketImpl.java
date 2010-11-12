/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.market.ForwardPrice;
import scenario.market.Market;
import scenario.market.MarketPackage;
import scenario.market.StepwisePriceCurve;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.market.impl.MarketImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.market.impl.MarketImpl#getPriceCurve <em>Price Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarketImpl extends EObjectImpl implements Market {
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
	 * The cached value of the '{@link #getPriceCurve() <em>Price Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceCurve()
	 * @generated
	 * @ordered
	 */
	protected StepwisePriceCurve priceCurve;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MarketPackage.Literals.MARKET;
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
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.MARKET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StepwisePriceCurve getPriceCurve() {
		return priceCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPriceCurve(StepwisePriceCurve newPriceCurve, NotificationChain msgs) {
		StepwisePriceCurve oldPriceCurve = priceCurve;
		priceCurve = newPriceCurve;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MarketPackage.MARKET__PRICE_CURVE, oldPriceCurve, newPriceCurve);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceCurve(StepwisePriceCurve newPriceCurve) {
		if (newPriceCurve != priceCurve) {
			NotificationChain msgs = null;
			if (priceCurve != null)
				msgs = ((InternalEObject)priceCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MarketPackage.MARKET__PRICE_CURVE, null, msgs);
			if (newPriceCurve != null)
				msgs = ((InternalEObject)newPriceCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MarketPackage.MARKET__PRICE_CURVE, null, msgs);
			msgs = basicSetPriceCurve(newPriceCurve, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.MARKET__PRICE_CURVE, newPriceCurve, newPriceCurve));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MarketPackage.MARKET__PRICE_CURVE:
				return basicSetPriceCurve(null, msgs);
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
			case MarketPackage.MARKET__NAME:
				return getName();
			case MarketPackage.MARKET__PRICE_CURVE:
				return getPriceCurve();
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
			case MarketPackage.MARKET__NAME:
				setName((String)newValue);
				return;
			case MarketPackage.MARKET__PRICE_CURVE:
				setPriceCurve((StepwisePriceCurve)newValue);
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
			case MarketPackage.MARKET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MarketPackage.MARKET__PRICE_CURVE:
				setPriceCurve((StepwisePriceCurve)null);
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
			case MarketPackage.MARKET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MarketPackage.MARKET__PRICE_CURVE:
				return priceCurve != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //MarketImpl
