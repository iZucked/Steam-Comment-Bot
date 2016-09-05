/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import java.time.YearMonth;

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
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getYearStart <em>Year Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getPurchaseContractProfiles <em>Purchase Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getSalesContractProfiles <em>Sales Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getBindingRules <em>Binding Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ADPModelImpl extends EObjectImpl implements ADPModel {
	/**
	 * The default value of the '{@link #getYearStart() <em>Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearStart()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth YEAR_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getYearStart() <em>Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearStart()
	 * @generated
	 * @ordered
	 */
	protected YearMonth yearStart = YEAR_START_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPurchaseContractProfiles() <em>Purchase Contract Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseContractProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<PurchaseContractProfile> purchaseContractProfiles;

	/**
	 * The cached value of the '{@link #getSalesContractProfiles() <em>Sales Contract Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesContractProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<SalesContractProfile> salesContractProfiles;

	/**
	 * The cached value of the '{@link #getBindingRules() <em>Binding Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<BindingRule> bindingRules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.ADP_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getYearStart() {
		return yearStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearStart(YearMonth newYearStart) {
		YearMonth oldYearStart = yearStart;
		yearStart = newYearStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__YEAR_START, oldYearStart, yearStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PurchaseContractProfile> getPurchaseContractProfiles() {
		if (purchaseContractProfiles == null) {
			purchaseContractProfiles = new EObjectContainmentEList.Resolving<PurchaseContractProfile>(PurchaseContractProfile.class, this, ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES);
		}
		return purchaseContractProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SalesContractProfile> getSalesContractProfiles() {
		if (salesContractProfiles == null) {
			salesContractProfiles = new EObjectContainmentEList.Resolving<SalesContractProfile>(SalesContractProfile.class, this, ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES);
		}
		return salesContractProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BindingRule> getBindingRules() {
		if (bindingRules == null) {
			bindingRules = new EObjectContainmentEList.Resolving<BindingRule>(BindingRule.class, this, ADPPackage.ADP_MODEL__BINDING_RULES);
		}
		return bindingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return ((InternalEList<?>)getPurchaseContractProfiles()).basicRemove(otherEnd, msgs);
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return ((InternalEList<?>)getSalesContractProfiles()).basicRemove(otherEnd, msgs);
			case ADPPackage.ADP_MODEL__BINDING_RULES:
				return ((InternalEList<?>)getBindingRules()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				return getYearStart();
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return getPurchaseContractProfiles();
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return getSalesContractProfiles();
			case ADPPackage.ADP_MODEL__BINDING_RULES:
				return getBindingRules();
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				setYearStart((YearMonth)newValue);
				return;
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				getPurchaseContractProfiles().clear();
				getPurchaseContractProfiles().addAll((Collection<? extends PurchaseContractProfile>)newValue);
				return;
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				getSalesContractProfiles().clear();
				getSalesContractProfiles().addAll((Collection<? extends SalesContractProfile>)newValue);
				return;
			case ADPPackage.ADP_MODEL__BINDING_RULES:
				getBindingRules().clear();
				getBindingRules().addAll((Collection<? extends BindingRule>)newValue);
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				setYearStart(YEAR_START_EDEFAULT);
				return;
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				getPurchaseContractProfiles().clear();
				return;
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				getSalesContractProfiles().clear();
				return;
			case ADPPackage.ADP_MODEL__BINDING_RULES:
				getBindingRules().clear();
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				return YEAR_START_EDEFAULT == null ? yearStart != null : !YEAR_START_EDEFAULT.equals(yearStart);
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return purchaseContractProfiles != null && !purchaseContractProfiles.isEmpty();
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return salesContractProfiles != null && !salesContractProfiles.isEmpty();
			case ADPPackage.ADP_MODEL__BINDING_RULES:
				return bindingRules != null && !bindingRules.isEmpty();
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
		result.append(" (yearStart: ");
		result.append(yearStart);
		result.append(')');
		return result.toString();
	}

} //ADPModelImpl
