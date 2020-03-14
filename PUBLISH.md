## Intructions for publishing the artifact to Maven Central

Prerequisite: knowing `e-mail` and `password` of dataClay's Sonatype account.

#### Publish to Maven Central 

Set Sonatype account's user and password environment variables :

`SONATYPE_USERNAME="user"`
`SONATYPE_PASSWORD="password"` 

Execute `mvn -P publish deploy -s settings.xml` in order to publish an SNAPSHOT version.

Execute `mvn -P publish release:clean release:prepare release:perform -s settings.xml` to create a Release.

