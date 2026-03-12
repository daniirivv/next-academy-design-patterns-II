# RESOLUCIÓN DE LOS EJERCICIOS

## 1. Añadir un nuevo tipo de ataque

**¿Qué problema te encuentras al añadir "Meteoro"?**

Que debo seguir ampliando el switch, y esto implica dos cosas:

- Tendré que seguir modificando CombatEngine para añadir ataques nuevos y esto no debería de ser su responsabilidad (SRP)
- El código debería extenderse, no modificarse; un switch creciente no es la mejor solucion (OCP)

**¿Qué pasa si mañana piden 10 ataques más?**

Que la clase se vuelve excesivamente grande solo por la lógica de creación de ataques, opacando el resto de funcionalidad 

**¿Qué patrón permitiría añadir ataques sin modificar CombatEngine?**

Un patrón Factory sería la solución, puesto que:

- Permite desacoplar CombatEngine de la creación de ataques, liberándolo de esa responsabilidad y cumpliendo el SRP
- Cuando quiera añadir un nuevo ataque, solamente será necesario crear la factoría correspondiente y realizar cambios menores (OCP)

### Razonamiento de la solución

Finalmente, para la refactorización de este código opté por una implementación concreta de factoría que encapsulase la
lógica de decisión para el tipo de ataque en base al discriminador recibido en el body (previamente parseado al valor 
enumerado) por los siguientes motivos:

- Implementar una factoría por tipo de ataque me parecía excesivo teniendo en cuenta la simplicidad de la entidad Attack.

- La mayor ventaja de aplicar el patrón consistía en desacoplar la lógica de creación de CombatEngine, lo cual se sigue 
cumpliendo con esta implementación a pesar de constar de menos clases.

### Implementación de la solución

- En lugar de implementar una factoría por tipo de ataque y discriminar la factoría en el servicio, lo que hice fue
dejar que el servicio traduzca el String al valor enumerado de Movimiento de ataque (que también creé para esta solución) 
y lo pase a CombatEngine, quien dispone de la Factoría como atributo para delegar en ella en el momento de la creación 
del ataque.

- La factoría se encarga de mapear el discriminador enumerado al tipo de ataque correspondiente. Utiliza una implementación
concreta de Map (EnumMap) que es más eficiente para tipos enumerados usados como clave.

- Decidí que era asumible que la Factoría fuese un atributo de CombatEngine porque la estructura previa del código tiene 
un método en la misma que consiste en crear el ataque, aunque realmente se podría haber optado también por delegar la 
creación a la factoría desde el propio Servicio, añadiendo a la factoría como atributo en dicha clase en su lugar.

- Esta implementación sustituye el mapa del discriminador-factoría en el servicio y no pasa como argumento un objeto 
factoría por múltiples niveles hasta llegar a su punto de consumo (lo cual tampoco es que esté mal de por sí, pero esta 
opción me ha parecido algo más interesante).

### Posibles puntos de mejora

- Para esta implementación, he convertido a Attack en clase abstracta, y he creado una jerarquía de herencias con cada 
tipo de ataque. No estoy muy seguro de esta jerarquía; al fin y al cabo, no dejan de ser instancias con valores 
predefinidos para los atributos que define Attack. Soluciones para esto podrían ser usar Record (que debido a que es 
clase final no entró en mi estrategia) y delegar en la Factoría también para la selección de atributos con los que 
inicializar cada instancia de ataque según el movimiento que vaya a ser.

PD: al final implementé estos cambios también

- PDPD: Por lo visto dentro de los enumerados puedo poner constructores y parámetros también (Power Use de manual). 
Esto me permitiría introducir las características propias de cada movimiento de ataque (nombre, daño base y tipo) 
directamente dentro del enumerado, haciendo que sea la única fuente de verdad con respecto a este tema. Podría, incluso, 
hacer que el enumerado transformase a récord con un simple `new Attack()` usando los parámetros definidos en cada valor 
del enum, y eso me ahorraría la Factoría entera (que en este punto, más que crear, transforma los valores del enumerado 
con sus parámetros en un objeto Record encapsulado, como si fuera un Value Object). Parece potente, aunque tampoco sé 
si el enumerado como "generador" de ataques me convence demasiado. Desde luego, para la semántica de tener los valores 
de cada movimiento de ataque asociados al valor del enumerado, parece lo mejor.


### Profundización: otras opciones

El bueno de *Gemini* propone una solución adicional:

> En una solución basada en Spring Boot, podrías delegar la gestión del discriminador al propio contenedor de inversión 
> de control (IoC). Al definir una interfaz común (por ejemplo, AttackStrategy) y marcar cada implementación de ataque 
> con la anotación @Component, Spring es capaz de inyectar automáticamente todas las instancias disponibles en un 
> Map<String, AttackStrategy> dentro de tu factoría o del propio CombatEngine. De este modo, el mapa se autocompleta en 
> tiempo de ejecución: para añadir un nuevo ataque como "Terremoto", simplemente crearías una nueva clase; Spring la 
> detectaría y la añadiría al mapa por ti basándose en el nombre del bean. Esto permite que el sistema crezca de forma 
> infinita sin que tengas que editar nunca el código de la factoría o del motor, cumpliendo de forma estricta y elegante 
> con el Principio de Abierto/Cerrado (OCP).

Bueno saberlo.