package it.sdc.restserver.controller;

import it.sdc.restserver.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/modules")
@CrossOrigin(origins = "http://localhost:4200")
public class ModulesController {

        private final PdfService pdfService;

        public ModulesController(PdfService pdfService) {
                this.pdfService = pdfService;
        }

        @GetMapping("/generate-pdf")
        public void generatePdf(@RequestParam(required = false) String name,
                        @RequestParam(required = false) String surname,
                        @RequestParam(required = false) String jobTitle,
                        HttpServletResponse response) throws IOException {

                response.setContentType("application/pdf");
                String fileName = "module";
                if (surname != null && !surname.trim().isEmpty())
                        fileName += "-" + surname;
                if (name != null && !name.trim().isEmpty())
                        fileName += "-" + name;
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

                pdfService.generatePdf(response.getOutputStream(), name, surname, jobTitle);
        }
}
