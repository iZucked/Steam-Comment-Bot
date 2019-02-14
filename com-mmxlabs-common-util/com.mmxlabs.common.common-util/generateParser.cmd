@REM
@REM Copyright (C) Minimax Labs Ltd., 2010 - 2019
@REM All rights reserved.
@REM

java -jar java-cup-11b.jar -interface -parser Parser -destdir src/com/mmxlabs/common/parser/impl -package com.mmxlabs.common.parser.impl -symbols ParserSymbols seriesparser.cup
java -jar jflex-1.6.1.jar seriesparser.flex -d src/com/mmxlabs/common/parser/impl 