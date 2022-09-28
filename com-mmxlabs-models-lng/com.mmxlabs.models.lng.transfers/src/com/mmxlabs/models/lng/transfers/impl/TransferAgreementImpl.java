/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.lng.transfers.TransfersPackage;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getPricingBasis <em>Pricing Basis</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl#getBufferDays <em>Buffer Days</em>}</li>
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
	 * The default value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICING_BASIS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected String pricingBasis = PRICING_BASIS_EDEFAULT;

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
	public String getPricingBasis() {
		return pricingBasis;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingBasis(String newPricingBasis) {
		String oldPricingBasis = pricingBasis;
		pricingBasis = newPricingBasis;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_AGREEMENT__PRICING_BASIS, oldPricingBasis, pricingBasis));
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
			case TransfersPackage.TRANSFER_AGREEMENT__PRICING_BASIS:
				return getPricingBasis();
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				return getBufferDays();
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
			case TransfersPackage.TRANSFER_AGREEMENT__PRICING_BASIS:
				setPricingBasis((String)newValue);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				setBufferDays((Integer)newValue);
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
			case TransfersPackage.TRANSFER_AGREEMENT__PRICING_BASIS:
				setPricingBasis(PRICING_BASIS_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				unsetBufferDays();
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
			case TransfersPackage.TRANSFER_AGREEMENT__PRICING_BASIS:
				return PRICING_BASIS_EDEFAULT == null ? pricingBasis != null : !PRICING_BASIS_EDEFAULT.equals(pricingBasis);
			case TransfersPackage.TRANSFER_AGREEMENT__BUFFER_DAYS:
				return isSetBufferDays();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (priceExpression: ");
		result.append(priceExpression);
		result.append(", incoterm: ");
		result.append(incoterm);
		result.append(", companyStatus: ");
		result.append(companyStatus);
		result.append(", pricingBasis: ");
		result.append(pricingBasis);
		result.append(", bufferDays: ");
		if (bufferDaysESet) result.append(bufferDays); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //TransferAgreementImpl