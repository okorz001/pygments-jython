package org.korz.pygments;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PygmentsContextTest {
    private static String readResource(String filename) {
        try {
            return Resources.toString(Resources.getResource(filename),
                                      StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(
                "Cannot read resource: " + filename, e);
        }
    }

    private static void assertHighlight(PygmentsContext p,
                                        String inFile,
                                        String outFile) {
        String actual = p.highlight(readResource(inFile));
        String expected = readResource(outFile);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void highlight() {
        PygmentsContext p = PygmentsContext.newContext()
            .setLexerName("c")
            .setFormatterName("html")
            .build();
        assertHighlight(p, "samples/main.c", "samples/main.c.html");
    }

    @Test
    public void formatterOptions() {
        PygmentsContext p = PygmentsContext.newContext()
            .setLexerName("c")
            .setFormatterName("html")
            .setFormatterOption("linenos", "table")
            .build();
        assertHighlight(p, "samples/main.c", "samples/main.c.table.html");
    }

    @Test
    public void lexerOptions() {
        PygmentsContext p = PygmentsContext.newContext()
            .setLexerName("text")
            .setLexerOption("ensurenl", false)
            .setLexerOption("tabsize", 4)
            .setFormatterName("text")
            .build();
        assertThat(p.highlight("a\tb"), equalTo("a   b"));
    }

    @Test
    public void withInstances() {
        PygmentsContext p = PygmentsContext.newContext()
            .setLexer(Lexer.byName("c").build())
            .setFormatter(Formatter.byName("html").build())
            .build();
        assertHighlight(p, "samples/main.c", "samples/main.c.html");
    }

    @Test(expected = NullPointerException.class)
    public void missingLexer() {
        PygmentsContext.newContext().setFormatterName("html").build();
    }

    @Test(expected = NullPointerException.class)
    public void missingFormatter() {
        PygmentsContext.newContext().setLexerName("c").build();
    }

    // PygmentsContext doesn't use this directly any more
    @Test
    public void pygmentsHighlight() {
        String input = readResource("samples/main.c");
        Object lexer = Pygments.getLexer("c",
                                         Collections.emptyMap());
        Object formatter = Pygments.getFormatter("html",
                                                 Collections.emptyMap());
        String output = readResource("samples/main.c.html");
        String actual = Pygments.highlight(input, lexer, formatter);
        assertThat(actual, equalTo(output));
    }
}
