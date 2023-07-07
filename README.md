before buld download tomee to projcet file specified in Dockerfile
docker build:
    docker build -t film-base .
docker save to file:
    docker save -o film-base.tar film-base
docker run:
    docker run --rm -p 127.0.0.1:8080:8080 -v /home/mar/documents/studies/film-base/target/:/usr/local/tomee/webapps --name film-base film-base
    // then optimize docer run(add war file at the end in Dockerfile)
