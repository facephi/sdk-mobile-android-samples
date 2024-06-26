# DEMO VOZ


## 1. Introducción

En esta demo se puede realizar un proceso de onboarding utilizando el SDK de Facephi.
Los componentes utilizados son:

- Core
- Sdk
- NFC
- Tracking

## 2. Detalle de la aplicación demo

Documentación del SDK:

https://facephi.github.io/sdk-mobile-documentation/

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

Las dependencias de las librerías se podrán importar directamente en el gradle (libs):

```
    implementation (libs.facephi.sdk)
    implementation (libs.facephi.core)
    implementation (libs.facephi.tracking)
    implementation (libs.facephi.voice)

```


### 2.2 Uso del SDK

#### 2.2.1 Inicialización del SDK

El SDK se utilizará a través del objeto [SDKController]. Este objeto necesita inicializarse una única vez. 
1. Se decide si la licencia se incluirá a través de un String o con un servicio de licenciamiento remoto.
2. Se activa el modo Debug si se quieren depurar los logs internos (IMPORTANTE: Sólo en desarrollo).
3. Si la inicialización devuelve un STATUS_OK el controlador del SDK estará listo para su uso.

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

Para escuchar los errores de tracking:
```
SDKController.launch(TrackingErrorController {
    log("Tracking Error: ${it.name}")
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


#### 2.2.3 Captura de VOZ

La captura facial se realiza a través de Selphi. 
En esta demo el proceso se realiza en un botón del Fragment:

```
viewModelScope.launch {
    val data = SdkData.voiceAuthConfigurationData
    data.showTutorial = showTutorial
    when (val result =
        SDKController.launch(VoiceController(data))) {
        is SdkResult.Success -> {
            log("Voice Auth: OK")
        }
        is SdkResult.Error -> log("Voice Auth: Error - ${result.error.name}")
    }
}
```


#### 2.2.3 Cierre de sesión

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

### 2.3 Servicios de verificación

Si el cliente tiene acceso a los servición de verificación podrá hacer uso de ellos. 

### 2.4 Pasos para iniciar la demo

Los pasos a seguir para iniciar la demo son:

1. El el fichero build.gradle rellenar el campo applicationId con el id de la aplicación para la que se ha solicitado la licencia.

2. En el fichero local.properties añadir los usuarios de artifactory facilitados por Facephi:
   artifactory.user=user
   artifactory.token=token
   
3. En el fichero SdkData añadir o los datos del servicio del que se va a obtener la licencia o la licencia en String:
      val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
         apiKey = "..."
      )
      
      o
      
      const val LICENSE = ""
      
4. Dependiendo de cómo se haya añadido la licencia adaptar el valor de la variable:
      const val LICENSE_ONLINE = false
  
5. Si se van a hacer uso de los servicios de verificación, completar el código del manager.
