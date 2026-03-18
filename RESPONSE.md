# RESOLUCIÓN DE LOS EJERCICIOS

## 1. Añadir un nuevo tipo de ataque

**¿Qué problema te encuentras al añadir "Meteoro"?**

Que debo seguir ampliando el switch, y esto implica dos cosas:

- Tendré que seguir modificando CombatEngine para añadir ataques nuevos y esto no debería de ser su responsabilidad
  (SRP)
- El código debería extenderse, no modificarse; un switch creciente no es la mejor solucion (OCP)

**¿Qué pasa si mañana piden 10 ataques más?**

Que la clase se vuelve excesivamente grande solo por la lógica de creación de ataques, opacando el resto de
funcionalidad

**¿Qué patrón permitiría añadir ataques sin modificar CombatEngine?**

Un patrón Factory sería la solución, puesto que:

- Permite desacoplar CombatEngine de la creación de ataques, liberándolo de esa responsabilidad y cumpliendo el SRP
- Cuando quiera añadir un nuevo ataque, solamente será necesario crear la factoría correspondiente y realizar cambios
  menores (OCP)

### Razonamiento de la solución

Finalmente, para la refactorización de este código opté por una implementación concreta de factoría que encapsulase la
lógica de decisión para el tipo de ataque en base al discriminador recibido en el body (previamente parseado al valor
enumerado) por los siguientes motivos:

- Implementar una factoría por tipo de ataque me parecía excesivo teniendo en cuenta la simplicidad de la entidad
  Attack.

- La mayor ventaja de aplicar el patrón consistía en desacoplar la lógica de creación de CombatEngine, lo cual se sigue
  cumpliendo con esta implementación a pesar de constar de menos clases.

### Implementación de la solución

En lugar de implementar una factoría por tipo de ataque y discriminar la factoría en el servicio, lo que hice fue
dejar que el servicio traduzca el String al valor enumerado de Movimiento de ataque (que también creé para esta
solución)
y lo pase a CombatEngine, quien dispone de la Factoría como atributo para delegar en ella en el momento de la creación
del ataque.

- La factoría se encarga de mapear el discriminador enumerado al tipo de ataque correspondiente. Utiliza una
  implementación
  concreta de Map (EnumMap) que es más eficiente para tipos enumerados usados como clave.

- Decidí que era asumible que la Factoría fuese un atributo de CombatEngine porque la estructura previa del código tiene
  un método en la misma que consiste en crear el ataque, aunque realmente se podría haber optado también por delegar la
  creación a la factoría desde el propio Servicio, añadiendo a la factoría como atributo en dicha clase en su lugar.

