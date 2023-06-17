# ECGSCAN
***
Trabajo fin de grado del alumno Ismael Raqi Picardo en el grado de Ingeniería Informática de la Universidad de Cádiz.


## Tabla de contenido
1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [Collaboration](#collaboration)
5. [FAQs](#faqs)

### Información General
***
Estado del proyecto: Finalizado
### Screenshot
![Portada de la aplicación](https://i.imgur.com/VGpbvSP.jpeg)
## Tecnologías
***
Para el proyecto se han utilizado las siguientes tecnologías:
* [Java](https://www.java.com/es/download/ie_manual.jsp): Version 1.8
* [Android](https://developer.android.com/studio): Version 13 SDK 33
* [Matlab](https://la.mathworks.com/products/matlab.html): Version R2021b
* [Matlab Compiler](https://la.mathworks.com/products/compiler.html): Version R2021b
* [Matlab Production Server](https://www.mathworks.com/products/matlab-production-server.html): Version R2021b
* [Kubernetes]
* [Docker]
* [NGINX]
* [Google Cloud]
* [Google Firebase]


## Instalación
***
Habrá que descargar el repositorio y generar el apk (lo usual es importar el proyecto en Android Studio y clickar en "Build APK") para luego transferirlo al dispositivo móvil o emulador e instalar la aplicación.
```
$ https://github.com/IsmaelRP/TFG
$ cd ../path/to/the/file
$ gradlew assembleDebug
```

## FAQs
***

1. **¿De qué trata el proyecto?**
_El proyecto trata de una plataforma en la cual se interactua mediante una aplicación móvil en Android, en la cual se permite la subida o toma de imágenes (de electrocardiogramas) del dispositivo para realizar un diagnóstico de ellas y mostrarlo en pantalla_. 
2. **¿Dónde se diagnostican las imágenes de los electrocardiogramas?**
_En la nube de Google (Google Cloud), de forma que liberamos de toda la ejecución al dispositivo móvil y realizamos la computación en la nube, obteniendo sólo el diagnostico._
3. **¿Se incorporan funcionalidad adicional aparte del diagnóstico?**
_Sí, se incorpora múltiple funcionalidad como puede ser el uso de listas online y offline, el filtrado de electrocardiogramas, múltiples proveedores de identidad para la autenticación y autorización del usuario, etc.._
4. **¿De que tecnologías consta el repositorio?**
_Contendrá la mayor parte del código en Java con el framework de Android para la plataforma Android, y una menor parte de código Matlab para el diagnóstico de las imágenes._
5. **¿Cómo se estructura el repositorio?**
_El repositorio consta de una rama principal 'master' la cual contendrá la versión final de la plataforma Android, luego tendrá una rama adicional 'development' la cual contendrá el histórico del proyecto y futuras mejoras en las que se esté trabajando, y por último una rama 'matlab' la que contendrá el histórico y el código final del código Matlab que se encarga de realizar el tratamiento de imágenes y diagnóstico de estas._