package com.mmxlabs.scheduler.optimiser.moves.util;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveHandlerWrapper;
import com.mmxlabs.scheduler.optimiser.lso.guided.MoveTypes;
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
import com.mmxlabs.scheduler.optimiser.moves.util.impl.BreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;

public class MoveGeneratorModule extends AbstractModule {
	@Override
	protected void configure() {

		bind(MoveHandlerHelper.class).in(Singleton.class);

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

		bind(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
		bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class);
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Insert_Cargo)
	private GuidedMoveHandlerWrapper provide_Insert_Cargo_Generator(Injector injector, InsertCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Insert_Cargo, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Insert_DES_Purchase)
	private GuidedMoveHandlerWrapper provide_Insert_DES_Purchase_Generator(Injector injector, InsertDESPurchaseMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Insert_DES_Purchase, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Insert_FOB_Sale)
	private GuidedMoveHandlerWrapper provide_Insert_FOB_Sale_Generator(Injector injector, InsertFOBSaleMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Insert_FOB_Sale, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Insert_Slot)
	private GuidedMoveHandlerWrapper provide_Insert_Slot_Generator(Injector injector, InsertCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Insert_Slot, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Remove_DES_Purchase)
	private GuidedMoveHandlerWrapper provide_Remove_DES_Purchase_Generator(Injector injector, RemoveCargoMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Remove_DES_Purchase, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Swap_Cargo_Vessel)
	private GuidedMoveHandlerWrapper provide_Swap_Cargo_Vessel_Generator(Injector injector, SwapCargoVesselMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Swap_Cargo_Vessel, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Swap_Slot)
	private GuidedMoveHandlerWrapper provide_Swap_Slot_Generator(Injector injector, RemoveCargoMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Swap_Slot, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}

	@Provides
	@Singleton
	@MoveTypesAnnotation(MoveTypes.Remove_FOB_Sale)
	private GuidedMoveHandlerWrapper provide_Remove_FOB_Sale_Generator(Injector injector, SwapSlotMoveHandler handler) {

		GuidedMoveHandlerWrapper wrapper = new GuidedMoveHandlerWrapper(MoveTypes.Remove_FOB_Sale, handler);
		injector.injectMembers(wrapper);
		return wrapper;
	}
}
