package foodstore_backend.dto;

import foodstore_backend.model.enums.Rol;

// DTO de respuesta para login
public class LoginResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private String mensaje;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Long id, String nombre, String apellido, String email, Rol rol, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {
        return rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
