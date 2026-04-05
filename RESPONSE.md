# RESOLUCIÓN DE LOS EJERCICIOS

## 1. Añadir un nuevo tipo de ataque

**¿Qué problema te encuentras al añadir "Meteoro"?**

Que debo seguir ampliando el switch, y esto implica dos cosas:

- Tendré que seguir modificando CombatEngine para añadir ataques nuevos y esto no debería de ser su responsabilidad
  (SRP)
- El código debería extenderse, no modificarse; un switch creciente no es la mejor solucion (OCP)

**¿Qué pasa si mañana piden 10 ataques más?**

Que la clase se vuelve excesivamente grande solo por la lógica de creación de ataques, opacando el resto de
funcionalidad y haciendo que la clase sea más difícil de entender o trabajar con ella (La longitud de una clase, métodos
e incluso de los números de caracteres de una línea de código son elementos que se tienen en cuenta a la hora de
programar
y son lo que diferencias a un buen programador de uno mediocre).

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
  Attack. -> Aunque la clase Atack sea simple, eso no es una razón para no usar un factory. Te entiendo, y es cierto que
  si la clase fuera más compleja el factory ayudaría mucho más, pero en este caso, ayuda con el SRP.

- La mayor ventaja de aplicar el patrón consistía en desacoplar la lógica de creación de CombatEngine, lo cual se sigue
  cumpliendo con esta implementación a pesar de constar de menos clases. -> Efectivamente, tu approach no está mal,
  simplemente dejas el mapa dentro de la factory. EL tip que di en clase sobre que, en caso de duda, dejéis estas cosas
  lo más cerca de donde se reciben los datos, es porque así evitas arrastrar lógica que puedes gestionar anter, pero es
  eso,
  en caso de duda. Si tú lo ves bien así, yo también.

### Implementación de la solución

En lugar de implementar una factoría por tipo de ataque y discriminar la factoría en el servicio, lo que hice fue
dejar que el servicio traduzca el String al valor enumerado de Movimiento de ataque (que también creé para esta
solución) y lo pase a CombatEngine, quien dispone de la Factoría como atributo para delegar en ella en el momento de la
creación del ataque. -> Buen approach, a los enums cuando los uso para este tip de cosas, les suelo poner un método
value() para asegurarme de que van a devolver lo que yo quiero. Tu app funciona bien, esto es cuestión de gustos.

Es como la primera parte de este artículo: https://www.baeldung.com/java-enum-values

- La factoría se encarga de mapear el discriminador enumerado al tipo de ataque correspondiente. Utiliza una
  implementación concreta de Map (EnumMap) que es más eficiente para tipos enumerados usados como clave. -> Mola

- Decidí que era asumible que la Factoría fuese un atributo de CombatEngine porque la estructura previa del código tiene
  un método en la misma que consiste en crear el ataque, aunque realmente se podría haber optado también por delegar la
  creación a la factoría desde el propio Servicio, añadiendo a la factoría como atributo en dicha clase en su lugar. ->
  Buen argumento

- Esta implementación sustituye el mapa del discriminador-factoría en el servicio y no pasa como argumento un objeto
  factoría por múltiples niveles hasta llegar a su punto de consumo (lo cual tampoco es que esté mal de por sí, pero
  esta opción me ha parecido algo más interesante). -> Bieeen

### Posibles puntos de mejora -> Me encanta este apartado, se nota que lo has pensado bien. Esto es lo que se espera de un ingeniero

- Para esta implementación, he convertido a Attack en clase abstracta, y he creado una jerarquía de herencias con cada
  tipo de ataque. No estoy muy seguro de esta jerarquía; al fin y al cabo, no dejan de ser instancias con valores
  predefinidos para los atributos que define Attack. -> Te iba a decir eso, no veo la herencia por ningún lado. Osea lo
  que tienes está
  bien, no sé si es que no he visto algo, pero recuerda que tampoco hace falta matar moscas a cañonazos. El diseño sirve
  para lo que hay
  y facilita la extensión en el futuro, está bien pensar evoluciones pero no hace falta complicar el código si ahora
  mismo no lo necesita.
  El límite entre un código extrensible y sobrediseñado es pequeño.
