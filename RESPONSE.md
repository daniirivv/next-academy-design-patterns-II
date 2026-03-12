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