#docker-up:
#    docker build -t api\:latest -f Dockerfile . \
#    docker run -p 8080\:8080 -v ~/.aws/\:/root/.aws -d api

set-actions-secrets:
	gh secret set --app actions --body $$(aws configure get aws_secret_access_key) aws_secret_access_key
	gh secret set --app actions --body $$(aws configure get aws_access_key_id) aws_access_key_id