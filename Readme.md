## BTCTÜRK API TEST

Run simply *./run.sh* or alternatively follow these steps in order to run the test.

### Creating and Running Docker Container
- Open Docker Desktop
- Run following commands:
   - *docker pull selenium/standalone-chrome*
   - *docker run -d --name selenium -p 4444:4444 -v --shm-size="2g" selenium/standalone-chrome*
- Now the container created for test. You can navigate http://localhost:4444/ui to watch the session.
### Running Test
- Run *./mvnw clean test*
### Cleaning Docker Container
- After the test is finished run following commands:
    - *docker stop selenium*
    - *docker rm selenium*

Tip: Can run locally with *./mvnw clean test* without docker.