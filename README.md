## Observation Tracker API
Observation Tracker is a tool designed for both amateur and professional astronomers. It provides a platform to record, organize, and share your celestial discoveries with the world! 

## Features
- Completely HATEOAS compliant Spring Boot API with paginated and sorted responses
- Cached responses for frequently accessed data through Redis/ElastiCache
- Asynchronous processing of notification emails through RabbitMQ/AmazonMQ and SES
- Fault tolerant API through a load balanced multi-az ECS deployment
- Repository and data management of S3/RDS data through Hibernate
- Docker image built and pushed to ECR on merge from feature branch through GitHub Actions 
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

## Run the API Locally
Configuration of the local development environment can be done through the `.env` file.
1. Navigate to cloned repository.
2. Run `make localstack-permission`. This gives the `init_localstack_infra.sh` script executable permissions to run as an init hook in docker compose to locally initialize S3 and SES.
3. Run `docker compose up` to spin up the Redis, RabbitMQ, Localstack, Postgres and API services.
4. Run `curl -X GET http://localhost:8080/v1/api-docs` to check if the API is up and running. 

## Deploy the API on AWS
Configuration of AWS services is done entirely through Terraform, and connection details for each service is stored and fetched by the API through SSM Parameter Store.
1. Navigate to cloned repository.
2. Run `make set-actions-secrets`. This will use the GitHub CLI to set AWS secrets to be used with GitHub Actions.
3. Run `make aws-infra`. This uses terraform to provision all the services defined under `infrastructure/`.
   - The only input asked here will be an email to use through SES.
   - Once completed, the API URL will be given. It can also be retrieved from parameter store if needed.
4. Run `curl -X GET http://<api_url>/v1/api-docs` to check if the API is up and running.
   
## Sample Response
`curl -X GET "http://localhost:8080/v1/celestial-events?page=1&size=2`

```json
{
    "_embedded": {
        "getSlimCelestialEventDtoList": [{
            "uuid": "4c9699e1-4aac-4f00-9001-551017a45777",
            "createdTimestamp": 1710099872974,
            "updatedTimestamp": 1710099872974,
            "celestialEventName": "Saturn at Opposition",
            "celestialEventDescription": "The ringed planet will be at its closest approach to Earth and its face will be fully illuminated by the Sun. It will be brighter than any other time of the year and will be visible all night long.",
            "images": [{
                "uuid": "acb1cf1b-40ea-498e-9409-c5b59cfbebd5",
                "createdTimestamp": 1710099872975,
                "updatedTimestamp": 1710099872975,
                "url": "https://bucketname.s3.us-east-1.amazonaws.com/1710099872960-saturn-equinox.png"
            }],
            "celestialEventDateTime": "2024-03-25 17:15:00",
            "eventStatus": "UPCOMING",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/v1/celestial-events/4c9699e1-4aac-4f00-9001-551017a45777",
                    "type": "GET, PATCH, DELETE"
                }
            }
        }, {
            "uuid": "9057fdea-777d-49c6-9caa-3ff7f67c732a",
            "createdTimestamp": 1710099991624,
            "updatedTimestamp": 1710099991624,
            "celestialEventName": "September Equinox",
            "celestialEventDescription": "The September equinox occurs at 12:39 UTC. The Sun will shine directly on the equator and there will be nearly equal amounts of day and night throughout the world.",
            "images": [{
                "uuid": "405cdd8e-0a0d-48f7-8482-ce4b7b4bbc08",
                "createdTimestamp": 1710099991627,
                "updatedTimestamp": 1710099991627,
                "url": "https://bucketname.s3.us-east-1.amazonaws.com/1710099991538-sun.png"
            }, {
                "uuid": "a1a6f4e5-8bab-43d1-bf5b-a63a5e264b05",
                "createdTimestamp": 1710099991629,
                "updatedTimestamp": 1710099991629,
                "url": "https://bucketname.s3.us-east-1.amazonaws.com/1710099991611-equinox.png"
            }],
            "celestialEventDateTime": "2024-11-22 17:15:00",
            "eventStatus": "UPCOMING",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/v1/celestial-events/9057fdea-777d-49c6-9caa-3ff7f67c732a",
                    "type": "GET, PATCH, DELETE"
                }
            }
        }]
    },
    "_links": {
        "first": {
            "href": "http://localhost:8080/v1/celestial-events?page=0&size=2"
        },
        "prev": {
            "href": "http://localhost:8080/v1/celestial-events?page=0&size=2"
        },
        "self": {
            "href": "http://localhost:8080/v1/celestial-events?page=1&size=2"
        },
        "last": {
            "href": "http://localhost:8080/v1/celestial-events?page=1&size=2"
        },
        "filter-by-status": {
            "href": "http://localhost:8080/v1/celestial-events?status={status}",
            "type": "GET",
            "templated": true
        }
    },
    "page": {
        "size": 2,
        "totalElements": 4,
        "totalPages": 2,
        "number": 1
    }
}
```
