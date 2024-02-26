resource "aws_ssm_parameter" "aws_region" {
  name  = "/config/observation-tracker_prod/region"
  type  = "SecureString"
  value = var.aws_region
}

resource "aws_ssm_parameter" "s3_secret" {
  name  = "/config/observation-tracker_prod/s3.bucket.name"
  type  = "SecureString"
  value = aws_s3_bucket.observation-tracker-bucket.id
}


resource "aws_ssm_parameter" "database_username" {
  name  = "/config/observation-tracker_prod/rds.username"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.username
}


resource "aws_ssm_parameter" "database_password" {
  name  = "/config/observation-tracker_prod/rds.password"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.password
}


resource "aws_ssm_parameter" "database_port" {
  name  = "/config/observation-tracker_prod/rds.port"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.port
}


resource "aws_ssm_parameter" "database_name" {
  name  = "/config/observation-tracker_prod/rds.database"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.db_name
}

resource "aws_ssm_parameter" "database_endpoint" {
  name  = "/config/observation-tracker_prod/rds.endpoint"
  type  = "SecureString"
  value = aws_db_instance.rds_instance.endpoint
}