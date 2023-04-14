/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.commands;

import org.eclipse.emf.common.command.Command;

import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Extension which allows to execute custom commands before base case is published
 * @author FM
 *
 */
public interface IBaseCasePrePublishCommandExtension {
	
	Command createPrePublishCommand(IScenarioDataProvider scenarioDataProvider);
	
	String getCommandDescription();
}
