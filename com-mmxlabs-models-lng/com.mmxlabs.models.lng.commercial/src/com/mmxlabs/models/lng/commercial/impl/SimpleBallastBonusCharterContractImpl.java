/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Ballast Bonus Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getEntity <em>Entity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleBallastBonusCharterContractImpl extends NamedObjectImpl implements SimpleBallastBonusCharterContract {
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
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleBallastBonusCharterContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, oldBallastBonusContract, newBallastBonusContract);
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
				msgs = ((InternalEObject)ballastBonusContract).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, null, msgs);
			if (newBallastBonusContract != null)
				msgs = ((InternalEObject)newBallastBonusContract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, null, msgs);
			msgs = basicSetBallastBonusContract(newBallastBonusContract, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, newBallastBonusContract, newBallastBonusContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return getBallastBonusContract();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)newValue);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY:
				setEntity((BaseLegalEntity)newValue);
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)null);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY:
				setEntity((BaseLegalEntity)null);
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return ballastBonusContract != null;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__ENTITY:
				return entity != null;
		}
		return super.eIsSet(featureID);
	}

} //SimpleBallastBonusCharterContractImpl
