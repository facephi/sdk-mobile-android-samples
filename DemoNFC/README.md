# Demo NFC

Demo app to validate an onboarding flow using the Facephi SDK.

## Overview

**Components**
- Core
- SDK
- NFC
- SelphID

**What you can do**
- Capture document data with SelphID (MRZ)
- Read the NFC chip using captured or manual data

## App flow

1. **Terms and conditions**: a screen shows the terms of use. You must scroll to the end to enable the **Accept** button and proceed.
2. **Main screen with tabs**:
   - **SelphID + NFC**: first, the document data is captured with the camera by reading the MRZ; then those data are used to read the NFC chip. In SelphID configuration you can change the document country to read.
   - **NFC**: allows manually entering the required data to read the NFC chip.

## Setup

### Maven repository

Add this to `settings.gradle` (usually after `mavenCentral()`):

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

Add credentials in `local.properties`:

```
artifactory.user=TUS_CREDENCIALES_USER
artifactory.token=TUS_CREDENCIALES_TOKEN
```

### Gradle dependencies

```
implementation (libs.facephi.sdk)
implementation (libs.facephi.core)
implementation (libs.facephi.selphid)
implementation (libs.facephi.nfc){
    exclude group : "org.bouncycastle", module : "bcprov-jdk15on"
    exclude group : "org.bouncycastle", module : "jetified-bcprov-jdk15on-1.68"
}
```

For the NFC component add:

```
packaging {
    resources {
        pickFirsts.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}
```

## SDK configuration

The `SdkData` class stores all the necessary data.

**License via service**
```
val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
    apiKey = "....."
)
```

**License as String**
```
const val LICENSE = "...." 
```

**Client identifier and operation**
```
const val CUSTOMER_ID = "...." 
val OPERATION_TYPE = OperationType.ONBOARDING
```

> IMPORTANT: the `bundleId` of the application must match the one requested in the license.

## Run the demo

1. In `build.gradle`, set `applicationId` to the app id requested in the license.
2. In `local.properties`, add the artifactory users provided by Facephi:
   `artifactory.user=user`  
   `artifactory.token=token`
3. In `SdkData`, add either licensing service data or a license String.
4. Depending on how the license was added, set:
   `const val LICENSE_ONLINE = false`

## Links

- SDK docs: https://facephi.github.io/sdk-mobile-documentation/