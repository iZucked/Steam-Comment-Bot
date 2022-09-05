/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transfer Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferModel#getTransferAgreements <em>Transfer Agreements</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.TransferModel#getTransferRecords <em>Transfer Records</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferModel()
 * @model
 * @generated
 */
public interface TransferModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Transfer Agreements</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.transfers.TransferAgreement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Agreements</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferModel_TransferAgreements()
	 * @model containment="true"
	 * @generated
	 */
	EList<TransferAgreement> getTransferAgreements();

	/**
	 * Returns the value of the '<em><b>Transfer Records</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.transfers.TransferRecord}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Records</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#getTransferModel_TransferRecords()
	 * @model containment="true"
	 * @generated
	 */
	EList<TransferRecord> getTransferRecords();

} // TransferModel
