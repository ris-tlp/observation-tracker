variable "aws_region" {
  type        = string
  default     = "us-east-1"
}

variable "bucket_prefix" {
  description = "Bucket prefix for the S3"
  type        = string
  default     = "observation-tracker-"
}

variable "versioning" {
  type    = string
  default = "Enabled"
}

#variable "rds_password" {
#  description = "Password for the database in the RDS cluster"
#  type        = string
#}
