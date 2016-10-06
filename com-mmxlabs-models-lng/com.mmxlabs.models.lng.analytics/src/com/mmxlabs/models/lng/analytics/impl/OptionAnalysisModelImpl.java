/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;

import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
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
 * An implementation of the model object '<em><b>Option Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getBuys <em>Buys</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getSells <em>Sells</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getShippingTemplates <em>Shipping Templates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getRules <em>Rules</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getPartialCase <em>Partial Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getResultSets <em>Result Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#isUseTargetPNL <em>Use Target PNL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptionAnalysisModelImpl extends NamedObjectImpl implements OptionAnalysisModel {
	/**
	 * The cached value of the '{@link #getBuys() <em>Buys</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuys()
	 * @generated
	 * @ordered
	 */
	protected EList<BuyOption> buys;

	/**
	 * The cached value of the '{@link #getSells() <em>Sells</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSells()
	 * @generated
	 * @ordered
	 */
	protected EList<SellOption> sells;

	/**
	 * The cached value of the '{@link #getBaseCase() <em>Base Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCase()
	 * @generated
	 * @ordered
	 */
	protected BaseCase baseCase;

	/**
	 * The cached value of the '{@link #getShippingTemplates() <em>Shipping Templates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingTemplates()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingOption> shippingTemplates;

	/**
	 * The cached value of the '{@link #getRules() <em>Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRules()
	 * @generated
	 * @ordered
	 */
	protected EList<OptionRule> rules;

	/**
	 * The cached value of the '{@link #getPartialCase() <em>Partial Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartialCase()
	 * @generated
	 * @ordered
	 */
	protected PartialCase partialCase;

	/**
	 * The cached value of the '{@link #getResultSets() <em>Result Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSets()
	 * @generated
	 * @ordered
	 */
	protected EList<ResultSet> resultSets;

	/**
	 * The default value of the '{@link #isUseTargetPNL() <em>Use Target PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseTargetPNL()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_TARGET_PNL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseTargetPNL() <em>Use Target PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseTargetPNL()
	 * @generated
	 * @ordered
	 */
	protected boolean useTargetPNL = USE_TARGET_PNL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptionAnalysisModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseCase getBaseCase() {
		return baseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseCase(BaseCase newBaseCase, NotificationChain msgs) {
		BaseCase oldBaseCase = baseCase;
		baseCase = newBaseCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, oldBaseCase, newBaseCase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseCase(BaseCase newBaseCase) {
		if (newBaseCase != baseCase) {
			NotificationChain msgs = null;
			if (baseCase != null)
				msgs = ((InternalEObject)baseCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, null, msgs);
			if (newBaseCase != null)
				msgs = ((InternalEObject)newBaseCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, null, msgs);
			msgs = basicSetBaseCase(newBaseCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, newBaseCase, newBaseCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ShippingOption> getShippingTemplates() {
		if (shippingTemplates == null) {
			shippingTemplates = new EObjectContainmentEList<ShippingOption>(ShippingOption.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
		}
		return shippingTemplates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BuyOption> getBuys() {
		if (buys == null) {
			buys = new EObjectContainmentEList<BuyOption>(BuyOption.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS);
		}
		return buys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SellOption> getSells() {
		if (sells == null) {
			sells = new EObjectContainmentEList<SellOption>(SellOption.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS);
		}
		return sells;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OptionRule> getRules() {
		if (rules == null) {
			rules = new EObjectContainmentEList<OptionRule>(OptionRule.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES);
		}
		return rules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCase getPartialCase() {
		return partialCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPartialCase(PartialCase newPartialCase, NotificationChain msgs) {
		PartialCase oldPartialCase = partialCase;
		partialCase = newPartialCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, oldPartialCase, newPartialCase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPartialCase(PartialCase newPartialCase) {
		if (newPartialCase != partialCase) {
			NotificationChain msgs = null;
			if (partialCase != null)
				msgs = ((InternalEObject)partialCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, null, msgs);
			if (newPartialCase != null)
				msgs = ((InternalEObject)newPartialCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, null, msgs);
			msgs = basicSetPartialCase(newPartialCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, newPartialCase, newPartialCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResultSet> getResultSets() {
		if (resultSets == null) {
			resultSets = new EObjectContainmentEList<ResultSet>(ResultSet.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS);
		}
		return resultSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseTargetPNL() {
		return useTargetPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseTargetPNL(boolean newUseTargetPNL) {
		boolean oldUseTargetPNL = useTargetPNL;
		useTargetPNL = newUseTargetPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, oldUseTargetPNL, useTargetPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
				return ((InternalEList<?>)getBuys()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
				return ((InternalEList<?>)getSells()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return basicSetBaseCase(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return ((InternalEList<?>)getShippingTemplates()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
				return ((InternalEList<?>)getRules()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return basicSetPartialCase(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				return ((InternalEList<?>)getResultSets()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
				return getBuys();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
				return getSells();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return getBaseCase();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return getShippingTemplates();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
				return getRules();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return getPartialCase();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				return getResultSets();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return isUseTargetPNL();
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				getBuys().addAll((Collection<? extends BuyOption>)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				getSells().addAll((Collection<? extends SellOption>)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				setBaseCase((BaseCase)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
				getShippingTemplates().addAll((Collection<? extends ShippingOption>)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
				getRules().clear();
				getRules().addAll((Collection<? extends OptionRule>)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				setPartialCase((PartialCase)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				getResultSets().clear();
				getResultSets().addAll((Collection<? extends ResultSet>)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL((Boolean)newValue);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				setBaseCase((BaseCase)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
				getRules().clear();
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				setPartialCase((PartialCase)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				getResultSets().clear();
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL(USE_TARGET_PNL_EDEFAULT);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
				return buys != null && !buys.isEmpty();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
				return sells != null && !sells.isEmpty();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return baseCase != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return shippingTemplates != null && !shippingTemplates.isEmpty();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
				return rules != null && !rules.isEmpty();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return partialCase != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				return resultSets != null && !resultSets.isEmpty();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return useTargetPNL != USE_TARGET_PNL_EDEFAULT;
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
		result.append(" (useTargetPNL: ");
		result.append(useTargetPNL);
		result.append(')');
		return result.toString();
	}

} //OptionAnalysisModelImpl
