resource "aws_ssm_parameter" "aws_region" {
  name  = "/config/observation-tracker/region"
  type  = "SecureString"
  value = var.aws_region
}

# S3
resource "aws_ssm_parameter" "s3_secret" {
  name  = "/config/observation-tracker/s3.bucket.name"
  type  = "SecureString"
  value = aws_s3_bucket.observation-tracker-bucket.id
}

# RDS
resource "aws_ssm_parameter" "database_username" {
  name  = "/config/observation-tracker/rds.username"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.username
}


resource "aws_ssm_parameter" "database_password" {
  name  = "/config/observation-tracker/rds.password"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.password
}


resource "aws_ssm_parameter" "database_port" {
  name  = "/config/observation-tracker/rds.port"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.port
}

resource "aws_ssm_parameter" "database_name" {
  name  = "/config/observation-tracker/rds.database"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.db_name
}

resource "aws_ssm_parameter" "database_endpoint" {
  name  = "/config/observation-tracker/rds.endpoint"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.endpoint
}

# ECR
resource "aws_ssm_parameter" "repository_uri" {
  name = "/config/observation-tracker/repository.uri"
  type = "SecureString"
  value = aws_ecrpublic_repository.observation-tracker-docker-repo.repository_uri
}


resource "aws_ssm_parameter" "repository_name" {
  name = "/config/observation-tracker/repository.name"
  type = "SecureString"
  value = aws_ecrpublic_repository.observation-tracker-docker-repo.repository_name
}

# ECS
resource "aws_ssm_parameter" "api_url" {
  name = "/config/observation-tracker/api.url"
  type = "SecureString"
  value = "http://${aws_alb.observation_tracker_api_alb.dns_name}"
}


# SES
resource "aws_ssm_parameter" "notification_sender_email" {
  name = "/config/observation-tracker/notification.sender-email"
  type = "SecureString"
  value = var.notification_sender_email
}

# MQ
resource "aws_ssm_parameter" "rabbitmq_username" {
  name = "/config/observation-tracker/rabbitmq.username"
  type = "SecureString"
  value = random_string.rabbitmq_username.result
}

resource "aws_ssm_parameter" "rabbitmq_password" {
  name = "/config/observation-tracker/rabbitmq.password"
  type = "SecureString"
  value = random_password.rabbitmq_password.result
}

resource "aws_ssm_parameter" "rabbitmq_endpoint" {
  name = "/config/observation-tracker/rabbitmq.endpoint"
  type = "SecureString"
  value = aws_mq_broker.rabbitmq_broker.instances.0.endpoints.0
}