- Soluciones para esto podrían ser usar Record (que debido a que es
  clase final no entró en mi estrategia) y delegar en la Factoría también para la selección de atributos con los que
  inicializar cada instancia de ataque según el movimiento que vaya a ser.

PD: al final implementé estos cambios también -> Aaaah por eso no vi yo ninguna herencia, vale, no estoy loca. Esto está
bien.

- PDPD: Por lo visto dentro de los enumerados puedo poner constructores y parámetros también (Power Use de manual).
  Literal lo que te comenté arriba, siento que me lees la mente. Me gusta como piensas, nos parecemos.

  Esto me permitiría introducir las características propias de cada movimiento de ataque (nombre, daño base y tipo)
  directamente dentro del enumerado, haciendo que sea la única fuente de verdad con respecto a este tema. Podría,
  incluso,
  hacer que el enumerado transformase a récord con un simple `new Attack()` usando los parámetros definidos en cada
  valor
  del enum, y eso me ahorraría la Factoría entera (que en este punto, más que crear, transforma los valores del
  enumerado
  con sus parámetros en un objeto Record encapsulado, como si fuera un Value Object). Parece potente, aunque tampoco sé
  si el enumerado como "generador" de ataques me convence demasiado. Desde luego, para la semántica de tener los valores
  de cada movimiento de ataque asociados al valor del enumerado, parece lo mejor. -> Es otra buena solución, de nuevo,
  no tienes por qué usar patrones para programar. Sigo sintiendo que esto es matar moscas a cañonazos. Y que se entiende
  más fácil el factory que el enum. Me gusta como piensas, mi consejo para tí entonces es que te centres en qué es lo
  que
  facilita trabajar con tu código (código limpio, fácil de entender, intuitivo...) porque así, además de razonar bien,
  será
  fácil trabajar contigo. Dos aptitudes muy importantes y que, te prometo, no mucha gente tiene.

- En Java moderno existen las *sealed interfaces* que quizás puedan ayudar a mejorar algún punto de esta implementación.
  Igual merece la pena investigarlas, pero estoy bastante satisfecho con esta implementación por ahora. -> Eso es, hay
  que
  saber cuando parar y, por qué no, sentirte orgulloso y satisfecho con tu trabajo.

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

-> Exacto, yo trabajo con spring boot y hacemos esto. Aquí tampoco quería mezclaros cosas, pero hay formas de deshacerte
de
switch y demás gracias a, por ejemplo, un framework. No sé si habéis dado spring boot en clase, o si solo ha sido por
encima, pero
si tienes curiosidad, se usa bastante en la vida real y saberte este tipo de truquitos está bien. Pero si no, ya tendrás
tiempo
de hacerte algún cursillo por ahí. Spring boot a veces lo carga el diablo con toda esta "automagia" que hace por detrás.

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
  parámetro para aplicar la estrategia. -> Clásico

- Usar los "poderes" de la clase Enum de Java: por lo descubierto anteriormente, es posible asociar un comportamiento
  distinto a cada valor de un Enum en Java, de forma que la implementación de un Strategy se basa simplemente en
  ENUM_INSTANCE.doSth()
  y esto implementa directamente la estrategia específica (previamente definida) para ese valor del enum. Es una
  implementación algo más "implícita" para el patrón Strategy, extremadamente ligada al tipo del valor enumerado,
  aunque en nuestro caso eso no es precisamente una desventaja, puesto que la lógica así lo expresa.

### Implementación de la solución -> Te lo he emtido todo en una nueva carpeta, que siento que queda más ordenado

He optado por utilizar la implementación del enumerado, puesto que viendo la solución de este taller, no he encontrado
otra forma de hacer un strategy tradicional sin violar el OCP. Además, me parecía más rica en cuanto a aprendizaje y
más interesante en cuanto a verdadero beneficio. -> Guay, yo te lo compro, me parece bien que tengáis esta curiosidad y
aprovechéis
para aprender cosas nuevas, pero ten cuidado porque al final esto es un taller de patrones xD Anyway, es refrescante ver
una entrega
distinta en medio de tantas correcciones.

