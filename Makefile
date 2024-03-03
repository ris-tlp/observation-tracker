#docker-start:
#    docker build -t api:latest -f Dockerfile . \
#    docker run -p 8080\:8080 -v ~/.aws/\:/root/.aws -d api

#aws-infra:
	#terraform -chdir=terraform/ init -input=false
	#terraform -chdir=infrastructure/ apply --auto-approve

test-actions-ecrpush:
	 act -s AWS_ACCESS_KEY_ID=$$(aws configure get aws_access_key_id) -s AWS_SECRET_ACCESS_KEY=$$(aws configure get aws_secret_access_key)

set-actions-secrets:
	gh secret set --app actions --body $$(aws configure get aws_secret_access_key) aws_secret_access_key
	gh secret set --app actions --body $$(aws configure get aws_access_key_id) aws_access_key_id

down-cluster-service:
	aws ecs update-service --desired-count 0 --cluster "observation_tracker_api_cluster" --service "observation_tracker_api_service"