/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.handlers;

import java.time.LocalDate;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.PlatformUI;

/*
 * pre
 */
public class TodayHandler extends AbstractHandler{
	
	public static final String EVENT_SNAP_TO_DATE = "EVENT_SNAP_TO_DATE";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post(EVENT_SNAP_TO_DATE, LocalDate.now());
		return null;
	}
	
}

/* 
 * This is to be added into the plugin.xml to create a button on the toolbar
  <pre>
    <extension
       point="org.eclipse.ui.menus">
    <menuContribution
          allPopups="false"
	          locationURI="toolbar:org.eclipse.ui.main.toolbar?after=additions">
       <toolbar
             id="com.mmxlabs.models.lng.analytics.editor.toolbarSnapToToday">
          <command
                commandId="com.mmxlabs.rcp.common.TodayHandler"
                label="Today"
                style="push">
          </command>
       </toolbar>
    </menuContribution>
 	</extension>
 	</pre>
 	
  *	This to be added to create a command
 	<pre>
 	   <extension
         point="org.eclipse.ui.commands">
      <command
            defaultHandler="com.mmxlabs.rcp.common.handlers.TodayHandler"
            id="com.mmxlabs.rcp.common.TodayHandler"
            name="Snap to today">
      </command>
   </extension>
 	</pre>
 	
 */