Aquí una explicación de la implementación:

- He definido la interfaz funcional DamageStrategy (con la anotación @FunctionalInterface -> Sabes por qué se le llama
  interfac funcional?
  esto está basado en un concepto de programación de verdad. Lo vas a usar poco en la vida real, yo lo conozco porque mi
  pareja enseña
  programación en la universidad también, así que tengo cosas frescas, pero en tu trabajo seguramente lo dejarías como
  interfaz y ya.
  También depende de dónde acabes trabajandoi, claro) que define los parámetros y
  el tipo de retorno. Esto actúa como "contrato de estrategia"; todos los métodos que tengan estos parámetros y este
  tipo de retorno cumplen la interfaz (duck typing; si hace quack como un pato y anda como un pato, es un pato). -> Siii

- He agregado un atributo de tipo DamageStrategy a la clase enumerado. De esta forma, cada valor del enumerado tendrá
  una estrategia como atributo, la cual podrán aplicar usando el .calculateDamage que define la interfaz funcional; en
  cada caso se implementará como toque.

- He creado una clase final por cada estrategia; con constructor privado (las estrategias son estáticas, no debería
  necesitar una instancia para ejecutarlas) que definen, cada una, un método estático que cumple la interfaz funcional
  previamente definida; el compilador lo acepta.

- He referenciado esos métodos estáticos mediante la sintaxis de referenciado de métodos estáticos `Class::staticMethod`
  directamente al constructor de cada valor del enumerado AttackType. De esta manera, cada tipo de ataque tiene
  implícito su estrategia de cálculo accesible desde su atributo DamageStrategy. -> Sí, esgto se hace, está bien

Te he dejado comentarios en el enumerado, me parece que si lo has hecho por aprender, está bien, pero en la vida real
esta solución
no me parece la mejor. Es difícil de entender, explicar, más larga... De nuevo, vuelvo a mi tip anterior: prioriza
código fácil de
entender.

### Posibles puntos de mejora

- Al tener un enumerado, se debe seguir modificando el mismo para agregar un nuevo tipo de ataque. Sin embargo, al ser
  el tipo de ataque un atributo del propio ataque, es algo que tendría que ocurrir de todas formas. De todas formas,
  para agregar una estrategia a ese nuevo tipo de ataque basta con crear la clase nueva que la defina, por lo que de
  considerarse violación del OCP, es muy asumible.

Si se encuentran más "pegas" a esta solución, me encantaría escucharlo, debatirlo y aprender de ello :) -> Si quieres,
podemos hablar de
ello por correo, por si quieres que lo comentemos más. Es interesante debatir sobre estas cosas, a mí me gusta mucho, y
es así como
ambos podemos aprender de verdad ;)

### Profundización: otras opciones

He tenido que profundizar mucho para llegar a esta solución: anteriormente no había usado (ni conocía) la anotación de
interfaces funcionales pese a conocer el concepto, ni había utilizado la referencia a métodos estáticos `::` más allá de
su uso inconsciente en streams.

-> Está guay que investigues y aprendas, pero no pienses que por no usar este tipo de cosas
programas peor. No es mejor programador el que usa funcionalidades más nuevas o complejas. Permítete ir poco a poco.
COmo te dije
ariba, la referencia a métodos estáticos yo sí que la suelo usar, pero empecé a hacerlo al salir de la universidad y ver
código de compañeros
y ver casos reales donde el cuerpo me pedía usarlo. Así lo aprendí y para mí es normal.

La anotación de la interfaz es la primera vez que la veo. Entiendo por qué existe, pero también sé que todo funciona
igual sin ella.
Todo depende de dónde acabes trabajando, si acabas en un trabajo en arquitecturas de altas prestaciones, en las que haya
que hacer código muy
concreto y optimizado, verás cosas muy avanzadas que igual ni se parecen a esto que estás usando ahora. Por el
contrario, si
acabas en otro tipo de trabajo, quizá incluso no uses java. O igual hasta uses un producto interno de la empresa que
poca gente sepa
usar. O si te quedas en la universidad, esto sólo lo verás en la teoría.

