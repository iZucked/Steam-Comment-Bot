/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SimpleCharterContract;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleCharterContractImpl#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleCharterContractImpl extends UUIDObjectImpl implements SimpleCharterContract {
	/**
	 * The cached value of the '{@link #getBallastBonusContract() <em>Ballast Bonus Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusContract()
	 * @generated
	 * @ordered
	 */
	protected BallastBonusContract ballastBonusContract;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleCharterContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.SIMPLE_CHARTER_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BallastBonusContract getBallastBonusContract() {
		return ballastBonusContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastBonusContract(BallastBonusContract newBallastBonusContract, NotificationChain msgs) {
		BallastBonusContract oldBallastBonusContract = ballastBonusContract;
		ballastBonusContract = newBallastBonusContract;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, oldBallastBonusContract, newBallastBonusContract);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastBonusContract(BallastBonusContract newBallastBonusContract) {
		if (newBallastBonusContract != ballastBonusContract) {
			NotificationChain msgs = null;
			if (ballastBonusContract != null)
				msgs = ((InternalEObject)ballastBonusContract).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, null, msgs);
			if (newBallastBonusContract != null)
				msgs = ((InternalEObject)newBallastBonusContract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, null, msgs);
			msgs = basicSetBallastBonusContract(newBallastBonusContract, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, newBallastBonusContract, newBallastBonusContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return basicSetBallastBonusContract(null, msgs);
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
			case CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return getBallastBonusContract();
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
			case CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)newValue);
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
			case CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)null);
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
			case CommercialPackage.SIMPLE_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return ballastBonusContract != null;
		}
		return super.eIsSet(featureID);
	}

} //SimpleCharterContractImpl
