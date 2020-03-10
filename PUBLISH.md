## Intructions for publishing the artifact to Maven Central

Prerequisite: knowing `e-mail` and `password` of dataClay's Sonatype account.

#### Generate and register a GPG key

(Skip the following step if the key already exists) 

Execute `gpg --gen-key` to interactively create a new key, specifying Sonatype account's e-mail as owner.

(Skip the following steps if the key has already been registered to a trusted GPG server) 

Execute `gpg --list-keys` to copy the public key's digest (e.g. `ED0C46314BC7F08EDD7C18625B3CF66DB19AEE78`)

Execute `gpg --keyserver hkp://keyserver.ubuntu.com --send-keys PUBLIC_KEY_DIGEST` for registering the key to the trusted Ubuntu server.

Execute `gpg --keyserver hkp://keyserver.ubuntu.com --recv-keys PUBLIC_KEY_DIGEST` to check that the key has correctly been registered.

#### Publish to Maven Central 

Create and replace the placeholder in `settings.xml` with Sonatype account's password: 

```
<settings>
	<servers>
		<server>
			<id>ossrh</id>
			<username>dataclay</username>
			<password>HERE_GOES_THE_SONATYPE_PASSWORD</password>
		</server>
	</servers>
</settings>
```

Execute `mvn -P publish deploy -s settings.xml` in order to publish a new version.
