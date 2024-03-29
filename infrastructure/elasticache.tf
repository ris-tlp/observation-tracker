resource "aws_elasticache_cluster" "redis_cache" {
  cluster_id           = "redis-cluster"
  engine               = "redis"
  engine_version       = "6.x"
  node_type            = "cache.t3.micro"
  num_cache_nodes      = 1
  parameter_group_name = "default.redis6.x"
  security_group_ids = [aws_security_group.redis_cache_sg.id]
  subnet_group_name = aws_elasticache_subnet_group.elasticache_subnet.name
}

resource "aws_security_group" "redis_cache_sg" {
  vpc_id = aws_vpc.vpc.id
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

resource "aws_elasticache_subnet_group" "elasticache_subnet" {
  name       = "subnet"
  subnet_ids = [aws_subnet.private_a.id]
}
