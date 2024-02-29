resource "aws_lb_target_group" "observation_tracker_api" {
  name        = "observation-tracker-api"
  port        = 8080
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.vpc.id

  health_check {
    enabled = true
    healthy_threshold = 2
    interval = 30
    path = "/actuator/health/db"
    matcher = "200"
  }

  depends_on = [aws_alb.observation_tracker_alb]
}

resource "aws_alb" "observation_tracker_alb" {
  name               = "observation-tracker-lb"
  internal           = false
  load_balancer_type = "application"

  subnets = [
    aws_subnet.public_a.id, aws_subnet.public_b.id
  ]

  security_groups = [
    aws_security_group.http.id,
    aws_security_group.https.id,
    aws_security_group.egress_all.id,
  ]

  depends_on = [aws_internet_gateway.igw]
}

resource "aws_alb_listener" "sun_api_http" {
  load_balancer_arn = aws_alb.observation_tracker_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.observation_tracker_api.arn
  }
}

output "alb_url" {
  value = "http://${aws_alb.observation_tracker_alb.dns_name}"
}
