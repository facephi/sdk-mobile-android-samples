# DEMO NFC


## 1. Introduction

In this demo you can carry out an onboarding process using the Facephi SDK.
The components used are:

- Core
- Sdk
- NFC
- SelphId

## 2. Demo App Detail

### 2.1 Dependencies

The following code must be included in the settings.gradle file, normally after mavenCentral(), to download the Facephi libraries:

```
maven {
   Properties props = new Properties()
   def propsFile = new File('local.properties')
   if(propsFile.exists()){
       props.load(new FileInputStream(propsFile))
   }
   name = "external"
   url = uri("https://facephicorp.jfrog.io/artifactory/maven-pro-fphi")
   credentials {
        username = props["artifactory.user"] 
        password = props["artifactory.token"] 
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
    implementation (libs.facephi.nfc){
        exclude group : "org.bouncycastle", module : "bcprov-jdk15on"
        exclude group : "org.bouncycastle", module : "jetified-bcprov-jdk15on-1.68"
    }

```


### 2.2 Using the SDK

#### 2.2.1 SDK Initialization

The SDK will be used through the [SDKController] object. This object needs to be initialized only once.
1. It is decided whether the license will be included through a String or with a remote licensing service.
2. Debug mode is activated if you want to debug the internal logs (IMPORTANT: Only in development).
3. If the initialization returns a STATUS_OK the SDK driver will be ready for use.

```
if (BuildConfig.DEBUG) {
   SDKController.enableDebugMode()
}


/*   INIT WITH STRING   */

SDKController.initSdk(
    sdkApplication = SdkApplication(application),
    environmentLicensingData = SdkData.environmentLicensingData,
) { result ->
    when (result) {
        is SdkResult.Success -> Napier.d("APP: INIT SDK: OK")
        is SdkResult.Error -> Napier.d("APP: INIT SDK: KO - ${result.error}")
    }
}
  
/*   INIT WITH EnvironmentLicensingData   */

SDKController.initSdk(
    sdkApplication = SdkApplication(application),
    license = SdkData.LICENSE,
) { result ->
    when (result) {
        is SdkResult.Success -> Napier.d("APP: INIT SDK: OK")
        is SdkResult.Error -> Napier.d("APP: INIT SDK: KO - ${result.error}")
    }
}
        
```

The **SdkData** class stores all the necessary data in the SDK. (Section 2.2.4)


#### 2.2.2 Creating an operation

To start an ONBOARDING or AUTHENTICATION operation, a new operation must be created. To do this, the function [SDKController.newOperation] will be used, it has 3 input parameters:

1. operationType: Indicates whether an ONBOARDING or AUTHENTICATION process is going to be carried out
2. customerId: User ID if available
3. steps: List of steps of the operation if they have been previously defined

In this demo the process is carried out in a button (viewModel.newOperation):

```
SDKController.newOperation(
    operationType = SdkData.OPERATION_TYPE,
    customerId = SdkData.CUSTOMER_ID,
) {
    when (it) {
        is SdkResult.Success -> {
            Napier.d("APP: NEW OPERATION OK")
            debugLogs("NEW OPERATION: OK")
        }
        is SdkResult.Error -> {
            Napier.d("APP: NEW OPERATION ERROR: ${it.error}")
            debugLogs("NEW OPERATION: KO - ${it.error}")
        }
    }
}
```


#### 2.2.3 NFC Capture

Capturing chip information (ViewModel):

```
SDKController.launch(
    NfcController(componentData = nfcConfigurationData,
        debugLogs = {
            Napier.d("APP: Logs: $it")
            debugLogs("NFC: Logs: $it")
        },
        state = { state ->
            Napier.d("APP: NFC  State: ${state.name}")
            debugLogs("NFC: State: ${state.name}")
        }) {
        when (it) {
            is SdkResult.Success -> {
                Napier.d("APP: NFC OK")
                debugLogs("NFC: OK")
                debugLogs("VALIDATIONS: ${it.data.nfcValidations}")
            }
            is SdkResult.Error -> {
                Napier.d("APP: NFC ERROR - ${it.error}")
                debugLogs("NFC: ERROR - ${it.error}")
            }
        }
    }
)
```

#### 2.2.3 SelphId+NFC Capture

Capturing the document first and the chip information later (ViewModel):


```
SDKController.launch(
    SelphIDController(SdkData.getSelphIdConfig(docType)){
        when (it) {
            is SdkResult.Success -> {
                val birthDate = it.data.personalData?.birthDate.orEmpty()
                val expirationDate = it.data.personalData?.expiryDate.orEmpty()
                val nfcKey = it.data.personalData?.nfcKey.orEmpty()

                if (birthDate.isNotEmpty() && expirationDate.isNotEmpty() && nfcKey.isNotEmpty()){
                   SDKController.launch(
                        NFCController(
                        NfcConfigurationData(
                                    documentNumber = nfcKey, // Num support.
                                    birthDate = birthDate, // "dd/MM/yyyy"
                                    expirationDate = expirationDate, // "dd/MM/yyyy",
                                    enableDebugMode = true,
                                    showTutorial = true,
                                    showReadingScreen = true
                                )
                        ) {
                            when (it) {
                                is SdkResult.Success -> {
                                // OK
                                it.data
                
                                }
                                is SdkResult.Error -> // KO: it.error
                            }
                     }
               })
                              
              }
                
            }
            is SdkResult.Error -> // KO: it.error
        }
    }
)
```

#### 2.2.3 Log out

When you finish using the SDK, you must log out:

```
SDKController.closeSession()
```
#### 2.2.4 Data necessary for the use of the SDK

For the application to work correctly, the following information must be filled out.

In the SdkData class:

- Necessary data if a service is going to be used to obtain licenses:

```
val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "https://....",
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


- IMPORTANT: The bundleId of the application must match the one requested in the license

### 2.3 Steps to start the demo

The steps to follow to start the demo are:

1. In the build.gradle file, fill in the applicationId field with the id of the application for which the license has been requested.

2. In the local.properties file add the artifactory users provided by Facephi:
   artifactory.user=user
   artifactory.token=token

3. In the SdkData file add either the data of the service from which the license is to be obtained or the license in String:
   val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
   url = "https://...",
   apiKey = "..."
   )

   o

   const val LICENSE = ""

4. Depending on how the license was added, adapt the value of the variable:
   const val LICENSE_ONLINE = false