[:es: Español](#diseño-y-desarrollo-de-sistemas-de-información) | [:gb: English](#design-and-development-of-information-systems)

---
# Diseño y Desarrollo de Sistemas de Información #
## :books: Curso 2020-2021, Grado en Ingeniería Informática, ETSIIT UGR.
### :pushpin: Introducción
El apartado práctico de la asignatura se dividió en tres entregas, todas referentes a la creación desde cero de un Sistema de Información, las primeras dos entregas fueron las etapas iniciales mientras que la tercera fue la implementación en código de partes del sistema. Se trata de una aplicación para gestionar todos los aspectos relacionados con torneos de Pádel, desde los jugadores hasta los usuarios que compran entradas y los trabajadores y materiales que se utilizan.

El sistema de información se divide en cinco subsistemas: **Jugadores/Entrenadores**, **Usuarios/Entradas**, **Patrocinadores/Colaboradores**, **Personal/Horarios** y **Materiales/Pedidos**

Adicionalmente se realizó un seminario práctico para probar la conexión de Java a la conexión de la base de datos Oracle de la ETSIIT.

### :busts_in_silhouette: Equipo
Las prácticas fueron realizadas en equipo junto a [Eladia Gómez Morales](https://github.com/elaypunto), [José Correa Maldonado](https://github.com/JoseMaldonadoC), [Amanda Moyano Romero](https://github.com/amxndam) y [Jesús Muela Cárdenas](https://github.com/KTJota).

### :gear: Compilación
:warning: Debido a que se utiliza la base de datos de la ETSIIT, para el funcionamiento correcto se debe estar en la VPN de la UGR o bien cambiar el código apropiadamente para utilizar otra base de datos.

Para compilar, se posee un ```makefile``` con los siguientes comandos:
  * ```make``` para compilar el Sistema de Información.
  * ```make exe``` para ejecutar el Sistema de Información.
  * ```make clean``` para limpiar los objetos ```.class``` generados.
### :link: Contenido
#### :building_construction: Seminario 2
El Seminario 2 consistió en la realización de un Sistema de Información sencillo, solamente con tres tablas, para probar la conectividad entre Java y Oracle SGBD, además conocer como interactuar por SQL por medio de Java, y cómo realizar transacciones.
#### :tennis: Práctica 3
La Práctica 3 consistió, como se ha mencionado, en la codificación  del Sistema de Información que se había prototipado en las prácticas anteriores. De cada subsistema se realizó una funcionalidad de las totales que se habían obtenido y un disparador de la base de datos de algún requerimiento semántico del mismo subsistema.

---
# Design and Development of Information Systems #
## :books: 2020-2021 Course, Computer Science Engineering Degree, ETSIIT UGR.
### :pushpin: Introduction
The practical side of the course was divided in three practical assignments, all relating to the creation from scratch of an Information System, the first two assignments where for the initial steps while the third one was the implementation in code of parts of the system. The system is an app for managing all the aspects related to Padel tournaments, ranging from the players, the users that buy tickets and the workers and materials being used.

The information system can be divided in five subsystems: **Players/Trainers**, **Users/Tickets**, **Sponsors/Collaborators**, **Staff/Schedules** and **Materials/Orders**

Additionally, there was a practical seminar to test the connection between Java to ETSIIT's Oracle Database.
### :busts_in_silhouette: Team
The practices where made alongside [Eladia Gómez Morales](https://github.com/elaypunto), [José Correa Maldonado](https://github.com/JoseMaldonadoC), [Amanda Moyano Romero](https://github.com/amxndam) and [Jesús Muela Cárdenas](https://github.com/KTJota).
### :gear: Compilation
:warning: Since the system uses ETSIIT's Database, the computer needs to be connected to UGR's VPN for the proper functioning of the app otherwise the code should be changed to use another database.
  
   To compile, there's a  ```makefile``` with the following commands:
  * ```make``` to compile the Information System.
  * ```make exe``` to execute the Information System.
  * ```make clean``` to clean the ```.class``` objects generated.
### :link: Contents
#### :building_construction: Seminar 2
The second seminar was about the realization of a pretty basic Information System, with only three tables, to test the connectivity between Java and Oracle DBMS, and know how to interact with SQL through Java and how to make transactions. 

#### :tennis: Practical Assignment 3
The 3rd Practical Assignment, as previously mentioned, was about coding the Information System that had been previously prototyped in previous assignments, of each subsystem one functionality was developed alongside a database trigger related to a semantic requirements of said subsystem.
