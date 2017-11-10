package org.korz.pygments;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PygmentsTest {
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

    private static void assertHighlight(Pygments p,
                                        String inFile,
                                        String outFile) {
        String actual = p.highlight(readResource(inFile));
        String expected = readResource(outFile);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test() {
        Pygments p = Pygments.newBuilder()
            .setLexerName("c")
            .setFormatterName("html")
            .build();
        assertHighlight(p, "samples/main.c", "samples/main.c.html");
    }

    @Test
    public void testFormatterOptions() {
        Pygments p = Pygments.newBuilder()
            .setLexerName("c")
            .setFormatterName("html")
            .setFormatterOption("linenos", "table")
            .build();
        assertHighlight(p, "samples/main.c", "samples/main.c.table.html");
    }

    @Test
    public void testUnknownLexer() {
        try {
            Pygments.newBuilder()
                .setLexerName("poopies")
                .setFormatterName("html")
                .build();
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

    @Test
    public void testUnknownFormatter() {
        try {
            Pygments.newBuilder()
                .setLexerName("c")
                .setFormatterName("poopies")
                .build();
            fail();
        }
        catch (IllegalArgumentException e) {}
    }
}
