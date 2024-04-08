# DEMO CLASSIC ONBOARDING


## 1. Introduction

In this demo you can carry out an onboarding process using the Facephi SDK.
The components used are:

- Core
- Sdk
- Selphi
- SelphID
- Tracking

## 2. Demo App Detail

### 2.1 Dependencies

The following code must be included in the settings.gradle file, normally after mavenCentral(), to download the Facephi libraries:

```
 var user = System.getenv("USERNAME_ARTIFACTORY")
    var token = System.getenv("TOKEN_ARTIFACTORY")
    if (user.isNullOrEmpty() || token.isNullOrEmpty()) {
        val properties = File(rootDir, "local.properties").inputStream().use {
            java.util.Properties().apply { load(it) }
        }
        user = properties.getValue("artifactory.user") as String
        token = properties.getValue("artifactory.token") as String
    }

...

 maven {
            url = uri("https://facephicorp.jfrog.io/artifactory/maven-pro-fphi")
            credentials {
                username = user
                password = token
            }
        }


```

Users must be provided by Facephi and included in the local.properties:

```
artifactory.user=TUS_CREDENCIALES_USER
artifactory.token=TUS_CREDENCIALES_TOKEN
```

The library dependencies can be imported directly into gradle (from libs):

```
    implementation (libs.facephi.sdk)
    implementation (libs.facephi.core)
    implementation (libs.facephi.selphid)
    implementation (libs.facephi.selphi)
    implementation (libs.facephi.tracking)

```


### 2.2 Using the SDK

The SDK documentation:

https://facephi.github.io/sdk-mobile-documentation/



### 2.3 Data necessary for the use of the SDK

For the application to work correctly, the following information must be filled out.

In the SdkData class:

- Necessary data if a service is going to be used to obtain licenses:

```
val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "....."
    )
```

- License string if a service is not going to be used:
```
const val LICENSE = "...." 
```

- Client identifier and type of operation to use in initialization:
```
const val CUSTOMER_ID = "...." 
val OPERATION_TYPE = OperationType.ONBOARDING

```

- For Selphi the name of the resource ZIP file (which will be in the application's assets folder) and the configuration data:
```
const val SELPHI_RESOURCES = ".....zip"

val selphiConfiguration = SelphiConfigurationData(
        ...,
        resourcesPath = SELPHI_RESOURCES
    )
```

- For SelphID the name of the resource ZIP file (which will be in the application's assets folder) and the configuration data:
```
const val SELPHID_RESOURCES = "...zip"

val selphIDConfiguration = SelphIDConfigurationData(
       ...,
        resourcesPath = SELPHID_RESOURCES
    )
```

- In the case of using the verification services (liveness and matching), the call to the service must be entered

- IMPORTANT: The bundleId of the application must match the one requested in the license

### 2.4 Verification services

If the client has access to the verification service, they can use Liveness and MatchingFacial. To do this, it will be necessary to have previously done facial and document recognition.


### 2.5 Steps to start the demo

The steps to follow to start the demo are:

1. In the build.gradle file, fill in the applicationId field with the id of the application for which the license has been requested.

2. In the local.properties file add the artifactory users provided by Facephi:
   artifactory.user=user
   artifactory.token=token

3. In the SdkData file add either the data of the service from which the license is to be obtained or the license in String:
      val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
         apiKey = "..."
      )
      
      o
      
      const val LICENSE = ""

4. Depending on how the license was added, adapt the value of the variable:
   const val LICENSE_ONLINE = false

   And verify that the POSTs of the repository are correct.

