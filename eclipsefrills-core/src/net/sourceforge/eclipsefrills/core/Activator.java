/**************************************************************************
**  Copyright (c) 2006-2009 Christopher J. Stehno                        **
**  chris@stehno.com                                                     **
**  http://www.stehno.com                                                **
**                                                                       **
**  All rights reserved                                                  **
**                                                                       **
**  This program and the accompanying materials are made available under **
**  the terms of the Eclipse Public License v1.0 which accompanies this  **
**  distribution, and is available at:                                   **
**  http://www.stehno.com/legal/epl-1_0.html                             **
**                                                                       **
**  A copy is found in the file license.txt.                             **
**                                                                       **
**  This copyright notice MUST APPEAR in all copies of the file!         **
**************************************************************************/
package net.sourceforge.eclipsefrills.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class Activator extends AbstractUIPlugin {
	// TODO: do I really need this class for this plug-in

	public static final String PLUGIN_ID = "net.sourceforge.eclipsefrills.core";
	private static Activator plugin;

	public Activator() {}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance of the plug-in.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {return plugin;}
}
