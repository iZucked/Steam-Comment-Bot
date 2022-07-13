/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transfer Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferModelImpl#getTransferAgreements <em>Transfer Agreements</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferModelImpl#getTransferRecords <em>Transfer Records</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransferModelImpl extends EObjectImpl implements TransferModel {
	/**
	 * The cached value of the '{@link #getTransferAgreements() <em>Transfer Agreements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferAgreements()
	 * @generated
	 * @ordered
	 */
	protected EList<TransferAgreement> transferAgreements;

	/**
	 * The cached value of the '{@link #getTransferRecords() <em>Transfer Records</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferRecords()
	 * @generated
	 * @ordered
	 */
	protected EList<TransferRecord> transferRecords;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransferModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TransfersPackage.Literals.TRANSFER_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TransferAgreement> getTransferAgreements() {
		if (transferAgreements == null) {
			transferAgreements = new EObjectContainmentEList<TransferAgreement>(TransferAgreement.class, this, TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS);
		}
		return transferAgreements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TransferRecord> getTransferRecords() {
		if (transferRecords == null) {
			transferRecords = new EObjectContainmentEList<TransferRecord>(TransferRecord.class, this, TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS);
		}
		return transferRecords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS:
				return ((InternalEList<?>)getTransferAgreements()).basicRemove(otherEnd, msgs);
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS:
				return ((InternalEList<?>)getTransferRecords()).basicRemove(otherEnd, msgs);
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
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS:
				return getTransferAgreements();
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS:
				return getTransferRecords();
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
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS:
				getTransferAgreements().clear();
				getTransferAgreements().addAll((Collection<? extends TransferAgreement>)newValue);
				return;
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS:
				getTransferRecords().clear();
				getTransferRecords().addAll((Collection<? extends TransferRecord>)newValue);
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
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS:
				getTransferAgreements().clear();
				return;
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS:
				getTransferRecords().clear();
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
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_AGREEMENTS:
				return transferAgreements != null && !transferAgreements.isEmpty();
			case TransfersPackage.TRANSFER_MODEL__TRANSFER_RECORDS:
				return transferRecords != null && !transferRecords.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TransferModelImpl
