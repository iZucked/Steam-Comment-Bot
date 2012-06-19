

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NetbackPurchaseContract;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.types.AIndex;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl#getMultiplier <em>Multiplier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NetbackPurchaseContractImpl extends PurchaseContractImpl implements NetbackPurchaseContract {
	/**
	 * The cached value of the '{@link #getNotionalBallastParameters() <em>Notional Ballast Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalBallastParameters()
	 * @generated
	 * @ordered
	 */
	protected NotionalBallastParameters notionalBallastParameters;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected AIndex index;

	/**
	 * The default value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected static final double CONSTANT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected double constant = CONSTANT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double MULTIPLIER_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double multiplier = MULTIPLIER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NetbackPurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.NETBACK_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotionalBallastParameters getNotionalBallastParameters() {
		return notionalBallastParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNotionalBallastParameters(NotionalBallastParameters newNotionalBallastParameters, NotificationChain msgs) {
		NotionalBallastParameters oldNotionalBallastParameters = notionalBallastParameters;
		notionalBallastParameters = newNotionalBallastParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS, oldNotionalBallastParameters, newNotionalBallastParameters);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotionalBallastParameters(NotionalBallastParameters newNotionalBallastParameters) {
		if (newNotionalBallastParameters != notionalBallastParameters) {
			NotificationChain msgs = null;
			if (notionalBallastParameters != null)
				msgs = ((InternalEObject)notionalBallastParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS, null, msgs);
			if (newNotionalBallastParameters != null)
				msgs = ((InternalEObject)newNotionalBallastParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS, null, msgs);
			msgs = basicSetNotionalBallastParameters(newNotionalBallastParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS, newNotionalBallastParameters, newNotionalBallastParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (AIndex)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AIndex basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(AIndex newIndex) {
		AIndex oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getConstant() {
		return constant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstant(double newConstant) {
		double oldConstant = constant;
		constant = newConstant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__CONSTANT, oldConstant, constant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplier(double newMultiplier) {
		double oldMultiplier = multiplier;
		multiplier = newMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NETBACK_PURCHASE_CONTRACT__MULTIPLIER, oldMultiplier, multiplier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return basicSetNotionalBallastParameters(null, msgs);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return getNotionalBallastParameters();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__CONSTANT:
				return getConstant();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MULTIPLIER:
				return getMultiplier();
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				setNotionalBallastParameters((NotionalBallastParameters)newValue);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX:
				setIndex((AIndex)newValue);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__CONSTANT:
				setConstant((Double)newValue);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MULTIPLIER:
				setMultiplier((Double)newValue);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				setNotionalBallastParameters((NotionalBallastParameters)null);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX:
				setIndex((AIndex)null);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__CONSTANT:
				setConstant(CONSTANT_EDEFAULT);
				return;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MULTIPLIER:
				setMultiplier(MULTIPLIER_EDEFAULT);
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
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS:
				return notionalBallastParameters != null;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__INDEX:
				return index != null;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__CONSTANT:
				return constant != CONSTANT_EDEFAULT;
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT__MULTIPLIER:
				return multiplier != MULTIPLIER_EDEFAULT;
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
		result.append(" (constant: ");
		result.append(constant);
		result.append(", multiplier: ");
		result.append(multiplier);
		result.append(')');
		return result.toString();
	}

} // end of NetbackPurchaseContractImpl

// finish type fixing
