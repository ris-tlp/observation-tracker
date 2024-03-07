#!/bin/bash
awslocal s3 mb s3://${LOCALSTACK_S3_BUCKET}
awslocal ses verify-email-identity --email-address ${LOCALSTACK_SES_EMAIL}
