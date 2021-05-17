/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generic Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getRepositioningFeeTerms <em>Repositioning Fee Terms</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getBallastBonusTerms <em>Ballast Bonus Terms</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl#getEndHeel <em>End Heel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GenericCharterContractImpl extends NamedObjectImpl implements GenericCharterContract {
	/**
	 * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

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
	 * The cached value of the '{@link #getRepositioningFeeTerms() <em>Repositioning Fee Terms</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFeeTerms()
	 * @generated
	 * @ordered
	 */
	protected IRepositioningFee repositioningFeeTerms;

	/**
	 * The cached value of the '{@link #getBallastBonusTerms() <em>Ballast Bonus Terms</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonusTerms()
	 * @generated
	 * @ordered
	 */
	protected IBallastBonus ballastBonusTerms;

	/**
	 * The cached value of the '{@link #getStartHeel() <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartHeel()
	 * @generated
	 * @ordered
	 */
	protected StartHeelOptions startHeel;

	/**
	 * The cached value of the '{@link #getEndHeel() <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeel()
	 * @generated
	 * @ordered
	 */
	protected EndHeelOptions endHeel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenericCharterContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUuid() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUuid(String newUuid) {
		String oldUuid = uuid;
		uuid = newUuid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID, oldUuid, uuid));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
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
	public IRepositioningFee getRepositioningFeeTerms() {
		return repositioningFeeTerms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRepositioningFeeTerms(IRepositioningFee newRepositioningFeeTerms, NotificationChain msgs) {
		IRepositioningFee oldRepositioningFeeTerms = repositioningFeeTerms;
		repositioningFeeTerms = newRepositioningFeeTerms;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS, oldRepositioningFeeTerms, newRepositioningFeeTerms);
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
	public void setRepositioningFeeTerms(IRepositioningFee newRepositioningFeeTerms) {
		if (newRepositioningFeeTerms != repositioningFeeTerms) {
			NotificationChain msgs = null;
			if (repositioningFeeTerms != null)
				msgs = ((InternalEObject)repositioningFeeTerms).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS, null, msgs);
			if (newRepositioningFeeTerms != null)
				msgs = ((InternalEObject)newRepositioningFeeTerms).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS, null, msgs);
			msgs = basicSetRepositioningFeeTerms(newRepositioningFeeTerms, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS, newRepositioningFeeTerms, newRepositioningFeeTerms));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IBallastBonus getBallastBonusTerms() {
		return ballastBonusTerms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastBonusTerms(IBallastBonus newBallastBonusTerms, NotificationChain msgs) {
		IBallastBonus oldBallastBonusTerms = ballastBonusTerms;
		ballastBonusTerms = newBallastBonusTerms;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, oldBallastBonusTerms, newBallastBonusTerms);
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
	public void setBallastBonusTerms(IBallastBonus newBallastBonusTerms) {
		if (newBallastBonusTerms != ballastBonusTerms) {
			NotificationChain msgs = null;
			if (ballastBonusTerms != null)
				msgs = ((InternalEObject)ballastBonusTerms).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, null, msgs);
			if (newBallastBonusTerms != null)
				msgs = ((InternalEObject)newBallastBonusTerms).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, null, msgs);
			msgs = basicSetBallastBonusTerms(newBallastBonusTerms, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, newBallastBonusTerms, newBallastBonusTerms));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StartHeelOptions getStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(StartHeelOptions newStartHeel, NotificationChain msgs) {
		StartHeelOptions oldStartHeel = startHeel;
		startHeel = newStartHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL, oldStartHeel, newStartHeel);
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
	public void setStartHeel(StartHeelOptions newStartHeel) {
		if (newStartHeel != startHeel) {
			NotificationChain msgs = null;
			if (startHeel != null)
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL, newStartHeel, newStartHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EndHeelOptions getEndHeel() {
		return endHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEndHeel(EndHeelOptions newEndHeel, NotificationChain msgs) {
		EndHeelOptions oldEndHeel = endHeel;
		endHeel = newEndHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL, oldEndHeel, newEndHeel);
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
	public void setEndHeel(EndHeelOptions newEndHeel) {
		if (newEndHeel != endHeel) {
			NotificationChain msgs = null;
			if (endHeel != null)
				msgs = ((InternalEObject)endHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL, null, msgs);
			if (newEndHeel != null)
				msgs = ((InternalEObject)newEndHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL, null, msgs);
			msgs = basicSetEndHeel(newEndHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL, newEndHeel, newEndHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS:
				return basicSetRepositioningFeeTerms(null, msgs);
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS:
				return basicSetBallastBonusTerms(null, msgs);
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL:
				return basicSetStartHeel(null, msgs);
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL:
				return basicSetEndHeel(null, msgs);
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
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID:
				return getUuid();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION:
				return getMinDuration();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION:
				return getMaxDuration();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS:
				return getRepositioningFeeTerms();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS:
				return getBallastBonusTerms();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL:
				return getStartHeel();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL:
				return getEndHeel();
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
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID:
				setUuid((String)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION:
				setMaxDuration((Integer)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS:
				setRepositioningFeeTerms((IRepositioningFee)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS:
				setBallastBonusTerms((IBallastBonus)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL:
				setStartHeel((StartHeelOptions)newValue);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL:
				setEndHeel((EndHeelOptions)newValue);
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
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION:
				unsetMinDuration();
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION:
				unsetMaxDuration();
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS:
				setRepositioningFeeTerms((IRepositioningFee)null);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS:
				setBallastBonusTerms((IBallastBonus)null);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL:
				setStartHeel((StartHeelOptions)null);
				return;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL:
				setEndHeel((EndHeelOptions)null);
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
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MIN_DURATION:
				return isSetMinDuration();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__MAX_DURATION:
				return isSetMaxDuration();
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS:
				return repositioningFeeTerms != null;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS:
				return ballastBonusTerms != null;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__START_HEEL:
				return startHeel != null;
			case CommercialPackage.GENERIC_CHARTER_CONTRACT__END_HEEL:
				return endHeel != null;
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
		if (baseClass == UUIDObject.class) {
			switch (derivedFeatureID) {
				case CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID: return MMXCorePackage.UUID_OBJECT__UUID;
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
		if (baseClass == UUIDObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.UUID_OBJECT__UUID: return CommercialPackage.GENERIC_CHARTER_CONTRACT__UUID;
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
		result.append(" (uuid: ");
		result.append(uuid);
		result.append(", minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //GenericCharterContractImpl
