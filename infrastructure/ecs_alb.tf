resource "aws_lb_target_group" "observation_tracker_lb_target" {
  name        = "observation-tracker-api"
  port        = 8080
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.vpc.id

  health_check {
    enabled = true
    path    = "/actuator/health/db"
    matcher = "200"
  }

  depends_on = [aws_alb.observation_tracker_api_alb]
}

resource "aws_alb" "observation_tracker_api_alb" {
  name               = "observation-tracker-api-alb"
  internal           = false
  load_balancer_type = "application"

  subnets = [
    aws_subnet.public_a.id,
    aws_subnet.public_b.id,
  ]

  security_groups = [
    aws_security_group.http.id,
    aws_security_group.https.id,
    aws_security_group.egress_all.id,
  ]

  depends_on = [aws_internet_gateway.internet_gateway]
}

resource "aws_alb_listener" "observation_tracker_api_http" {
  load_balancer_arn = aws_alb.observation_tracker_api_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.observation_tracker_lb_target.arn
  }
}

output "alb_url" {
  value = "http://${aws_alb.observation_tracker_api_alb.dns_name}"
}
