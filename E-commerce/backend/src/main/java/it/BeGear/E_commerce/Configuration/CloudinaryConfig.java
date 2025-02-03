package it.BeGear.E_commerce.Configuration;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        // Specifica il percorso completo del file .env
        Dotenv dotenv = Dotenv.configure()
                .directory("C:/Users/Pietro/Documents/Progetti Java/E-commerce/E-commerce")
                .load();  // Carica il file .env

        // Restituisce l'oggetto Cloudinary utilizzando la variabile di ambiente
        return new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }
}
