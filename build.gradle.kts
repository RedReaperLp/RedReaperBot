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
    implementation("net.dv8tion:JDA:5.0.0-beta.2")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("mysql:mysql-connector-java:8.0.31")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        manifest {
            attributes(
                    "Main-Class" to "com.github.redreaperlp.Main"
            )
        }
    }
}

application {
    mainClass.set("com.github.redreaperlp.Main")
}