- Esta implementación sustituye el mapa del discriminador-factoría en el servicio y no pasa como argumento un objeto
  factoría por múltiples niveles hasta llegar a su punto de consumo (lo cual tampoco es que esté mal de por sí, pero
  esta
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
  directamente dentro del enumerado, haciendo que sea la única fuente de verdad con respecto a este tema. Podría,
  incluso,
  hacer que el enumerado transformase a récord con un simple `new Attack()` usando los parámetros definidos en cada
  valor
  del enum, y eso me ahorraría la Factoría entera (que en este punto, más que crear, transforma los valores del
  enumerado
  con sus parámetros en un objeto Record encapsulado, como si fuera un Value Object). Parece potente, aunque tampoco sé
  si el enumerado como "generador" de ataques me convence demasiado. Desde luego, para la semántica de tener los valores
  de cada movimiento de ataque asociados al valor del enumerado, parece lo mejor.

- En Java moderno existen las *sealed interfaces* que quizás puedan ayudar a mejorar algún punto de esta implementación.
  Quizás merezca la pena investigarlas, pero estoy bastante satisfecho con esta implementación por ahora.

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

## 2. Añadir una nueva fórmula de daño

**¿Qué principio SOLID se viola al añadir otro case en el switch?**

El OCP.

**¿Qué patrón permitiría tener fórmulas de daño intercambiables sin tocar el código existente?**

El patrón Strategy

### Razonamiento de la solución

Para esta solución se me ocurren dos implementaciones, basándome en lo aprendido en la implementación del ejercicio
anterior:

- Implementación tradicional de Strategy: una interfaz para la estrategia con implementaciones para cada tipo de daño.
  Esto permite añadir clases para cada implementación, y el método solamente delega en la implementación que recibe por
  parámetro
  para aplicar la estrategia.

- Usar los "poderes" de la clase Enum de Java: por lo descubierto anteriormente, es posible asociar un comportamiento
  distinto
  a cada valor de un Enum en Java, de forma que la implementación de un Strategy se basa simplemente en
  ENUM_INSTANCE.doSth()
  y esto implementa directamente la estrategia específica (previamente definida) para ese valor del enum. Es una
  implementación
  algo más "implícita" para el patrón Strategy, extremadamente ligada al tipo del valor enumerado, aunque en nuestro
  caso eso
  no es precisamente una desventaja, puesto que la lógica así lo expresa.

### Implementación de la solución

He optado por utilizar la implementación del enumerado, puesto que viendo la solución de este taller, no he encontrado
otra forma de hacer un strategy tradicional sin violar el OCP. Aquí una explicación de la implementación:

- He definido la interfaz funcional DamageStrategy (con la anotación @FunctionalInterface) que define los parámetros y
  el tipo de retorno.
  Esto actúa como "contrato de estrategia"; todos los métodos que tengan estos parámetros y este tipo de retorno cumplen
  la interfaz
  (duck typing; si hace quack como un pato y anda como un pato, es un pato).

- He agregado un atributo de tipo DamageStrategy a la clase enumerado. De esta forma, cada valor del enumerado tendrá
  una
  estrategia como atributo, la cual podrán aplicar usando el .calculateDamage que define la interfaz funcional; en cada
  caso
  se implementará como toque.

- He creado una clase final por cada estrategia; con constructor privado (las estrategias son estáticas, no debería
  necesitar
  una instancia para ejecutarlas) que definen, cada una, un método estático que cumple la interfaz funcional previamente
  definida;
  el compilador lo acepta.

- He referenciado esos métodos estáticos mediante la sintaxis de referenciado de métodos estáticos `Class::staticMethod`
  directamente al constructor de cada valor del enumerado AttackType. De esta manera, cada tipo de ataque tiene
  implícito su
  estrategia de cálculo accesible desde su atributo DamageStrategy.

### Posibles puntos de mejora

- Al tener un enumerado, se debe seguir modificando el mismo para agregar un nuevo tipo de ataque. Sin embargo, al ser
  el tipo
  de ataque un atributo del propio ataque, es algo que tendría que ocurrir de todas formas. De todas formas, para
  agregar una estrategia
  a ese nuevo tipo de ataque basta con crear la clase nueva que la defina, por lo que de considerarse violación del OCP,
  es muy asumible.

Si se encuentran más "pegas" a esta solución, me encantaría escucharlo, debatirlo y aprender de ello :)

### Profundización: otras opciones

He tenido que profundizar mucho para llegar a esta solución: anteriormente no había usado (ni conocía) la anotación de
interfaces funcionales pese a conocer el concepto, ni había utilizado la referencia a métodos estáticos `::` más allá de
su
uso inconsciente en streams.

Si existe alguna forma de refinar la solución o utilizar algún otro concepto de Java, sería de gran ayuda.

## 3. Crear personajes con muchas estadísticas

**¿Qué problema tiene un constructor con muchos parámetros?**

Que es fácil equivocarse con el orden de los parámetros al instanciar. Además es muy poco legible.

**¿Cómo harías para que `new Character(...)` sea legible cuando hay valores por defecto?**

- Seguramente, crearía un constructor canónico (parámetros para todos los atributos) y crearía constructores con
  parámetros concretos
  para los atributos a los que quiera darles valor. En estos constructores, se delegaría al canónico con los valores por
  defecto,
  evitando así valores nulos.

