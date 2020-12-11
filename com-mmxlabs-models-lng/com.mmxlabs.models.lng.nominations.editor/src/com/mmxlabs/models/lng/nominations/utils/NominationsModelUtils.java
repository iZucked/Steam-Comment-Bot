/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.DatePeriodPrior;
import com.mmxlabs.models.lng.nominations.NominationsFactory;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.nominations.presentation.composites.TimeWindowHolder;
import com.mmxlabs.models.lng.nominations.util.GeneratedNominationsProvider;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class NominationsModelUtils {

	private static final NumberFormat volumeFormatter = NumberFormat.getIntegerInstance();
	
	private NominationsModelUtils() {
		//Do not construct instances of this class.
	}
	
	public static NominationsModel createNominationsModel() {
		final NominationsModel nominationsModel = NominationsFactory.eINSTANCE.createNominationsModel();
		nominationsModel.setNominationParameters(NominationsFactory.eINSTANCE.createNominationsParameters());
		return nominationsModel;
	}

	public static @NonNull GeneratedNominationsProvider getGeneratedNominationsProvider(IScenarioDataProvider scenarioDataProvider) {
		if (scenarioDataProvider != null) {
			return scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.GENERATED_NOMINATIONS, GeneratedNominationsProvider.class);
		}
		throw new IllegalStateException("Unable to get generated nominations provider");
	}
	
	public static String mapName(final DatePeriodPrior units) {
		switch (units) {
		case DAYS_PRIOR:
			return "Days prior";
		case MONTHS_PRIOR:
			return "Months prior";
		default:
			break;
		}
		return units.getName();
	}
	
	public static AbstractNomination findNominationForSlot(final IScenarioDataProvider sdp, final Slot<?> slot, final String nominationType) {
		if (sdp != null && slot != null && slot.getName() != null) {
			final String name = slot.getName();
			final NominationsModel nm = ScenarioModelUtil.getNominationsModel(sdp);

			// Check specific slot nominations.
			for (final AbstractNomination nomination : nm.getNominations()) {
				if (nomination.getSide() == Side.BUY && slot instanceof LoadSlot && Objects.equals(nomination.getNomineeId(), name) && Objects.equals(nomination.getType(), nominationType)) {
					return nomination;
				}
				if (nomination.getSide() == Side.SELL && slot instanceof DischargeSlot && Objects.equals(nomination.getNomineeId(), name) && Objects.equals(nomination.getType(), nominationType)) {
					return nomination;
				}
			}

			// If none there, check nominations generated from specifications.
			final List<AbstractNomination> generatedNominations = getGeneratedNominationsForAllDates(sdp);
			for (final AbstractNomination nomination : generatedNominations) {
				if (nomination.getSide() == Side.BUY && slot instanceof LoadSlot && Objects.equals(nomination.getNomineeId(), name) && Objects.equals(nomination.getType(), nominationType)) {
					return nomination;
				}
				if (nomination.getSide() == Side.SELL && slot instanceof DischargeSlot && Objects.equals(nomination.getNomineeId(), name) && Objects.equals(nomination.getType(), nominationType)) {
					return nomination;
				}
			}
		}

		// None found.
		return null;
	}

	public static List<AbstractNomination> findNominationsForSlot(final IScenarioDataProvider sdp, @Nullable final Slot<?> slot) {
		if (slot != null && slot.getName() != null) {
			final String name = slot.getName();
			return findNominationsForSlot(sdp, name);
		}

		// None found.
		return Collections.emptyList();
	}

	public static List<AbstractNomination> findNominationsForSlot(final IScenarioDataProvider sdp, final String name) {		
		if (name == null) { 
			return Collections.emptyList();
		}
		final NominationsModel nm = ScenarioModelUtil.getNominationsModel(sdp);
		final List<AbstractNomination> nominations = new ArrayList<>();

		// Check specific slot nominations.
		for (final AbstractNomination nomination : nm.getNominations()) {
			if (Objects.equals(nomination.getNomineeId(), name)) {
				nominations.add(nomination);
			}
		}

		// If none there, check nominations generated from specifications.
		final List<AbstractNomination> generatedNominations = getGeneratedNominationsForAllDates(sdp);
		for (final AbstractNomination nomination : generatedNominations) {
			if (Objects.equals(nomination.getNomineeId(), name)) {
				nominations.add(nomination);
			}
		}

		return nominations;
	}

	public static String mapName(final com.mmxlabs.models.lng.nominations.Side e) {
		return e.getName();
	}

	public static String getFrom(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		String from = "";

		if (!nomination.isCounterparty()) {
			from = NominationsModelUtils.getEntity(scenarioModel, nomination);
		} else {
			from = NominationsModelUtils.getCounterparty(scenarioModel, nomination);
		}
		if (from != null) {
			return from;
		} else {
			return "";
		}
	}

	public static String getTo(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		String to = "";

		if (nomination.isCounterparty()) { // NB: for to, it is the entity if isCounterparty is set (different from
											// getFrom() above).
			to = NominationsModelUtils.getEntity(scenarioModel, nomination);
		} else {
			to = NominationsModelUtils.getCounterparty(scenarioModel, nomination);
		}
		if (to != null) {
			return to;
		} else {
			return "";
		}
	}

	public static String getEntity(final LNGScenarioModel scenarioModel, final AbstractNomination nomination) {
		if (scenarioModel != null && nomination.getNomineeId() != null) {
			final Slot<?> slot = findSlot(scenarioModel, nomination);
			if (slot != null) {
				final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
				if (entity != null) {
					return entity.getName();
				}
			}
		}
		return null;
	}

	public static String getCounterparty(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		if (nomination.getNomineeId() != null) {
			final Slot<?> slot = findSlot(scenarioModel, nomination);
			if (slot != null) {
				return slot.getSlotOrDelegateCounterparty();
			}
		}
		return null;
	}

	public static String getCN(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		final Slot<?> slot = findSlot(scenarioModel, nomination);
		String cn = null;
		if (slot != null) {
			cn = slot.getSlotOrDelegateCN();
		}
		if (cn != null)
			return cn;
		else
			return "";
	}

	public static String getSide(@NonNull final AbstractNomination nomination) {
		if (nomination.getSide() == Side.BUY) {
			return "Buy";
		}
		else if (nomination.getSide() == Side.SELL) {
			return "Sell";
		}
		return "";
	}

	public static List<AbstractNomination> getGeneratedNominations(@NonNull final IScenarioEditingLocation jointModelEditor, final LocalDate startDate, final LocalDate endDate) {
		return getGeneratedNominations(jointModelEditor.getScenarioDataProvider(), startDate, endDate);
	}
	
	public static NominationsModel getNominationsModel(@NonNull final IScenarioEditingLocation jointModelEditor) {
		final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(jointModelEditor.getScenarioDataProvider());
		if (scenarioModel != null) {
			return scenarioModel.getNominationsModel();
		}
		//Otherwise doesn't exist.
		return null;
	}

	private static List<AbstractNomination> getGeneratedNominations(@NonNull final IScenarioDataProvider sdp, final LocalDate startDate, final LocalDate endDate) {
		List<AbstractNomination> generatedNominations = getGeneratedNominationsForAllDates(sdp);
		
		if (startDate == null && endDate == null) {
			return generatedNominations;
		}
		else {
			LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(sdp);
			final LocalDate startDate1 = startDate == null ? LocalDate.MIN : startDate;
			final LocalDate endDate1 = endDate == null ? LocalDate.MAX : endDate;
			return generatedNominations.stream().filter(n -> {
				if (n != null) {
					LocalDate date = getDate(scenarioModel, n);
					return date != null && !date.isBefore(startDate1) && !date.isAfter(endDate1); 
				}
				else {
					return false;
				}
			}).collect(Collectors.toList());
		}
	}

	public static List<AbstractNomination> getGeneratedNominationsForAllDates(IScenarioDataProvider sdp) {
		return getGeneratedNominationsProvider(sdp).getGeneratedNominations(NominationsModelUtils::generateNominationsFromSpecs);
	}
	
	/**
	 * @deprecated DO NOT USE DIRECTLY UNLESS (as can cause performance issues!!!), instead use static List<AbstractNomination> getGeneratedNominationsForAllDates(IScenarioDataProvider sdp) 
	 */
	@Deprecated(since="4.10.28", forRemoval=false)
	public static List<AbstractNomination> generateNominationsFromSpecs(final NominationsModel nominationsModel) {
		if (nominationsModel == null) {
			return Collections.emptyList();
		}

		LNGScenarioModel scenarioModel = (LNGScenarioModel)nominationsModel.eContainer();
		if (scenarioModel == null) {
			return Collections.emptyList();
		}
			
		// Get the cargo model, if none, we have no cargos/slots, so we can't generate
		// any nominations, so return an empty list.
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		if (cargoModel == null) {
			return Collections.emptyList();
		}

		// Generate the nominations.
		final List<AbstractNomination> nominations = new ArrayList<>();

		// Make a map of existing nominations, so we don't generate nominations, where
		// an existing generated nomination has been overridden.
		final HashMap<String, AbstractNomination> existingSlotNominations = new HashMap<>();
		for (final AbstractNomination n : nominationsModel.getNominations()) {
			existingSlotNominations.put(n.getSpecUuid() + ":" + n.getNomineeId(), n);
		}

		// Refresh.
		List<Slot<?>> slots = new ArrayList<>();
		slots.addAll(cargoModel.getLoadSlots());
		slots.addAll(cargoModel.getDischargeSlots());

		for (Slot<?> slot : slots) {
			if (slot == null || slot instanceof SpotSlot)
				continue;

			// Set nomination type as part of nomination id, so we know which sort of slot
			// made this nomination.
			Side side = null;
			if (slot instanceof DischargeSlot) {
				side = Side.SELL;
			} else if (slot instanceof LoadSlot) {
				side = Side.BUY;
			}
			final String nomineeId = slot.getName();
			final Contract contract = slot.getContract();
			String contractName = "";
			if (contract != null && contract.getName() != null) {
				contractName = contract.getName();
			}

			for (final AbstractNominationSpec sp : nominationsModel.getNominationSpecs()) {
				String refererId = "";
				if (sp.getRefererId() != null) {
					refererId = sp.getRefererId();
				}

				if (!Objects.equals(refererId, "") && sp.getSide() == side && Objects.equals(contractName, refererId)) {
					AbstractNomination sn = existingSlotNominations.get(sp.getUuid() + ":" + nomineeId);
					if (sn == null) {
						sn = NominationsFactory.eINSTANCE.createSlotNomination();
						populateNomination(sn, nomineeId, sp);
						nominations.add(sn);
					}
				}
			}
		}

		return nominations;
	}

	private static void populateNomination(@NonNull final AbstractNomination sn, final String nomineeId, @NonNull final AbstractNominationSpec sp) {

		// Populate the nomination specification fields.
		sn.setType(sp.getType());
		sn.setSide(sp.getSide());
		sn.setSize(sp.getSize());
		sn.setSizeUnits(sp.getSizeUnits());
		sn.setAlertSize(sp.getAlertSize());
		sn.setAlertSizeUnits(sp.getAlertSizeUnits());
		sn.setDayOfMonth(sp.getDayOfMonth());
		sn.setCounterparty(sp.isCounterparty());
		sn.setRemark(sp.getRemark());

		// Populate the nomination fields.
		sn.setNomineeId(nomineeId);
		sn.setSpecUuid(sp.getUuid());
		sn.setRefererId(sp.getRefererId());
		sn.setDone(false);
	}

	public static Slot<?> findSlot(LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		if (scenarioModel != null && nomination.getNomineeId() != null) {
			if (nomination.getSide() == Side.SELL) {
				return findDischargeSlot(scenarioModel, nomination);
			} else if (nomination.getSide() == Side.BUY) {
				return findLoadSlot(scenarioModel, nomination);
			}
		}
		return null;
	}

	public static @NonNull List<String> getPossibleSlotNames(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		final List<String> possibleSlotNames = new ArrayList<>();
		final CargoModel cm = scenarioModel.getCargoModel();
		if (cm != null) {
			if (nomination.getSide() == Side.BUY) {
				cm.getLoadSlots().forEach(s -> possibleSlotNames.add(s.getName()));
			} else if (nomination.getSide() == Side.SELL) {
				cm.getDischargeSlots().forEach(s -> possibleSlotNames.add(s.getName()));
			}
		}
		return possibleSlotNames;
	}

	public static LoadSlot findLoadSlot(final LNGScenarioModel scenarioModel, final AbstractNomination n) {
		LoadSlot slot = null;
		final String nomineeId = n.getNomineeId();
		slot = findLoadSlot(scenarioModel, nomineeId);
		return slot;
	}

	public static DischargeSlot findDischargeSlot(final LNGScenarioModel scenarioModel, final AbstractNomination n) {
		DischargeSlot slot = null;
		final String nomineeId = n.getNomineeId();
		slot = findDischargeSlot(scenarioModel, nomineeId);
		return slot;
	}

	public static LoadSlot findLoadSlot(@NonNull final LNGScenarioModel scenarioModel, @NonNull final String nomineeId) {
		final CargoModel model = ScenarioModelUtil.getCargoModel(scenarioModel);
		return model.getLoadSlotByName(nomineeId);
	}
	
	public static DischargeSlot findDischargeSlot(@NonNull final LNGScenarioModel scenarioModel, @NonNull final String nomineeId) {
		final CargoModel model = ScenarioModelUtil.getCargoModel(scenarioModel);
		return model.getDischargeSlotByName(nomineeId);
	}

	/**
	 * Gets the window start date for the slot associated with a nomination.
	 * 
	 * @param scenarioModel
	 * @param nomination
	 * @return window start date.
	 */
	public static LocalDate getDate(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination nomination) {
		final Slot<?> slot = findSlot(scenarioModel, nomination);
		if (slot != null) {
			return slot.getWindowStart();
		}
		return null;
	}

	private static AbstractNominationSpec getNominationSpec(@NonNull final LNGScenarioModel scenarioModel, final String uuid) {
		final NominationsModel nm = scenarioModel.getNominationsModel();
		if (nm != null && uuid != null) {
			final EList<AbstractNominationSpec> specs = nm.getNominationSpecs();
			for (final AbstractNominationSpec spec : specs) {
				if (Objects.equals(uuid, spec.getUuid())) {
					return spec;
				}
			}
		}
		return null;
	}

	private static LocalDate computeDueDate(@NonNull final LNGScenarioModel scenarioModel, @NonNull final LocalDate referenceDate, @NonNull final AbstractNomination sn) {
		LocalDate dueDate = sn.getDueDate();
		if (dueDate == null) {
			int size = sn.getSize();
			DatePeriodPrior sizeUnits = sn.getSizeUnits();
			int dayOfMonth = sn.getDayOfMonth();
			final String specUuid = sn.getSpecUuid();
			if (specUuid != null) {
				final AbstractNominationSpec spec = getNominationSpec(scenarioModel, specUuid);
				if (spec != null) {
					size = spec.getSize();
					sizeUnits = spec.getSizeUnits();
					dayOfMonth = spec.getDayOfMonth();
				}
			}
			dueDate = computePriorDate(referenceDate, size, sizeUnits);

			// If day of month set, then use it.
			if (dayOfMonth != 0) {
				dueDate = dueDate.withDayOfMonth(dayOfMonth);
			}
		}
		return dueDate;
	}

	public static LocalDate getDueDate(@NonNull final LNGScenarioModel scenarioModel, final AbstractNomination n) {
		final LocalDate date = getDate(scenarioModel, n);
		LocalDate dueDate = null;
		if (!n.isSetDueDate()) {
			if (date != null) {
				dueDate = computeDueDate(scenarioModel, date, n);
			}
		} else {
			dueDate = n.getDueDate();
		}
		return dueDate;
	}

	public static LocalDate getAlertDate(@NonNull final LNGScenarioModel scenarioModel, final AbstractNomination n) {
		final LocalDate date = getDate(scenarioModel, n);
		LocalDate alertDate = null;
		if (!n.isSetAlertDate()) {
			if (date != null) {
				alertDate = computeAlertDate(scenarioModel, date, n);
			}
		} else {
			alertDate = n.getAlertDate();
		}
		return alertDate;
	}

	private static LocalDate computeAlertDate(@NonNull final LNGScenarioModel scenarioModel, @NonNull final LocalDate referenceDate, @NonNull final AbstractNomination sn) {
		LocalDate alertDate = sn.getAlertDate();
		if (alertDate == null) {
			boolean alertSizeSet = sn.isSetAlertSize();
			int alertSize = sn.getAlertSize();
			DatePeriodPrior alertSizeUnits = sn.getAlertSizeUnits();
			final String specUuid = sn.getSpecUuid();
			if (specUuid != null) {
				final AbstractNominationSpec spec = getNominationSpec(scenarioModel, specUuid);
				if (spec != null && spec.isSetAlertSize()) {
					alertSize = spec.getAlertSize();
					alertSizeUnits = spec.getAlertSizeUnits();
					alertSizeSet = true;
				}
			}
			if (!alertSizeSet) {
				// Use due date instead.
				alertDate = computeDueDate(scenarioModel, referenceDate, sn);
			} else {
				alertDate = computePriorDate(referenceDate, alertSize, alertSizeUnits);
			}
		}
		return alertDate;
	}

	private static LocalDate computePriorDate(@NonNull final LocalDate referenceDate, final int size, final DatePeriodPrior sizeUnits) {
		LocalDate priorDate = referenceDate;
		if (sizeUnits != null) {
			switch (sizeUnits.getValue()) {
			case DatePeriodPrior.DAYS_PRIOR_VALUE:
				priorDate = referenceDate.minusDays(size);
				break;
			case DatePeriodPrior.MONTHS_PRIOR_VALUE:
				priorDate = referenceDate.minusMonths(size);
				break;
			default:
				throw new IllegalArgumentException("Unimplemented DataPeriodPeriod enum type.");
			}
		}
		return priorDate;
	}

	public static String getRemark(final LNGScenarioModel scenarioModel, final AbstractNomination n) {
		String remark = n.getRemark();
		if (remark == null) {
			final AbstractNominationSpec sp = getNominationSpec(scenarioModel, n.getSpecUuid());
			if (sp != null) {
				remark = sp.getRemark();
			}
			if (remark == null) {
				remark = "";
			}
		}
		return remark;
	}

	public static boolean isKnownNominationType(final String nominationType) {
		return getNominationTypes().contains(nominationType);
	}

	public static List<String> getNominationTypes() {
		return NominationTypeRegistry.getInstance().getNominationTypes();
	}

	public static @NonNull List<String> getContracts(@NonNull final LNGScenarioModel scenarioModel, final Side side) {
		final CommercialModel cm = ScenarioModelUtil.getCommercialModel(scenarioModel);
		final List<String> contractNames = new ArrayList<>();
		if (side == Side.BUY) {
			cm.getPurchaseContracts().forEach(c -> contractNames.add(c.getName()));
		} else {
			cm.getSalesContracts().forEach(c -> contractNames.add(c.getName()));
		}
		return contractNames;
	}

	public static LocalDate getNominationDate(final String nominationType, final IScenarioDataProvider scenarioDataProvider, final Slot<?> slot) {
		final AbstractNomination n = NominationsModelUtils.findNominationForSlot(scenarioDataProvider, slot, nominationType);
		if (n != null) {
			return n.getDueDate();
		} else {
			return null;
		}
	}

	public static Contract findContract(final LNGScenarioModel scenarioModel, final AbstractNomination nomination) {
		final CommercialModel cm = ScenarioModelUtil.getCommercialModel(scenarioModel);
		final Side side = nomination.getSide();
		final String nomineeId = nomination.getNomineeId();
		Contract contract = null;
		if (nomineeId != null && !nomineeId.equals("")) {
			if (side == Side.BUY) {
				for (final Contract c : cm.getPurchaseContracts()) {
					if (c.getName().equals(nomineeId)) {
						contract = c;
					}
				}
			} else {
				for (final Contract c : cm.getSalesContracts()) {
					if (c.getName().equals(nomineeId)) {
						contract = c;
					}
				}
			}
		}
		return contract;
	}

	public static boolean containsNomination(final List<AbstractNomination> existingNominations, final AbstractNomination nomination) {
		for (final AbstractNomination n : existingNominations) {
			if (n == nomination) {
				return true;
			}
			if (nomination != null && n != null && trimmedEquals(n.getType(), nomination.getType()) && Objects.equals(n.getSide(), nomination.getSide())
					&& Objects.equals(n.isCounterparty(), nomination.isCounterparty()) && trimmedEquals(n.getRemark(), nomination.getRemark())
					&& trimmedEquals(n.getNomineeId(), nomination.getNomineeId()) && trimmedEquals(n.getRefererId(), nomination.getRefererId())
					&& Objects.equals(n.getDueDate(), nomination.getDueDate()) && Objects.equals(n.getAlertSize(), nomination.getAlertSize())
					&& Objects.equals(n.getAlertDate(), nomination.getAlertDate()) && Objects.equals(n.getAlertSizeUnits(), nomination.getAlertSizeUnits())
					&& trimmedEquals(n.getSpecUuid(), nomination.getSpecUuid()) && Objects.equals(n.getDayOfMonth(), nomination.getDayOfMonth()) && Objects.equals(n.getSize(), nomination.getSize())
					&& Objects.equals(n.getSizeUnits(), nomination.getSizeUnits()) && Objects.equals(n.isDone(), nomination.isDone())) {
				return true;
			}
		}
		return false;
	}
	
	public static Integer getNominatedVolumeValue(AbstractNomination n) {
		String nomValue = n.getNominatedValue();
		if (nomValue != null && !nomValue.isBlank()) {
			try {
				Number volNum = volumeFormatter.parse(nomValue);
				return volNum.intValue();
			}
			catch (ParseException e) {
				//Will return Integer.MAX_VALUE below.
			}
		}
		return Integer.MAX_VALUE;
	}

	public static boolean trimmedEquals(String str1, String str2) {
		if (str1 != null && str2 != null) {
			str1 = str1.trim();
			str2 = str2.trim();
		}
		return Objects.equals(str1, str2);
	}

	public static boolean containsNominationSpec(final List<AbstractNominationSpec> existingNominationSpec, final AbstractNominationSpec nominationSpec) {
		for (final AbstractNominationSpec ns : existingNominationSpec) {
			if (ns == nominationSpec) {
				return true;
			}
			if (nominationSpec != null && ns != null && Objects.equals(ns.getType(), nominationSpec.getType()) && Objects.equals(ns.getSide(), nominationSpec.getSide())
					&& Objects.equals(ns.isCounterparty(), nominationSpec.isCounterparty()) && Objects.equals(ns.getRemark(), nominationSpec.getRemark())
					&& Objects.equals(ns.getRefererId(), nominationSpec.getRefererId()) && Objects.equals(ns.getAlertSize(), nominationSpec.getAlertSize())
					&& Objects.equals(ns.getAlertSizeUnits(), nominationSpec.getAlertSizeUnits()) && Objects.equals(ns.getDayOfMonth(), nominationSpec.getDayOfMonth())
					&& Objects.equals(ns.getSize(), nominationSpec.getSize()) && Objects.equals(ns.getSizeUnits(), nominationSpec.getSizeUnits())) {
				return true;
			}
		}
		return false;
	}

	public static String getNominatedValue(AbstractNomination nomination) {
		if (nomination != null && nomination.getNominatedValue() != null) {
			Object object = getNominatedValueObjectFromJSON(nomination.getType(), nomination.getNominatedValue());
			if (object != null && !(object instanceof String)) {
				return object.toString();
			}
			else if (object instanceof String){
				return (String)object;
			}
		}
		
		//If no suitable value can be found, return empty string.
		return "";
 	}

	@SuppressWarnings("null")
	public static @NonNull List<String> getPossibleNominatedValues(LNGScenarioModel scenarioModel, AbstractNomination nomination) {
		if (nomination.getType() == null) {
			return Collections.emptyList();
		}
		else if (nomination.getType().toLowerCase().contains("port")) {
			PortModel pm = scenarioModel.getReferenceModel().getPortModel();
			List<Port> ports = pm.getPorts();
			return ports.stream().map(Port::getName).collect(Collectors.toUnmodifiableList());
		}
		else if (nomination.getType().toLowerCase().contains("vessel")) {
			FleetModel fm = scenarioModel.getReferenceModel().getFleetModel();
			List<Vessel> vessel = fm.getVessels();
			return vessel.stream().map(Vessel::getName).collect(Collectors.toUnmodifiableList());	
		}
		else {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("null")
	public static Object getNominatedValueObjectFromJSON(String nominationType, String value) {
		if (value != null && nominationType != null && nominationType.toLowerCase().contains("window")) {
			//Need a complex object for the time window.
			ObjectMapper mapper = new ObjectMapper();
			TimeWindowHolder nominatedObject = null;
			boolean failed = false;
			try {
				nominatedObject = mapper.readValue(value, TimeWindowHolder.class);
			} catch (Exception e) {
				failed = true;
			} 
			if (failed) {
				return value;
			}
			else {
				return nominatedObject;
			}
		}
		else {
			//Everything else just a simple string for now.
			return value;
		}
	}
	
	public static String getNominatedValueJSONString(Object nominatedValueObject) {
		if (!(nominatedValueObject instanceof String)) {
			//Need a complex object for the time window.
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(nominatedValueObject);
			} catch (Exception e) {
				return "";
			}
		}
		else {
			return ((String)nominatedValueObject);
		}
	}

	public static String toStringSummary(LNGScenarioModel sm, AbstractNomination n) {
		StringBuilder sb = new StringBuilder();
		sb.append(n.getNomineeId()).append(":").append(n.getType()).append(":").append(n.getDueDate()).append(":").append(n.getRemark());
		if (n.getNominatedValue() != null && !n.getNominatedValue().isBlank() && sm != null) {
			sb.append("=").append(getNominatedValue(n));
		}
		return sb.toString();
	}

	public static Collection<EObject> duplicateNominations(List<EObject> nominations) {
		//Copy the objects.
		Collection<EObject> duplicatedNoms = EcoreUtil.copyAll(nominations);
		
		for (EObject duplicateNom : duplicatedNoms) {
			
			if (duplicateNom instanceof AbstractNomination) {
				AbstractNomination dn = (AbstractNomination)duplicateNom;

				//Create new UIDs and if generated nomination. 
				((UUIDObjectImpl)dn).setUuid(UUID.randomUUID().toString());

				if (dn.getSpecUuid() != null) {
					//Null out specUUId as this will no longer be a generated nomination after duplication.
					dn.setSpecUuid(null);
					dn.unsetSpecUuid();
				}
			}
		}
		
		return duplicatedNoms;
	}
}
