import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.redreaperlp"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.3")
    implementation("org.json:json:20090211")

}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        manifest {
            attributes(
                    "Main-Class" to "com.github.redreaperlp.RedReaperBot"
            )
        }
    }
}

application {
    mainClass.set("com.github.redreaperlp.RedReaperBot")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}