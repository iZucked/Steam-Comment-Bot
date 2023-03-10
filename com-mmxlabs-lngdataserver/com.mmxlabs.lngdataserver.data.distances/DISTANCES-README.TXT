====
    Copyright (C) Minimax Labs Ltd., 2010 - 2023
    All rights reserved.
====

Distance updater notes
======================

Notes
 * A file called apikey.txt should be present in the project root with the AtoBviaC api key to be able to perfrom any upstream distance lookups.
 * This should not be committed.
 * The "demo" key can be used for API testing against a limited number of ports.

 This project is not part of the LiNGO build. It contains the various distance versions we have used since we started recording them. It contains classes to perform distance DB updates.
 
 There are a collection of classes with main methods than can be run as Java Applications directly in eclipse.
 
 As AtoBviaC bill per distance lookup these tools will generally cache upstream queries to avoid looking up again.
 The distance cache file is a txt file with multiple json entries representing the upstream lookups.
 Re-running a query will load in the cache file (if present) as use that data instead of the upstream query.
 This is useful to replay in case of connection issues or to re-run after a bug fix.
 
 
 How-to: Add a new port
 
 Ports need an upstream ID. These can be found using either the port list or the port map.
 
 https://atobviac.com/ReleaseInformation/PortsList
 https://atobviac.com/PortLocationsMap/Index
 
Once a port code has been found the "AddNewPort" class can be used to query the basic information and update the ports.json. The class takes three inputs: - a list of port code, a source dataset id and a new dataset id.

Review the ports.json for the new data set (under the data folder of this project) and make sure the entries are correct (they will be towards the end of the file). 
Strip off elements such as "LNG Terminal" etc from the port name and ensure the timezone is correct. https://time.is/ is useful to get a better timezone mapping.

Run "DumpAccountInfo" and check the remaining distance count. A new port will typically require ~ 530 distance lookups (this increases by 2 for each new port). Talk to proshun if we need more.

Next run "AddDistanceForNewPorts" checking the source and dest versions are correct. This will use the source data to determine existing ports.
It will generate all the to/from port distance queries between the existing ports and the new ones.
** Note: You will need to ensure perform upstream queries flag has been set to true and you have set up the apikey as described above.

To test in LiNGO, copy the ports.json, distances.json and distance-version.json into the data folder of the "com.mmxlabs.lngdataserver.lng.importers" project. Run the distance update in the Data menus.

Edit and run the com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests.DistanceUpdaterTests (this a standard JUNIT test, not a plugin test).
The top part (distanceTransitions) specifies the from and to versions to update date between. Add your new version. Typically we would add froms for the last couple of versions. The tolerance should normally be set to 0.
CURRENT means the version copied into LiNGO.

The "CompareDistanceSets" should used to check the distances diffs between data sets.
This document should be used to record what has changed and the distance deltas between data sets. It is used to track our invoicing for AtoBviaC.
https://docs.google.com/spreadsheets/d/1zM0TkW24RaCOGo6km3KssWSk2yzZxdz5JPG9RM_Tstw/edit#gid=0

We have pre-paid for our copy of the data, but will need to pay for any real distance (i.e. we can ignore the missing distance lines) for each client we have.
James/Ludo should be reminded of the new outstanding bill.



