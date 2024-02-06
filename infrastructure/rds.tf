resource "aws_db_instance" "rds_instance" {
  allocated_storage = 20
  identifier        = "rds-terraform"
  instance_class    = "db.t3.micro"
  engine            = "postgres"
#  engine_version    = "14.1"

  db_name  = "observation_tracker_database"
  username = "awsadmin"
  password = var.rds_password


  publicly_accessible    = true
  skip_final_snapshot    = true
  vpc_security_group_ids = [aws_security_group.rds_security_group.id]

}

resource "aws_security_group" "rds_security_group" {
  name = "rds_security_group"
  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ssm_parameter" "database_username" {
  name = "/config/observation-tracker_dev/rds.username"
  type = "SecureString"
  value = aws_db_instance.rds_instance.username
}


resource "aws_ssm_parameter" "database_password" {
  name = "/config/observation-tracker_dev/rds.password"
  type = "SecureString"
  value = aws_db_instance.rds_instance.password
}


resource "aws_ssm_parameter" "database_port" {
  name = "/config/observation-tracker_dev/rds.port"
  type = "SecureString"
  value = aws_db_instance.rds_instance.port
}


resource "aws_ssm_parameter" "database_name" {
  name = "/config/observation-tracker_dev/rds.database"
  type = "SecureString"
  value = aws_db_instance.rds_instance.db_name
}

resource "aws_ssm_parameter" "database_endpoint" {
  name = "/config/observation-tracker_dev/rds.endpoint"
  type = "SecureString"
  value = aws_db_instance.rds_instance.endpoint
}