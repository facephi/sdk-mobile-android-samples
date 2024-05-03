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

For the NFC component it is necessary to add in gradle:

```
   packaging {
        resources {
            pickFirsts.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        }
    }
```


### 2.2 Using the SDK

The SDK documentation:

https://facephi.github.io/sdk-mobile-documentation/

The **SdkData** class stores all the necessary data in the SDK. (Section 2.3)

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


- IMPORTANT: The bundleId of the application must match the one requested in the license

### 2.4 Steps to start the demo

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