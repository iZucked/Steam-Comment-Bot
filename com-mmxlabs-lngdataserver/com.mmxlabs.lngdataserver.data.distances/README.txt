Example command to upload data file to a data hub. Note this assumes no authentication. Basic auth options can be used also.

curl -X POST -H "Content-Type: application/json" http://localhost:8080/distances/sync/versions -d @filename