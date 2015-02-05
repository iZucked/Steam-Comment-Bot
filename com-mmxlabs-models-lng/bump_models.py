#
# Copyright (C) Minimax Labs Ltd., 2010 - 2015
# All rights reserved.
#

import shutil, os
#####################################
path_to_ws = os.getcwd()
#####################################
from_version = 23
latest_version = 25


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
for f in files:
	(d, e) = f
	arr = [x for x in xrange(from_version, latest_version+1)]
	arr.sort(reverse=True)
	for version in arr:
		source = "{0}{1}/model/{2}-v{3}.ecore".format(root,d,e,version)
		dest = "{0}{1}/model/{2}-v{3}.ecore".format(root,d,e,version+1)
		shutil.copyfile(source,dest)

