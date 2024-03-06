resource "random_password" "rabbitmq_password" {
  length  = 16
  special = false
}

resource "random_string" "rabbitmq_username" {
  length  = 10
  special = false
}


resource "aws_mq_broker" "rabbitmq_broker" {
  broker_name         = "rabbitmq-broker"
  engine_type         = "RabbitMQ"
  engine_version      = "3.11.28"
  host_instance_type  = "mq.t3.micro"
  publicly_accessible = true
  deployment_mode     = "SINGLE_INSTANCE"
  logs { general = true }

  user {
    password       = random_password.rabbitmq_password.result
    username       = random_string.rabbitmq_username.result
    groups         = ["admins"]
    console_access = true
  }
}