package tech.erben.java17.photopdf.web;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tech.erben.java17.photopdf.model.ColorMode;
import tech.erben.java17.photopdf.model.PrintOptions;
import tech.erben.java17.photopdf.service.PhotoRenderService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/")
public class PhotoPrintController {

    private final PhotoRenderService photoRenderService;

    public PhotoPrintController(PhotoRenderService photoRenderService) {
        this.photoRenderService = photoRenderService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("colorModes", ColorMode.values());
        return "index";
    }

    @PostMapping("/print")
    public ResponseEntity<byte[]> renderPdf(@RequestParam("file") MultipartFile file,
                                            @RequestParam(value = "mode", defaultValue = "COLOR") String mode,
                                            @RequestParam(value = "requestedBy", required = false) String requestedBy,
                                            @RequestParam(value = "annotation", required = false) String annotation) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bitte ein Bild auswählen.");
        }

        ColorMode colorMode = parseMode(mode);
        PrintOptions printOptions = new PrintOptions(colorMode, requestedBy, annotation);

        try {
            byte[] pdf = photoRenderService.renderPdf(file, printOptions);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(buildOutputName(file, colorMode), StandardCharsets.UTF_8)
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Konnte PDF nicht erzeugen.", e);
        }
    }

    private ColorMode parseMode(String mode) {
        try {
            return ColorMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ColorMode.COLOR;
        }
    }

    private String buildOutputName(MultipartFile file, ColorMode mode) {
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "foto";
        int dot = originalFilename.lastIndexOf('.');
        String baseName = dot > 0 ? originalFilename.substring(0, dot) : originalFilename;
        return baseName + "-" + mode.name().toLowerCase() + ".pdf";
    }
}
