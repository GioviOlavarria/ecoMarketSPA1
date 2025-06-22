# ecoMarketSPA1

## Descripción
Este es un sistema de microservicios desarrollado con Java y Spring Boot, que utiliza varias tecnologías para la gestión de usuarios, productos y compras. El sistema está diseñado para ofrecer una experiencia sencilla de interactuar con los servicios a través de un entorno visual y una API documentada con Swagger.

## Tecnologías Utilizadas
- **Java** 
- **Spring Boot** 
- **MySQL** 
- **Laragon**
- **Spring Security**
- **Spring Boot Starter**
- **Spring Web**
- **Lombok**
- **Mockito**
- **H2 Database**
- **HateOAS**
- **Thymeleaf**
- **JUnit**
- **JPA**
- **Swagger UI**

## Requisitos Previos
- Java 17 o superior
- MySQL (instalado y configurado en Laragon)
- Maven o Gradle para manejar dependencias

## Instalación y Ejecución

1. **Clona el repositorio**:
    ```bash
    git clone https://github.com/tuusuario/tu-repositorio.git
    ```

2. **Base de datos**:
   Al utilizar h2 para un entorno de pruebas, no es necesaria una base de datos. Sin embargo en el proyecto real, se utilizará MySQL, por ende, en `application.properties` debes tener lo siguiente:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/db_ecomarket
    spring.datasource.username=root
    spring.datasource.password=
    ```

3. **Construye el proyecto**:
    Si usas Maven:
    ```bash
    mvn clean install
    ```
    Si usas Gradle:
    ```bash
    gradle build
    ```

4. **Ejecuta el sistema**:
    ```bash
    mvn spring-boot:run
    ```

## Uso del Sistema

### Inicio de Sesión
- Al iniciar el sistema, se te pedirá ingresar credenciales.
- **Credenciales de acceso predeterminadas**:
    - Usuario: `admin`
    - Clave: `clavepulenta`

### Enlaces del Sistema

- **Página principal**:
    [http://localhost:8080/?continue](http://localhost:8080/?continue)
  
- **Gestión de productos**:
    [http://localhost:8080/web/productos](http://localhost:8080/web/productos)
    - Permite crear, editar y borrar productos.

- **Gestión de usuarios**:
    [http://localhost:8080/web/usuarios](http://localhost:8080/web/usuarios)
    - Permite crear, editar y borrar usuarios.

- **Generación de compras**:
    [http://localhost:8080/web/compras](http://localhost:8080/web/compras)
    - Permite generar compras utilizando los productos y usuarios existentes.

### Swagger UI para API
Para explorar y probar los endpoints de la API, utiliza el siguiente enlace:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Contribuciones
Si deseas contribuir al proyecto, sigue estos pasos:
1. Haz un fork del repositorio.
2. Crea una nueva rama: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y haz un commit: `git commit -am 'Añadir nueva funcionalidad'`.
4. Envía tus cambios a tu repositorio remoto.
5. Abre un Pull Request para que tu contribución sea revisada y fusionada.


---

¡Gracias por usar este sistema de microservicios! Si tienes alguna pregunta o necesitas soporte, no dudes en abrir un problema en GitHub.
