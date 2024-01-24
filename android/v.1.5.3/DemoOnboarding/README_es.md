# DEMO CLASSIC ONBOARDING


## 1. Introducción

En esta demo se puede realizar un proceso de onboarding utilizando el SDK de Facephi.
Los componentes utilizados son:

- Core
- Sdk
- Selphi
- SelphID
- Tracking

## 2. Detalle de la aplicación demo

### 2.1 Dependencias

Se deberá incluir en el fichero settings.gradle, normalmente tras mavenCentral(), el siguiente código para descargar las librerías de Facephi:

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

Los usuarios deben ser facilitados por Facephi e incluidos en el local.properties:

```
artifactory.user=TUS_CREDENCIALES_USER
artifactory.token=TUS_CREDENCIALES_TOKEN
```

Las dependencias de las librerías se podrán importar directamente en el gradle, en este caso desde libs:

```
    implementation (libs.facephi.sdk)
    implementation (libs.facephi.core)
    implementation (libs.facephi.selphid)
    implementation (libs.facephi.selphi)
    implementation (libs.facephi.tracking)

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
    sdkApplication = sdkApplication,
    license = SdkData.LICENSE,
    trackingController = TrackingController()
) { result ->
    when (result) {
        is SdkResult.Success -> {
            Napier.d("APP: INIT SDK: OK")
            logs.add("INIT SDK OK")
        }
        is SdkResult.Error -> {
            Napier.d("APP: INIT SDK ERROR - ${result.error}")
            logs.add("INIT SDK ERROR: ${result.error}")
        }
    }
}
  
/*   INIT WITH EnvironmentLicensingData   */

SDKController.initSdk(
    sdkApplication = sdkApplication,
    environmentLicensingData = SdkData.environmentLicensingData,
    trackingController = TrackingController()
) { result ->
    when (result) {
        is SdkResult.Success -> {
            Napier.d("APP: INIT SDK: OK")
            logs.add("INIT SDK OK")
        }
        is SdkResult.Error -> {
            Napier.d("APP: INIT SDK: KO - ${result.error}")
            logs.add("INIT SDK ERROR: ${result.error}")
        }
    }
}
        
```

Para escuchar los errores de tracking:
```
SDKController.launch(TrackingErrorController {
    logs.add("Tracking Error: ${it.name}")
})
```

La clase **SdkData** almacena todos los datos necesarios en el SDK. (Apartado 2.2.5)


#### 2.2.2 Creación de una operación

Para comenzar una operación de ONBOARDING o AUTHENTICATION se tiene que crear una nueva operación. Para ello se usará la función [SDKController.newOperation] tiene 3 parámetros de entrada:

1. operationType: Indica si se va a hacer un proceso de ONBOARDING o de AUTHENTICATION
2. customerId: Id del usuario si se tiene
3. steps: Lista de pasos de la operación si se han definido previamente

En esta demo el proceso se realiza en un botón del Fragment:

```
SDKController.newOperation(
    operationType = SdkData.OPERATION_TYPE,
    customerId = SdkData.CUSTOMER_ID,
) {
    when (it) {
        is SdkResult.Error -> {
            Napier.d("APP: NEW OPERATION ERROR ${it.error}")
            logs.add("NEW OPERATION ERROR ${it.error}")
        }
        is SdkResult.Success -> {
            Napier.d("APP: NEW OPERATION OK")
            logs.add("NEW OPERATION OK")
        }
    }
}
```


#### 2.2.3 Captura facial

La captura facial se realiza a través de Selphi. 
En esta demo el proceso se realiza en un botón del Fragment:

```
SDKController.launch(
    SelphiController(SdkData.selphiConfiguration) {
        when (it) {
            is SdkResult.Success -> {
                Napier.d("APP: SELPHI OK")
                logs.add("SELPHI OK")
                selphiFace = it.data.bestImageBmp!!.bitmap.toBase64() ?: ""
                //liveness(selphiFace)
            }
            is SdkResult.Error -> {
                logs.add("SELPHI ERROR ${it.error}")
                Napier.d("APP: SELPHI ERROR: ${it.error.name}")
            }
        }
    }
)
```

#### 2.2.3 Captura del documento

La captura del documento se realiza a través de SelphID. 
En esta demo el proceso se realiza en un botón del Fragment:

```
SDKController.launch(
    SelphIDController(SdkData.selphIDConfiguration) { sdkResult ->
        when (sdkResult) {
            is SdkResult.Success -> {
                Napier.d("APP: SELPHID OK")
                logs.add("SELPHID OK")
                /*if (selphiFace.isNotEmpty()) {
                    matchingFacial(selphiFace,
                        sdkResult.data.tokenFaceImage)
                }*/
            }
            is SdkResult.Error -> {
                Napier.d("APP: SELPHID ERROR: ${sdkResult.error.name}")
                logs.add("SELPHID ERROR: ${sdkResult.error.name}")
            }
        }
    }
)
```

#### 2.2.4 Cierre de sesión

Cuando se finalice el uso del SDK se deberá cerrar sesión:

```
SDKController.closeSession()
```
#### 2.2.5 Datos necesarios para el uso del SDK

Para que la aplicación funcione correctamente se deberán rellenar los siguientes datos.

En la clse SdkData:

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

- Para Selphi el nombre del fichero ZIP de recursos (que estará en la carpeta assets de la aplicación) y los datos de configuración:
```
const val SELPHI_RESOURCES = ".....zip"

val selphiConfiguration = SelphiConfigurationData(
        ...,
        resourcesPath = SELPHI_RESOURCES
    )
```

- Para SelphID el nombre del fichero ZIP de recursos (que estará en la carpeta assets de la aplicación) y los datos de configuración:
```
const val SELPHID_RESOURCES = "...zip"

val selphIDConfiguration = SelphIDConfigurationData(
       ...,
        resourcesPath = SELPHID_RESOURCES
    )
```

- En el caso de utilizar los servicios de verificación (liveness y matching) habrá que introducir la llamada al servicio

- IMPORTANTE: El bundleId de la aplicación debe coincidir con el que se ha solicitado en la licencia

### 2.3 Servicios de verificación

Si el cliente tiene acceso a los servición de verificación podrá hacer uso del Liveness y del MatchingFacial. Para ello será necesario haber hecho un reconocimiento facial y de documento previamente. 

### 2.4 Pasos para iniciar la demo

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
  

