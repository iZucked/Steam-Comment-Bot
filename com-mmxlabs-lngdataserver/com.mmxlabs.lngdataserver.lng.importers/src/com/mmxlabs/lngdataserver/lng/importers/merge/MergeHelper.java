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
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
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
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingHelper;
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
	final ObjectMapper jsonReader;
	final List<Pair<JSONReference, String>> missingReferences = new LinkedList<>();
	final Set<Cargo> cargoesToAdd = new HashSet<>();
	final Set<Cargo> cargoesToRemove = new HashSet<>();
	final List<ToAddLater> cmdsToAddLater = new ArrayList<>();
	final List<ToReplaceLater> cmdsToReplaceLater = new ArrayList<>();
	final List<ToRemoveLater> cmdsToRemoveLater = new ArrayList<>();
	final List<ToSetLater> cmdsToSetLater = new ArrayList<>();
	final EMFDeserializationContext ctx;
	final CargoEditingHelper cargoEditingHelper;
	
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
		this.ctx = new EMFDeserializationContext(BeanDeserializerFactory.instance);
		this.jsonReader = createJSONReader();
		this.cargoEditingHelper = new CargoEditingHelper(editingDomain, targetLNGScenario);
	}

	private ObjectMapper createJSONReader() {
		
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
					//Replace object in target, with version from source:
					EObject oldObjectToOverwrite = targetNamedObjects.get(name);
					EObject newObjectToReplaceWith = cloneEObject(no); //Clone to get rid of references to source scenario.
					toReplace.add(Pair.of(oldObjectToOverwrite, newObjectToReplaceWith));	
					
					//Add the cargo as well if there is one if it is a slot.
					addCargoIfSlot(no);
					
					//Remove any cargo if it is a slot.
					removeCargoOnOldObjectIfSlot(oldObjectToOverwrite);
					
					//Clean up rest of references on target scenario:
					updateReferencesViaCmd(this.targetLNGScenario, mg.getModel(this.targetScenarioDataProvider), feature, oldObjectToOverwrite, newObjectToReplaceWith);
					break;
					
				case Map:
					//FIXME: A bit flaky perhaps, but seems to work and need to like this in case source does not have target object in it.
					EObject oldObject = no;
					EObject newObjectInTarget = cloneEObject(targetNamedObjects.get(ma.getTargetName()));
					updateReferencesViaSet(this.sourceLNGScenario, no, newObjectInTarget);
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

	private void removeCargoOnOldObjectIfSlot(EObject oldObject) {
		if (oldObject instanceof Slot<?>) {
			Slot<?> targetSlot = (Slot<?>)oldObject;
			if (targetSlot.getCargo() != null) {
				this.cargoesToRemove.add(targetSlot.getCargo());
			}
		}
	}

	abstract class AbstractToExecuteLater {
		Object owner; 
		EStructuralFeature feature;
		List<Object> values;
		
		/**
		 * @param owner
		 * @param cmd
		 * @param mg
		 * @param feature
		 * @param values
		 */
		public AbstractToExecuteLater(Object owner, EStructuralFeature feature, List<Object> values) {
			super();
			this.owner = owner;
			this.feature = feature;
			this.values = values;
		}	
		
		/**
		 * Execute now!
		 * @param cmd
		 */
		abstract public void createCmdNow(CompoundCommand cmd);
	}
	
	class ToAddLater extends AbstractToExecuteLater {

		/**
		 * @param owner
		 * @param cmd
		 * @param mg
		 * @param feature
		 * @param toAdd
		 */
		public ToAddLater(Object owner, EStructuralFeature feature, List<Object> toAdd) {
			super(owner, feature, toAdd);
		}
			
		/**
		 * Execute now!
		 * @param cmd
		 */
		public void createCmdNow(CompoundCommand cmd) {
			cmd.append(AddCommand.create(editingDomain, owner, feature, values));
		}
	}
	
	class ToRemoveLater extends AbstractToExecuteLater {
		public ToRemoveLater(Object owner, EStructuralFeature feature, List<Object> toDel) {
			super(owner, feature, toDel);
		}
		
		/**
		 * Execute now!
		 * @param cmd
		 */
		@Override
		public void createCmdNow(CompoundCommand cmd) {
			cmd.append(RemoveCommand.create(editingDomain, owner, feature, values));
		}
	}
	
	class ToReplaceLater extends AbstractToExecuteLater {
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
			cmd.append(ReplaceCommand.create(editingDomain, owner, feature, oldObject, this.values));
		}
	}

	class ToSetLater extends AbstractToExecuteLater {
		Object value;
		
		/**
		 * @param owner
		 * @param cmd
		 * @param mg
		 * @param feature
		 * @param value
		 */
		public ToSetLater(Object owner, EStructuralFeature feature, Object value) {
			super(owner, feature, null);
			this.value = value;
		}
		
		/**
		 * Execute now!
		 * @param cmd
		 */
		@Override
		public void createCmdNow(CompoundCommand cmd) {
			Object newValue = value;
			if (value == null) {
				newValue = SetCommand.UNSET_VALUE;
			}			
			cmd.append(SetCommand.create(editingDomain, owner, feature, newValue));
		}
	}
	
	
	private void add(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Object> toAdd) {
		if (!toAdd.isEmpty()) {
			Object owner = mg.getModel(targetScenarioDataProvider);
			this.cmdsToAddLater.add(new ToAddLater(owner, feature, toAdd));
		}
	}

	private void remove(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Object> toRemove) {
		if (!toRemove.isEmpty()) {
			Object owner = mg.getModel(targetScenarioDataProvider);
			this.cmdsToRemoveLater.add(new ToRemoveLater(owner, feature, toRemove));
		}
	}

	private void set(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, Object newValue) {
		Object owner = mg.getModel(targetScenarioDataProvider);
		this.cmdsToSetLater.add(new ToSetLater(owner, feature, newValue));
	}
	
	private void replace(CompoundCommand cmd, ModelGetter mg, EStructuralFeature feature, List<Pair<Object, Object>> toReplace) {
		Object owner = mg.getModel(targetScenarioDataProvider);
		for (Pair<Object,Object> replace : toReplace) {
			Object oldObject = replace.getFirst();  //The target version
			Object newObject = replace.getSecond(); //The source version to replace it.
			this.cmdsToReplaceLater.add(new ToReplaceLater(owner, feature, oldObject, Collections.singletonList(newObject)));
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

	private boolean addCargoIfSlot(NamedObject no) throws JsonMappingException, JsonProcessingException {
		if (no instanceof Slot<?> && ((Slot<?>)no).getCargo() != null) {
			Cargo cargo = ((Slot<?>)no).getCargo();
			this.cargoesToAdd.add(cargo);	
			return true;
		}
		return false;
	}
	
	private void updateReferencesViaSet(LNGScenarioModel model, EObject oldObject, EObject newObject) {
		List<EObject> oldContracts = Collections.singletonList(oldObject);
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(oldContracts, model);
		for (EObject oldContract : oldContracts) {
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(oldContract);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature().isMany()) {
						Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
						if (collection.contains(newObject)) {
							// Replacement is already in the collection, so just remove it
							collection.remove(oldContract);
						} else {
							EcoreUtil.replace(setting, oldContract, newObject);
						}
					} else {
						EcoreUtil.replace(setting, oldContract, newObject);
					}
				}
			}
		}
	}
	
	private void updateReferencesViaCmd(LNGScenarioModel model, Object owner, EStructuralFeature ownerFeature, EObject oldObject, EObject newObject) {
		List<EObject> oldContracts = Collections.singletonList(oldObject);
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(oldContracts, model);
		for (EObject oldContract : oldContracts) {
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(oldContract);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					
					//We don't want to add twice in the owner collection (Ignore cargoes also as dealt with separately)
					if (setting.getEStructuralFeature() != ownerFeature && !(setting.getEObject() instanceof Cargo)) {
						if (setting.getEStructuralFeature().isMany()) {
							Collection<?> collection = (Collection<?>) setting.getEObject().eGet(setting.getEStructuralFeature());
							if (collection.contains(newObject)) {
								// Replacement is already in the collection, so just remove it
								this.cmdsToRemoveLater.add(new ToRemoveLater(setting.getEObject(), setting.getEStructuralFeature(), Collections.singletonList(oldObject)));
							} else {
								this.cmdsToReplaceLater.add(new ToReplaceLater(setting.getEObject(), setting.getEStructuralFeature(), oldObject, Collections.singletonList(newObject)));
							}
						} else {
							//Use set command, as replace cmd only for collections.
							this.cmdsToSetLater.add(new ToSetLater(setting.getEObject(), setting.getEStructuralFeature(), newObject));
						}
					}
				}
			}
		}
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

		//Now create set commands.
		for (ToSetLater toSet : this.cmdsToSetLater) {
			toSet.createCmdNow(cmd);
		}
		
		editingDomain.getCommandStack().execute(cmd);
	}

	public void addRemoveCargoes(CompoundCommand cmd) throws JsonMappingException, JsonProcessingException {
		
		if (!this.cargoesToRemove.isEmpty()) {
			//remove(cmd, s -> this.targetLNGScenario.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, Arrays.asList(this.cargoesToRemove.toArray()));	
			//Removeing or unpairing a cargo is more complicated than just above, so...
			cargoEditingHelper.unpairCargoes(cmd, this.cargoesToRemove);
		}
		if (!this.cargoesToAdd.isEmpty()) {			
			final String toAddJSON = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(this.cargoesToAdd);		
			final List<Cargo> cargoTarget = jsonReader.readValue(toAddJSON, new TypeReference<List<Cargo>>() {});
			
			add(cmd, s-> this.targetLNGScenario.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, Arrays.asList(cargoTarget.toArray()));
		}		
	}
}
