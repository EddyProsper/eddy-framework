package mg.eddy.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.servlet.http.Part;

import mg.eddy.annotation.MyController;
import mg.eddy.annotation.URLAnnotation;
import mg.eddy.mapping.HTTPMethod;
import mg.eddy.util.ModelView;

@MyController
public class UploadController {
    private static final long MAX_FILE_SIZE = 5L * 1024L * 1024L;

    @URLAnnotation(value = "/files/upload", httpMethod = HTTPMethod.POST)
    public ModelView upload(Part file) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Le fichier dépasse la limite de 5 Mo.");
        }

        String fileName = safeFileName(file.getSubmittedFileName());
        Path uploadDirectory = Paths.get(System.getProperty("java.io.tmpdir"), "eddy-uploads");
        Files.createDirectories(uploadDirectory);
        Path destination = uploadDirectory.resolve(UUID.randomUUID().toString() + "-" + fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return new ModelView("status/success.jsp")
                .add("title", "Sprint 8 — Fichier uploadé")
                .add("message", fileName + " a été enregistré avec succès (" + file.getSize() + " octets). ");
    }

    private String safeFileName(String submittedFileName) {
        if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier est vide.");
        }
        String baseName = Paths.get(submittedFileName).getFileName().toString();
        return baseName.replaceAll("[^\\p{L}\\p{N}._ -]", "_");
    }
}
