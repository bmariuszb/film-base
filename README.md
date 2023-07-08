before buld download tomee to projcet file specified in Dockerfile \
docker build:
```
docker build -t film-base .
```
docker save to file:
```
docker save -o film-base.tar film-base
```
docker load image:
```
docker load -i film-base.tar 
```
docker run:
```
docker run --rm -p 127.0.0.1:8080:8080 --name film-base film-base
```
open in a browser
```
http://localhost:8080/film-base-1.0-SNAPSHOT/api/jpa/movies
```
