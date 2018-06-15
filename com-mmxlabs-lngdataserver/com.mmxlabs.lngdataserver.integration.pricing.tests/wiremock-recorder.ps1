$WMOCK_BIN =  "$($PSScriptRoot)\wiremock-standalone-2.5.1.jar"

if(![System.IO.File]::Exists($WMOCK_BIN)){
    echo "no wiremock jar found... will download to $WMOCK_BIN"
    $client = new-object System.Net.WebClient
	$client.DownloadFile("http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.6.0/wiremock-standalone-2.6.0.jar",$WMOCK_BIN)
}

Set-Location "$($PSScriptRoot)\data"
java -jar $WMOCK_BIN --proxy-all="http://localhost:8090" --port 8089 --record-mappings --verbose