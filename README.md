[![Pipeline Status](https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software/badges/master/pipeline.svg)](https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software/commits/master)
[![Heroku](http://heroku-badge.herokuapp.com/?app=evolution-metrics&style=flat&svg=1)](https://evolution-metrics.herokuapp.com/)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/745c3cea58574491b488ebf7ee2e5aa2)](https://www.codacy.com/manual/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/745c3cea58574491b488ebf7ee2e5aa2)](https://www.codacy.com/manual/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software&amp;utm_campaign=Badge_Coverage)
[![Coverage Report](https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software/badges/master/coverage.svg)](https://mlb0029.gitlab.io/comparador-de-metricas-de-evolucion-en-repositorios-software)

![Poster](https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software/blob/master/MemoriaProyecto/Poster.png)

# Evaluación de la actividad de un proyecto software

Aplicación Java que toma como entrada un conjunto de direcciones de repositorios públicos o privados y calcula  medidas de la evolución que permiten comparar los repositorios.

## Metricas de evolución

*   I1 total number of issues. (6-44)

*   I2  commits  per  issue.  I1  divided  by  total  number  of commits. (0.5-1)

*   I3 percentage of issues closed( number of closed issues ∗ 100/I1). (87-100)

*   TI1 average of days to close an issue. (2.2-19.41)

*   TC1 average of days between commits. (1-4.3)

*   TC2 days between the first and the last commit. (81-198)

*   TC3 change activity range per month: total number of commits divided by lifespan number of months. (6-26.4)

*   C1 peak change: count number of commits in the peak month divided by total number of commits. (30%-40%)

## Trabajos previos

*   [Activity-Api](https://github.com/dba0010/Activiti-Api )

*   [Soporte de Métricas con Independencia del Lenguaje para la Inferencia de Refactorizaciones](https://www.researchgate.net/profile/Yania_Crespo/publication/221595114_Soporte_de_Metricas_con_Independencia_del_Lenguaje_para_la_Inferencia_de_Refactorizaciones/links/09e4150b5f06425e32000000/Soporte-de-Metricas-con-Independencia-del-Lenguaje-para-la-Inferencia-de-Refactorizaciones.pdf)

*   [Software Project Assessment in the Course of Evolution -  Jacek Ratzinger](http://www.inf.usi.ch/jazayeri/docs/Thesis_Jacek_Ratzinger.pdf)

## Datos de estudio experimental

*   [Datos experimentales: Métricas de evolución en TFGs](https://github.com/clopezno/clopezno.github.io/blob/master/agile_practices_experiment/DataSet_EvolutionSoftwareMetrics_FYP.csv)

## Repositorios para pruebas

*   https://gitlab.com/mlb0029/privatetestproject

*   https://gitlab.com/mlb0029/publictestproject

*   https://gitlab.com/mlb0029/ListaCompra

## APIs Investigadas para consexión con GitLab
Estas APIs de envoltura nos ahorran trabajo adaptando [GitLab REST API](https://docs.gitlab.com/ee/api/) a Java.

*   [java-gitlab-api](https://github.com/timols/java-gitlab-api)

*   [gitlab4j-api](https://github.com/gmessner/gitlab4j-api)

## Herramientas utilizadas

*   [Eclipse IDE for Java EE Developers. Version: 2018-09 (4.9.0)](https://www.eclipse.org/)

*   [Apache Maven v3.6.0](https://maven.apache.org/)

*   [Apache Tomcat v9.0.13](http://tomcat.apache.org/)

*   [Java SE 11 (JDK)](https://www.oracle.com/technetwork/java/javase/overview/index.html)

*   [gitlab4j-api v4.9.14](https://github.com/gmessner/gitlab4j-api)

*   [JUnit5 v5.3.1](https://junit.org/junit5/)

*   [Codacy](https://www.codacy.com/)

*   [Heroku](https://www.heroku.com/)

*   [Vaadin 13.0.0](https://vaadin.com/)