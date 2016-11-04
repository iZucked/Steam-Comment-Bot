/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouper;

import com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Result Grouper</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperImpl#getGroupResults <em>Group Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperImpl#getReferenceRow <em>Reference Row</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperImpl#getFeatureName <em>Feature Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultipleResultGrouperImpl extends EObjectImpl implements MultipleResultGrouper {
	/**
	 * The cached value of the '{@link #getGroupResults() <em>Group Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupResults()
	 * @generated
	 * @ordered
	 */
	protected EList<MultipleResultGrouperRow> groupResults;

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
	 * The cached value of the '{@link #getReferenceRow() <em>Reference Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceRow()
	 * @generated
	 * @ordered
	 */
	protected PartialCaseRow referenceRow;

	/**
	 * The default value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected String featureName = FEATURE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleResultGrouperImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MULTIPLE_RESULT_GROUPER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MultipleResultGrouperRow> getGroupResults() {
		if (groupResults == null) {
			groupResults = new EObjectContainmentEList<MultipleResultGrouperRow>(MultipleResultGrouperRow.class, this, AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS);
		}
		return groupResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MULTIPLE_RESULT_GROUPER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCaseRow getReferenceRow() {
		if (referenceRow != null && referenceRow.eIsProxy()) {
			InternalEObject oldReferenceRow = (InternalEObject)referenceRow;
			referenceRow = (PartialCaseRow)eResolveProxy(oldReferenceRow);
			if (referenceRow != oldReferenceRow) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW, oldReferenceRow, referenceRow));
			}
		}
		return referenceRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCaseRow basicGetReferenceRow() {
		return referenceRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceRow(PartialCaseRow newReferenceRow) {
		PartialCaseRow oldReferenceRow = referenceRow;
		referenceRow = newReferenceRow;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW, oldReferenceRow, referenceRow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureName(String newFeatureName) {
		String oldFeatureName = featureName;
		featureName = newFeatureName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MULTIPLE_RESULT_GROUPER__FEATURE_NAME, oldFeatureName, featureName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS:
				return ((InternalEList<?>)getGroupResults()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS:
				return getGroupResults();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__NAME:
				return getName();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW:
				if (resolve) return getReferenceRow();
				return basicGetReferenceRow();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__FEATURE_NAME:
				return getFeatureName();
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS:
				getGroupResults().clear();
				getGroupResults().addAll((Collection<? extends MultipleResultGrouperRow>)newValue);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__NAME:
				setName((String)newValue);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW:
				setReferenceRow((PartialCaseRow)newValue);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__FEATURE_NAME:
				setFeatureName((String)newValue);
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS:
				getGroupResults().clear();
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW:
				setReferenceRow((PartialCaseRow)null);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__FEATURE_NAME:
				setFeatureName(FEATURE_NAME_EDEFAULT);
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__GROUP_RESULTS:
				return groupResults != null && !groupResults.isEmpty();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__REFERENCE_ROW:
				return referenceRow != null;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER__FEATURE_NAME:
				return FEATURE_NAME_EDEFAULT == null ? featureName != null : !FEATURE_NAME_EDEFAULT.equals(featureName);
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
		result.append(", featureName: ");
		result.append(featureName);
		result.append(')');
		return result.toString();
	}

} //MultipleResultGrouperImpl
