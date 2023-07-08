FROM tomee:latest

# Remove the existing TomEE installation
RUN rm -rf /usr/local/tomee
COPY target/film-base-1.0-SNAPSHOT.war /usr/local/tomee/webapps/film-base-1.0-SNAPSHOT.war

# Copy your Apache TomEE installation
COPY apache-tomee-plume-9.1.0/ /usr/local/tomee