Lo que digo es, sigue así, investiga y aprende, pero no creas que por salir de la universidad sin usar estas cosas o sin
conocerlas, vas mal.
Yo te veo bastante bien, y es más importante esta curiosidad, actitud y ganas de trabajar (junto con un código simple y
entendible) que
el conocimiento técnico que tengas. Lo difícil es lo primero, lo segundo se consigue con más formación y ya.

Si existe alguna forma de refinar la solución o utilizar algún otro concepto de Java, sería de gran ayuda.

## 3. Crear personajes con muchas estadísticas

**¿Qué problema tiene un constructor con muchos parámetros?**

Que es fácil equivocarse con el orden de los parámetros al instanciar. Además es muy poco legible. -> Lo diría al revés,
me preocupa
más la poca legibilidad que equivocarte con un parámetro, sobre todo porque con IDEs comp IntelliJ, es propio IDE te
chiva qué parámetros son.

También, si por el medio no quieres inicializarlo todo... es una pesadez tener que escribir valores nulos por el medio.

**¿Cómo harías para que `new Character(...)` sea legible cuando hay valores por defecto?**

- Seguramente, crearía un constructor canónico (parámetros para todos los atributos) y crearía constructores con
  parámetros concretos para los atributos a los que quiera darles valor. En estos constructores, se delegaría al
  canónico con los valores por defecto, evitando así valores nulos. -> Meh, tener muchso cosntructores tampoco es lo
  mejor,
  es más código para sólo caqsos concretos... el builder te da esto, con menos código, y más legible. Yo uso mucho los
  builder.

- A lo mejor podría hacerse algo con los setter, pero no creo que sea la mejor opción. -> Exacto, aunque esto está más
  cerca de
  la idea de builder

**¿Qué patrón permite construir objetos complejos paso a paso?**

El patrón Builder, el cual Lombok implementa fenomenal con la anotación @Builder ;). Yess yo uso esa siempre, pero no
está mal
saber lo que crea por detrás.

### Cómo lo implementaría

Seguramente, con @Builder XD. Es broma, plantearía una ClaseBuilder con un método para cada atributo de la clase. Al
final, tendría un .build(), que me devolvería una instancia con los atributos inicializados a los valores que he ido
definiendo método a método. -> Si. COmo dije en clase, antes de dar estas clases de patrones nunca había implementado
uno a mano,
pero está bien aprender cómo es y no confiar ciegamente en anotaciones. Seguiré usando la anotación, pero ahora con más
conocimiento cobre ella. Y si algún día no pueod usar lombok, puedo seguir teniendo mis builders :)

Esto mejora ampliamente la legibilidad y flexibilidad en la construcción de objetos, al ser mucho más expresivo y no
forzar a poner valores por defecto o nulos en ciertos parámetros de los constructores. -> Exacto

## 4. Un único almacén de batallas

**¿Qué pasaría si dos clases crean su propio BattleRepository sin el static?**

Que tendrían instancias distintas del repositorio (sin el static, `battles` es atributo de instancia, no de clase), y,
por tanto, no tendrían la misma información. -> No tener le mimsa información en todas las instancias (bbdd, aplicación,
logs...)
es un problema grande.

**¿Cómo asegurar que toda la aplicación use la misma instancia de almacenamiento?**

Controlando la creación de instancias de `BattleRepository` a nivel global, asegurando que solo se pueda crear 1 y dando
acceso global a la misma.

**¿Qué patrón garantiza una única instancia de una clase?**

El patrón Singleton

### Razonamiento de la solución

Se me ocurren varias implementaciones del Singleton, aunque todas pasan por hacer el constructor privado, cada una con
sus pros y sus contras: -> El constructor privado siempre, porque si no, se pierde la gracia del singleton, bien.

- Implementación temprana por atributo estático: Consiste en que `BattleRepository` sea un atributo estático (y por
  tanto pre-inicializado en la definición de la clase). Es más directo y no requiere instanciar, pero puede resultar
  innecesario si al final la clase no se usa. En nuestro contexto, esto no es una desventaja -> Exacto. Pero de nuevo,
  patrón =
  comunicación. Puedes hacer esto, pero una clase que en el UMl tenga un método getInstance() ya me dice mucha
  información
  sobre ella sin necesidad siguiera de entrar a la clase a leerla.

