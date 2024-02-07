terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~>4.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}


resource "aws_ssm_parameter" "aws_region" {
  name = "/config/observation-tracker_dev/region"
  type = "SecureString"
  value = var.aws_region
}