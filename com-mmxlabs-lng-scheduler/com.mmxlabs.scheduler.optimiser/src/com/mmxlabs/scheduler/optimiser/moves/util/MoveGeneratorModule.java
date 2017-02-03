package com.mmxlabs.scheduler.optimiser.moves.util;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;

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

		bind(MoveHelper.class).in(Singleton.class);
		bind(IMoveHelper.class).to(MoveHelper.class);

		bind(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
		bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class);
	}
}
