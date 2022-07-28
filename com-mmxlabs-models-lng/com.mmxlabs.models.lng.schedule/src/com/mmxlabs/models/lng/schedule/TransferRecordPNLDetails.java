/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.mmxcore.NamedObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transfer Record PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getTransferRecord <em>Transfer Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getTransferPrice <em>Transfer Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntity <em>From Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityRevenue <em>From Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityCost <em>From Entity Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntity <em>To Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityRevenue <em>To Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityCost <em>To Entity Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails()
 * @model
 * @generated
 */
public interface TransferRecordPNLDetails extends GeneralPNLDetails {
	/**
	 * Returns the value of the '<em><b>Transfer Record</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Record</em>' reference.
	 * @see #setTransferRecord(NamedObject)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_TransferRecord()
	 * @model
	 * @generated
	 */
	NamedObject getTransferRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getTransferRecord <em>Transfer Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer Record</em>' reference.
	 * @see #getTransferRecord()
	 * @generated
	 */
	void setTransferRecord(NamedObject value);

	/**
	 * Returns the value of the '<em><b>Transfer Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer Price</em>' attribute.
	 * @see #setTransferPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_TransferPrice()
	 * @model
	 * @generated
	 */
	double getTransferPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getTransferPrice <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer Price</em>' attribute.
	 * @see #getTransferPrice()
	 * @generated
	 */
	void setTransferPrice(double value);

	/**
	 * Returns the value of the '<em><b>From Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Entity</em>' reference.
	 * @see #setFromEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_FromEntity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getFromEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntity <em>From Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Entity</em>' reference.
	 * @see #getFromEntity()
	 * @generated
	 */
	void setFromEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>From Entity Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Entity Revenue</em>' attribute.
	 * @see #setFromEntityRevenue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_FromEntityRevenue()
	 * @model
	 * @generated
	 */
	int getFromEntityRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityRevenue <em>From Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Entity Revenue</em>' attribute.
	 * @see #getFromEntityRevenue()
	 * @generated
	 */
	void setFromEntityRevenue(int value);

	/**
	 * Returns the value of the '<em><b>From Entity Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Entity Cost</em>' attribute.
	 * @see #setFromEntityCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_FromEntityCost()
	 * @model
	 * @generated
	 */
	int getFromEntityCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityCost <em>From Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Entity Cost</em>' attribute.
	 * @see #getFromEntityCost()
	 * @generated
	 */
	void setFromEntityCost(int value);

	/**
	 * Returns the value of the '<em><b>To Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Entity</em>' reference.
	 * @see #setToEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_ToEntity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getToEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntity <em>To Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Entity</em>' reference.
	 * @see #getToEntity()
	 * @generated
	 */
	void setToEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>To Entity Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Entity Revenue</em>' attribute.
	 * @see #setToEntityRevenue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_ToEntityRevenue()
	 * @model
	 * @generated
	 */
	int getToEntityRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityRevenue <em>To Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Entity Revenue</em>' attribute.
	 * @see #getToEntityRevenue()
	 * @generated
	 */
	void setToEntityRevenue(int value);

	/**
	 * Returns the value of the '<em><b>To Entity Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Entity Cost</em>' attribute.
	 * @see #setToEntityCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_ToEntityCost()
	 * @model
	 * @generated
	 */
	int getToEntityCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityCost <em>To Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Entity Cost</em>' attribute.
	 * @see #getToEntityCost()
	 * @generated
	 */
	void setToEntityCost(int value);

} // TransferRecordPNLDetails
