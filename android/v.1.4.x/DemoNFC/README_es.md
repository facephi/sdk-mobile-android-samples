# DEMO NFC


## 1. Introducción

En esta demo se puede realizar un proceso de onboarding utilizando el SDK de Facephi.
Los componentes utilizados son:

- Core
- Sdk
- NFC
- SelphId

## 2. Detalle de la aplicación demo

### 2.1 Dependencias

Se deberá incluir en el fichero settings.gradle, normalmente tras mavenCentral(), el siguiente código para descargar las librerías de Facephi:

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

Los usuarios deben ser facilitados por Facephi e incluidos en el local.properties:

```
artifactory.user=TUS_CREDENCIALES_USER
artifactory.token=TUS_CREDENCIALES_TOKEN
```

Las dependencias de las librerías se podrán importar directamente en el gradle (desde libs):

```
    implementation (libs.facephi.sdk)
    implementation (libs.facephi.core)
    implementation (libs.facephi.selphid)
    implementation (libs.facephi.nfc){
        exclude group : "org.bouncycastle", module : "bcprov-jdk15on"
        exclude group : "org.bouncycastle", module : "jetified-bcprov-jdk15on-1.68"
    }

```


### 2.2 Uso del SDK

#### 2.2.1 Inicialización del SDK

El SDK se utilizará a través del objeto [SDKController]. Este objeto necesita inicializarse una única vez. 
1. Se decide si la licencia se incluirá a través de un String o con un servicio de licenciamiento remoto.
2. Se activa el modo Debug si se quieren depurar los logs internos (IMPORTANTE: Sólo en desarrollo).
3. Si la inicialización devuelve un STATUS_OK el controlador del SDK estará listo para su uso.

```
if (BuildConfig.DEBUG) {
   SDKController.enableDebugMode()
}


/*   INIT WITH STRING   */

SDKController.initSdk(
    application = application,
    license = SdkData.LICENSE,
) { result ->
    when (result.finishStatus) {
        FinishStatus.STATUS_OK -> Napier.d("APP: INIT SDK: OK")
        FinishStatus.STATUS_ERROR -> Napier.d("APP: INIT SDK: KO - ${result.errorType.name}")
    }
}
  
/*   INIT WITH EnvironmentLicensingData   */

SDKController.initSdk(
    application = application,
    environmentLicensingData = SdkData.environmentLicensingData,
) { result ->
    when (result.finishStatus) {
        FinishStatus.STATUS_OK -> Napier.d("APP: INIT SDK: OK")
        FinishStatus.STATUS_ERROR -> Napier.d("APP: INIT SDK: KO - ${result.errorType.name}")
    }
}
        
```

La clase **SdkData** almacena todos los datos necesarios en el SDK. (Apartado 2.2.5)


#### 2.2.2 Creación de una operación

Para comenzar una operación de ONBOARDING o AUTHENTICATION se tiene que crear una nueva operación. Para ello se usará la función [SDKController.newOperation] tiene 3 parámetros de entrada:

1. operationType: Indica si se va a hacer un proceso de ONBOARDING o de AUTHENTICATION
2. customerId: Id del usuario si se tiene
3. steps: Lista de pasos de la operación si se han definido previamente

En esta demo el proceso se realiza en un botón (viewModel.newOperation):

```
SDKController.newOperation(
    operationType = SdkData.OPERATION_TYPE,
    customerId = SdkData.CUSTOMER_ID,
) {
    when (it.finishStatus) {
        FinishStatus.STATUS_OK -> {
            Napier.d("APP: NEW OPERATION OK")
            debugLogs("NEW OPERATION: OK")
        }
        FinishStatus.STATUS_ERROR -> {
            Napier.d("APP: NEW OPERATION ERROR: ${it.errorType.name}")
            debugLogs("NEW OPERATION: KO - ${it.errorType.name}")
        }
    }
}
```


#### 2.2.3 Captura de NFC

La captura de la información del chip (ViewModel):

```
SDKController.launch(
    NfcController(componentData = nfcConfigurationData,
        state = { state ->
            Napier.d("APP: NFC  State: ${state.name}")
            debugLogs("NFC: State: ${state.name}")
        }) {
        when (it.finishStatus) {
            FinishStatus.STATUS_OK -> {
                Napier.d("APP: NFC OK")
                debugLogs("NFC: OK")
                debugLogs("VALIDATIONS: ${it.data?.nfcValidations}"
            }
            FinishStatus.STATUS_ERROR -> {
                Napier.d("APP: NFC ERROR - ${it.errorType.name}")
                debugLogs("NFC: ERROR - ${it.errorType.name}")
            }
        }
    }
)
```

#### 2.2.3 Captura de Selphi+NFC

La captura del documento primero y de la información del chip después (ViewModel):


```
SDKController.launch(
    SelphIDController(SdkData.getSelphIdConfig(docType)){
        when (it.finishStatus) {
            FinishStatus.STATUS_OK -> {
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
                            when (it.finishStatus) {
                                FinishStatus.STATUS_OK -> {
                                // OK
                                it.data
                
                                }
                                FinishStatus.STATUS_ERROR -> // KO: it.error
                            }
                     }
               })
                              
              }
                
            }
            FinishStatus.STATUS_ERROR -> // KO: it.error
        }
    }
)
```

#### 2.2.3 Cierre de sesión

Cuando se finalice el uso del SDK se deberá cerrar sesión:

```
SDKController.closeSession()
```
#### 2.2.4 Datos necesarios para el uso del SDK

Para que la aplicación funcione correctamente se deberán rellenar los siguientes datos.

En la clase SdkData:

- Datos necesarios si se va a utilizar un servicio para obtener las licencias:

```
val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "https://....",
        apiKey = "....."
    )
```

- String de la licencia si no se va a utilizar un servicio:
```
const val LICENSE = "...." 
```

- Identificador del cliente y tipo de operación que se va a utilizar en la inicialización:
```
const val CUSTOMER_ID = "...." 
val OPERATION_TYPE = OperationType.ONBOARDING

```


- IMPORTANTE: El bundleId de la aplicación debe coincidir con el que se ha solicitado en la licencia

### 2.3 Pasos para iniciar la demo

Los pasos a seguir para iniciar la demo son:

1. El el fichero build.gradle rellenar el campo applicationId con el id de la aplicación para la que se ha solicitado la licencia.

2. En el fichero local.properties añadir los usuarios de artifactory facilitados por Facephi:
   artifactory.user=user
   artifactory.token=token
   
3. En el fichero SdkData añadir o los datos del servicio del que se va a obtener la licencia o la licencia en String:
      val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
         url = "https://...",
         apiKey = "..."
      )
      
      o
      
      const val LICENSE = ""
      
4. Dependiendo de cómo se haya añadido la licencia adaptar el valor de la variable:
      const val LICENSE_ONLINE = false


