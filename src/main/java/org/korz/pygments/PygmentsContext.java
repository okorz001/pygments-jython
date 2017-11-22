package org.korz.pygments;

import java.util.HashMap;
import java.util.Map;

/**
 * A Pygments highlighting context.
 * <p>
 * There are two different APIs for setting the lexer and formatter for the
 * Pygments context:
 * <ol>
 * <li>Build a Lexer or Formatter on demand. This is most concise option.
 * <pre><code>
 * PygmentsContext p = PygmentsContext.newContext()
 *      .setLexerName("someLexer")
 *      .setLexerOption("someOption", "someValue")
 *      .setFormatterName("someFormatter")
 *      .setFormatterOption("anotherOption", "anotherValue")
 *      .build();
 * </code></pre></li>
 * <li>Provide a Lexer or Formatter instance directly. This allows usage of
 * other lexer or formatter factories (e.g. by filename) as well as sharing
 * lexers or formatters between PygmentsContext instances.
 * <pre><code>
 * Lexer lexer = Lexer.byName("someLexer")
 *     .setOption("someOption", "someValue")
 *     .build();
 * Formatter formatter = Formatter.byName("someFormatter")
 *     .setOption("anotherOption", "anotherValue")
 *     .build();
 * PygmentsContext p = PygmentsContext.newContext()
 *     .setLexer(lexer)
 *     .setFormatter(formatter)
 *     .build();
 * </code></pre></li>
 * </ol>
 * Both code samples above create functionally equivalent PygmentsContext
 * instances.
 * <p>
 * It is also possible to mix and match these APIs:
 * <pre><code>
 * static Formatter FORMATTER = Formatter.byName("html").build();
 *
 * String highlight(String language, String text) {
 *     return PygmentsContext.newContext()
 *         .setLexerName(language)
 *         .setFormatter(FORMATTER)
 *         .build()
 *         .highlight(text);
 * }
 * </code></pre>
 * @see Lexer
 * @see Formatter
 */
public class PygmentsContext {
    /**
     * Constructs a PygmentsContext highlight context.
     */
    public static class Builder {
        private Lexer lexer;
        private String lexerName;
        private final Map<String, Object> lexerOptions = new HashMap<>();
        private Formatter formatter;
        private String formatterName;
        private final Map<String, Object> formatterOptions = new HashMap<>();

        private Builder() {}

        /**
         * Sets the lexer instance.
         * @param lexer The lexer.
         * @return This builder for method chaining.
         */
        public Builder setLexer(Lexer lexer) {
            this.lexer = lexer;
            return this;
        }

        /**
         * Sets the lexer name.
         * @param lexerName The name or alias of the lexer.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/lexers/">
         *      Pygments: Available Lexers</a>
         */
        public Builder setLexerName(String lexerName) {
            this.lexerName = lexerName;
            return this;
        }

        /**
         * Sets a lexer option.
         * @param name The name of the option.
         * @param value The value of the option.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/lexers/">
         *      Pygments: Available Lexers</a>
         */
        public Builder setLexerOption(String name, Object value) {
            lexerOptions.put(name, value);
            return this;
        }

        /**
         * Sets the formatter instance.
         * @param formatter The formatter.
         * @return This builder for method chaining.
         */
        public Builder setFormatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        /**
         * Sets the formatter name.
         * @param formatterName The name or alias of the formatter.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/formatters/">
         *      Pygments: Available Formatters</a>
         */
        public Builder setFormatterName(String formatterName) {
            this.formatterName = formatterName;
            return this;
        }

        /**
         * Sets a formatter option.
         * @param name The name of the option.
         * @param value The value of the option.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/formatters/">
         *      Pygments: Available Formatters</a>
         */
        public Builder setFormatterOption(String name, Object value) {
            formatterOptions.put(name, value);
            return this;
        }

        /**
         * Creates a new PygmentsContext instance.
         * @return A new instance.
         * @throws NullPointerException If no lexer or no formatter has been
         *                              selected.
         * @throws ClassNotFoundException If the lexer or formatter cannot be
         *                                created.
         */
        public PygmentsContext build() {
            Lexer lexer;
            if (this.lexer != null) {
                lexer = this.lexer;
            }
            else if (lexerName != null) {
                lexer = Lexer.byName(lexerName)
                    .setOptions(lexerOptions)
                    .build();
            }
            else {
                throw new NullPointerException(
                    "Both lexer and lexerName are null");
            }

            Formatter formatter;
            if (this.formatter != null) {
                formatter = this.formatter;
            }
            else if (formatterName != null) {
                formatter = Formatter.byName(formatterName)
                    .setOptions(formatterOptions)
                    .build();
            }
            else {
                throw new NullPointerException(
                    "Both formatter and formatterName are null");
            }

            return new PygmentsContext(lexer, formatter);
        }
    }

    /**
     * Creates a new PygmentsContext builder.
     * @return A new builder.
     */
    public static Builder newContext() {
        return new Builder();
    }

    private final Lexer lexer;
    private final Formatter formatter;

    private PygmentsContext(Lexer lexer, Formatter formatter) {
        this.lexer = lexer;
        this.formatter = formatter;
    }

    /**
     * Highlights text.
     * @param text The text to highlight.
     * @return The highlighted text.
     */
    public String highlight(String text) {
        return formatter.format(lexer.lex(text));
    }
}
