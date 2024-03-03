variable "notification_sender_email" {
  type = string
}

resource "aws_ses_email_identity" "notification_sender_email" {
  email = var.notification_sender_email
}

