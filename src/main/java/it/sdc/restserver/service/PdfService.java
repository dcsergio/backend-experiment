package it.sdc.restserver.service;

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
import it.sdc.restserver.dto.VoucherDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PdfService {

    public static final int LOGO_PT_SIZE = 100;
    public static final float CARD_WIDTH = 242.6f; // ~85.6mm
    public static final float CARD_HEIGHT = 153.0f; // ~53.98mm
    public static final float MARGIN = 30.0f;
    public static final float GUTTER = 20.0f;

    public PdfService() {
    }

    public void generateVouchersPdf(OutputStream out, List<VoucherDto> vouchers) throws IOException {
        PdfFont labelFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont valueFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        try {
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);
            document.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);

            if (vouchers == null || vouchers.isEmpty()) {
                document.add(new Paragraph("No vouchers found.")
                        .setFont(valueFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(200));
            } else {
                float pageWidth = PageSize.A4.getWidth();
                float pageHeight = PageSize.A4.getHeight();

                float currentX = MARGIN;
                float currentY = pageHeight - MARGIN - CARD_HEIGHT;

                for (VoucherDto voucher : vouchers) {
                    // Background and border for the card
                    Table cardTable = new Table(UnitValue.createPercentArray(new float[] { 100 }))
                            .useAllAvailableWidth()
                            .setBorder(new SolidBorder(new DeviceRgb(52, 152, 219), 1f))
                            .setBackgroundColor(new DeviceRgb(248, 249, 250))
                            .setPadding(10);

                    cardTable.addCell(new Cell().add(new Paragraph("VOUCHER")
                            .setFont(labelFont)
                            .setFontSize(14)
                            .setFontColor(new DeviceRgb(44, 62, 80))
                            .setTextAlignment(TextAlignment.CENTER))
                            .setBorder(Border.NO_BORDER));

                    String code = voucher.code() != null ? voucher.code().replaceAll("(.{4})(?!$)", "$1-") : "N/A";
                    cardTable.addCell(new Cell().add(new Paragraph("Code: " + code)
                            .setFont(valueFont)
                            .setFontSize(12)
                            .setMarginTop(10))
                            .setBorder(Border.NO_BORDER));

                    String amountStr = voucher.amount() != null ? String.format("%.2f", voucher.amount()) : "0.00";
                    cardTable.addCell(new Cell().add(new Paragraph("Amount: €" + amountStr)
                            .setFont(valueFont)
                            .setFontSize(12))
                            .setBorder(Border.NO_BORDER));

                    String signature = voucher.signature() != null ? voucher.signature() : "N/A";
                    cardTable.addCell(new Cell().add(new Paragraph("PIN: " + signature)
                            .setFont(valueFont)
                            .setFontSize(10)
                            .setFontColor(new DeviceRgb(127, 140, 141))
                            .setMarginTop(10))
                            .setBorder(Border.NO_BORDER));

                    // Position table on page
                    cardTable.setFixedPosition(currentX, currentY, CARD_WIDTH);
                    document.add(cardTable);

                    // Update positions for next card
                    currentX += CARD_WIDTH + GUTTER;
                    if (currentX + CARD_WIDTH > pageWidth - MARGIN) {
                        currentX = MARGIN;
                        currentY -= CARD_HEIGHT + GUTTER;
                    }

                    if (currentY < MARGIN) {
                        document.add(new AreaBreak());
                        currentX = MARGIN;
                        currentY = pageHeight - MARGIN - CARD_HEIGHT;
                    }
                }
            }
            document.close();
        } finally {
            if (!pdfDoc.isClosed()) {
                pdfDoc.close();
            }
        }
    }

    public void generatePdf(OutputStream out, String name, String surname, String jobTitle) throws IOException {
        PdfFont labelFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont valueFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        try {
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
            Table table = new Table(UnitValue.createPercentArray(new float[] { 30, 70 }));
            table.useAllAvailableWidth();
            table.setMarginBottom(10);

            // Get or create form
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            // Name field
            addFieldRow(table, "Name:", name, form, pdfDoc, 0, labelFont, valueFont);

            // Surname field
            addFieldRow(table, "Surname:", surname, form, pdfDoc, 1, labelFont, valueFont);

            // Job Title field
            addFieldRow(table, "Job Title:", jobTitle, form, pdfDoc, 2, labelFont, valueFont);

            document.add(table);
            form.flattenFields();
            document.close();
        } finally {
            if (!pdfDoc.isClosed()) {
                pdfDoc.close();
            }
        }
    }

    private void addFieldRow(Table table, String label, String value, PdfAcroForm form, PdfDocument pdfDoc,
            int rowIndex, PdfFont labelFont, PdfFont valueFont) {
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
