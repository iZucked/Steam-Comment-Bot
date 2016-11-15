/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow;
import com.mmxlabs.models.lng.analytics.ResultSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Result Grouper Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperRowImpl#getGroupResults <em>Group Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MultipleResultGrouperRowImpl#getObject <em>Object</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultipleResultGrouperRowImpl extends EObjectImpl implements MultipleResultGrouperRow {
	/**
	 * The cached value of the '{@link #getGroupResults() <em>Group Results</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupResults()
	 * @generated
	 * @ordered
	 */
	protected EList<ResultSet> groupResults;

	/**
	 * The cached value of the '{@link #getObject() <em>Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected EObject object;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleResultGrouperRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MULTIPLE_RESULT_GROUPER_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResultSet> getGroupResults() {
		if (groupResults == null) {
			groupResults = new EObjectResolvingEList<ResultSet>(ResultSet.class, this, AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__GROUP_RESULTS);
		}
		return groupResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getObject() {
		if (object != null && object.eIsProxy()) {
			InternalEObject oldObject = (InternalEObject)object;
			object = eResolveProxy(oldObject);
			if (object != oldObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT, oldObject, object));
			}
		}
		return object;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetObject() {
		return object;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setObject(EObject newObject) {
		EObject oldObject = object;
		object = newObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT, oldObject, object));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__GROUP_RESULTS:
				return getGroupResults();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT:
				if (resolve) return getObject();
				return basicGetObject();
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__GROUP_RESULTS:
				getGroupResults().clear();
				getGroupResults().addAll((Collection<? extends ResultSet>)newValue);
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT:
				setObject((EObject)newValue);
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__GROUP_RESULTS:
				getGroupResults().clear();
				return;
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT:
				setObject((EObject)null);
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
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__GROUP_RESULTS:
				return groupResults != null && !groupResults.isEmpty();
			case AnalyticsPackage.MULTIPLE_RESULT_GROUPER_ROW__OBJECT:
				return object != null;
		}
		return super.eIsSet(featureID);
	}

} //MultipleResultGrouperRowImpl
