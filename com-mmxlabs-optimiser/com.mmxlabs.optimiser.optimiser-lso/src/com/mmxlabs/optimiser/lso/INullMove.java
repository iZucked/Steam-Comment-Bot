/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * Interface defining a Move as used in the Local Search Optimiser.
 * 
 * @author NS
 * 
 */
public interface INullMove extends IMove {

	public String getFailure();

	public void setFailure(String failureName);
	
	public String getGenerator();
	
	public void setGenerator(String generator);
	
	public String getFullMessage();

}
