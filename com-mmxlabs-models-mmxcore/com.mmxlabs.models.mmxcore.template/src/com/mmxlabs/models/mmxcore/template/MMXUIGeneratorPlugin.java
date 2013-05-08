/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.template;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;


public class MMXUIGeneratorPlugin extends EMFPlugin
{
          /**
           * The singleton instance of the plugin.
           */
          public static final MMXUIGeneratorPlugin INSTANCE = new MMXUIGeneratorPlugin();

          /**
           * The one instance of this class.
           */
          private static Implementation plugin;

          public static final String ID = Activator.PLUGIN_ID;

          /**
           * Creates the singleton instance.
           */
          private MMXUIGeneratorPlugin()
          {
            super(new ResourceLocator [] { });
          }

          /*
           * Javadoc copied from base class.
           */
          @Override
          public ResourceLocator getPluginResourceLocator()
          {
            return plugin;
          }

          /**
           * Returns the singleton instance of the Eclipse plugin.
           * @return the singleton instance.
           */
          public static Implementation getPlugin()
          {
            return plugin;
          }

          /**
           * The actual implementation of the Eclipse <b>Plugin</b>.
           */
          public static class Implementation extends EclipsePlugin
          {
            /**
             * Creates an instance.
             */
            public Implementation()
            {
              super();

              // Remember the static instance.
              //
              plugin = this;
            }
          }}
