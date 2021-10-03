Tomcat-Container-Deployment (manual deployment steps)


Deploying a containerized web application

Create a repository(Artifact Registry)
1.	Set the PROJECT_ID environment variable to your Google Cloud project ID (PROJECT_ID). You'll use this environment variable when you build the container image and push it to your repository.
export PROJECT_ID=customer-n-test-service

2.	Confirm that the PROJECT_ID environment variable has the correct value:
echo $PROJECT_ID

3.	Set your project ID for the gcloud command-line tool:
gcloud config set project $PROJECT_ID

4.	Create the hello-repo repository with the following command:
gcloud artifacts repositories create hello-repo \
   --repository-format=docker \
   --location=us-central1 \
   --description="Docker repository"

Building the container image

5.	Download the PSQLconnection project source code and Dockerfile by running the following commands:

git clone https://github.com/rithika531/PSQLConProject.git
cd PSQLConProject

Note : The github repo mentioned above has DockerFile which installs Tomcat Server .

DockerFile :(Tomcat 8.5.0)

FROM centos
    
RUN mkdir /opt/tomcat/ 
    
WORKDIR /opt/tomcat
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.0/bin/apache-tomcat-8.5.0.tar.gz  
RUN tar xvfz apache*.tar.gz
RUN mv apache-tomcat-8.5.0/* /opt/tomcat/.
RUN yum -y install java
RUN java -version
    
WORKDIR /opt/tomcat/webapps
    
EXPOSE 8080 
    
CMD ["/opt/tomcat/bin/catalina.sh", "run"]

DockerFile:(Passing Tomcat version as a variable)

FROM centos

RUN mkdir /opt/tomcat/ 

ARG tomcatversion=tomcat-8/v8.5.0/bin/apache-tomcat-8.5.0.tar.gz
WORKDIR /opt/tomcat
RUN curl -O https://archive.apache.org/dist/tomcat/${tomcatversion}
RUN tar xvfz apache*.tar.gz
RUN mv apache-tomcat-8.5.0/* /opt/tomcat/.
RUN yum -y install java
RUN java -version

WORKDIR /opt/tomcat/webapps
#ARG WAR_FILE=dist/PSQLConProject.war
#COPY ${WAR_FILE} PSQLConProject.war

EXPOSE 8080 

CMD ["/opt/tomcat/bin/catalina.sh", "run"]

6.	Build and tag the Docker image for PSQL-app:
docker build -t us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/hello-app:v1 .

7.	Run the docker images command to verify that the build was successful:
 	docker images

Pushing the Docker image to Artifact Registry

8.	Configure the Docker command-line tool to authenticate to Artifact Registry:
gcloud auth configure-docker us-central1-docker.pkg.dev


9.	Push the Docker image that you just built to the repository:
docker push us-central1-docker.pkg.dev/${PROJECT_ID}/hello-repo/hello-app:v1

Pushing the Docker image to Container Registry

	Build docker image using  : 
 	docker build -t [Name of the image] .

	Tag the local Docker image for uploading
docker tag test-vpc-app "gcr.io/customer-n-test-service/simple-web-app:v1"

	 Finally, push the Docker image to your private Container Registry:
docker push "gcr.io/customer-n-test-service/simple-web-app:v1"

	Add the image location in the .yaml file as follows : 
gcr.io/customer-n-test-service/simple-web-app@sha256:e710d773afeca33cad228d0a47b08f3bfe22e438e39a2153fbb1ce53dc07d474


Reference : https://gruntwork.io/guides/kubernetes/deploying-a-dockerized-app-on-gcp-gke/#push-the-docker-image
Reference:  https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app


Sample App Deployment on GKE

10.	Create a Service Account
Eg : test-service-account.
Note : SQL Client , Basic-Admin,Owner,Editor roles should be given to the service account .

11.	Create Kubernetes Engine
Using the above created service account create a GKE with 1 node . You can select the service account created under NODE-POOLS -Security .

12.	Create a PSQL instance with both Public IP and Private IP
After creating the instance , under users change the password of uername postgress to  “sampledb” . Because “sampledb” is hardcoded in the sample app.

13.	Using Public IP connect the PSQL instance from pgAdmin and create table :
create table Test (custid numeric(10) primary key,custname varchar(50)) ;
select * from Test ;

14.	Create a Secret Object
kubectl create secret generic cloudsql-db-credentials \
--from-literal=username=postgres --from-literal=password=sampled 

Connecting using the Cloud SQL Auth proxy

17 . Enable Workload Identity for your cluster :
To enable Workload Identity on an existing cluster, modify the cluster with       the following command:
                     gcloud container clusters update CLUSTER_NAME \
  	  --workload-pool=PROJECT_ID.svc.id.goog

gcloud container clusters update test-cluster-manual --zone=us-central1-c  --workload-pool=customer-n-test-service.svc.id.goog

18 . Typically, each application has its own identity, represented by a KSA and GSA pair. Create a KSA for your application by running kubectl apply -f service-account.yaml:

19. Enable the IAM binding between your YOUR-GSA-NAME and YOUR-KSA-NAME:
	
	gcloud iam service-accounts add-iam-policy-binding \
--role="roles/iam.workloadIdentityUser" \
--member="serviceAccount:YOUR-GCP-PROJECT.svc.id.goog[YOUR-K8S-NAMESPACE/YOUR-KSA-NAME]" \
YOUR-GSA-NAME@YOUR-GCP-PROJECT.iam.gserviceaccount.com


20 . Add an annotation to YOUR-KSA-NAME to complete the binding:
kubectl annotate serviceaccount \
testapp \
iam.gke.io/gcp-service-account=test-account@customer-n-test-service.iam.gserviceaccount.com

21 . Finally, make sure to specify the service account for the k8s object.
	kubectl apply -f proxy_with_workload_identity.yaml
	
	Note : Please verify the deployment should be successful under Workloads .

Exposing the sample app to the internet
22. Use the kubectl expose command to generate a Kubernetes Service for the hello-app deployment:

‪	kubectl expose deployment ‬test-environment-deployment --name=psql-app-service --type=LoadBalancer --port 80 --target-port 8080‬‬‬‬‬‬‬

23. Run the following command to get the Service details for hello-app-service:
	kubectl get service

24 . Copy the EXTERNAL_IP address to the clipboard (for instance: 203.0.113.0).



Refference : https://cloud.google.com/sql/docs/postgres/connect-kubernetes-engine

![image](https://user-images.githubusercontent.com/90042969/135715459-783e7ff0-b9c4-4f73-a56e-ce03fe310674.png)

