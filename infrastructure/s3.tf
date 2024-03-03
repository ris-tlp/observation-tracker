variable "bucket_prefix" {
  description = "Bucket prefix for the S3"
  type        = string
  default     = "observation-tracker-"
}


resource "aws_s3_bucket" "observation-tracker-bucket" {
  bucket_prefix = var.bucket_prefix
  force_destroy = true
}

resource "aws_s3_bucket_acl" "s3_acl" {
  bucket     = aws_s3_bucket.observation-tracker-bucket.id
  acl        = "private"
  depends_on = [aws_s3_bucket_ownership_controls.s3_bucket_acl_ownership]

}

resource "aws_s3_bucket_public_access_block" "s3_public_block" {
  bucket                  = aws_s3_bucket.observation-tracker-bucket.id
  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_ownership_controls" "s3_bucket_acl_ownership" {
  bucket = aws_s3_bucket.observation-tracker-bucket.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

resource "aws_s3_bucket_versioning" "audiophile-bucket-versioning" {
  bucket = aws_s3_bucket.observation-tracker-bucket.id
  versioning_configuration {
    status = "Enabled"
  }
}