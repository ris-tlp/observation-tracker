#resource "aws_subnet" "rds_subnet_a" {
#  vpc_id            = aws_vpc.vpc.id
#  cidr_block        = "10.0.3.128/25"
#  availability_zone = "us-east-1a"
#
#  tags = {
#    "Name" = "rds | us-east-1a"
#  }
#}
#
#resource "aws_subnet" "rds_subnet_b" {
#  vpc_id            = aws_vpc.vpc.id
#  cidr_block        = "10.0.4.128/25"
#  availability_zone = "us-east-1b"
#
#  tags = {
#    "Name" = "rds | us-east-1b"
#  }
#}
#
#
#resource "aws_route_table_association" "rds_subnet_a_association" {
#  subnet_id      = aws_subnet.rds_subnet_a.id
#  route_table_id = aws_route_table.private.id
#}
#
#resource "aws_route_table_association" "rds_subnet_b_association" {
#  subnet_id      = aws_subnet.rds_subnet_b.id
#  route_table_id = aws_route_table.private.id
#}
#
#resource "aws_db_subnet_group" "rds_subnet_group" {
#  name       = "rds-subnet-group"
#  subnet_ids = [aws_subnet.rds_subnet_a.id, aws_subnet.rds_subnet_b.id]
#}
#
#
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
