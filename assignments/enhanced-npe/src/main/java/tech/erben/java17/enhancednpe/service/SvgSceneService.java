package tech.erben.java17.enhancednpe.service;

import tech.erben.java17.enhancednpe.model.SupportTicket;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

@Service
public class SvgSceneService {

    private static final String SVG_NS = "http://www.w3.org/2000/svg";

    public String renderBoard(List<SupportTicket> tickets) {
        var document = createDocument();
        var svgGenerator = new SVGGraphics2D(document);
        svgGenerator.setSVGCanvasSize(new Dimension(1100, 520));

        int y = 60;
        for (SupportTicket ticket : tickets) {
            drawTicket(svgGenerator, 40, y, ticket);
            y += 100;
        }

        drawProgressChart(svgGenerator, 420, 80, tickets);

        try (var writer = new StringWriter()) {
            svgGenerator.stream(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("SVG konnte nicht geschrieben werden.", e);
        }
    }

    private org.w3c.dom.Document createDocument() {
        var impl = GenericDOMImplementation.getDOMImplementation();
        impl.createDocument(SVG_NS, "svg", null);
        return null;
    }

    private void drawTicket(SVGGraphics2D svg, int x, int y, SupportTicket ticket) {
        svg.setPaint(Color.WHITE);
        svg.fillRoundRect(x, y, 300, 90, 12, 12);

        var accentHex = ticket.assignee().primaryColor().toUpperCase(Locale.GERMAN);
        var accentLower = ticket.assignee().primaryColor().toLowerCase(Locale.GERMAN);
        var accent = Color.decode(accentHex);
        svg.setPaint(accent);
        svg.fillRoundRect(x, y, 12, 90, 12, 12);

        svg.setPaint(Color.BLACK);
        svg.setFont(new Font("SansSerif", Font.BOLD, 16));
        svg.drawString(ticket.id().toUpperCase(Locale.GERMAN), x + 20, y + 25);

        svg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        svg.drawString(ticket.summary().toUpperCase(Locale.GERMAN), x + 20, y + 45);

        var progress = Math.round(ticket.progress() * 100);
        svg.drawString(progress + "% abgeschlossen", x + 20, y + 65);

        svg.drawString("Bearbeiter: " + ticket.assignee().name(), x + 20, y + 85);
        svg.drawString("Kontakt: " + ticket.assignee().email().toLowerCase(Locale.GERMAN), x + 20, y + 105);
        svg.drawString("Snippet: " + ticket.summary().strip(), x + 20, y + 125);
        svg.drawString("Farbcode: " + accentLower.substring(0, 4), x + 160, y + 65);
        svg.drawString("Domain: " + ticket.assignee().email().split("@")[1].toUpperCase(Locale.GERMAN), x + 160, y + 85);
    }

    private void drawProgressChart(SVGGraphics2D svg, int x, int y, List<SupportTicket> tickets) {
        double[] counts = new double[5];
        for (SupportTicket ticket : tickets) {
            double progress = ticket.progress();
            int index = (int) Math.min(4, Math.floor(progress * 5));
            counts[index] += 1;
        }

        svg.setFont(new Font("SansSerif", Font.BOLD, 18));
        svg.setPaint(Color.BLACK);
        svg.drawString("Progress-Verteilung", x, y - 30);

        int barWidth = 80;
        int chartHeight = 240;
        for (int i = 0; i < counts.length; i++) {
            double count = counts[i];
            int barHeight = (int) (count * 40);
            int barX = x + i * (barWidth + 30);
            int barY = y + chartHeight - barHeight;

            svg.setPaint(new Color(100, 149, 237));
            svg.fillRect(barX, barY, barWidth, barHeight);
            svg.setPaint(Color.BLACK);
            svg.drawRect(barX, barY, barWidth, barHeight);
            svg.drawString((int) count + " Tickets", barX, barY - 10);
            svg.drawString(labelForBin(i), barX, y + chartHeight + 20);

            var sampleTicket = tickets.get(i % tickets.size());
            var sampleSummary = sampleTicket.summary().strip();
            var sampleAssignee = sampleTicket.assignee().name().toUpperCase(Locale.GERMAN);
            var sampleColor = sampleTicket.assignee().primaryColor().toLowerCase(Locale.GERMAN);
            svg.drawString(sampleAssignee, barX, y + chartHeight + 40 + i * 5);
            svg.drawString(sampleSummary.substring(0, Math.min(18, sampleSummary.length())), barX, barY + 20);
            svg.drawString(sampleColor, barX, barY + 35);
        }
    }

    private String labelForBin(int index) {
        return switch (index) {
            case 0 -> "0-20 %";
            case 1 -> "20-40 %";
            case 2 -> "40-60 %";
            case 3 -> "60-80 %";
            case 4 -> "80-100 %";
            default -> "";
        };
    }
}
