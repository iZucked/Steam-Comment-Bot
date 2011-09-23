/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?187">Case 187: Min travel heel is used</a>
 * <p>
 * Vessel classes have a min heel for travel, which they must keep while moving on NBO. Because this heel is sitting around at arrival time and will boil off, it's "free" at that point and so should
 * be always be used for idling.
 * <p>
 * There should be a test in which the vessel class has some nonzero min heel, and arrives on a ballast leg for which NBO is the cheapest travel option but Base Fuel is the cheapest idle option - in
 * this situation there should be enough NBO used in the Idle to boil off all the travel heel.
 * <p>
 * Note that although this idle boil off is kind of free, it's not priced as free, since you still really pay for it because you are forced to keep it on board rather than selling it at the discharge.
 * 
 * @author Adam Semenenko
 * 
 */
public class MinTravelHeelTests {

}
