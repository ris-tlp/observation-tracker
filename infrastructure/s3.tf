resource "aws_s3_bucket" "observation-tracker-bucket" {
  bucket_prefix = var.bucket_prefix
  force_destroy = true
}

resource "aws_s3_bucket_versioning" "audiophile-bucket-versioning" {
  bucket = aws_s3_bucket.observation-tracker-bucket.id
  versioning_configuration {
    status = var.versioning
  }
}

resource "aws_ssm_parameter" "s3_secret" {
  name = "/config/observation-tracker_dev/bucketname"
  type = "SecureString"
  value = aws_s3_bucket.observation-tracker-bucket.id
}