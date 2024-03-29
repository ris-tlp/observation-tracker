aws-infra:
	terraform -chdir=infrastructure/ init -input=false
	terraform -chdir=infrastructure/ apply --auto-approve

test-actions-ecrpush:
	 act -s AWS_ACCESS_KEY_ID=$$(aws configure get aws_access_key_id) -s AWS_SECRET_ACCESS_KEY=$$(aws configure get aws_secret_access_key)

set-actions-secrets:
	gh secret set --app actions --body $$(aws configure get aws_secret_access_key) aws_secret_access_key
	gh secret set --app actions --body $$(aws configure get aws_access_key_id) aws_access_key_id

down-cluster-service:
	aws ecs update-service --desired-count 0 --cluster "observation_tracker_api_cluster" --service "observation_tracker_api_service"

up-cluster-service:
	aws ecs update-service --cluster "observation_tracker_api_cluster" --service "observation_tracker_api_service" --force-new-deployment

localstack-permission:
	chmod +x infrastructure/init_localstack_infra.sh