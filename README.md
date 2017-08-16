# poc-java9-modules-api
Prueba de concepto con jigsaw java9 api-rest modular con spark framework y gradle4.1

## Modulos
Este proyecto consta de 4 modulos:

### main 
Este modulo contiene el run de la aplicacion que cargara de forma dinamica los modulos a ejecutar que implementen la interfaz SparkService 

```
    public static void main(String[] args) {
        List<String> services = new ArrayList<>();
        ServiceLoader<SparkService> loader = ServiceLoader.load(SparkService.class);
        if (!loader.iterator().hasNext()) {
            System.out.println("No se han encontrado modulos para cargar");
        }
        for (SparkService service : loader) {
            services.add(service.getClass().getName());
            service.initService();
        }
```

Para que el modulo pueda saber quien esta usando esta dependencia tenemos que configurar el **module-info**

```
module com.apolo.jdk.main {
    requires com.apolo.jdk.services; (1)
    uses com.apolo.jdk.services.SparkService; (2)
}
```
 **(1) required** con esta sentencia decimos que modulos son necesarios para poder ejecutar este modulo correctamente.
 
 **(2) uses** con esta sentencia decimos que vamos a necesitar las implementaciones de esta interfaz de manera dinamica.
 
 Por ultimo debemos de configurar la tarea de gradle para poder compilar este proyecto.

```
plugins {
    id 'java'
    id 'application'
}

dependencies {
    implementation project(':services')

    runtimeOnly project(':person')
    runtimeOnly project(':gift')
}
import java.util.regex.Matcher;

ext.moduleName  = 'com.apolo.jdk.main'

mainClassName = "$moduleName/com.apolo.jdk.main.App"

run {
    inputs.property("moduleName", moduleName)
    doFirst {
        jvmArgs = [
            '--module-path', classpath.asPath,
            '--module', mainClassName // <2>
        ]
        classpath = files()
    }
}
startScripts {
    inputs.property("moduleName", moduleName)
    doFirst {
        classpath = files()
        defaultJvmOpts = [
            '--module-path', 'APP_HOME_LIBS',  // <1>
            '--module', mainClassName
        ]
    }
    doLast{
        def bashFile = new File(outputDir, applicationName)
        String bashContent = bashFile.text
        bashFile.text = bashContent.replaceFirst('APP_HOME_LIBS', Matcher.quoteReplacement('$APP_HOME/lib'))

        def batFile = new File(outputDir, applicationName + ".bat")
        String batContent = batFile.text
        batFile.text = batContent.replaceFirst('APP_HOME_LIBS', Matcher.quoteReplacement('%APP_HOME%\\lib'))
    }
}
```
Como podemos ver en dependencies añadimos los modulos en modo de runtime de tal manera que el App ** solo levantara los servicios (modulos) que encuentre al momento de arrancar**


### Services
Este modulo contiene la interfaz de contrato SparkService.

### gift
Este modulo levanta un servicio rest de manera dinamica con spark framework.

```
public class GiftService implements SparkService {
    @Override
    public void initService() {
        System.out.print("Iniciando Gift");
        Spark.get("/Gift", ((request, response) -> {
            System.out.println("Hola desde Gift");
            return "Hola desde Gift";
        }));
    }
}

module com.apolo.jdk.service.gift {
    requires transitive com.apolo.jdk.services;

    provides com.apolo.jdk.services.SparkService
        with com.apolo.jdk.service.gift.GiftService;

    requires spark.core;
}
```


### Person 
Este modulo levanta un nuevo servicio rest de manera dinamica.

## Tareas Gradle
Si queremos compilar este jar podemos hacer un classico gradle build esto nos generara una carpeta distribution entre otras donde tendremos un zip con todas la libs necesarias para poder ejecutar este proyecto ademas de un script **main.bat** para poder ejecutar facilmente.

## Link
Este proyecto lo he realizado siguiendo el tutorial de gradle: https://guides.gradle.org/building-java-9-modules/
Plugin experimental de gradle para facilitar las tareas de compilacion: https://plugins.gradle.org/plugin/org.gradle.java.experimental-jigsaw











