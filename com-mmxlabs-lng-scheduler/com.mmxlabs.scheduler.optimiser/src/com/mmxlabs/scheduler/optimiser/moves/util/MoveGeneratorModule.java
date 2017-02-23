package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.scheduler.optimiser.lso.RouletteWheelMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveHandlerWrapper;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveMapper;
import com.mmxlabs.scheduler.optimiser.lso.guided.HintManager;
import com.mmxlabs.scheduler.optimiser.lso.guided.MoveTypeHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveTypes;
import com.mmxlabs.scheduler.optimiser.lso.guided.MoveTypesAnnotation;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.MoveSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveLinkedSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveMapper;
import com.mmxlabs.scheduler.optimiser.moves.handlers.InsertOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.MoveSegmentSequenceMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.RemoveOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.SwapSegmentSequenceMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.SwapTailsSequenceMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.BreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.ParallelFollowersAndPrecedersProviderImpl;

public class MoveGeneratorModule extends AbstractModule {

	private boolean parallelFollowerAndPreceeders;

	public MoveGeneratorModule() {
		this(false);
	}

	public MoveGeneratorModule(boolean parallelFollowerAndPreceeders) {
		this.parallelFollowerAndPreceeders = parallelFollowerAndPreceeders;

	}

	@Override
	protected void configure() {

		bind(HintManager.class);
		bind(MoveTypeHelper.class);

		bind(InsertCargoVesselMoveHandler.class).in(Singleton.class);
		bind(InsertDESPurchaseMoveHandler.class).in(Singleton.class);
		bind(InsertFOBSaleMoveHandler.class).in(Singleton.class);
		bind(RemoveSlotMoveHandler.class).in(Singleton.class);
		bind(RemoveCargoMoveHandler.class).in(Singleton.class);
		bind(SwapCargoVesselMoveHandler.class).in(Singleton.class);
		bind(SwapSlotMoveHandler.class).in(Singleton.class);
		bind(RemoveLinkedSlotMoveHandler.class).in(Singleton.class);
		bind(MoveSlotMoveHandler.class).in(Singleton.class);

		bind(MoveHelper.class).in(Singleton.class);
		bind(IMoveHelper.class).to(MoveHelper.class);

		bind(BreakPointHelper.class).in(Singleton.class);
		bind(IBreakPointHelper.class).to(BreakPointHelper.class);

		if (parallelFollowerAndPreceeders) {
			bind(ParallelFollowersAndPrecedersProviderImpl.class).in(Singleton.class);
			bind(IFollowersAndPreceders.class).to(ParallelFollowersAndPrecedersProviderImpl.class);
		} else {
			bind(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
			bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class);
		}

		// RouletteWheel
		bind(MoveHandlerHelper.class).in(Singleton.class);
		bind(IMoveHandlerHelper.class).to(MoveHandlerHelper.class);

		bind(MoveMapper.class).in(Singleton.class);

		bind(InsertOptionalElementMoveHandler.class).in(Singleton.class);
		bind(RemoveOptionalElementMoveHandler.class).in(Singleton.class);
		bind(SwapSegmentSequenceMoveHandler.class).in(Singleton.class);
		bind(MoveSegmentSequenceMoveHandler.class).in(Singleton.class);
		bind(SwapTailsSequenceMoveHandler.class).in(Singleton.class);

		bind(GuidedMoveMapper.class).in(Singleton.class);

		// Stateful!
		bind(GuidedMoveGenerator.class);

	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Insert_Cargo)
	private GuidedMoveHandlerWrapper provide_Insert_Cargo_Generator(Injector injector, InsertCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Insert_Cargo, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Insert_DES_Purchase)
	private GuidedMoveHandlerWrapper provide_Insert_DES_Purchase_Generator(Injector injector, InsertDESPurchaseMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Insert_DES_Purchase, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Insert_FOB_Sale)
	private GuidedMoveHandlerWrapper provide_Insert_FOB_Sale_Generator(Injector injector, InsertFOBSaleMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Insert_FOB_Sale, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Insert_Slot)
	private GuidedMoveHandlerWrapper provide_Insert_Slot_Generator(Injector injector, InsertCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Insert_Slot, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Remove_DES_Purchase)
	private GuidedMoveHandlerWrapper provide_Remove_DES_Purchase_Generator(Injector injector, RemoveCargoMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Remove_DES_Purchase, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Swap_Cargo_Vessel)
	private GuidedMoveHandlerWrapper provide_Swap_Cargo_Vessel_Generator(Injector injector, SwapCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Swap_Cargo_Vessel, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Move_Vessel_Event)
	private GuidedMoveHandlerWrapper provide_Move_Vessel_Event_Generator(Injector injector, SwapCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Move_Vessel_Event, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Remove_Cargo)
	private GuidedMoveHandlerWrapper provide_Remove_Cargo_Generator(Injector injector, RemoveCargoMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Remove_Cargo, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Swap_Slot)
	private GuidedMoveHandlerWrapper provide_Swap_Slot_Generator(Injector injector, SwapSlotMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Swap_Slot, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Remove_FOB_Sale)
	private GuidedMoveHandlerWrapper provide_Remove_FOB_Sale_Generator(Injector injector, SwapSlotMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Remove_FOB_Sale, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(GuidedMoveTypes.Move_Slot_NonShipped_Resource)
	private GuidedMoveHandlerWrapper provide_Move_Slot_NonShipped_Resource(Injector injector, MoveSlotMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(GuidedMoveTypes.Move_Slot_NonShipped_Resource, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Named(RouletteWheelMoveGenerator.EQUAL_DISTRIBUTION)
	private boolean isEqualDistributions() {
		return true;
	}

	@Provides
	@Named(RouletteWheelMoveGenerator.MOVE_DISTRIBUTION)
	private Map<String, Double> getMoveDistributions() {
		return new HashMap<String, Double>();
	}

	@Provides
	@Named(MoveHelper.LEGACY_CHECK_RESOURCE)
	private boolean useLegacyCheck() {
		return false;
	}

	@Provides
	@Named(MoveMapper.USE_GUIDED_MOVES)
	private boolean useguidedMoves() {
		return false;
	}
}
