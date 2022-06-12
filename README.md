![build-and-deploy](https://github.com/Joaquin-GM/GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software/actions/workflows/maven.yml/badge.svg)
[![Heroku](http://heroku-badge.herokuapp.com/?app=evolution-metrics-v2&style=flat&svg=1)](https://evolution-metrics-v2.herokuapp.com/)

<img src="https://github.com/Joaquin-GM/GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software/blob/main/Memoria/img/_LOGOAPP.png" alt="logo" width="200" height="200" />

# GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software 

Aplicación Java desarrollada empelada el framework Vaadin que toma como entrada un conjunto de direcciones de repositorios públicos o privados y calcula  medidas de la evolución que permiten comparar los repositorios.</br>
Segunda iteración del software **Evolution Metrics Gauge** donde se añaden nuevas métricas y se integra GitHub además de otras mejoras.

### Autor

- Joaquín García Molina - [jgm1009@alu.ubu.es](mailto:jgm1009@alu.ubu.es)

### Tutor

- Carlos López Nozal - [clopezno@ubu.es](mailto:clopezno@ubu.es)


## Metricas de evolución

Leyenda I=Issues C=Commits IC=Integración Continua DC=Despliegue Continuo 
   
*   I1 total number of issues. (6-44)

*   I2  commits  per  issue.  I1  divided  by  total  number  of commits. (0.5-1)

*   I3 percentage of issues closed( number of closed issues ∗ 100/I1). (87-100)

*   TI1 average of days to close an issue. (2.2-19.41)

*   TC1 average of days between commits. (1-4.3)

*   TC2 days between the first and the last commit. (81-198)

*   TC3 change activity range per month: total number of commits divided by lifespan number of months. (6-26.4)

*   C1 peak change: count number of commits in the peak month divided by total number of commits. (0.25-0-55)

*   IC1 total number of jobs executed (1-1000)

*   IC2 number of jobs executed last year (1-200)

*   IC3 number of executed job types  (1-8)

*   DC1 total number of releases (1-10)

*   DC2 number of releases released last year (1-5)

## Trabajos previos

*   [comparador-de-metricas-de-evolucion-en-repositorios-software](https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software)

*   [Activity-Api](https://github.com/dba0010/Activiti-Api )

*   [Software Project Assessment in the Course of Evolution -  Jacek Ratzinger](http://www.inf.usi.ch/jazayeri/docs/Thesis_Jacek_Ratzinger.pdf)
   
*   [Soporte de Métricas con Independencia del Lenguaje para la Inferencia de Refactorizaciones](https://www.researchgate.net/profile/Yania_Crespo/publication/221595114_Soporte_de_Metricas_con_Independencia_del_Lenguaje_para_la_Inferencia_de_Refactorizaciones/links/09e4150b5f06425e32000000/Soporte-de-Metricas-con-Independencia-del-Lenguaje-para-la-Inferencia-de-Refactorizaciones.pdf)


## Datos de estudio experimental

*   [Datos experimentales: Métricas de evolución en TFGs](https://github.com/clopezno/clopezno.github.io/blob/master/agile_practices_experiment/DataSet_EvolutionSoftwareMetrics_FYP.csv)

*  [TFGs previos](https://github.com/Joaquin-GM/GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software/tree/0032466f20aba15fe562f90fa7d8a7300673af15/utils-analysis)

## Repositorios para pruebas

*  https://github.com/alejandrolampreave/GPS
*  https://github.com/Victoracha/TFG-Reglas-de-prioridad
*  https://github.com/CarolinaCCZ/LucErik/
*  https://github.com/masantamario/Geslab-2.0
*  https://github.com/EduardoRisco/SurveyingPointCode
*  https://github.com/ysi0000/-FatigaMasPR
*  https://github.com/alejandrolampreave/GPS
*  https://gitlab.com/mlb0029/publictestproject
*  https://gitlab.com/mlb0029/ListaCompra
*  https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software


## APIs de conexión 
Estas APIs de envoltura nos ahorran trabajo adaptando [GitHub REST API](https://docs.github.com/es/rest) y [GitLab REST API](https://docs.gitlab.com/ee/api/) a Java.

*   [github-api.kohsuke](https://github-api.kohsuke.org/)

*   [gitlab4j-api](https://github.com/gmessner/gitlab4j-api)

## Herramientas utilizadas

*   [Eclipse IDE for Java EE Developers. Version: 2019-03](https://www.eclipse.org/)

*   [Apache Maven v3.8.4](https://maven.apache.org/)

*   [Maven Jetty Plugin](https://mvnrepository.com/artifact/org.eclipse.jetty/jetty-maven-plugin/9.4.36.v20210114)

*   [Java SE 11 (JDK)](https://www.oracle.com/technetwork/java/javase/overview/index.html)

*   [github-api.kohsuke](https://github-api.kohsuke.org/)

*   [gitlab4j-api v4.9.14](https://github.com/gmessner/gitlab4j-api)

*   [JUnit5 v5.3.1](https://junit.org/junit5/)

*   [Heroku](https://www.heroku.com/)

*   [Vaadin 13.0.0](https://vaadin.com/)

## Licencia
[<img src="https://www.gnu.org/graphics/gplv3-with-text-136x68.png" alt="gplv3license" width="100"  />](https://www.gnu.org/licenses/gpl-3.0.html)
