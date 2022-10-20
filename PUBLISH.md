# Publish

## Intructions for publishing the artifact to Maven Central

Prerequisite: knowing `e-mail` and `password` of dataClay's Sonatype account 
or obtain access token from https://oss.sonatype.org/

#### Generate your GPG signature

https://central.sonatype.org/pages/working-with-pgp-signatures.html

#### Publish to Maven Central 

Set Sonatype account's user and password environment variables :

`SONATYPE_USERNAME="user"`
`SONATYPE_PASSWORD="password"` 

Execute `mvn -P publish deploy -s settings.xml` in order to publish an SNAPSHOT version.

Execute `mvn -P publish release:clean release:prepare release:perform -s settings.xml` to create a Release.

## Instructions for building and publishing Docker images

**Release images:**

```bash

VERSION=3.0.0-alpha.1

# dsjava
docker buildx build --platform linux/amd64,linux/arm64 \
-t bscdataclay/dsjava:$VERSION-jdk11-bullseye \
-t bscdataclay/dsjava:$VERSION \
-t bscdataclay/dsjava:latest \
-f Dockerfile.dsjava \
--build-arg JDK_VERSION=11 \
--build-arg JRE_VERSION=11-jre-bullseye --push .

# logicmodule
docker buildx build --platform linux/amd64,linux/arm64 \
-t bscdataclay/logicmodule:$VERSION-jdk11-bullseye \
-t bscdataclay/logicmodule:$VERSION \
-t bscdataclay/logicmodule:latest \
-f Dockerfile.logicmodule \
--build-arg JDK_VERSION=11 \
--build-arg JRE_VERSION=11-jre-bullseye --push .
```

**Development images:**

To generate development images use the following tag:  
**devYYYYMMDD-jdk{version}-bullseye**

For example:

```bash
docker buildx build --platform linux/amd64,linux/arm64 \
-t bscdataclay/dsjava:dev20220614-jdk11-bullseye -f Dockerfile.dsjava \
--build-arg JDK_VERSION=11 \
--build-arg JRE_VERSION=11-jre-bullseye --push .
```

## Pre Release

Create release branch, update versions and create a pull request to `master`.

Execute `mvn -P publish release:clean release:prepare release:perform -s settings.xml` to create a Release.

## Post Release

Update in `develop`:

- pom.xml version
- PUBLISH.md versions
- README.md version

