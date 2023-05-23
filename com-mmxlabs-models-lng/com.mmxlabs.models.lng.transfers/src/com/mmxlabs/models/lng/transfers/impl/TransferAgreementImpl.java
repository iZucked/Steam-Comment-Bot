/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.commercial.PreferredFormulaeWrapper;
import com.mmxlabs.models.lng.commercial.PreferredPricingBasesWrapper;
import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transfer Agreement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getFromEntity <em>From Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getToEntity <em>To Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getIncoterm <em>Incoterm</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getCompanyStatus <em>Company Status</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getBufferDays <em>Buffer Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getFromBU <em>From BU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getToBU <em>To BU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getCode <em>Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getPreferredFormulae <em>Preferred Formulae</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransferAgreementImpl extends NamedObjectImpl implements TransferAgreement {
	/**
	 * The cached value of the '{@link #getFromEntity() <em>From Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity fromEntity;

	/**
	 * The cached value of the '{@link #getToEntity() <em>To Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity toEntity;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getIncoterm() <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncoterm()
	 * @generated
	 * @ordered
	 */
	protected static final TransferIncoterm INCOTERM_EDEFAULT = TransferIncoterm.BOTH;

	/**
	 * The cached value of the '{@link #getIncoterm() <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncoterm()
	 * @generated
	 * @ordered
	 */
	protected TransferIncoterm incoterm = INCOTERM_EDEFAULT;

	/**
	 * The default value of the '{@link #getCompanyStatus() <em>Company Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompanyStatus()
	 * @generated
	 * @ordered
	 */
	protected static final CompanyStatus COMPANY_STATUS_EDEFAULT = CompanyStatus.INTRA;

	/**
	 * The cached value of the '{@link #getCompanyStatus() <em>Company Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompanyStatus()
	 * @generated
	 * @ordered
	 */
	protected CompanyStatus companyStatus = COMPANY_STATUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getBufferDays() <em>Buffer Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBufferDays()
	 * @generated
	 * @ordered
	 */
	protected static final int BUFFER_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBufferDays() <em>Buffer Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBufferDays()
	 * @generated
	 * @ordered
	 */
	protected int bufferDays = BUFFER_DAYS_EDEFAULT;

	/**
	 * This is true if the Buffer Days attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean bufferDaysESet;

	/**
	 * The cached value of the '{@link #getFromBU() <em>From BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromBU()
	 * @generated
	 * @ordered
	 */
	protected BusinessUnit fromBU;

	/**
	 * This is true if the From BU reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fromBUESet;

	/**
	 * The cached value of the '{@link #getToBU() <em>To BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToBU()
	 * @generated
	 * @ordered
	 */
	protected BusinessUnit toBU;

	/**
	 * This is true if the To BU reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean toBUESet;

	/**
	 * The default value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected static final String CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected String code = CODE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPreferredFormulae() <em>Preferred Formulae</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferredFormulae()
	 * @generated
	 * @ordered
	 */
	protected EList<PreferredFormulaeWrapper> preferredFormulae;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransferAgreementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TransfersPackage.Literals.TRANSFER_AGREEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseLegalEntity getFromEntity() {
		if (fromEntity != null && fromEntity.eIsProxy()) {
			InternalEObject oldFromEntity = (InternalEObject)fromEntity;
			fromEntity = (BaseLegalEntity)eResolveProxy(oldFromEntity);
			if (fromEntity != oldFromEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY, oldFromEntity, fromEntity));
			}
		}
		return fromEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetFromEntity() {
		return fromEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromEntity(BaseLegalEntity newFromEntity) {
		BaseLegalEntity oldFromEntity = fromEntity;
		fromEntity = newFromEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY, oldFromEntity, fromEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseLegalEntity getToEntity() {
		if (toEntity != null && toEntity.eIsProxy()) {
			InternalEObject oldToEntity = (InternalEObject)toEntity;
			toEntity = (BaseLegalEntity)eResolveProxy(oldToEntity);
			if (toEntity != oldToEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY, oldToEntity, toEntity));
			}
		}
		return toEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetToEntity() {
		return toEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToEntity(BaseLegalEntity newToEntity) {
		BaseLegalEntity oldToEntity = toEntity;
		toEntity = newToEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY, oldToEntity, toEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferIncoterm getIncoterm() {
		return incoterm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncoterm(TransferIncoterm newIncoterm) {
		TransferIncoterm oldIncoterm = incoterm;
		incoterm = newIncoterm == null ? INCOTERM_EDEFAULT : newIncoterm;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__INCOTERM, oldIncoterm, incoterm));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompanyStatus getCompanyStatus() {
		return companyStatus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCompanyStatus(CompanyStatus newCompanyStatus) {
		CompanyStatus oldCompanyStatus = companyStatus;
		companyStatus = newCompanyStatus == null ? COMPANY_STATUS_EDEFAULT : newCompanyStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__COMPANY_STATUS, oldCompanyStatus, companyStatus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBufferDays() {
		return bufferDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBufferDays(int newBufferDays) {
		int oldBufferDays = bufferDays;
		bufferDays = newBufferDays;
		boolean oldBufferDaysESet = bufferDaysESet;
		bufferDaysESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS, oldBufferDays, bufferDays, !oldBufferDaysESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetBufferDays() {
		int oldBufferDays = bufferDays;
		boolean oldBufferDaysESet = bufferDaysESet;
		bufferDays = BUFFER_DAYS_EDEFAULT;
		bufferDaysESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS, oldBufferDays, BUFFER_DAYS_EDEFAULT, oldBufferDaysESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetBufferDays() {
		return bufferDaysESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessUnit getFromBU() {
		if (fromBU != null && fromBU.eIsProxy()) {
			InternalEObject oldFromBU = (InternalEObject)fromBU;
			fromBU = (BusinessUnit)eResolveProxy(oldFromBU);
			if (fromBU != oldFromBU) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_AGREEMENT__FROM_BU, oldFromBU, fromBU));
			}
		}
		return fromBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessUnit basicGetFromBU() {
		return fromBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromBU(BusinessUnit newFromBU) {
		BusinessUnit oldFromBU = fromBU;
		fromBU = newFromBU;
		boolean oldFromBUESet = fromBUESet;
		fromBUESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__FROM_BU, oldFromBU, fromBU, !oldFromBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFromBU() {
		BusinessUnit oldFromBU = fromBU;
		boolean oldFromBUESet = fromBUESet;
		fromBU = null;
		fromBUESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_AGREEMENT__FROM_BU, oldFromBU, null, oldFromBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFromBU() {
		return fromBUESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessUnit getToBU() {
		if (toBU != null && toBU.eIsProxy()) {
			InternalEObject oldToBU = (InternalEObject)toBU;
			toBU = (BusinessUnit)eResolveProxy(oldToBU);
			if (toBU != oldToBU) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_AGREEMENT__TO_BU, oldToBU, toBU));
			}
		}
		return toBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessUnit basicGetToBU() {
		return toBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToBU(BusinessUnit newToBU) {
		BusinessUnit oldToBU = toBU;
		toBU = newToBU;
		boolean oldToBUESet = toBUESet;
		toBUESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__TO_BU, oldToBU, toBU, !oldToBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetToBU() {
		BusinessUnit oldToBU = toBU;
		boolean oldToBUESet = toBUESet;
		toBU = null;
		toBUESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_AGREEMENT__TO_BU, oldToBU, null, oldToBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetToBU() {
		return toBUESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCode(String newCode) {
		String oldCode = code;
		code = newCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__CODE, oldCode, code));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PreferredFormulaeWrapper> getPreferredFormulae() {
		if (preferredFormulae == null) {
			preferredFormulae = new EObjectContainmentEList<PreferredFormulaeWrapper>(PreferredFormulaeWrapper.class, this, TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE);
		}
		return preferredFormulae;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BusinessUnit getAgreementOrDelegateFromBU() {
		return (BusinessUnit) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BusinessUnit getAgreementOrDelegateToBU() {
		return (BusinessUnit) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE:
				return ((InternalEList<?>)getPreferredFormulae()).basicRemove(otherEnd, msgs);
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
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY:
				if (resolve) return getFromEntity();
				return basicGetFromEntity();
			case TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY:
				if (resolve) return getToEntity();
				return basicGetToEntity();
			case TransfersPackage.TRANSFER_AGREEMENT__PRICE_EXPRESSION:
				return getPriceExpression();
			case TransfersPackage.TRANSFER_AGREEMENT__INCOTERM:
				return getIncoterm();
			case TransfersPackage.TRANSFER_AGREEMENT__COMPANY_STATUS:
				return getCompanyStatus();
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				return getBufferDays();
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_BU:
				if (resolve) return getFromBU();
				return basicGetFromBU();
			case TransfersPackage.TRANSFER_AGREEMENT__TO_BU:
				if (resolve) return getToBU();
				return basicGetToBU();
			case TransfersPackage.TRANSFER_AGREEMENT__CODE:
				return getCode();
			case TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE:
				return getPreferredFormulae();
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
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY:
				setFromEntity((BaseLegalEntity)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY:
				setToEntity((BaseLegalEntity)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__INCOTERM:
				setIncoterm((TransferIncoterm)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__COMPANY_STATUS:
				setCompanyStatus((CompanyStatus)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				setBufferDays((Integer)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_BU:
				setFromBU((BusinessUnit)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__TO_BU:
				setToBU((BusinessUnit)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__CODE:
				setCode((String)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE:
				getPreferredFormulae().clear();
				getPreferredFormulae().addAll((Collection<? extends PreferredFormulaeWrapper>)newValue);
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
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY:
				setFromEntity((BaseLegalEntity)null);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY:
				setToEntity((BaseLegalEntity)null);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__INCOTERM:
				setIncoterm(INCOTERM_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__COMPANY_STATUS:
				setCompanyStatus(COMPANY_STATUS_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				unsetBufferDays();
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_BU:
				unsetFromBU();
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__TO_BU:
				unsetToBU();
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__CODE:
				setCode(CODE_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE:
				getPreferredFormulae().clear();
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
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_ENTITY:
				return fromEntity != null;
			case TransfersPackage.TRANSFER_AGREEMENT__TO_ENTITY:
				return toEntity != null;
			case TransfersPackage.TRANSFER_AGREEMENT__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
			case TransfersPackage.TRANSFER_AGREEMENT__INCOTERM:
				return incoterm != INCOTERM_EDEFAULT;
			case TransfersPackage.TRANSFER_AGREEMENT__COMPANY_STATUS:
				return companyStatus != COMPANY_STATUS_EDEFAULT;
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				return isSetBufferDays();
			case TransfersPackage.TRANSFER_AGREEMENT__FROM_BU:
				return isSetFromBU();
			case TransfersPackage.TRANSFER_AGREEMENT__TO_BU:
				return isSetToBU();
			case TransfersPackage.TRANSFER_AGREEMENT__CODE:
				return CODE_EDEFAULT == null ? code != null : !CODE_EDEFAULT.equals(code);
			case TransfersPackage.TRANSFER_AGREEMENT__PREFERRED_FORMULAE:
				return preferredFormulae != null && !preferredFormulae.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case TransfersPackage.TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_FROM_BU:
				return getAgreementOrDelegateFromBU();
			case TransfersPackage.TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_TO_BU:
				return getAgreementOrDelegateToBU();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (priceExpression: ");
		result.append(priceExpression);
		result.append(", incoterm: ");
		result.append(incoterm);
		result.append(", companyStatus: ");
		result.append(companyStatus);
		result.append(", bufferDays: ");
		if (bufferDaysESet) result.append(bufferDays); else result.append("<unset>");
		result.append(", code: ");
		result.append(code);
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		final TransfersPackage transferPackage = TransfersPackage.eINSTANCE;
		if (transferPackage.getTransferAgreement_FromBU() == feature) {
			return new DelegateInformation(null, null, null) {

				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU);
				}

				@Override
				public Object getValue(final EObject object) {
					if (getFromEntity() != null) {
						final BaseLegalEntity entity = getFromEntity();
						if (entity.getBusinessUnits() != null && !entity.getBusinessUnits().isEmpty()) {
							for (final var bu : entity.getBusinessUnits()) {
								if (bu.isDefault()) {
									return bu;
								}
							}
							return entity.getBusinessUnits().get(0);
						}
					}
					return ECollections.emptyEList();
				}
			};
		} else if (transferPackage.getTransferAgreement_ToBU() == feature) {
			return new DelegateInformation(null, null, null) {

				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_BU);
				}

				@Override
				public Object getValue(final EObject object) {
					if (getToEntity() != null) {
						final BaseLegalEntity entity = getToEntity();
						if (entity.getBusinessUnits() != null && !entity.getBusinessUnits().isEmpty()) {
							for (final var bu : entity.getBusinessUnits()) {
								if (bu.isDefault()) {
									return bu;
								}
							}
							return entity.getBusinessUnits().get(0);
						}
					}
					return ECollections.emptyEList();
				}
			};
		}
		return super.getUnsetValueOrDelegate(feature);
	}

} //TransferAgreementImpl
