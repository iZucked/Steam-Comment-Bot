/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
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
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusCharterContractImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleBallastBonusCharterContractImpl extends NamedObjectImpl implements SimpleBallastBonusCharterContract {
	/**
	 * The default value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected int minDuration = MIN_DURATION_EDEFAULT;

	/**
	 * This is true if the Min Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minDurationESet;

	/**
	 * The default value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected int maxDuration = MAX_DURATION_EDEFAULT;

	/**
	 * This is true if the Max Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxDurationESet;

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
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final String REPOSITIONING_FEE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected String repositioningFee = REPOSITIONING_FEE_EDEFAULT;

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
	@Override
	public int getMinDuration() {
		return minDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinDuration(int newMinDuration) {
		int oldMinDuration = minDuration;
		minDuration = newMinDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinDuration() {
		int oldMinDuration = minDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDuration = MIN_DURATION_EDEFAULT;
		minDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinDuration() {
		return minDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxDuration(int newMaxDuration) {
		int oldMaxDuration = maxDuration;
		maxDuration = newMaxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxDuration() {
		int oldMaxDuration = maxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDuration = MAX_DURATION_EDEFAULT;
		maxDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxDuration() {
		return maxDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
	public String getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRepositioningFee(String newRepositioningFee) {
		String oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee));
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION:
				return getMinDuration();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION:
				return getMaxDuration();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return getBallastBonusContract();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__REPOSITIONING_FEE:
				return getRepositioningFee();
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION:
				setMaxDuration((Integer)newValue);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)newValue);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__REPOSITIONING_FEE:
				setRepositioningFee((String)newValue);
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION:
				unsetMinDuration();
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION:
				unsetMaxDuration();
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				setBallastBonusContract((BallastBonusContract)null);
				return;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__REPOSITIONING_FEE:
				setRepositioningFee(REPOSITIONING_FEE_EDEFAULT);
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
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MIN_DURATION:
				return isSetMinDuration();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__MAX_DURATION:
				return isSetMaxDuration();
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT:
				return ballastBonusContract != null;
			case CommercialPackage.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT__REPOSITIONING_FEE:
				return REPOSITIONING_FEE_EDEFAULT == null ? repositioningFee != null : !REPOSITIONING_FEE_EDEFAULT.equals(repositioningFee);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(", repositioningFee: ");
		result.append(repositioningFee);
		result.append(')');
		return result.toString();
	}

} //SimpleBallastBonusCharterContractImpl
