/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.pricing.CommodityIndex;

import java.time.YearMonth;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exposure Detail</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInMMBTU <em>Volume In MMBTU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInNativeUnits <em>Volume In Native Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getNativeValue <em>Native Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getCurrencyUnit <em>Currency Unit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail()
 * @model
 * @generated
 */
public interface ExposureDetail extends EObject {
	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(CommodityIndex)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_Index()
	 * @model
	 * @generated
	 */
	CommodityIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(CommodityIndex value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(YearMonth)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_Date()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Volume In MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume In MMBTU</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume In MMBTU</em>' attribute.
	 * @see #setVolumeInMMBTU(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_VolumeInMMBTU()
	 * @model
	 * @generated
	 */
	double getVolumeInMMBTU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInMMBTU <em>Volume In MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In MMBTU</em>' attribute.
	 * @see #getVolumeInMMBTU()
	 * @generated
	 */
	void setVolumeInMMBTU(double value);

	/**
	 * Returns the value of the '<em><b>Volume In Native Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume In Native Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume In Native Units</em>' attribute.
	 * @see #setVolumeInNativeUnits(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_VolumeInNativeUnits()
	 * @model
	 * @generated
	 */
	double getVolumeInNativeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInNativeUnits <em>Volume In Native Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In Native Units</em>' attribute.
	 * @see #getVolumeInNativeUnits()
	 * @generated
	 */
	void setVolumeInNativeUnits(double value);

	/**
	 * Returns the value of the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Price</em>' attribute.
	 * @see #setUnitPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_UnitPrice()
	 * @model
	 * @generated
	 */
	double getUnitPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getUnitPrice <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Price</em>' attribute.
	 * @see #getUnitPrice()
	 * @generated
	 */
	void setUnitPrice(double value);

	/**
	 * Returns the value of the '<em><b>Native Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Native Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Native Value</em>' attribute.
	 * @see #setNativeValue(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_NativeValue()
	 * @model
	 * @generated
	 */
	double getNativeValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getNativeValue <em>Native Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Native Value</em>' attribute.
	 * @see #getNativeValue()
	 * @generated
	 */
	void setNativeValue(double value);

	/**
	 * Returns the value of the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Unit</em>' attribute.
	 * @see #setVolumeUnit(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_VolumeUnit()
	 * @model
	 * @generated
	 */
	String getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(String value);

	/**
	 * Returns the value of the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Currency Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Currency Unit</em>' attribute.
	 * @see #setCurrencyUnit(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getExposureDetail_CurrencyUnit()
	 * @model
	 * @generated
	 */
	String getCurrencyUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getCurrencyUnit <em>Currency Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Currency Unit</em>' attribute.
	 * @see #getCurrencyUnit()
	 * @generated
	 */
	void setCurrencyUnit(String value);

} // ExposureDetail
