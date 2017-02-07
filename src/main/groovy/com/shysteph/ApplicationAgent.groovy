/*
 * Copyright 2017 Stephanie Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.shysteph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.testing.Test
import org.springframework.boot.gradle.run.BootRunTask

/**
 * Add java agents from project dependencies into the run configuration
 */
class ApplicationAgentPlugin implements Plugin<Project> {
  void apply(Project project) {
    project.getPlugins().apply(ApplicationPlugin.class)

    project.extensions.create("applicationAgent", ApplicationAgentPluginExtension)

    project.configurations {
      agent
      runtime.extendsFrom(agent)
    }

    project.startScripts {
      doLast {
        if (project.applicationAgent.applyToStartScripts) {

          project.configurations.agent.each { agent ->
            String agentFileName = agent.name

            String forwardSlash = "/"
            String unixRegex = $/exec "$$JAVACMD" /$
            String unixReplacement = $/exec "$$JAVACMD" -javaagent:"$$APP_HOME/lib${forwardSlash}${agentFileName}" /$
            unixScript.text = unixScript.text.replace(unixRegex, unixReplacement)

            String windowsRegex = $/"%JAVA_EXE%" %DEFAULT_JVM_OPTS%/$
            String windowsReplacement = $/"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -javaagent:"%APP_HOME%\lib\$agentFileName"/$
            windowsScript.text = windowsScript.text.replace(windowsRegex, windowsReplacement)
          }
        }
      }
    }

    project.tasks.withType(Test) {
      doFirst {
        if (project.applicationAgent.applyToTests) {
          project.configurations.agent.each { agent ->
            jvmArgs += ["-javaagent:${agent.path}"]
          }
        }
      }
    }


    project.tasks.run {
      doFirst {
        if (project.applicationAgent.applyToRun) {
          project.configurations.agent.each { agent ->
            project.applicationDefaultJvmArgs += ["-javaagent:${agent.path}"]
          }
        }
      }
    }

    project.tasks.withType(BootRunTask) { bootRunTask ->
      doFirst {
        if (project.applicationAgent.applyToRun) {
          project.configurations.agent.each { agent ->
            String agentFileName = agent.absolutePath
            bootRunTask.jvmArgs += ["-javaagent:${agentFileName}"]
          }
        }
      }
    }
  }
}

class ApplicationAgentPluginExtension {
  /**
   * Use the agent when running tests
   */
  Boolean applyToTests = true

  /**
   * Use the agent when running normally
   */
  Boolean applyToRun = true


  /**
   * Use the agent in the generated script for the distribution plugin
   */
  Boolean applyToStartScripts = true
}