- A lo mejor podría hacerse algo con los setter, pero no creo que sea la mejor opción.

**¿Qué patrón permite construir objetos complejos paso a paso?**

El patrón Builder, el cual Lombok implementa fenomenal con la anotación @Builder ;).

## ¿Cómo lo implementaría (si me diese la vida para más)?

Seguramente, con @Builder XD. Es broma, plantearía una ClaseBuilder con un método para cada atributo de la clase. Al
final,
tendría un .build(), que me devolvería una instancia con los atributos inicializados a los valores que he ido definiendo
método
a método.

Esto mejora ampliamente la legibilidad y flexibilidad en la construcción de objetos, al ser mucho más expresivo y no
forzar
a poner valores por defecto o nulos en ciertos parámetros de los constructores.

## 4. Un único almacén de batallas

**¿Qué pasaría si dos clases crean su propio BattleRepository sin el static?**

Que tendrían instancias distintas del repositorio (sin el static, `battles` es atributo de instancia, no de clase), y,
por
tanto, no tendrían la misma información.

**¿Cómo asegurar que toda la aplicación use la misma instancia de almacenamiento?**

Controlando la creación de instancias de `BattleRepository` a nivel global, asegurando que solo se pueda crear 1 y dando
acceso global a la misma.

**¿Qué patrón garantiza una única instancia de una clase?**

El patrón Singleton

## Razonamiento de la solución

Se me ocurren varias implementaciones del Singleton, aunque todas pasan por hacer el constructor privado, cada una con
sus pros y sus contras:

- Implementación temprana por atributo estático: Consiste en que `BattleRepository` sea un atributo estático (y por
  tanto
  pre-inicializado en la definición de la clase). Es más directo y no requiere instanciar, pero puede resultar
  innecesario
  si al final la clase no se usa. En nuestro contexto, esto no es una desventaja

- Implementación perezosa con getInstance: Es la implementación "clasica"; `BattleRepository` tendría un atributo no
  estático
  llamado instance. Este atributo se inicializa con un método estático público llamado getInstance(), devolviendo la
  única instancia
  recién creada cuando se le llame. En el resto de llamadas, simplemente retorna la variable inicializada previamente en
  la primera llamada.
  Esta implementación requiere una instancia al no ser estática, pero retrasa la creación del objeto hasta su uso real.
  Además,
  también es algo más flexible al permitir elegir la implementación del Map por parámetro (lo cual - creo - no se
  debería hacer en un
  Singleton y por eso no figura como desventaja en la otra implementación)

Ahora visto en una tabla (cortesía de Gemini):

### Comparativa de Implementaciones Singleton

| Implementación                                                                              | Pros (Ventajas)                                                                                                                                                                                                                                                                  | Contras (Desventajas)                                                                                                                                                                                                      |
|:--------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Inicialización Temprana (Eager)**<br>`private static final Clase instance = new Clase();` | - **Thread-safe por defecto:** La JVM garantiza que la instancia se cree antes de que cualquier hilo acceda a ella.<br>- **Simplicidad:** Código muy limpio y directo.<br>- **Alto rendimiento:** No requiere bloqueos ni comprobaciones `if` cada vez que se pide la instancia. | - **Uso de recursos:** La instancia se crea al cargar la clase, aunque nunca se llegue a utilizar.<br>- **Rigidez:** No permite manejar excepciones del constructor de forma sencilla (fuera de bloques `static`).         |
| **Inicialización Perezosa (Lazy)**<br>`if (instance == null) { instance = new Clase(); }`   | - **Eficiencia de recursos:** Solo ocupa memoria si realmente se solicita la instancia.<br>- **Flexibilidad:** Permite lógica adicional o manejo de errores (`try-catch`) durante la creación.                                                                                   | - **Riesgo en multihilo:** Sin sincronización, dos hilos podrían crear dos instancias distintas.<br>- **Complejidad/Rendimiento:** Hacerlo seguro (con `synchronized`) añade una penalización de velocidad en cada acceso. |

