/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.AnnotatedObject;
import scenario.ScenarioPackage;
import scenario.fleet.FleetPackage;
import scenario.fleet.PortExclusion;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselStateAttributes;
import scenario.impl.NamedObjectImpl;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getMinHeelVolume <em>Min Heel Volume</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getDailyCharterInPrice <em>Daily Charter In Price</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getCanalCosts <em>Canal Costs</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getWarmupTime <em>Warmup Time</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getCooldownTime <em>Cooldown Time</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassImpl#getCooldownVolume <em>Cooldown Volume</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselClassImpl extends NamedObjectImpl implements VesselClass {
	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final long CAPACITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected long capacity = CAPACITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final float MIN_SPEED_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getMinSpeed() <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSpeed()
	 * @generated
	 * @ordered
	 */
	protected float minSpeed = MIN_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final float MAX_SPEED_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getMaxSpeed() <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSpeed()
	 * @generated
	 * @ordered
	 */
	protected float maxSpeed = MAX_SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinHeelVolume() <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long MIN_HEEL_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMinHeelVolume() <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelVolume()
	 * @generated
	 * @ordered
	 */
	protected long minHeelVolume = MIN_HEEL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final Double FILL_CAPACITY_EDEFAULT = new Double(0.958);

	/**
	 * The cached value of the '{@link #getFillCapacity() <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFillCapacity()
	 * @generated
	 * @ordered
	 */
	protected Double fillCapacity = FILL_CAPACITY_EDEFAULT;

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
	 * The default value of the '{@link #getDailyCharterInPrice() <em>Daily Charter In Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterInPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_CHARTER_IN_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyCharterInPrice() <em>Daily Charter In Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterInPrice()
	 * @generated
	 * @ordered
	 */
	protected int dailyCharterInPrice = DAILY_CHARTER_IN_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_CHARTER_OUT_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected int dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;

	/**
	 * This is true if the Daily Charter Out Price attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean dailyCharterOutPriceESet;

	/**
	 * The cached value of the '{@link #getLadenAttributes() <em>Laden Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ladenAttributes;

	/**
	 * The cached value of the '{@link #getBallastAttributes() <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastAttributes()
	 * @generated
	 * @ordered
	 */
	protected VesselStateAttributes ballastAttributes;

	/**
	 * The cached value of the '{@link #getBaseFuel() <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuel()
	 * @generated
	 * @ordered
	 */
	protected VesselFuel baseFuel;

	/**
	 * The default value of the '{@link #getPilotLightRate() <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotLightRate()
	 * @generated
	 * @ordered
	 */
	protected static final int PILOT_LIGHT_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPilotLightRate() <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPilotLightRate()
	 * @generated
	 * @ordered
	 */
	protected int pilotLightRate = PILOT_LIGHT_RATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInaccessiblePorts() <em>Inaccessible Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessiblePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> inaccessiblePorts;

	/**
	 * The cached value of the '{@link #getCanalCosts() <em>Canal Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClassCost> canalCosts;

	/**
	 * The default value of the '{@link #getWarmupTime() <em>Warmup Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWarmupTime()
	 * @generated
	 * @ordered
	 */
	protected static final int WARMUP_TIME_EDEFAULT = 24;

	/**
	 * The cached value of the '{@link #getWarmupTime() <em>Warmup Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWarmupTime()
	 * @generated
	 * @ordered
	 */
	protected int warmupTime = WARMUP_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCooldownTime() <em>Cooldown Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownTime()
	 * @generated
	 * @ordered
	 */
	protected static final int COOLDOWN_TIME_EDEFAULT = 12;

	/**
	 * The cached value of the '{@link #getCooldownTime() <em>Cooldown Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownTime()
	 * @generated
	 * @ordered
	 */
	protected int cooldownTime = COOLDOWN_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCooldownVolume() <em>Cooldown Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int COOLDOWN_VOLUME_EDEFAULT = 500;

	/**
	 * The cached value of the '{@link #getCooldownVolume() <em>Cooldown Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownVolume()
	 * @generated
	 * @ordered
	 */
	protected int cooldownVolume = COOLDOWN_VOLUME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_CLASS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getCapacity() {
		return capacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapacity(long newCapacity) {
		long oldCapacity = capacity;
		capacity = newCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__CAPACITY, oldCapacity, capacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMinSpeed() {
		return minSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinSpeed(float newMinSpeed) {
		float oldMinSpeed = minSpeed;
		minSpeed = newMinSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_SPEED, oldMinSpeed, minSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxSpeed(float newMaxSpeed) {
		float oldMaxSpeed = maxSpeed;
		maxSpeed = newMaxSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MAX_SPEED, oldMaxSpeed, maxSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getLadenAttributes() {
		if (ladenAttributes != null && ladenAttributes.eIsProxy()) {
			InternalEObject oldLadenAttributes = (InternalEObject)ladenAttributes;
			ladenAttributes = (VesselStateAttributes)eResolveProxy(oldLadenAttributes);
			if (ladenAttributes != oldLadenAttributes) {
				InternalEObject newLadenAttributes = (InternalEObject)ladenAttributes;
				NotificationChain msgs = oldLadenAttributes.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, null);
				if (newLadenAttributes.eInternalContainer() == null) {
					msgs = newLadenAttributes.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, oldLadenAttributes, ladenAttributes));
			}
		}
		return ladenAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes basicGetLadenAttributes() {
		return ladenAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLadenAttributes(VesselStateAttributes newLadenAttributes, NotificationChain msgs) {
		VesselStateAttributes oldLadenAttributes = ladenAttributes;
		ladenAttributes = newLadenAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, oldLadenAttributes, newLadenAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenAttributes(VesselStateAttributes newLadenAttributes) {
		if (newLadenAttributes != ladenAttributes) {
			NotificationChain msgs = null;
			if (ladenAttributes != null)
				msgs = ((InternalEObject)ladenAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			if (newLadenAttributes != null)
				msgs = ((InternalEObject)newLadenAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, null, msgs);
			msgs = basicSetLadenAttributes(newLadenAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES, newLadenAttributes, newLadenAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes getBallastAttributes() {
		if (ballastAttributes != null && ballastAttributes.eIsProxy()) {
			InternalEObject oldBallastAttributes = (InternalEObject)ballastAttributes;
			ballastAttributes = (VesselStateAttributes)eResolveProxy(oldBallastAttributes);
			if (ballastAttributes != oldBallastAttributes) {
				InternalEObject newBallastAttributes = (InternalEObject)ballastAttributes;
				NotificationChain msgs = oldBallastAttributes.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, null);
				if (newBallastAttributes.eInternalContainer() == null) {
					msgs = newBallastAttributes.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, oldBallastAttributes, ballastAttributes));
			}
		}
		return ballastAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselStateAttributes basicGetBallastAttributes() {
		return ballastAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBallastAttributes(VesselStateAttributes newBallastAttributes, NotificationChain msgs) {
		VesselStateAttributes oldBallastAttributes = ballastAttributes;
		ballastAttributes = newBallastAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, oldBallastAttributes, newBallastAttributes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastAttributes(VesselStateAttributes newBallastAttributes) {
		if (newBallastAttributes != ballastAttributes) {
			NotificationChain msgs = null;
			if (ballastAttributes != null)
				msgs = ((InternalEObject)ballastAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			if (newBallastAttributes != null)
				msgs = ((InternalEObject)newBallastAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, null, msgs);
			msgs = basicSetBallastAttributes(newBallastAttributes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES, newBallastAttributes, newBallastAttributes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMinHeelVolume() {
		return minHeelVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinHeelVolume(long newMinHeelVolume) {
		long oldMinHeelVolume = minHeelVolume;
		minHeelVolume = newMinHeelVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME, oldMinHeelVolume, minHeelVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getFillCapacity() {
		return fillCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFillCapacity(Double newFillCapacity) {
		Double oldFillCapacity = fillCapacity;
		fillCapacity = newFillCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__FILL_CAPACITY, oldFillCapacity, fillCapacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDailyCharterOutPrice() {
		return dailyCharterOutPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDailyCharterOutPrice(int newDailyCharterOutPrice) {
		int oldDailyCharterOutPrice = dailyCharterOutPrice;
		dailyCharterOutPrice = newDailyCharterOutPrice;
		boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPriceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, dailyCharterOutPrice, !oldDailyCharterOutPriceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDailyCharterOutPrice() {
		int oldDailyCharterOutPrice = dailyCharterOutPrice;
		boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;
		dailyCharterOutPriceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, DAILY_CHARTER_OUT_PRICE_EDEFAULT, oldDailyCharterOutPriceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDailyCharterOutPrice() {
		return dailyCharterOutPriceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDailyCharterInPrice() {
		return dailyCharterInPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDailyCharterInPrice(int newDailyCharterInPrice) {
		int oldDailyCharterInPrice = dailyCharterInPrice;
		dailyCharterInPrice = newDailyCharterInPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__DAILY_CHARTER_IN_PRICE, oldDailyCharterInPrice, dailyCharterInPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getInaccessiblePorts() {
		if (inaccessiblePorts == null) {
			inaccessiblePorts = new EObjectResolvingEList<Port>(Port.class, this, FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS);
		}
		return inaccessiblePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselClassCost> getCanalCosts() {
		if (canalCosts == null) {
			canalCosts = new EObjectContainmentEList.Resolving<VesselClassCost>(VesselClassCost.class, this, FleetPackage.VESSEL_CLASS__CANAL_COSTS);
		}
		return canalCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWarmupTime() {
		return warmupTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWarmupTime(int newWarmupTime) {
		int oldWarmupTime = warmupTime;
		warmupTime = newWarmupTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__WARMUP_TIME, oldWarmupTime, warmupTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCooldownTime() {
		return cooldownTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCooldownTime(int newCooldownTime) {
		int oldCooldownTime = cooldownTime;
		cooldownTime = newCooldownTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__COOLDOWN_TIME, oldCooldownTime, cooldownTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCooldownVolume() {
		return cooldownVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCooldownVolume(int newCooldownVolume) {
		int oldCooldownVolume = cooldownVolume;
		cooldownVolume = newCooldownVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__COOLDOWN_VOLUME, oldCooldownVolume, cooldownVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselFuel getBaseFuel() {
		if (baseFuel != null && baseFuel.eIsProxy()) {
			InternalEObject oldBaseFuel = (InternalEObject)baseFuel;
			baseFuel = (VesselFuel)eResolveProxy(oldBaseFuel);
			if (baseFuel != oldBaseFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_CLASS__BASE_FUEL, oldBaseFuel, baseFuel));
			}
		}
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselFuel basicGetBaseFuel() {
		return baseFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseFuel(VesselFuel newBaseFuel) {
		VesselFuel oldBaseFuel = baseFuel;
		baseFuel = newBaseFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__BASE_FUEL, oldBaseFuel, baseFuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPilotLightRate() {
		return pilotLightRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPilotLightRate(int newPilotLightRate) {
		int oldPilotLightRate = pilotLightRate;
		pilotLightRate = newPilotLightRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE, oldPilotLightRate, pilotLightRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return basicSetLadenAttributes(null, msgs);
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return basicSetBallastAttributes(null, msgs);
			case FleetPackage.VESSEL_CLASS__CANAL_COSTS:
				return ((InternalEList<?>)getCanalCosts()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.VESSEL_CLASS__NOTES:
				return getNotes();
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return getCapacity();
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return getMinSpeed();
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return getMaxSpeed();
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				return getMinHeelVolume();
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return getFillCapacity();
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_IN_PRICE:
				return getDailyCharterInPrice();
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE:
				return getDailyCharterOutPrice();
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				if (resolve) return getLadenAttributes();
				return basicGetLadenAttributes();
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				if (resolve) return getBallastAttributes();
				return basicGetBallastAttributes();
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				if (resolve) return getBaseFuel();
				return basicGetBaseFuel();
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				return getPilotLightRate();
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return getInaccessiblePorts();
			case FleetPackage.VESSEL_CLASS__CANAL_COSTS:
				return getCanalCosts();
			case FleetPackage.VESSEL_CLASS__WARMUP_TIME:
				return getWarmupTime();
			case FleetPackage.VESSEL_CLASS__COOLDOWN_TIME:
				return getCooldownTime();
			case FleetPackage.VESSEL_CLASS__COOLDOWN_VOLUME:
				return getCooldownVolume();
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
			case FleetPackage.VESSEL_CLASS__NOTES:
				setNotes((String)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity((Long)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed((Float)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed((Float)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				setMinHeelVolume((Long)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity((Double)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_IN_PRICE:
				setDailyCharterInPrice((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE:
				setDailyCharterOutPrice((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				setBaseFuel((VesselFuel)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				setPilotLightRate((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				getInaccessiblePorts().addAll((Collection<? extends Port>)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__CANAL_COSTS:
				getCanalCosts().clear();
				getCanalCosts().addAll((Collection<? extends VesselClassCost>)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__WARMUP_TIME:
				setWarmupTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_TIME:
				setCooldownTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_VOLUME:
				setCooldownVolume((Integer)newValue);
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
			case FleetPackage.VESSEL_CLASS__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				setCapacity(CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				setMinSpeed(MIN_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				setMaxSpeed(MAX_SPEED_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				setMinHeelVolume(MIN_HEEL_VOLUME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				setFillCapacity(FILL_CAPACITY_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_IN_PRICE:
				setDailyCharterInPrice(DAILY_CHARTER_IN_PRICE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE:
				unsetDailyCharterOutPrice();
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				setLadenAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				setBallastAttributes((VesselStateAttributes)null);
				return;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				setBaseFuel((VesselFuel)null);
				return;
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				setPilotLightRate(PILOT_LIGHT_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				getInaccessiblePorts().clear();
				return;
			case FleetPackage.VESSEL_CLASS__CANAL_COSTS:
				getCanalCosts().clear();
				return;
			case FleetPackage.VESSEL_CLASS__WARMUP_TIME:
				setWarmupTime(WARMUP_TIME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_TIME:
				setCooldownTime(COOLDOWN_TIME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_VOLUME:
				setCooldownVolume(COOLDOWN_VOLUME_EDEFAULT);
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
			case FleetPackage.VESSEL_CLASS__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case FleetPackage.VESSEL_CLASS__CAPACITY:
				return capacity != CAPACITY_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
				return minSpeed != MIN_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
				return maxSpeed != MAX_SPEED_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
				return minHeelVolume != MIN_HEEL_VOLUME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
				return FILL_CAPACITY_EDEFAULT == null ? fillCapacity != null : !FILL_CAPACITY_EDEFAULT.equals(fillCapacity);
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_IN_PRICE:
				return dailyCharterInPrice != DAILY_CHARTER_IN_PRICE_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE:
				return isSetDailyCharterOutPrice();
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
				return ladenAttributes != null;
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
				return ballastAttributes != null;
			case FleetPackage.VESSEL_CLASS__BASE_FUEL:
				return baseFuel != null;
			case FleetPackage.VESSEL_CLASS__PILOT_LIGHT_RATE:
				return pilotLightRate != PILOT_LIGHT_RATE_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__INACCESSIBLE_PORTS:
				return inaccessiblePorts != null && !inaccessiblePorts.isEmpty();
			case FleetPackage.VESSEL_CLASS__CANAL_COSTS:
				return canalCosts != null && !canalCosts.isEmpty();
			case FleetPackage.VESSEL_CLASS__WARMUP_TIME:
				return warmupTime != WARMUP_TIME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_TIME:
				return cooldownTime != COOLDOWN_TIME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS__COOLDOWN_VOLUME:
				return cooldownVolume != COOLDOWN_VOLUME_EDEFAULT;
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
		if (baseClass == AnnotatedObject.class) {
			switch (derivedFeatureID) {
				case FleetPackage.VESSEL_CLASS__NOTES: return ScenarioPackage.ANNOTATED_OBJECT__NOTES;
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
		if (baseClass == AnnotatedObject.class) {
			switch (baseFeatureID) {
				case ScenarioPackage.ANNOTATED_OBJECT__NOTES: return FleetPackage.VESSEL_CLASS__NOTES;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (notes: ");
		result.append(notes);
		result.append(", capacity: ");
		result.append(capacity);
		result.append(", minSpeed: ");
		result.append(minSpeed);
		result.append(", maxSpeed: ");
		result.append(maxSpeed);
		result.append(", minHeelVolume: ");
		result.append(minHeelVolume);
		result.append(", fillCapacity: ");
		result.append(fillCapacity);
		result.append(", spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(", dailyCharterInPrice: ");
		result.append(dailyCharterInPrice);
		result.append(", dailyCharterOutPrice: ");
		if (dailyCharterOutPriceESet) result.append(dailyCharterOutPrice); else result.append("<unset>");
		result.append(", pilotLightRate: ");
		result.append(pilotLightRate);
		result.append(", warmupTime: ");
		result.append(warmupTime);
		result.append(", cooldownTime: ");
		result.append(cooldownTime);
		result.append(", cooldownVolume: ");
		result.append(cooldownVolume);
		result.append(')');
		return result.toString();
	}

} //VesselClassImpl
