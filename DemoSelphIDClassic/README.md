# DEMO CLASSIC ONBOARDING


## 1. Introduction

In this demo you can carry out an onboarding process using the Facephi SDK.
The components used are:

- Core
- Sdk
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


The **SdkData** class stores all the necessary data in the SDK. (Section 2.2.5)


#### 2.2.2 Creating an operation

To start an ONBOARDING or AUTHENTICATION operation, a new operation must be created. To do this, the function [SDKController.newOperation] will be used, it has 3 input parameters:

1. operationType: Indicates whether an ONBOARDING or AUTHENTICATION process is going to be carried out
2. customerId: User ID if available
3. steps: List of steps of the operation if they have been previously defined

In this demo the process is carried out in a Fragment button:

```
viewModelScope.launch {
    val result = SDKController.newOperation(
        operationType = SdkData.OPERATION_TYPE,
        customerId = SdkData.CUSTOMER_ID,
    )
    when (result) {
        is SdkResult.Success -> log("NEW OPERATION: OK")
        is SdkResult.Error -> log("NEW OPERATION: Error - ${result.error.name}")
    }
}
```

#### 2.2.3 Document Capture

Document capture is done through SelphID.
In this demo the process is carried out in a Fragment button:

```
viewModelScope.launch {
    when (val result =
        SDKController.launch(SelphIDController(SdkData.selphIDConfiguration))) 
        is SdkResult.Success -> {
            log("SelphID: OK")
        }
        is SdkResult.Error -> log("SelphID: Error - ${result.error.name}")
 }

```

#### 2.2.4 Log out

When you finish using the SDK, you must log out:

```
SDKController.closeSession()
```
#### 2.2.5 Data necessary for the use of the SDK

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