- Implementación perezosa con getInstance: Es la implementación "clasica"; `BattleRepository` tendría un atributo no
  estático llamado instance. Este atributo se inicializa con un método estático público llamado getInstance(),
  devolviendo la única instancia recién creada cuando se le llame. En el resto de llamadas, simplemente retorna la
  variable inicializada previamente en la primera llamada. -> Ajá
  Esta implementación requiere una instancia al no ser estática, pero retrasa la creación del objeto hasta su uso real.
  Además, también es algo más flexible al permitir elegir la implementación del Map por parámetro (lo cual - creo - no
  se debería hacer en un Singleton y por eso no figura como desventaja en la otra implementación)

Ahora visto en una tabla (cortesía de Gemini):

### Comparativa de Implementaciones Singleton

| Implementación                                                                              | Pros (Ventajas)                                                                                                                                                                                                                                                                  | Contras (Desventajas)                                                                                                                                                                                                      |
|:--------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Inicialización Temprana (Eager)**<br>`private static final Clase instance = new Clase();` | - **Thread-safe por defecto:** La JVM garantiza que la instancia se cree antes de que cualquier hilo acceda a ella.<br>- **Simplicidad:** Código muy limpio y directo.<br>- **Alto rendimiento:** No requiere bloqueos ni comprobaciones `if` cada vez que se pide la instancia. | - **Uso de recursos:** La instancia se crea al cargar la clase, aunque nunca se llegue a utilizar.<br>- **Rigidez:** No permite manejar excepciones del constructor de forma sencilla (fuera de bloques `static`).         |
| **Inicialización Perezosa (Lazy)**<br>`if (instance == null) { instance = new Clase(); }`   | - **Eficiencia de recursos:** Solo ocupa memoria si realmente se solicita la instancia.<br>- **Flexibilidad:** Permite lógica adicional o manejo de errores (`try-catch`) durante la creación.                                                                                   | - **Riesgo en multihilo:** Sin sincronización, dos hilos podrían crear dos instancias distintas.<br>- **Complejidad/Rendimiento:** Hacerlo seguro (con `synchronized`) añade una penalización de velocidad en cada acceso. |

No dejes que Gemini piense por tí. Piensa tú: Piensa en si usas hilos (no), en si se entiende el código, en si te
interesa tener un método que poder personalizar, en lo fácil que podría ser testear tu clase (Un método se puede mockear
más fácil, por ejemplo)...
Y que Gemini te ponga "Códig muy limpio"... xD Eso es algo subjetivo, no te fíes de ello. Yo pienso que es más limpio lo
segundo, pero eso está basado en mis gustos y experiencia :)

## 5. Recibir datos de un API externo

**¿Qué problema hay en poner la lógica de conversión en el controller?**

Que acoplas el controlador a la lógica de conversión, que puede cambiar y da al controlador otro motivo para ser
modificado, violando el SRP. -> Ajá

**¿Cómo aislar la conversión "formato externo → nuestro dominio" para no ensuciar el controller?**

En una clase aparte que se encargue específicamente de eso; un envoltorio que contenga el objeto en formato externo y
exponga la funcionalidad de la forma que espera nuestro dominio. -> Bien

**¿Qué patrón permite que un objeto "adaptado" se use como si fuera uno de los nuestros?**

El patrón Adapter

### Cómo lo implementaría

Ya he dado pistas antes. Crearía una clase que contenga el body con el formato del proveedor externo y lo parsee al
formato que utiliza nuestro dominio.

Quizás también se pueda encapsular esta lógica en una Utility Class (o servicio estático), puesto que al final es un
mero parseo, pero tendría que analizar bien las implicaciones de esta opción frente a una clase que contenga el body a
adaptar como atributo.

A mí y a otra gente de la cual aprendí mucho, no nos gustan las clases de utilidad así en general. Cualquier clase puede
verse como un
Helper o un Utils. Adaper está bien, y si lo tratas como un DTO, te ahorras el mapa para convertir campos (de momento,
si aparece un
formato nuevo, hay que darle una vuelta)

