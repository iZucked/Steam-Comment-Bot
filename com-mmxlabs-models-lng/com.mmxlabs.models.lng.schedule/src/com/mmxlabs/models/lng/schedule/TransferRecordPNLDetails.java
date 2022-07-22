/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transfer Record PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getTransferPrice <em>Transfer Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityName <em>From Entity Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityRevenue <em>From Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityCost <em>From Entity Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityName <em>To Entity Name</em>}</li>
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
	 * Returns the value of the '<em><b>From Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Entity Name</em>' attribute.
	 * @see #setFromEntityName(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_FromEntityName()
	 * @model
	 * @generated
	 */
	String getFromEntityName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getFromEntityName <em>From Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Entity Name</em>' attribute.
	 * @see #getFromEntityName()
	 * @generated
	 */
	void setFromEntityName(String value);

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
	 * Returns the value of the '<em><b>To Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Entity Name</em>' attribute.
	 * @see #setToEntityName(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getTransferRecordPNLDetails_ToEntityName()
	 * @model
	 * @generated
	 */
	String getToEntityName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails#getToEntityName <em>To Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Entity Name</em>' attribute.
	 * @see #getToEntityName()
	 * @generated
	 */
	void setToEntityName(String value);

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
