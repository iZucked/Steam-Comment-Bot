#
# Copyright (C) Minimax Labs Ltd., 2010 - 2016
# All rights reserved.
#

import shutil, os
#####################################
path_to_ws = os.getcwd()
#####################################
from_version = 55
latest_version = 56


root = "{0}/com.mmxlabs.models.lng.".format(path_to_ws)
## (Directory,ecore)
files = [("port", "port"),
		 ("actuals","actuals"),
		 ("pricing","pricing"),
		 ("fleet","fleet"),
		 ("commercial","commercial"),
		 ("spotmarkets","spotmarkets"),
		 ("cargo","cargo"),
		 ("schedule","schedule"),
		 ("analytics","analytics"),
		 ("parameters","parameters"),
		 ("types","lngtypes"),
		 ("scenario.model","scenario"),
		 ]
for (d,e) in files:
	for version in range(latest_version, from_version -1, -1):
		source = "{0}{1}/model/{2}-v{3}.ecore".format(root,d,e,version)
		dest = "{0}{1}/model/{2}-v{3}.ecore".format(root,d,e,version+1)
		#print "{0} -> {1}".format(source, dest)
		shutil.copyfile(source,dest)

