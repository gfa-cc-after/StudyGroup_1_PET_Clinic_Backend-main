
<h1>Project phase - Study Group 1 - Backend_main</h1>


<h3 >Secret key set up for Docker (in IntelliJ):</h3>

- 0: Generate a secret (e.g. via https://jwt-keys.21no.de/)
- 1: Run the docker.compose.yml with the double play button
- 2: Set up configuration (top right corner, 3 dots):
    - 2.1: Select docker-compose.yml: Compose Deployment
    - 2.2: Modify options / environment variables / notebook icon on the right
    - 2.3: Add new env. var: SECRET_KEY - /your key, that you generated/
- 3: Save it (Apply)
- 4: Delete every container and image
- 5: From now on, you will only need to run the backend with the green arrow in the top right corner.

  (sometimes you need to restart the backend container, in order to work properly)