====
    Copyright (C) Minimax Labs Ltd., 2010 - 2013
    All rights reserved.
====

The target-definition.target file is used by Maven/Tycho to compile this project. As part of this process the com.mmxlabs.platform.feature project will embed the version numbers of various eclipse projects into the feature.xml based on those defined by this target file. We are also using reproducable version qualifiers as in [1]. Should the target definition  
update required eclipse dependencies - it is very likely that you will need to create a "fake" commit on this feature.xml to force the build qualifier to increment. For example the EMF plugins have a non-timestamp based qualifier which means it is ignored for the purposes of the timestamp calculation. Thus the updated feature will not be redownloaded as even though the content has changed the version number has not. 

[1] http://wiki.eclipse.org/Tycho/Reproducible_Version_Qualifiers#Feature_version_qualifiers