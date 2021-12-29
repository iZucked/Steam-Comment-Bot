#!/bin/bash

echo $RELEASE_VERSION
echo $DEVELOPMENT_VERSION


## Check Variables

if [ -z "$RELEASE_VERSION" ]; then
        echo "Release version is not set"
        exit 1
fi
LEN1=`expr match "$RELEASE_VERSION" '^[0-9]\+\.[0-9]\+\.[0-9]\+\.RELEASE$'`
if [ ! $LEN1 -gt 0 ]; then
        echo "Release version does not match expected pattern:  '^[0-9]\+\.[0-9]\+\.[0-9]\+\.RELEASE$'";
        exit 1
fi

if [ -z "$DEVELOPMENT_VERSION" ]; then
        echo "Development version is not set"
        exit 1
fi
LEN2=`expr match "$DEVELOPMENT_VERSION" '^[0-9]\+\.[0-9]\+\.[0-9]\+-SNAPSHOT$'`
if [ ! $LEN2 -gt 0 ]; then
        echo "Development version does not match expected pattern:  '^[0-9]\+\.[0-9]\+\.[0-9]\+-SNAPSHOT$'";
        exit 1
fi

## Set devel version number
for repo in com*/; do
        pushd $repo
        mvn -Dtycho.mode=maven -DnewVersion=$DEVELOPMENT_VERSION org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version
        popd
done
mvn -Dtycho.mode=maven -DnewVersion=$DEVELOPMENT_VERSION org.eclipse.tycho:tycho-versions-plugin::2.4.0:set-version
git commit -a -m "Bump version to $DEVELOPMENT_VERSION in prep for next development iteration"

SHORTVERSION=`echo $DEVELOPMENT_VERSION | sed -e "s/-SNAPSHOT//"`
echo "projectRoot ." > update-refs-dev.tiger
echo "setting osgi.release.qualifier RELEASE" >> update-refs-dev.tiger
for project in `cat known-projects.txt`; do
        echo "updateReferences $project $SHORTVERSION" >> update-refs-dev.tiger
done
java -jar libs/com.inventage.tools.versiontiger-1.3.0-SNAPSHOT-cli.jar update-refs-dev.tiger

for repo in com*/; do
        pushd $repo
        #Update refs does bad things to pom parent version
        git checkout -- pom.xml
        git checkout -- */pom.xml
        git checkout -- */category.xml
        git checkout -- */feature.xml
        git checkout -- */*.project
        popd
done


## Update commit
git commit -a --amend --no-edit
git branch build/$DEVELOPMENT_VERSION

## Generate bundles
git bundle create changes.git TMP_RELEASE_BASE..build/$DEVELOPMENT_VERSION TMP_RELEASE_BASE..${RELEASE_VERSION} --tags --branches
