package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.json.EMFDeserializationContext;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.rcp.common.json.JSONReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class MergeHelper implements Closeable {

	final ScenarioInstance sourceScenarioInstance;
	final ScenarioInstance targetScenarioInstance;
	final IScenarioDataProvider sourceScenarioDataProvider;
	final IScenarioDataProvider targetScenarioDataProvider;
	final LNGScenarioModel sourceLNGScenario;
	final LNGScenarioModel targetLNGScenario;
	final EditingDomain editingDomain;
	final ObjectMapper jsonWriter;
	ObjectMapper jsonReader;
	final List<Pair<JSONReference, String>> missingReferences = new LinkedList<>();
	final Set<Cargo> cargoesToAdd = new HashSet<>();
	final Set<Cargo> cargoesToRemove = new HashSet<>();
	final List<ToAddLater> cmdsToAddLater = new ArrayList<>();
	final List<ToReplaceLater> cmdsToReplaceLater = new ArrayList<>();
	final List<ToRemoveLater> cmdsToRemoveLater = new ArrayList<>();
	
	EMFDeserializationContext ctx;
	
	public MergeHelper(ScenarioInstance sourceScenarioInstance, ScenarioInstance targetScenarioInstance) {
		this.sourceScenarioInstance = sourceScenarioInstance;
		this.targetScenarioInstance = targetScenarioInstance;
		this.sourceScenarioDataProvider = getScenarioDataProvider(this.sourceScenarioInstance);
		this.targetScenarioDataProvider = getScenarioDataProvider(this.targetScenarioInstance);
		this.sourceLNGScenario = EcoreUtil.copy(sourceScenarioDataProvider.getTypedScenario(LNGScenarioModel.class));
		this.targetLNGScenario = targetScenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		this.editingDomain = targetScenarioDataProvider.getEditingDomain();
		this.jsonWriter = new ObjectMapper();
		this.jsonWriter.registerModule(new EMFJacksonModule());
		this.jsonReader = createJSONReader();
	}

	private ObjectMapper createJSONReader() {
		ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);
		
		ctx.setMissingFeatureHandler((ref, lbl) -> {
			System.out.println("Unknown reference " + ref.getName() + " " + ref.getGlobalId() + " " + ref.getClassType());

			missingReferences.add(Pair.of(ref, lbl));
			return false;
		});

		// Ignore back reference
		ctx.ignoreFeature(CargoPackage.Literals.SLOT__CARGO);

		final CargoModel cam = ScenarioModelUtil.getCargoModel(targetScenarioDataProvider);
		cam.getVesselAvailabilities().forEach(ctx::registerType);

		final PortModel pm = ScenarioModelUtil.getPortModel(targetScenarioDataProvider);
		pm.getPorts().forEach(ctx::registerType);
		pm.getPortGroups().forEach(ctx::registerType);
		pm.getPortCountryGroups().forEach(ctx::registerType);
		pm.getSpecialPortGroups().forEach(ctx::registerType);

		final FleetModel fm = ScenarioModelUtil.getFleetModel(targetScenarioDataProvider);
		fm.getBaseFuels().forEach(ctx::registerType);
		fm.getVessels().forEach(ctx::registerType);
		fm.getVesselGroups().forEach(ctx::registerType);

		final CommercialModel targetScenarioDataProviderCM = ScenarioModelUtil.getCommercialModel(targetScenarioDataProvider);
		targetScenarioDataProviderCM.getPurchaseContracts().forEach(ctx::registerType);
		targetScenarioDataProviderCM.getSalesContracts().forEach(ctx::registerType);
		targetScenarioDataProviderCM.getEntities().forEach(ctx::registerType);

		final SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(targetScenarioDataProvider);
		smm.getCharterInMarkets().forEach(ctx::registerType);
		smm.getDesPurchaseSpotMarket().getMarkets().forEach(ctx::registerType);
		smm.getDesSalesSpotMarket().getMarkets().forEach(ctx::registerType);
		smm.getFobPurchasesSpotMarket().getMarkets().forEach(ctx::registerType);
		smm.getFobSalesSpotMarket().getMarkets().forEach(ctx::registerType);

		final ObjectMapper mapper2 = new ObjectMapper(null, null, ctx);
		mapper2.registerModule(new EMFJacksonModule());
		return mapper2;
	}

	private IScenarioDataProvider getScenarioDataProvider(ScenarioInstance si) {
		final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(si);
		return sourceModelRecord.aquireScenarioDataProvider("MergeHelper.getLNGScenarioModel");
	}
	
	public void merge(CompoundCommand cmd, Pair<NamedObjectGetter, List<MergeMapping>> mapping, ModelGetter mg, EStructuralFeature feature) throws JsonProcessingException {
		
		//this.jsonReader = createJSONReader();
		
		NamedObjectGetter namedObjectGetter = mapping.getFirst();
		Map<String, MergeAction> mergeActions = getActions(mapping.getSecond());
		List<? extends NamedObject> namedObjects = namedObjectGetter.getNamedObjects(this.sourceLNGScenario);
		Map<String, NamedObject> targetNamedObjects = getNamedObjectMap(this.targetLNGScenario, namedObjectGetter);
		List<Object> toAdd = new LinkedList<>();
		List<Pair<Object,Object>> toReplace = new LinkedList<>();

		for (NamedObject no : namedObjects) {
			String name = no.getName();
			MergeAction ma = mergeActions.get(name);
			if (ma != null) {
				switch (ma.getMergeType()) {
				case Add:
					toAdd.add(cloneEObject(no));
					addCargoIfSlot(no);
					break;
									
				case Overwrite:
					toReplace.add(Pair.of(targetNamedObjects.get(no.getName()), cloneEObject(no)));			
					if (addCargoIfSlot(no)) {
						List<? extends NamedObject> tnos = namedObjectGetter.getNamedObjects(targetLNGScenario);
						addCargoToRemoveIfSlot(tnos, no);
					}
					break;
					
				case Map:
					EObject targetObject = cloneEObject(targetNamedObjects.get(ma.getTargetName()));
					updateReferences(no, targetObject);
					break;
				
				case Ignore:
					//Do nothing (does not exist in target and no references in source).
					break;
				}
			}
		}
		
		add(cmd, mg, feature, toAdd);
		
		replace(cmd, mg, feature, toReplace);
		
		//runDeferredActions(feature);
		
		if (!missingReferences.isEmpty()) {
			// Revert change if there are missing references

			if (System.getProperty("lingo.suppress.dialogs") == null) {
				final StringBuilder msg = new StringBuilder("Keep changes?\n\n");

				final Set<String> messages = new HashSet<>();
				for (final Pair<JSONReference, String> p : missingReferences) {
					final JSONReference ref = p.getFirst();
					String lbl = p.getSecond();
					if (lbl == null) {
						final int idx = ref.getClassType().lastIndexOf('/');
						lbl = (idx > 0) ? ref.getClassType().substring(idx + 1) : ref.getClassType();
					}
					messages.add(String.format("Missing %s: %s", lbl, ref.getName()));
				}
				messages.forEach(m -> msg.append(m).append("\n"));

				Boolean keep = RunnerHelper.syncExecFunc((d) -> MessageDialog.openQuestion(d.getActiveShell(), "Error - unable to find all data references", msg.toString()));
				if (Boolean.FALSE == keep) {
					RunnerHelper.asyncExec(() -> editingDomain.getCommandStack().undo());
				}
			} else {
				// ITS failure exit point
				throw new IllegalStateException();
			}
		}
	}

	private void runDeferredActions(EStructuralFeature feature) {
		if (feature == FleetPackage.Literals.FLEET_MODEL__VESSELS) {
			ctx.runDeferredActions();
		}
	}

	class ToAddLater {
		Object owner; 
		EStructuralFeature feature;
		List<Object> toAdd;
		
		/**
		 * @param owner
		 * @param cmd
		 * @param mg
		 * @param feature
		 * @param toAdd
		 */
		public ToAddLater(Object owner, EStructuralFeature feature, List<Object> toAdd) {
			super();
			this.owner = owner;
			this.feature = feature;
			this.toAdd = toAdd;
		}
			
		/**
		 * Execute now!
		 * @param cmd
		 */
		public void createCmdNow(CompoundCommand cmd) {
			cmd.append(AddCommand.create(editingDomain, owner, feature, toAdd));
		}
	}
	
	class ToRemoveLater extends ToAddLater {
		public ToRemoveLater(Object owner, EStructuralFeature feature, List<Object> toDel) {
			super(owner, feature, toDel);
		}
		
		/**
		 * Execute now!
		 * @param cmd
		 */
		@Override
		public void createCmdNow(CompoundCommand cmd) {
			cmd.append(RemoveCommand.create(editingDomain, owner, feature, toAdd));
		}
	}
	
	
	class ToReplaceLater extends ToAddLater {
		Object oldObject;

		/**
		 * @param owner
		 * @param cmd
		 * @param mg
		 * @param feature
		 * @param oldObject
		 * @param replacements
		 */
		public ToReplaceLater(Object owner, EStructuralFeature feature, Object oldObject, List<Object> replacements) {
			super(owner, feature, replacements);
			this.oldObject = oldObject;
		}
		
		/**
		 * Execute now!
		 * @param cmd
		 */
		@Override
		public void createCmdNow(CompoundCommand cmd) {
			cmd.append(ReplaceCommand.create(editingDomain, owner, feature, oldObject, this.toAdd));
		}
	}
	
	
	private void add(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Object> toAdd) {
		if (!toAdd.isEmpty()) {
			Object owner = mg.getModel(targetScenarioDataProvider);
			//cmd.append(AddCommand.create(editingDomain, owner, feature, toAdd));
			this.cmdsToAddLater.add(new ToAddLater(owner, feature, toAdd));
		}
	}

	private void remove(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Object> toAdd) {
		if (!toAdd.isEmpty()) {
			Object owner = mg.getModel(targetScenarioDataProvider);
			//cmd.append(AddCommand.create(editingDomain, owner, feature, toAdd));
			this.cmdsToRemoveLater.add(new ToRemoveLater(owner, feature, toAdd));
		}
	}

	
	private void replace(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Pair<Object, Object>> toReplace) {
		if (!toReplace.isEmpty()) {				
			Object owner = mg.getModel(targetScenarioDataProvider);
			for (Pair<Object,Object> replace : toReplace) {
				Object oldObject = replace.getFirst();
				Object newObject = replace.getSecond();
				//cmd.append(ReplaceCommand.create(editingDomain, owner, feature, oldObject, Collections.singleton(newObject)));
				this.cmdsToReplaceLater.add(new ToReplaceLater(owner, feature, oldObject, Collections.singletonList(newObject)));
			}
		}
	}

	
	private EObject cloneEObject(EObject sourceObject) throws JsonProcessingException {
		//to ask Simon: any reason why we cannot use ECoreUtils.copy for this?
		final String toAddJSON = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(sourceObject);		
		return (EObject)jsonReader.readValue(toAddJSON, sourceObject.eClass().getInstanceClass());
	}
	
	private Map<String,NamedObject> getNamedObjectMap(LNGScenarioModel lngScenario, NamedObjectGetter namedObjectGetter) {
		Map<String, NamedObject> objectMap = new HashMap<>();
		List<? extends NamedObject> os = namedObjectGetter.getNamedObjects(lngScenario);
		for (var o : os) {
			objectMap.put(o.getName(), o);
		}
		return objectMap;
	}

	private void addCargoToRemoveIfSlot(List<? extends NamedObject> tnos, NamedObject no) {
		for (var tno : tnos) {
			if (tno instanceof Slot<?> && ((Slot<?>)tno).getCargo() != null) {
				this.cargoesToRemove.add(((Slot<?>)tno).getCargo());
			}
		}
	}

	private boolean addCargoIfSlot(NamedObject no) throws JsonMappingException, JsonProcessingException {
		if (no instanceof Slot<?> && ((Slot<?>)no).getCargo() != null) {
			Cargo cargo = ((Slot<?>)no).getCargo();
			this.cargoesToAdd.add(cargo);	
			return true;
		}
		return false;
	}
	
	private void updateReferences(EObject oldPC, EObject newPurchaseContract) {
		
		/*
		 * This should be already done by the code below???
		for (LoadSlot load : cargoModel.getLoadSlots()) {
			if (isLoadWithOldContract(load, oldContractName)) {
				load.setContract(newPurchaseContract);
			}
		}*/

		List<EObject> oldContracts = Collections.singletonList(oldPC);
		
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(oldContracts, this.sourceLNGScenario);
		for (EObject oldContract : oldContracts) {
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(oldContract);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
			//		if (setting.getEStructuralFeature() != CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS) {
						if (setting.getEStructuralFeature().isMany()) {
							Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
							if (collection.contains(newPurchaseContract)) {
								// Replacement is already in the collection, so just remove it
								collection.remove(oldContract);
							} else {
								EcoreUtil.replace(setting, oldContract, newPurchaseContract);
							}
						} else {
							EcoreUtil.replace(setting, oldContract, newPurchaseContract);
						}
					}
			//	}
			}
		}
		
		//We'll do it above, as see no reason why not.
		//commercialModel.getPurchaseContracts().remove(oldPC);
		
	}

	@Override
	public void close() throws IOException {
		this.sourceScenarioDataProvider.close();
		this.targetScenarioDataProvider.close();
	}
	
	private Map<String, MergeAction> getActions(List<MergeMapping> mappings) {
		Map<String, MergeAction> sourceToAction = new HashMap<>();
		for (MergeMapping cm : mappings) {
			MergeType mt = MergeType.Map;
			try {
				mt = MergeType.valueOf(cm.getTargetName());
			}
			catch (IllegalArgumentException e) {
				//Ignore, as we just map it.
				if (cm.getTargetName().startsWith("Overwrite")) {
					mt = MergeType.Overwrite;
				}
			}
			if (!cm.getSourceName().equals(cm.getTargetName())) {
				switch (mt) {
				case Overwrite:
					sourceToAction.put(cm.getSourceName(), new MergeAction(mt, cm.getSourceName(), cm.getSourceName()));										
					break;
				case Add:
					sourceToAction.put(cm.getSourceName(), new MergeAction(mt, cm.getSourceName(), cm.getSourceName()));					
					break;
				case Map:
					sourceToAction.put(cm.getSourceName(), new MergeAction(mt, cm.getSourceName(), cm.getTargetName()));
					break;
				case Ignore:
					//Do nothing as should not be present in target.
					break;
				}
			}
		}
		return sourceToAction;
	}

	public void execute(CompoundCommand cmd) throws JsonMappingException, JsonProcessingException {

		addRemoveCargoes(cmd);
		
		ctx.runDeferredActions();
		
		//Create remove commands.
		for (ToRemoveLater toRemove : this.cmdsToRemoveLater) {
			toRemove.createCmdNow(cmd);
		}
		
		//Now create replace commands.
		for (ToReplaceLater toReplace : this.cmdsToReplaceLater) {
			toReplace.createCmdNow(cmd);
		}
		
		//Now create add commands.
		for (ToAddLater toAdd : this.cmdsToAddLater) {
			toAdd.createCmdNow(cmd);
		}
		
		
		editingDomain.getCommandStack().execute(cmd);
	}

	public void addRemoveCargoes(CompoundCommand cmd) throws JsonMappingException, JsonProcessingException {
		
		//this.jsonReader = createJSONReader();
		
		if (!this.cargoesToRemove.isEmpty()) {
			remove(cmd, s -> this.targetLNGScenario.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, Arrays.asList(this.cargoesToRemove.toArray()));	
		}
		if (!this.cargoesToAdd.isEmpty()) {
						
			final String toAddJSON = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(this.cargoesToAdd);		
			final List<Cargo> cargoTarget = jsonReader.readValue(toAddJSON, new TypeReference<List<Cargo>>() {});
			
			add(cmd, s-> this.targetLNGScenario.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, Arrays.asList(cargoTarget.toArray()));
		}		
	}
}
