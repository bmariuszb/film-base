FROM tomee:latest

# Remove the existing TomEE installation
RUN rm -rf /usr/local/tomee

# Copy your Apache TomEE installation
COPY apache-tomee-plume-9.1.0/ /usr/local/tomee
