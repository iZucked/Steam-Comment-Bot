/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getCharterInRate <em>Charter In Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getCharterContract <em>Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#isNominal <em>Nominal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#isMtm <em>Mtm</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterInMarketImpl extends SpotCharterMarketImpl implements CharterInMarket {
	/**
	 * The cached value of the '{@link #getExtensions() <em>Extensions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensions()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> extensions;

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
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The default value of the '{@link #getCharterInRate() <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected static final String CHARTER_IN_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCharterInRate() <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected String charterInRate = CHARTER_IN_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_CHARTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected int spotCharterCount = SPOT_CHARTER_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #isOverrideInaccessibleRoutes() <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOverrideInaccessibleRoutes() <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected boolean overrideInaccessibleRoutes = OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInaccessibleRoutes() <em>Inaccessible Routes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteOption> inaccessibleRoutes;

	/**
	 * The cached value of the '{@link #getCharterContract() <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterContract()
	 * @generated
	 * @ordered
	 */
	protected CharterContract charterContract;

	/**
	 * This is true if the Charter Contract reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterContractESet;

	/**
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final String REPOSITIONING_FEE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected String repositioningFee = REPOSITIONING_FEE_EDEFAULT;

	/**
	 * The default value of the '{@link #isNominal() <em>Nominal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNominal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NOMINAL_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isNominal() <em>Nominal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNominal()
	 * @generated
	 * @ordered
	 */
	protected boolean nominal = NOMINAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected int minDuration = MIN_DURATION_EDEFAULT;

	/**
	 * This is true if the Min Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minDurationESet;

	/**
	 * The default value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected int maxDuration = MAX_DURATION_EDEFAULT;

	/**
	 * This is true if the Max Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxDurationESet;

	/**
	 * The default value of the '{@link #isMtm() <em>Mtm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMtm()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MTM_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMtm() <em>Mtm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMtm()
	 * @generated
	 * @ordered
	 */
	protected boolean mtm = MTM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterInMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_IN_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EObject> getExtensions() {
		if (extensions == null) {
			extensions = new EObjectContainmentEList<EObject>(EObject.class, this, SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS);
		}
		return extensions;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isNominal() {
		return nominal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominal(boolean newNominal) {
		boolean oldNominal = nominal;
		nominal = newNominal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__NOMINAL, oldNominal, nominal));
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinDuration() {
		return minDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinDuration(int newMinDuration) {
		int oldMinDuration = minDuration;
		minDuration = newMinDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinDuration() {
		int oldMinDuration = minDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDuration = MIN_DURATION_EDEFAULT;
		minDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinDuration() {
		return minDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxDuration(int newMaxDuration) {
		int oldMaxDuration = maxDuration;
		maxDuration = newMaxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxDuration() {
		int oldMaxDuration = maxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDuration = MAX_DURATION_EDEFAULT;
		maxDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxDuration() {
		return maxDurationESet;
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isMtm() {
		return mtm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMtm(boolean newMtm) {
		boolean oldMtm = mtm;
		mtm = newMtm;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__MTM, oldMtm, mtm));
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getMarketOrContractMinDuration() {
		return (Integer) eGetWithDefault(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MIN_DURATION);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getMarketOrContractMaxDuration() {
		return (Integer) eGetWithDefault(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MAX_DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOverrideInaccessibleRoutes() {
		return overrideInaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOverrideInaccessibleRoutes(boolean newOverrideInaccessibleRoutes) {
		boolean oldOverrideInaccessibleRoutes = overrideInaccessibleRoutes;
		overrideInaccessibleRoutes = newOverrideInaccessibleRoutes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES, oldOverrideInaccessibleRoutes, overrideInaccessibleRoutes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RouteOption> getInaccessibleRoutes() {
		if (inaccessibleRoutes == null) {
			inaccessibleRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES);
		}
		return inaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterContract getCharterContract() {
		if (charterContract != null && charterContract.eIsProxy()) {
			InternalEObject oldCharterContract = (InternalEObject)charterContract;
			charterContract = (CharterContract)eResolveProxy(oldCharterContract);
			if (charterContract != oldCharterContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT, oldCharterContract, charterContract));
			}
		}
		return charterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterContract basicGetCharterContract() {
		return charterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterContract(CharterContract newCharterContract) {
		CharterContract oldCharterContract = charterContract;
		charterContract = newCharterContract;
		boolean oldCharterContractESet = charterContractESet;
		charterContractESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT, oldCharterContract, charterContract, !oldCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCharterContract() {
		CharterContract oldCharterContract = charterContract;
		boolean oldCharterContractESet = charterContractESet;
		charterContract = null;
		charterContractESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT, oldCharterContract, null, oldCharterContractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCharterContract() {
		return charterContractESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRepositioningFee(String newRepositioningFee) {
		String oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCharterInRate() {
		return charterInRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterInRate(String newCharterInRate) {
		String oldCharterInRate = charterInRate;
		charterInRate = newCharterInRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE, oldCharterInRate, charterInRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object getUnsetValue(EStructuralFeature feature) {
		DelegateInformation dfi = getUnsetValueOrDelegate(feature);
		if (dfi != null) {
			return dfi.getValue(this);
		}
		else {
			return eGet(feature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object eGetWithDefault(EStructuralFeature feature) {
		
		if (feature.isUnsettable() && !eIsSet(feature)) {
			return getUnsetValue(feature);
		} else {
			return eGet(feature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EObject eContainerOp() {
		 return eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return ((InternalEList<?>)getExtensions()).basicRemove(otherEnd, msgs);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return getExtensions();
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				return getName();
			case SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				return getCharterInRate();
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				return isOverrideInaccessibleRoutes();
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				return getInaccessibleRoutes();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				if (resolve) return getCharterContract();
				return basicGetCharterContract();
			case SpotMarketsPackage.CHARTER_IN_MARKET__REPOSITIONING_FEE:
				return getRepositioningFee();
			case SpotMarketsPackage.CHARTER_IN_MARKET__NOMINAL:
				return isNominal();
			case SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION:
				return getMinDuration();
			case SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION:
				return getMaxDuration();
			case SpotMarketsPackage.CHARTER_IN_MARKET__MTM:
				return isMtm();
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				getExtensions().clear();
				getExtensions().addAll((Collection<? extends EObject>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				setName((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				setCharterInRate((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				setOverrideInaccessibleRoutes((Boolean)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				getInaccessibleRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				setCharterContract((CharterContract)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__REPOSITIONING_FEE:
				setRepositioningFee((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NOMINAL:
				setNominal((Boolean)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION:
				setMaxDuration((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MTM:
				setMtm((Boolean)newValue);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				getExtensions().clear();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL:
				setVessel((Vessel)null);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				setCharterInRate(CHARTER_IN_RATE_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				setOverrideInaccessibleRoutes(OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				unsetCharterContract();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__REPOSITIONING_FEE:
				setRepositioningFee(REPOSITIONING_FEE_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NOMINAL:
				setNominal(NOMINAL_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION:
				unsetMinDuration();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION:
				unsetMaxDuration();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MTM:
				setMtm(MTM_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return extensions != null && !extensions.isEmpty();
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SpotMarketsPackage.CHARTER_IN_MARKET__VESSEL:
				return vessel != null;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				return CHARTER_IN_RATE_EDEFAULT == null ? charterInRate != null : !CHARTER_IN_RATE_EDEFAULT.equals(charterInRate);
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				return overrideInaccessibleRoutes != OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				return inaccessibleRoutes != null && !inaccessibleRoutes.isEmpty();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				return isSetCharterContract();
			case SpotMarketsPackage.CHARTER_IN_MARKET__REPOSITIONING_FEE:
				return REPOSITIONING_FEE_EDEFAULT == null ? repositioningFee != null : !REPOSITIONING_FEE_EDEFAULT.equals(repositioningFee);
			case SpotMarketsPackage.CHARTER_IN_MARKET__NOMINAL:
				return nominal != NOMINAL_EDEFAULT;
			case SpotMarketsPackage.CHARTER_IN_MARKET__MIN_DURATION:
				return isSetMinDuration();
			case SpotMarketsPackage.CHARTER_IN_MARKET__MAX_DURATION:
				return isSetMaxDuration();
			case SpotMarketsPackage.CHARTER_IN_MARKET__MTM:
				return mtm != MTM_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == VesselAssignmentType.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == MMXObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS: return MMXCorePackage.MMX_OBJECT__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_IN_MARKET__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == VesselAssignmentType.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == MMXObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.MMX_OBJECT__EXTENSIONS: return SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return SpotMarketsPackage.CHARTER_IN_MARKET__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", charterInRate: ");
		result.append(charterInRate);
		result.append(", spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(", overrideInaccessibleRoutes: ");
		result.append(overrideInaccessibleRoutes);
		result.append(", inaccessibleRoutes: ");
		result.append(inaccessibleRoutes);
		result.append(", repositioningFee: ");
		result.append(repositioningFee);
		result.append(", nominal: ");
		result.append(nominal);
		result.append(", minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(", mtm: ");
		result.append(mtm);
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		SpotMarketsPackage marketsPackage = SpotMarketsPackage.eINSTANCE;
		CommercialPackage commercial = CommercialPackage.eINSTANCE;
		if (marketsPackage.getCharterInMarket_MinDuration() == feature) {
			return new DelegateInformation(marketsPackage.getCharterInMarket_CharterContract(), commercial.getCharterContract_MinDuration(), (Integer) 0);
		} else if (marketsPackage.getCharterInMarket_MaxDuration() == feature) {
			return new DelegateInformation(marketsPackage.getCharterInMarket_CharterContract(), commercial.getCharterContract_MaxDuration(), (Integer) 0);
		}
		
		return new DelegateInformation(null, null, null);
	}

} //CharterInMarketImpl
