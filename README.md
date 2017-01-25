# Usage

```gradle
// load the plugin
plugins {
	id "com.shysteph.application-agent" version "1.0.17"
}

// note: this plugin depends on the `application` plugin so the following line is redundant; it is included here for clarity
apply plugin: 'application'
apply plugin: 'java'

// all of these are optional and default to true
applicationAgent {
	applyToTests true
	applyToRun true
	applyToStartScripts true
}

// if necessary, add a repository where your agent can be found
repositories {
	maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
}

// add a dependency to the java agent that you want to use to the `agent` configuration
dependencies {
	agent(group: 'co.paralleluniverse', name: 'quasar-core', version: '0.7.5-SNAPSHOT', classifier: 'jdk8', ext: 'jar')
}

// TODO: the rest of your build script
```
