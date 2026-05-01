package foodstore_backend.config;

import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.Rol;
import foodstore_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Seeder que crea un usuario administrador por defecto si no existe
@Component
public class UserLoad implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Verifica si ya existe el admin por email
        if (usuarioRepository.findByEmailAndEliminadoFalse("admin@admin.com").isEmpty()) {

            Usuario admin = new Usuario();

            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail("admin@admin.com");
            admin.setCelular("0000000000");

            // Se encripta la contraseña con BCrypt
            admin.setPassword(passwordEncoder.encode("123456"));

            admin.setRol(Rol.ADMIN);
            admin.setEliminado(false);

            usuarioRepository.save(admin);

            System.out.println("Administrador por defecto creado: admin@admin.com");
        }
    }
}