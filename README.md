## Observation Tracker API
Observation Tracker is a tool designed for both amateur and professional astronomers. It provides a platform to record, organize, and share your celestial discoveries with the world! 

## Features
- Completely HATEOAS compliant Spring Boot API with paginated and sorted responses
- Cached responses for frequently accessed data through Redis/ElastiCache
- Asynchronous processing of notification emails through RabbitMQ/AmazonMQ and SES
- Fault tolerant API through a load balanced multi-az deployment
- Repository and data management through Hibernate
- Docker image built and pushed to ECR on merge from feature branch through GitHub Actions CI/CD
- AWS infrastructure provisioned and managed through Terraform

## Production Architecture
![Architecture](https://raw.githubusercontent.com/ris-tlp/observation-tracker/main/media/architecture_diagram.png?token=GHSAT0AAAAAAB6SHAUHAWFQINV2Z7KE2WSEZPNLQSQ)

## Documentation
- OpenAPI Specification: `/v1/api-docs`
- SwaggerUI: `/v1/api-swagger-ui.html`

## Requirements 
- Configure AWS account through [AWS CLI](https://aws.amazon.com/cli/). [Required for Terraform]
- [Terraform](https://www.terraform.io/). [Required to provision AWS services]
- [Docker and Docker-Compose](https://www.docker.com/). [Required to locally run API with other services]
- [Make](https://www.gnu.org/software/make/manual/make.html). [Required to run a few setup commands for local and cloud deployments]
- [GitHub CLI](https://cli.github.com/). [(Optionally) Required to set AWS secrets for GitHub Actions]

## Local Environment Setup
Configuration of the local development environment can be done through the `.env` file.
1. Navigate to cloned repository.
2. Run `make localstack-permission`. This gives the `init_localstack_infra.sh` script executable permissions to run as an init hook in docker compose to locally initialize S3 and SES.
3. Run `docker compose up` to spin up the Redis, RabbitMQ, Localstack, Postgres and API services.
4. Run `curl -X GET http://localhost:8080/v1/api-docs` to check if the API is up and running. 

## AWS Environment Setup
Configuration of AWS services is done entirely through Terraform, and connection details for each service is stored and fetched by the API through SSM Parameter Store.
1. Navigate to cloned repository.
2. Run `make set-actions-secrets`. This will use the GitHub CLI to set AWS secrets to be used with GitHub Actions.
3. Run `make aws-infra`. This uses terraform to provision all the services defined under `infrastructure/`.
   - The only input asked here will be an email to use through SES.
   - Once completed, the API URL will be given. It can also be retrieved from parameter store if needed.
4. Run `curl -X GET http://<api_url>/v1/api-docs` to check if the API is up and running.
   

