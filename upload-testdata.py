import os
import boto3

## Upload dynamic test data from current folder (e.g. c:\ws\dynamictests\)
## Make sure you have installed boto3 (e.g. pip install boto3)
## Make sure you have your AWS API credentials in $HOME\.aws\config (or other supported mechanism)

s3_resource = boto3.resource("s3", region_name="eu-west-2")

def upload_objects():
    try:
        bucket_name = "mmxlabs-testing" #s3 bucket name
        root_path = '.' # local folder for upload

        my_bucket = s3_resource.Bucket(bucket_name)

        for path, subdirs, files in os.walk(root_path):
            path = path.replace("\\","/")
            directory_name = path.replace(root_path,"")
            if (path == "."):
                continue
            print (path)
            for file in files:
                target = 'dynamictests'+directory_name+"/"+file
                print (target)
                my_bucket.upload_file(os.path.join(path, file), target)

    except Exception as err:
        print(err)

if __name__ == '__main__':
    upload_objects()
