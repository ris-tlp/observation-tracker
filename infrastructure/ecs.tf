## Figure out IAM, dont use creds

resource "aws_ecs_cluster" "observation_tracker_cluster" {
  name = "observation_tracker_cluster"
}


resource "aws_ecs_service" "observation_tracker_api" {
  name            = "observation_tracker_api"
  cluster         = aws_ecs_cluster.observation_tracker_cluster.arn
  task_definition = aws_ecs_task_definition.observation_tracker_api.arn
  launch_type     = "FARGATE"
  desired_count   = 1
  load_balancer {
    target_group_arn = aws_lb_target_group.observation_tracker_api.arn
    container_name   = "observation_tracker_api"
    container_port   = "8080"
  }
  network_configuration {
    assign_public_ip = false

    security_groups = [
      aws_security_group.egress_all.id,
      aws_security_group.ingress_api.id,
    ]

    subnets = [
      aws_subnet.private_a.id,
      aws_subnet.private_b.id,
    ]
  }
}

resource "aws_ecs_task_definition" "observation_tracker_api" {
  family             = "observation_tracker_api"
  execution_role_arn = aws_iam_role.observation_tracker_api_task_execution_role.arn
  task_role_arn = aws_iam_role.ecs_task_role.arn

  container_definitions = <<EOF
  [
    {
      "name": "observation_tracker_api",
      "image": "${aws_ecrpublic_repository.observation-tracker-docker-repo.repository_uri}:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-region": "us-east-1",
          "awslogs-group": "/ecs/observation_tracker_api",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
  EOF

  cpu                      = 2048
  memory                   = 4096
  requires_compatibilities = ["FARGATE"]

  # This is required for Fargate containers (more on this later).
  network_mode = "awsvpc"
}

