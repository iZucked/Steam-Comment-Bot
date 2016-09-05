/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DistributionModel;

import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

import com.mmxlabs.models.lng.types.VolumeUnits;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getContractCode <em>Contract Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#isCustom <em>Custom</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getTotalVolume <em>Total Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getDistributionModel <em>Distribution Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getSubProfiles <em>Sub Profiles</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContractProfileImpl<T extends Slot> extends EObjectImpl implements ContractProfile<T> {
	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected Contract contract;

	/**
	 * The default value of the '{@link #getContractCode() <em>Contract Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractCode()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTRACT_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContractCode() <em>Contract Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractCode()
	 * @generated
	 * @ordered
	 */
	protected String contractCode = CONTRACT_CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCustom() <em>Custom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCustom()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CUSTOM_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCustom() <em>Custom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCustom()
	 * @generated
	 * @ordered
	 */
	protected boolean custom = CUSTOM_EDEFAULT;

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalVolume() <em>Total Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalVolume() <em>Total Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalVolume()
	 * @generated
	 * @ordered
	 */
	protected int totalVolume = TOTAL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected static final VolumeUnits VOLUME_UNIT_EDEFAULT = VolumeUnits.M3;

	/**
	 * The cached value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected VolumeUnits volumeUnit = VOLUME_UNIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDistributionModel() <em>Distribution Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistributionModel()
	 * @generated
	 * @ordered
	 */
	protected DistributionModel distributionModel;

	/**
	 * The cached value of the '{@link #getSubProfiles() <em>Sub Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<SubContractProfile<T>> subProfiles;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CONTRACT_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Contract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (Contract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.CONTRACT_PROFILE__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContract(Contract newContract) {
		Contract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContractCode() {
		return contractCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContractCode(String newContractCode) {
		String oldContractCode = contractCode;
		contractCode = newContractCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE, oldContractCode, contractCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCustom() {
		return custom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCustom(boolean newCustom) {
		boolean oldCustom = custom;
		custom = newCustom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CUSTOM, oldCustom, custom));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnabled(boolean newEnabled) {
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__ENABLED, oldEnabled, enabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTotalVolume() {
		return totalVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTotalVolume(int newTotalVolume) {
		int oldTotalVolume = totalVolume;
		totalVolume = newTotalVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME, oldTotalVolume, totalVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VolumeUnits getVolumeUnit() {
		return volumeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeUnit(VolumeUnits newVolumeUnit) {
		VolumeUnits oldVolumeUnit = volumeUnit;
		volumeUnit = newVolumeUnit == null ? VOLUME_UNIT_EDEFAULT : newVolumeUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT, oldVolumeUnit, volumeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DistributionModel getDistributionModel() {
		if (distributionModel != null && distributionModel.eIsProxy()) {
			InternalEObject oldDistributionModel = (InternalEObject)distributionModel;
			distributionModel = (DistributionModel)eResolveProxy(oldDistributionModel);
			if (distributionModel != oldDistributionModel) {
				InternalEObject newDistributionModel = (InternalEObject)distributionModel;
				NotificationChain msgs = oldDistributionModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, null);
				if (newDistributionModel.eInternalContainer() == null) {
					msgs = newDistributionModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, oldDistributionModel, distributionModel));
			}
		}
		return distributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistributionModel basicGetDistributionModel() {
		return distributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDistributionModel(DistributionModel newDistributionModel, NotificationChain msgs) {
		DistributionModel oldDistributionModel = distributionModel;
		distributionModel = newDistributionModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, oldDistributionModel, newDistributionModel);
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
	public void setDistributionModel(DistributionModel newDistributionModel) {
		if (newDistributionModel != distributionModel) {
			NotificationChain msgs = null;
			if (distributionModel != null)
				msgs = ((InternalEObject)distributionModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
			if (newDistributionModel != null)
				msgs = ((InternalEObject)newDistributionModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, null, msgs);
			msgs = basicSetDistributionModel(newDistributionModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL, newDistributionModel, newDistributionModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SubContractProfile<T>> getSubProfiles() {
		if (subProfiles == null) {
			subProfiles = new EObjectContainmentEList.Resolving<SubContractProfile<T>>(SubContractProfile.class, this, ADPPackage.CONTRACT_PROFILE__SUB_PROFILES);
		}
		return subProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				return basicSetDistributionModel(null, msgs);
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return ((InternalEList<?>)getSubProfiles()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				return getContractCode();
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				return isCustom();
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				return isEnabled();
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				return getTotalVolume();
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				return getVolumeUnit();
			case ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				if (resolve) return getDistributionModel();
				return basicGetDistributionModel();
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return getSubProfiles();
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				setContract((Contract)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				setContractCode((String)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				setCustom((Boolean)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				setEnabled((Boolean)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				setTotalVolume((Integer)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				setVolumeUnit((VolumeUnits)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				setDistributionModel((DistributionModel)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				getSubProfiles().clear();
				getSubProfiles().addAll((Collection<? extends SubContractProfile<T>>)newValue);
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				setContract((Contract)null);
				return;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				setContractCode(CONTRACT_CODE_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				setCustom(CUSTOM_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				setEnabled(ENABLED_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				setTotalVolume(TOTAL_VOLUME_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				setVolumeUnit(VOLUME_UNIT_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				setDistributionModel((DistributionModel)null);
				return;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				getSubProfiles().clear();
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				return contract != null;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				return CONTRACT_CODE_EDEFAULT == null ? contractCode != null : !CONTRACT_CODE_EDEFAULT.equals(contractCode);
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				return custom != CUSTOM_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				return enabled != ENABLED_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				return totalVolume != TOTAL_VOLUME_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				return volumeUnit != VOLUME_UNIT_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__DISTRIBUTION_MODEL:
				return distributionModel != null;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return subProfiles != null && !subProfiles.isEmpty();
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
		result.append(" (contractCode: ");
		result.append(contractCode);
		result.append(", custom: ");
		result.append(custom);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(", totalVolume: ");
		result.append(totalVolume);
		result.append(", volumeUnit: ");
		result.append(volumeUnit);
		result.append(')');
		return result.toString();
	}

} //ContractProfileImpl
