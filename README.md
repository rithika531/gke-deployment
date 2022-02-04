tomcat-container-deployment

pre-requisite :

Assumption : GKE and PSQL should be running on google cloud console

Create the secret in the GKE . The created secret can be found in the configuration of the GKE . Create the secret with the following command : kubectl create secret generic cloudsql-db-credentials
--from-literal=username=XXXXXXX --from-literal=password=XXXXXXX Note : The same username , password and database name should be updated in the properties file(which can be found under src folder of this folder).
2.Project-id and gke name should be changed in cloudbuild.yaml

3.psql connection name should be changed in deployment.yaml

4.create a trigger in the google cloud console . The created tigger looks for the cloudbuild.yaml file which is in the repo .

5.Any code push into this repo triggers the build in the google cloud console .



