/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Break Even Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.BreakEvenOptimisationStageImpl#getTargetProfitAndLoss <em>Target Profit And Loss</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenOptimisationStageImpl extends EObjectImpl implements BreakEvenOptimisationStage {
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
	 * The default value of the '{@link #getTargetProfitAndLoss() <em>Target Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected static final long TARGET_PROFIT_AND_LOSS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTargetProfitAndLoss() <em>Target Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected long targetProfitAndLoss = TARGET_PROFIT_AND_LOSS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenOptimisationStageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.BREAK_EVEN_OPTIMISATION_STAGE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTargetProfitAndLoss() {
		return targetProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetProfitAndLoss(long newTargetProfitAndLoss) {
		long oldTargetProfitAndLoss = targetProfitAndLoss;
		targetProfitAndLoss = newTargetProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS, oldTargetProfitAndLoss, targetProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__NAME:
				return getName();
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS:
				return getTargetProfitAndLoss();
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
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__NAME:
				setName((String)newValue);
				return;
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS:
				setTargetProfitAndLoss((Long)newValue);
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
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS:
				setTargetProfitAndLoss(TARGET_PROFIT_AND_LOSS_EDEFAULT);
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
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ParametersPackage.BREAK_EVEN_OPTIMISATION_STAGE__TARGET_PROFIT_AND_LOSS:
				return targetProfitAndLoss != TARGET_PROFIT_AND_LOSS_EDEFAULT;
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
		result.append(", targetProfitAndLoss: ");
		result.append(targetProfitAndLoss);
		result.append(')');
		return result.toString();
	}

} //BreakEvenOptimisationStageImpl
