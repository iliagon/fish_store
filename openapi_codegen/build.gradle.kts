plugins {
    id("org.openapi.generator") version "5.2.1"
}


tasks.withType<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>() {
    group = "openapi tools"

    val outputDirToGenerate = "$rootDir/application/build/generated/openApi"
    val appRootPackage = "com.example.fishstore"
    val appApiPackage = "$appRootPackage.controllers.api"
    val appModelPackage = "$appRootPackage.models.dto"
    val appApiDirectory = "$outputDirToGenerate/src/main/kotlin/${appApiPackage.replace(".", "/")}"
    val appModelDirectory = "$outputDirToGenerate/src/main/kotlin/${appModelPackage.replace(".", "/")}"

    doFirst {//clean old classes
        delete(appApiDirectory)
        delete(appModelDirectory)
    }

    inputSpec.set("$rootDir/fish_store_openapi.yml")
    outputDir.set(outputDirToGenerate)
    print("outputDir=$outputDir")
    skipOverwrite.set(false)
    invokerPackage.set(appRootPackage)
    apiPackage.set(appApiPackage)
    modelPackage.set(appModelPackage)
    generateModelTests.set(false)
    generateApiTests.set(false)

    generatorName.set("kotlin-spring")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "sortParamsByRequiredFlag" to "true",
            "useBeanValidation" to "true",
            "gradleBuildFile" to "false",
            "exceptionHandler" to "false"
        )
    )

    doLast { // delete unnecessary files
        delete("$outputDirToGenerate/pom.xml")
        delete("$outputDirToGenerate/.openapi-generator-ignore")
        delete("$outputDirToGenerate/.openapi-generator")
        delete("$outputDirToGenerate/README.md")
        delete("$outputDirToGenerate/README.md")
        delete("$appApiDirectory/ApiUtil.kt")
    }
}