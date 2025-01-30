package it.BeGear.E_commerce.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // Costruttore che carica la configurazione di Cloudinary
    public CloudinaryService() {
        // Carica il file .env
        Dotenv dotenv = Dotenv.load();

        // Prendi il valore della variabile di ambiente CLOUDINARY_URL
        String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

        // Inizializza Cloudinary con l'URL
        this.cloudinary = new Cloudinary(cloudinaryUrl);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("resource_type", "auto");

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new IOException("Errore durante l'upload dell'immagine", e);
        }
    }

    public void deleteImage(String imageUrl) throws IOException {
        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            Map<String, String> options = new HashMap<>();
            options.put("resource_type", "image");

            cloudinary.uploader().destroy(publicId, options);
        } catch (IOException e) {
            throw new IOException("Errore durante l'eliminazione dell'immagine", e);
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] urlParts = imageUrl.split("/");
        String fileName = urlParts[urlParts.length - 1];
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
