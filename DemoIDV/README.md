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

#### 2.2.1 SDK Initialization

The SDK will be used through the [SDKController] object. This object needs to be initialized only once.
1. It is decided whether the license will be included through a String or with a remote licensing service.
2. Debug mode is activated if you want to debug the internal logs (IMPORTANT: Only in development).
3. If the initialization returns a STATUS_OK the SDK driver will be ready for use.

```
viewModelScope.launch {
    SDKController.enableDebugMode()

    val sdkConfig = SdkData.getInitConfiguration(sdkApplication)
    when (val result = SDKController.initSdk(sdkConfig)) {
         is SdkResult.Success -> log("INIT SDK OK")
         is SdkResult.Error -> log("INIT SDK ERROR: ${result.error}")
    }
}
        
```
 
To get tracking errors:
```
SDKController.launch(TrackingErrorController {
    log("Tracking Error: ${it.name}")
})
```


The **SdkData** class stores all the necessary data in the SDK. (Section 2.2.3)


#### 2.2.2 Launch IDV Onboarding process

```
SDKController.launch(FlowController(SdkData.getIDVFlowConfigurationData()))
```

#### 2.2.3 Data necessary for the use of the SDK

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

```

- Workflow Identification:
```
const val IDV_ID = "...." 

```

- IMPORTANT: The bundleId of the application must match the one requested in the license

### 2.3 Steps to start the demo

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

