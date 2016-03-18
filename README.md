# Usage

```gradle
// load the plugin
buildscript {
	repositories { maven { url "https://plugins.gradle.org/m2/" } }
	dependencies { classpath "gradle.plugin.com.zoltu:application-agent:1.0.6" }
}

apply plugin: 'java'
apply plugin: 'application'
// 'application-agent' plugin must be applied *after* 'application' plugin
apply plugin: 'com.zoltu.application-agent'

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
