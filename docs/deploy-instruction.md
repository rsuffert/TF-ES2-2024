# Instructions for deploying the system to AWS
1. Log into your AWS account;
2. Search for the **Elastic Container Registry (ECR)** service and create one instance for each microsservice of the system;
    - Select "Public repository";
    - Give it a name;
    - Your public repository is created and ready to be used.
3. For each ECR repository you created on step 2, click on it and locate the "View push commands" button on the top-right corner of the screen. Go ahead and click on it. You'll see the commands you need to run step by stop on your local computer in order to build and push to the cloud the image of the microsservice that'll be stored in that container. Go ahead and follow those instructions. **Notice that you'll need to have the AWS CLI installed on your local machine in order to run the commands in the step by step**. Now, it is possible that, while following the step by step, you get some errors due to lack of **Identity and Access Management (IAM)** permissions. If that's the case, follow the instructions below to create a IAM user that has all the necessary permissions to authenticate to the ECR repository and push images to it:
    - Navigate to the IAM service;
    - Create a new IAM user;
    - Create the policy below for the ECR Public service and directly attach it to the user you've created;
    `{
      "Version": "2012-10-17",
      "Statement": [
        {
          "Sid": "",
          "Effect": "Allow",
          "Action": [
              "ecr-public:GetAuthorizationToken",
              "sts:GetServiceBearerToken"
            ],
          "Resource": "*"
        },
        {
          "Sid": "",
          "Effect": "Allow",
          "Action": [
            "ecr-public:UploadLayerPart",
            "ecr-public:PutImage",
            "ecr-public:InitiateLayerUpload",
            "ecr-public:CompleteLayerUpload",
            "ecr-public:BatchCheckLayerAvailability"
          ],
          "Resource": "*"
        }
      ]
    }`
    - On your local machine, run `aws configure` and enter the access key and secret access key for the newly created user, which has the necessary permissions for authenticating from command-line;
    - Re-run the command. OBS.: now you may get an error because your local Linux user does not have permission to access the Docker daemon and, when you run the command as `sudo`, you also get an error saying interactive login cannot be performed from a non-TTY device. This is not related to AWS, but to Linux and Docker. In such case, you need to make sure your user has the correct Docker socket permissions by running `sudo chown root:docker /var/run/docker.sock` and then `sudo chmod g+rw /var/run/docker.sock`. After that, add your user to the Docker group by running `sudo usermod -aG docker $USER`. Finally, restart your system and attempt to re-run the command. After you have all your images in the cloud, you may remove your user from the Docker group by running `sudo gpasswd -d $USER docker` or `sudo deluser $USER docker`.
4. At this point, you already have the images of all microsservices ready to execute in the cloud. Now, we need to run them as tasks in an **Elastic Container Service (ECS)** instance. Go ahead and search for the ECS service and create a new cluster by (1) giving it a name, (2) selecting "Amazon EC2 instances" in the "Infrastructure" section, (3) enabling auto-assign public IP in the "Network settings for Amazon EC2 instances" section and (4) hitting the "Create" button.

**OBS.:** [This](https://www.youtube.com/watch?v=YDNSItBN15w&t=10m15s) video on YouTube also describes the process of deploying a Docker image to AWS.