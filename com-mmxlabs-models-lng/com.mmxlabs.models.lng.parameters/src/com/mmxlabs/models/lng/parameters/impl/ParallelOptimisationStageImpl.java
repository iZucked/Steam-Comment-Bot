/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jdt.annotation.Nullable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parallel Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl#getJobCount <em>Job Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ParallelOptimisationStageImpl#getTemplate <em>Template</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParallelOptimisationStageImpl<T extends ParallisableOptimisationStage> extends EObjectImpl implements ParallelOptimisationStage<T> {
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
	 * The default value of the '{@link #getJobCount() <em>Job Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobCount()
	 * @generated
	 * @ordered
	 */
	protected static final int JOB_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getJobCount() <em>Job Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobCount()
	 * @generated
	 * @ordered
	 */
	protected int jobCount = JOB_COUNT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTemplate() <em>Template</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplate()
	 * @generated
	 * @ordered
	 */
	protected T template;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParallelOptimisationStageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getJobCount() {
		return jobCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setJobCount(int newJobCount) {
		int oldJobCount = jobCount;
		jobCount = newJobCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT, oldJobCount, jobCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public T getTemplate() {
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTemplate(T newTemplate, NotificationChain msgs) {
		T oldTemplate = template;
		template = newTemplate;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE, oldTemplate, newTemplate);
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
	public void setTemplate(T newTemplate) {
		if (newTemplate != template) {
			NotificationChain msgs = null;
			if (template != null)
				msgs = ((InternalEObject)template).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE, null, msgs);
			if (newTemplate != null)
				msgs = ((InternalEObject)newTemplate).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE, null, msgs);
			msgs = basicSetTemplate(newTemplate, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE, newTemplate, newTemplate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
				return basicSetTemplate((T)null, msgs);
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
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME:
				return getName();
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT:
				return getJobCount();
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
				return getTemplate();
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
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME:
				setName((String)newValue);
				return;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT:
				setJobCount((Integer)newValue);
				return;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
				setTemplate((T)newValue);
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
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT:
				setJobCount(JOB_COUNT_EDEFAULT);
				return;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
				setTemplate((T)null);
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
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT:
				return jobCount != JOB_COUNT_EDEFAULT;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
				return template != null;
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
		result.append(", jobCount: ");
		result.append(jobCount);
		result.append(')');
		return result.toString();
	}

} //ParallelOptimisationStageImpl
