#
# Copyright (C) Minimax Labs Ltd., 2010 - 2022
# All rights reserved.
#

import sys
import os

if len(sys.argv) < 2:
    print "please provide a csv files"
    sys.exit(1)


# traverse root directory, and list directories as dirs and files as files
for root, dirs, files in os.walk(sys.argv[1]):
    path = root.split(os.sep)
    for file in files:
    	if file == "Vessel Classes.csv":
	        print(root +  os.sep + file)
	        sys.stdout.flush()
	        os.system("python " + os.path.dirname(os.path.realpath(__file__)) + "/migrate_fleet_model.py \"" + root + "\"");
	        sys.stdout.flush()