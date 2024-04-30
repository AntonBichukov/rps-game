# Need to run 2 commands

 docker build -t rps-game:0.0.1 .
 docker run -p 8080:8080 --name=rps-game rps-game:0.0.1

for connection to the App use it  **telnet {host} 8080**