## 6. Notificar cuando ocurre daño

**¿Qué pasa si añades 5 "suscriptores" más? ¿Cuántas líneas tocarías en `applyDamage()`?**

Tendrías que notificar a todos manualmente en el flujo de `applyDamage()`, y eso no es nada deseable. -> Porque sería
una responsabilidad
que no le toca a un método como ese, que por cierto, ya hace más cosas de las que debería.

**¿Cómo desacoplar "ejecutar ataque" de "notificar a quien le interese"?**

Utilizando ¿herencia por composición? -> No entiendo, de forma que `BattleService` tenga una instancia de notificador
que almacene los
objetos que desean ser notificados para hacerles llegar la información del evento y puedan tratarla.

-> Un patrón observer, tener la lista de susbcribers y simplemente rellenarlos y hacer el notify cuando toca. No hace
falta complicarlo más

**¿Qué patrón permite que varios objetos reaccionen a un evento sin que el emisor los conozca?**

El patrón Observer (o Publisher-Subscriber)

### Cómo lo implementaría

Ya he aplicado este patrón alguna vez, aunque creo que no correctamente. Crearía una interfaz Publisher y otra
Subscriber. La clase que ejecuta el evento del cual se quiere notificar tendrá un componente por composición de la
interfaz Publisher que se encargue de notificar a todos los Susbcribers que tenga almacenados en una estructura de
datos cualquiera (normalmente una lista). -> Exacto, una simple lista, no hace falta que lo llames "componente por
composición".
Simplifica tu lenguaje, comunicación eficiente. Este lenguaje es muy académico.

Este patrón me interesa especialmente, por lo que cuando pueda me lanzaré a implementarlo y a revisar la solución para
comparar sensaciones. -> A tope

## 7. Deshacer el último ataque

**¿Qué tendrías que cambiar para poder "deshacer"?**

Seguramente toque encapsular toda la información de un ataque, así como el estado previo de los implicados, de forma
que se pueda revertir si quisiera. -> Eso es

**¿Cómo encapsular una acción (ataque) para poder ejecutarla, guardarla y revertirla?**

Em... ¿aplicando el patrón Command? :) -> Siiii, dándole toda la información para hacer el comportamiento, y deshacerlo.
También
Te tocará añadir algún otor método a otras clases. Y una nueva llamada GET en el controller.

**¿Qué patrón trata las acciones como objetos de primera clase?**

El patrón Command.

### Cómo lo implementaría

Buena pregunta. Le tengo un amor-odio a este patrón. Sé que pasaría por una interfaz, pero no tengo clara la
distribución de responsabilidades en este comando en concreto. Me pondré con ello cuando pueda.

# 8. Simplificar la API del combate

**¿Qué problema hay en exponer muchos detalles internos a quien solo quiere "hacer un ataque"?**

Que las cosas se acoplan demasiado y no se están respetando principios del paradigma del OOP como la encapsulación y
abstracción.

**¿Qué patrón ofrece una interfaz simple que oculta la complejidad del subsistema?**

El patrón facade

### Cómo lo implementaría

Es bastante similar al Adapter. Crearía una clase que sirva como "puerta de entrada" y que contenga todo lo necesario
para ejecutar un ataque, exponiendo métodos sencillos y orquestando por detrás toda la complejidad. Me gustaría
implementarlo más adelante. -> Yees

## 9. Ataques compuestos (combo)

**¿Cómo representar "un ataque que son varios ataques"?**

El propio combo es un ataque que tiene un conjunto de ataques en sí mismo, y el cálculo del daño se realiza en función
del cálculo de todos los ataques que lo forman.

**¿Qué patrón permite tratar un grupo de objetos igual que un objeto individual?**

El patrón composite -> Siii

### Cómo lo implementaría

No lo he implementado nunca y la verdad es que me gustaría mucho. Creo que Attack tendría una lista objetos Attack como
atributo, y el cálculo del daño del ataque debería agregar los cálculos individuales de todos los que lo forman.

-> Sí. Aquí hay una forma de hacerlo que te simplificaría mucho la vida. Tiene que ver con cómo creas y nombras
la interfaz. A ver si lo descubres.