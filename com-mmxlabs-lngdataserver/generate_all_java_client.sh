#!/bin/bash
#
# Copyright (C) Minimax Labs Ltd., 2010 - 2018
# All rights reserved.
#


cd ..

echo "Generating the port java client from the latest version"

cd com-mmxlabs-lngdataservice-ports.git
./swagger-java-client.sh 

cd ..

echo "Generating the vessels java client from the latest version"
cd com-mmxlabs-lngdataservice-vessels.git
./swagger-java-client.sh

cd ..

echo "Generating the distances java client from the latest version"
cd com-mmxlabs-lngdataservice-ports-distances.git
./generate_java_client.sh
