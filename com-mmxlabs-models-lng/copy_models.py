#
# Copyright (C) Minimax Labs Ltd., 2010 - 2016
# All rights reserved.
#

import shutil, os
#####################################
path_to_ws = os.getcwd()
version = 55
#####################################

root = "{0}/com.mmxlabs.models.lng.".format(path_to_ws)
files = ["port",
		 "actuals",
		 "pricing",
		 "fleet",
		 "commercial",
		 "spotmarkets",
		 "cargo",
		 "schedule",
		 "analytics",
		 "parameters",
		 ]
for f in files:
	source = "{0}{1}/model/{1}.ecore".format(root,f)
	dest = "{0}{1}/model/{1}-v{2}.ecore".format(root,f,version)
	shutil.copyfile(source,dest)

# special cases
f = "lngtypes"
source = "{0}types/model/{1}.ecore".format(root,f)
dest = "{0}types/model/{1}-v{2}.ecore".format(root,f,version)
shutil.copyfile(source,dest)
f = "scenario"
source = "{0}{1}.model/model/{1}.ecore".format(root,f)
dest = "{0}{1}.model/model/{1}-v{2}.ecore".format(root,f,version)
shutil.copyfile(source,dest)
