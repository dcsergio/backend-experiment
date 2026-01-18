package it.sdc.restserver.controller;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;


@RestController
@RequestMapping(value = "/modules")
@CrossOrigin(origins = "http://localhost:4200")
public class ModulesController {

    public static final int LOGO_PT_SIZE = 100;

    private final PdfFont labelFont;
    private final PdfFont valueFont;

    public ModulesController() throws IOException {
        labelFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        valueFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

    @GetMapping("/generate-pdf")
    public void generatePdf(@RequestParam(required = false) String name,
                            @RequestParam(required = false) String surname,
                            @RequestParam(required = false) String jobTitle,
                            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        String fileName = "module";
        if (surname != null && !surname.trim().isEmpty()) fileName += "-" + surname;
        if (name != null && !name.trim().isEmpty()) fileName += "-" + name;
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Set A4 page size
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);

            // Set document margins (20mm = ~56.7 points)
            document.setMargins(56.7f, 56.7f, 56.7f, 56.7f);

            // Load and position logo at top-left (20mm margins)
            Image logo = new Image(ImageDataFactory.create("src/main/resources/logo.png"));
            logo.setWidth(LOGO_PT_SIZE);
            logo.setHeight(LOGO_PT_SIZE);
            logo.setFixedPosition(56.7f, PageSize.A4.getHeight() - 56.7f - LOGO_PT_SIZE); // 20mm from top and left
            document.add(logo);

            // Create custom fonts
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);


            // Main title with modern styling
            Paragraph title = new Paragraph("YOUR DATA")
                    .setFont(titleFont)
                    .setFontSize(24)
                    .setFontColor(new DeviceRgb(44, 62, 80))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(80)
                    .setMarginBottom(40);
            document.add(title);

            // Decorative line
            LineSeparator line = new LineSeparator(new SolidLine(1.5f));
            line.setStrokeColor(new DeviceRgb(52, 152, 219));
            line.setMarginBottom(40);
            document.add(line);

            // Create a table for better field layout
            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table.useAllAvailableWidth();
            table.setMarginBottom(10);

            // Get or create form
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            // Name field
            addFieldRow(table, "Name:", name, form, pdfDoc, 0);

            // Surname field
            addFieldRow(table, "Surname:", surname, form, pdfDoc, 1);

            // Job Title field
            addFieldRow(table, "Job Title:", jobTitle, form, pdfDoc, 2);

            document.add(table);
            form.flattenFields();
            document.close();
        }
    }

    private void addFieldRow(Table table, String label, String value, PdfAcroForm form, PdfDocument pdfDoc, int rowIndex) {
        // Label cell
        Cell labelCell = new Cell()
                .add(new Paragraph(label)
                        .setFont(labelFont)
                        .setFontSize(12)
                        .setFontColor(new DeviceRgb(52, 73, 94)))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(236, 240, 241))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(labelCell);

        // Check if value is provided
        if (value != null && !value.trim().isEmpty()) {
            // Value provided - create static cell
            Cell valueCell = new Cell()
                    .add(new Paragraph(value)
                            .setFont(valueFont)
                            .setFontSize(12)
                            .setFontColor(new DeviceRgb(44, 62, 80)))
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(new DeviceRgb(255, 255, 255))
                    .setBorderBottom(new SolidBorder(new DeviceRgb(189, 195, 199), 0.5f))
                    .setPadding(12)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            table.addCell(valueCell);
        } else {
            // Value not provided - create empty cell and add editable form field
            Cell valueCell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(new DeviceRgb(255, 255, 255))
                    .setBorderBottom(new SolidBorder(new DeviceRgb(189, 195, 199), 0.5f))
                    .setPadding(12)
                    .setHeight(36)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            table.addCell(valueCell);

            // Calculate position for the form field
            // This is approximate - adjust based on your layout
            float yPosition = PageSize.A4.getHeight() - 260 - (rowIndex * 46);

            // Create editable text field
            String fieldName = label.replace(":", "").toLowerCase().replace(" ", "_");
            PdfFormField textField = new TextFormFieldBuilder(pdfDoc, fieldName)
                    .setWidgetRectangle(new Rectangle(250, yPosition, 300, 30))
                    .createText();

            textField
                    .setValue("")
                    .setFontSize(12);

            form.addField(textField);
        }
    }
}
