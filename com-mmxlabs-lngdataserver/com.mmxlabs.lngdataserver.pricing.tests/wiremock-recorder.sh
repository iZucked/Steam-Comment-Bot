#!/usr/bin/env bash
#
# Copyright (C) Minimax Labs Ltd., 2010 - 2018
# All rights reserved.
#


# http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.5.1/wiremock-standalone-2.5.1.jar
WMOCK_BIN=wiremock-standalone-2.5.1.jar

cd data

if [ ! -f $WMOCK_BIN ]; then
    echo "Wiremock not found... trying to download it"
    curl -o $WMOCK_BIN http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.5.1/wiremock-standalone-2.5.1.jar
fi

java -jar $WMOCK_BIN --proxy-all="http://localhost:8096" --port 8080 --record-mappings --verbose
