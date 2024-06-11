# Instructions for deploying the system
1. Build the docker images for the microsservices by running `docker build -t <desired-image-name> .` at root directory of each of them;
2. Log into your AWS account;
3. Search for the **Elastic Container Registry (ECR)** service. We'll use it to create a repository in the cloud to store the Docker images we built in step 1;
    - Select "Public repository";
    - Give it a name such as `servico-assinaturas-repo`.
    - Your public repository is created and ready to be used.
4. Click the repository and then locate the "View push commands" button on the top-right corner of the screen. Go ahead and click on it. You'll see the commands you need to run step by step on your local computer where your Docker images from step 1 are stored to push them to the cloud. Go ahead and follow the instructions. **Notice that'll need to have the AWS CLI installed on your local machine**. It is possible that you get an `AccessDeniedException` when calling `GetAuthorizationToken` when running the first command to authenticate to AWS, which is probably due to **Identity and Access Management (IAM)** permissions. If that's the case, follow the instructions below: 
    - Navigate to the IAM service;
    - Create a new IAM user;
    - Create the policy below for the ECR service and directly attach it to the user you've created;
    `{
      "Version": "2012-10-17",
      "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr-public:GetAuthorizationToken",
                "sts:GetServiceBearerToken"
            ],
            "Resource": "*"
        }
      ]
    }`
    - On your local machine, run `aws configure` and enter the access key and secret access key for the newly created user, which has the necessary permissions for authenticating from command-line;
    - Re-run the first command. OBS.: now you may get an error because you do not have permission to access the Docker daemon and, when you run the first command as `sudo`, you also get an error saying interactive login cannot be performed from a non-TTY device. In such case, make sure your user has the correct Docker socket permissions by running `sudo chown root:docker /var/run/docker.sock` and then `sudo chmod g+rw /var/run/docker.sock`. After that, add your user to the Docker group by running `sudo usermod -aG docker $USER`. Finally, restart your system and attempt to run the first command again as a regular user (non-`sudo`). After you're logged in, you may remove your user from the Docker group by running `sudo gpasswd -d $USER docker` or `sudo deluser $USER docker`.

[This](https://www.youtube.com/watch?v=YDNSItBN15w&t=10m15s) video on YouTube also describes the process of deploying a Docker image to AWS.