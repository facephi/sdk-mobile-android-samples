# Demo NFC

Aplicación demo para validar un flujo de onboarding con el SDK de Facephi.

## Resumen

**Componentes**
- Core
- SDK
- NFC
- SelphID

**Qué permite**
- Captura de datos del documento con SelphID (MRZ)
- Lectura del chip NFC con datos capturados o manuales

## Flujo de la aplicación

1. **Términos y condiciones**: se muestra una pantalla con los términos de uso. Es necesario deslizar hasta el final para habilitar el botón **Aceptar** y continuar.
2. **Pantalla principal con tabs**:
   - **SelphID + NFC**: primero se capturan los datos del documento con la cámara leyendo el MRZ; con esos datos se realiza la lectura del chip NFC. En la configuración de SelphID se puede cambiar el país del documento a leer.
   - **NFC**: permite introducir manualmente los datos necesarios para la lectura del chip NFC.

## Configuración

### Repositorio Maven

Añade esto en `settings.gradle` (normalmente después de `mavenCentral()`):

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

Añade las credenciales en `local.properties`:

```
artifactory.user=TUS_CREDENCIALES_USER
artifactory.token=TUS_CREDENCIALES_TOKEN
```

### Dependencias Gradle

```
implementation (libs.facephi.sdk)
implementation (libs.facephi.core)
implementation (libs.facephi.selphid)
implementation (libs.facephi.nfc){
    exclude group : "org.bouncycastle", module : "bcprov-jdk15on"
    exclude group : "org.bouncycastle", module : "jetified-bcprov-jdk15on-1.68"
}
```

Para el componente NFC añade:

```
packaging {
    resources {
        pickFirsts.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}
```

## Configuración del SDK

La clase `SdkData` almacena los datos necesarios.

**Licencia vía servicio**
```
val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
    apiKey = "....."
)
```

**Licencia como String**
```
const val LICENSE = "...." 
```

**Identificador del cliente y operación**
```
const val CUSTOMER_ID = "...." 
val OPERATION_TYPE = OperationType.ONBOARDING
```

> IMPORTANTE: el `bundleId` de la aplicación debe coincidir con el solicitado en la licencia.

## Ejecutar la demo

1. En `build.gradle`, rellena `applicationId` con el id de la aplicación para la que se ha solicitado la licencia.
2. En `local.properties`, añade los usuarios de artifactory facilitados por Facephi:  
   `artifactory.user=user`  
   `artifactory.token=token`
3. En `SdkData`, añade los datos del servicio de licencias o la licencia en String.
4. Según cómo se añada la licencia, ajusta:
   `const val LICENSE_ONLINE = false`

## Enlaces

- Documentación del SDK: https://facephi.github.io/sdk-mobile-documentation/
