import os
import boto3

## Download dynamic test data to current folder (e.g. c:\ws\dynamictests\)
## Make sure you have installed boto3 (e.g. pip install boto3)
## Make sure you have your AWS API credentials in $HOME\.aws\config (or other supported mechanism)

s3_resource = boto3.resource("s3", region_name="eu-west-2")

def downloadDirectory(bucketName):
    s3_resource = boto3.resource('s3')
    bucket = s3_resource.Bucket(bucketName) 
    for obj in bucket.objects.filter(): ##(Prefix = remoteDirectoryName):
        if not os.path.exists(os.path.dirname(obj.key)):
            os.makedirs(os.path.dirname(obj.key))
        bucket.download_file(obj.key, obj.key) # save to same path

if __name__ == '__main__':
	try:
		downloadDirectory("mmxlabs-testing")
	except Exception as err:
		print(err)    
