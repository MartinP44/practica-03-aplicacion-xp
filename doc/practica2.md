# Documentación Técnica de la Práctica

## Introducción

Esta documentación técnica describe la implementación de una serie de funcionalidades desarrolladas siguiendo historias de usuario, así como la integración de herramientas de gestión de proyectos y control de versiones. El objetivo de la práctica fue implementar una **barra de menú** global, el **listado de usuarios** registrados, la **descripción de usuario** y la **página “Acerca de”**, utilizando Spring Boot, Thymeleaf y Bootstrap. Además, se documenta el uso de **GitHub Projects**, **Trello Kanban** e **implementación de ramas** en GitHub para cada historia de usuario, así como la creación de pruebas unitarias para asegurar la calidad del código.

## Arquitectura de la Aplicación

La aplicación sigue una arquitectura MVC (Modelo-Vista-Controlador) clásica. En la capa de **controladores** (`UsuarioController.java`) se exponen las rutas HTTP, en la capa de **servicios** (`UsuarioService.java`) se encapsula la lógica de negocio, y en la carpeta `templates` se encuentran las **vistas** Thymeleaf que representan el UI. Se empleó **ModelMapper** para convertir entidades JPA en DTOs (`UsuarioData`), y **Spring Data JPA** para acceso a datos. Para el estilo, se utilizó **Bootstrap** mediante fragmentos Thymeleaf, garantizando consistencia visual.

## Capa de Controlador (`UsuarioController.java`)

El controlador `UsuarioController` gestiona las peticiones relacionadas con los usuarios registrados:

```java
@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/registrados")
    public String listadoUsuarios(Model model) {
        List<UsuarioData> usuarios = usuarioService.allUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listadoUsuarios";
    }

    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable("id") Long id, Model model) {
        UsuarioData usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }
}
```

- ``: recupera todos los usuarios del servicio y los añade al modelo.
- ``: busca un usuario por su identificador y lo envía a la vista.

## Capa de Servicio (`UsuarioService.java`)

En la capa de servicios se agregó el método `allUsuarios()` para obtener todos los registros:

```java
@Transactional(readOnly = true)
public List<UsuarioData> allUsuarios() {
    Iterable<Usuario> usuarios = usuarioRepository.findAll();
    return StreamSupport.stream(usuarios.spliterator(), false)
        .map(usuario -> modelMapper.map(usuario, UsuarioData.class))
        .collect(Collectors.toList());
}
```

- Se marca la transacción como **solo lectura**.
- Se convierte el `Iterable` del repositorio en un `Stream` y luego en una `List` de DTOs.
- El uso de `modelMapper` agiliza la conversión entidad–DTO.

## Implementación de Vistas

Se definieron cuatro plantillas Thymeleaf en `src/main/resources/templates`:

1. **about.html**: Página “Acerca de” con información estática de la aplicación.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments :: head (titulo='Acerca de')"></head>
<body>
  <div th:replace="navbar :: navbar"></div>
  <div class="container-fluid">
    <h1>ToDoList</h1>
    <ul>
      <li>Desarrollada por Martín Posso</li>
      <li>Versión 1.1.0</li>
      <li>Fecha de release: 10/06/2025</li>
    </ul>
  </div>
  <div th:replace="fragments::javascript" />
</body>
</html>
```

2. **navbar.html**: Fragmento común de navegación, controla enlaces según la sesión.

```html
<nav th:fragment="navbar" class="navbar navbar-expand-lg navbar-light bg-light">
  <!-- enlaces dinámicos según session.usuario -->
</nav>
```

3. **listadoUsuarios.html**: Tabla con usuarios y botón de descripción.

```html
<tr th:each="usuario : ${usuarios}">
  <td th:text="${usuario.id}"></td>
  <td th:text="${usuario.email}"></td>
  <td th:text="${usuario.nombre}"></td>
  <td>
    <a th:href="@{/registrados/{id}(id=${usuario.id})}" class="btn btn-primary btn-sm">Descripción</a>
  </td>
</tr>
```

4. **descripcionUsuario.html**: Muestra detalles de un usuario o un mensaje de error.

```html
<div th:if="${usuario == null}">
  <p class="text-danger">Usuario no encontrado.</p>
</div>
```

## Pruebas Unitarias

Se implementaron tests con **JUnit 5** y **Mockito** para validar:

- ``: comprueba mapeo y tamaño de la lista.
- ``:
  - `listadoUsuarios` añade `usuarios` al modelo y retorna la vista correcta.
  - `descripcionUsuario` añade el `usuario` al modelo y retorna su plantilla.
- **Patrón GIVEN–WHEN–THEN** en cada test para claridad.

Estos tests aseguran que la lógica de negocio y las rutas de presentación se comporten según lo esperado.

## Gestión de Proyecto y Control de Versiones

Para el seguimiento de las historias de usuario se crearon **issues** en GitHub, vinculados a **GitHub Projects** y sincronizados con un **tablero Kanban en Trello**. Cada historia se desarrolló en una rama independiente, lo que facilitó las revisiones de código y la integración continua. Una vez aprobados los cambios, se realizó **merge** a la rama principal, garantizando un historial limpio y modular.

---

*Fin de la documentación técnica.*

