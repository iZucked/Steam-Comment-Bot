/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;

import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sensitivity Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl#getSensitivityModel <em>Sensitivity Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SensitivityModelImpl#getSensitivitySolution <em>Sensitivity Solution</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SensitivityModelImpl extends UUIDObjectImpl implements SensitivityModel {
	/**
	 * The cached value of the '{@link #getSensitivityModel() <em>Sensitivity Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSensitivityModel()
	 * @generated
	 * @ordered
	 */
	protected OptionAnalysisModel sensitivityModel;

	/**
	 * The cached value of the '{@link #getSensitivitySolution() <em>Sensitivity Solution</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSensitivitySolution()
	 * @generated
	 * @ordered
	 */
	protected SensitivitySolutionSet sensitivitySolution;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SensitivityModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SENSITIVITY_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptionAnalysisModel getSensitivityModel() {
		return sensitivityModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSensitivityModel(OptionAnalysisModel newSensitivityModel, NotificationChain msgs) {
		OptionAnalysisModel oldSensitivityModel = sensitivityModel;
		sensitivityModel = newSensitivityModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL, oldSensitivityModel, newSensitivityModel);
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
	public void setSensitivityModel(OptionAnalysisModel newSensitivityModel) {
		if (newSensitivityModel != sensitivityModel) {
			NotificationChain msgs = null;
			if (sensitivityModel != null)
				msgs = ((InternalEObject)sensitivityModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL, null, msgs);
			if (newSensitivityModel != null)
				msgs = ((InternalEObject)newSensitivityModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL, null, msgs);
			msgs = basicSetSensitivityModel(newSensitivityModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL, newSensitivityModel, newSensitivityModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SensitivitySolutionSet getSensitivitySolution() {
		return sensitivitySolution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSensitivitySolution(SensitivitySolutionSet newSensitivitySolution, NotificationChain msgs) {
		SensitivitySolutionSet oldSensitivitySolution = sensitivitySolution;
		sensitivitySolution = newSensitivitySolution;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, oldSensitivitySolution, newSensitivitySolution);
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
	public void setSensitivitySolution(SensitivitySolutionSet newSensitivitySolution) {
		if (newSensitivitySolution != sensitivitySolution) {
			NotificationChain msgs = null;
			if (sensitivitySolution != null)
				msgs = ((InternalEObject)sensitivitySolution).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, null, msgs);
			if (newSensitivitySolution != null)
				msgs = ((InternalEObject)newSensitivitySolution).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, null, msgs);
			msgs = basicSetSensitivitySolution(newSensitivitySolution, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION, newSensitivitySolution, newSensitivitySolution));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL:
				return basicSetSensitivityModel(null, msgs);
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION:
				return basicSetSensitivitySolution(null, msgs);
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
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL:
				return getSensitivityModel();
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION:
				return getSensitivitySolution();
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
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL:
				setSensitivityModel((OptionAnalysisModel)newValue);
				return;
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION:
				setSensitivitySolution((SensitivitySolutionSet)newValue);
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
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL:
				setSensitivityModel((OptionAnalysisModel)null);
				return;
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION:
				setSensitivitySolution((SensitivitySolutionSet)null);
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
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_MODEL:
				return sensitivityModel != null;
			case AnalyticsPackage.SENSITIVITY_MODEL__SENSITIVITY_SOLUTION:
				return sensitivitySolution != null;
		}
		return super.eIsSet(featureID);
	}

} //SensitivityModelImpl
