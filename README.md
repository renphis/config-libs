## Config

A config management library for use with `JSON` files.

---

### Setup
To access the library you will need a Personal Access Token with the permission `read:packages`.

#### Gradle - Groovy
Add your GitHub email and personal access token to your project's `gradle.properties` file.
```gradle
github_user="your_email"
github_token="your_token"
```

Update your `build.gradle` to implement the library.
```gradle
// Add the GitHub repo
repositories {
  maven {
    url = "https://maven.pkg.github.com/renphis/config"
    // Your GitHub username and token are used here for authentication
    credentials {
      username = project.github_user
      password = project.github_token
    }
  }
}

// Add the library as a dependency
dependencies {
  implementation 'net.renphis.libs:config:1.0-SNAPSHOT'
}
```

---

### Annotations & AspectJ
The library utilises Aspect Oriented Programming (AOP) to provide annotations that perform conditional method execution. Some additional setup is required to take advantage of these annotations.

#### Gradle - Groovy
```gradle
// Add the official AspectJ plugin
plugins {
  id 'io.freefair.aspect' version '8.11'
}

// Update your dependencies
dependencies {
  // Add AspectJRT
  implementation 'org.aspectj:aspectjrt:1.9.22'
  // Update your library dependency from "implementation" to "aspect"
  aspect 'net.renphis.libs:config:1.0-SNAPSHOT'
}
