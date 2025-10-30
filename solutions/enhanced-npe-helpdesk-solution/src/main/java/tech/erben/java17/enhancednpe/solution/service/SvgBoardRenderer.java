package tech.erben.java17.enhancednpe.solution.service;

import tech.erben.java17.enhancednpe.solution.model.Agent;
import tech.erben.java17.enhancednpe.solution.model.SupportTicket;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SvgBoardRenderer {

    private static final String SVG_NS = "http://www.w3.org/2000/svg";

    public String renderBoard(List<SupportTicket> tickets) {
        Document document = createDocument();
        var svg = new SVGGraphics2D(document);
        svg.setSVGCanvasSize(new Dimension(1100, 520));

        int y = 60;
        for (SupportTicket ticket : tickets) {
            drawTicket(svg, 40, y, ticket);
            y += 100;
        }

        drawProgressChart(svg, 420, 80, tickets);

        try (var writer = new StringWriter()) {
            svg.stream(writer, true);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("SVG konnte nicht erzeugt werden", e);
        }
    }

    private Document createDocument() {
        var impl = GenericDOMImplementation.getDOMImplementation();
        return impl.createDocument(SVG_NS, "svg", null);
    }

    private void drawTicket(SVGGraphics2D svg, int x, int y, SupportTicket ticket) {
        var agent = Optional.ofNullable(ticket.assignee()).orElse(Agent.fallback());
        var summary = Optional.ofNullable(ticket.summary())
                .filter(s -> !s.isBlank())
                .orElse("Kein Betreff angegeben");
        var progress = Optional.ofNullable(ticket.progress()).orElse(0.0);
        var accent = parseColor(agent.primaryColor());
        var accentLower = safeLower(agent.primaryColor());
        var ticketId = Optional.ofNullable(ticket.id()).orElse("Ohne-ID");
        var contactDomain = Optional.ofNullable(agent.email())
                .filter(email -> email.contains("@"))
                .map(email -> email.substring(email.indexOf('@') + 1))
                .orElse("n/a");
        var emailLower = Optional.ofNullable(agent.email())
                .map(email -> email.toLowerCase(Locale.GERMAN))
                .orElse("kein Kontakt");
        var summarySnippet = summary.strip();

        svg.setPaint(Color.WHITE);
        svg.fillRoundRect(x, y, 300, 90, 12, 12);

        svg.setPaint(accent);
        svg.fillRoundRect(x, y, 12, 90, 12, 12);

        svg.setPaint(Color.BLACK);
        svg.setFont(new Font("SansSerif", Font.BOLD, 16));
        svg.drawString(ticketId.toUpperCase(Locale.GERMAN), x + 20, y + 25);

        svg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        svg.drawString(summary, x + 20, y + 45);

        var progressText = "%.0f%% abgeschlossen".formatted(progress * 100);
        svg.drawString(progressText, x + 20, y + 65);

        svg.drawString("Bearbeiter: " + agent.name(), x + 20, y + 85);
        svg.drawString("Kontakt: " + emailLower, x + 20, y + 105);
        svg.drawString("Snippet: " + summarySnippet, x + 20, y + 125);
        svg.drawString("Farbcode: " + accentLower, x + 160, y + 65);
        svg.drawString("Domain: " + contactDomain, x + 160, y + 85);
    }

    private Color parseColor(String hex) {
        if (hex == null || hex.isBlank()) {
            return Color.decode(Agent.fallback().primaryColor());
        }
        try {
            return Color.decode(hex);
        } catch (NumberFormatException ex) {
            return Color.decode(Agent.fallback().primaryColor());
        }
    }

    private void drawProgressChart(SVGGraphics2D svg, int x, int y, List<SupportTicket> tickets) {
        double[] counts = new double[5];
        for (SupportTicket ticket : tickets) {
            double progress = Optional.ofNullable(ticket.progress()).orElse(0.0);
            progress = Math.max(0.0, Math.min(progress, 0.999));
            int index = (int) Math.floor(progress * 5);
            counts[index] += 1;
        }

        svg.setFont(new Font("SansSerif", Font.BOLD, 18));
        svg.setPaint(Color.BLACK);
        svg.drawString("Progress-Verteilung", x, y - 30);

        int barWidth = 80;
        int chartHeight = 240;
        for (int i = 0; i < counts.length; i++) {
            double count = counts[i];
            int barHeight = (int) Math.round(count * 40);
            int barX = x + i * (barWidth + 30);
            int barY = y + chartHeight - barHeight;

            svg.setPaint(new Color(100, 149, 237));
            svg.fillRect(barX, barY, barWidth, barHeight);
            svg.setPaint(Color.BLACK);
            svg.drawRect(barX, barY, barWidth, barHeight);
            svg.drawString((int) count + " Tickets", barX, barY - 10);
            svg.drawString(labelForBin(i), barX, y + chartHeight + 20);

            if (!tickets.isEmpty()) {
                var sampleTicket = tickets.get(i % tickets.size());
                var sampleSummary = Optional.ofNullable(sampleTicket.summary()).orElse("Leerer Betreff").strip();
                var sampleAssignee = Optional.ofNullable(sampleTicket.assignee()).orElse(Agent.fallback());
                var sampleName = sampleAssignee.name().toUpperCase(Locale.GERMAN);
                var sampleColor = safeLower(sampleAssignee.primaryColor());
                svg.drawString(sampleName, barX, y + chartHeight + 40 + i * 5);
                svg.drawString(sampleSummary.substring(0, Math.min(18, sampleSummary.length())), barX, barY + 20);
                svg.drawString(sampleColor, barX, barY + 35);
            }
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

    private String safeLower(String value) {
        return Optional.ofNullable(value)
                .filter(v -> !v.isBlank())
                .map(v -> v.toLowerCase(Locale.GERMAN))
                .orElse(Agent.fallback().primaryColor());
    }
}
