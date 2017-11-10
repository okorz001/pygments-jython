package org.korz.pygments;

import org.python.core.PyException;
import org.python.core.PyObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Pygments highlighting context.
 */
public class Pygments {
    /**
     * Constructs a Pygments highlight context.
     */
    public static class Builder {
        private String lexerName;
        private final Map<String, Object> lexerOptions = new HashMap<>();
        private String formatterName;
        private final Map<String, Object> formatterOptions = new HashMap<>();

        private Builder() {}

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
         * Creates a new Pygments instance.
         * @return A new instance.
         * @throws NullPointerException If no lexer or no formatter has been
         *                              selected.
         * @throws IllegalArgumentException If an unknown lexer or formatter
         *                                  has been selected.
         */
        public Pygments build() {
            if (lexerName == null) {
                throw new NullPointerException("lexerName is null");
            }
            if (formatterName == null) {
                throw new NullPointerException("formatterName is null");
            }
            return new Pygments(this);
        }
    }

    /**
     * Creates a new Pygments builder.
     * @return A new builder.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    private static final PyObject PYGMENTS =
        PythonUtil.importModule("pygments");
    private static final PyObject GET_LEXER =
        PythonUtil.get(PYGMENTS, "lexers", "get_lexer_by_name");
    private static final PyObject GET_FORMATTER =
        PythonUtil.get(PYGMENTS, "formatters", "get_formatter_by_name");
    private static final PyObject HIGHLIGHT =
        PythonUtil.get(PYGMENTS, "highlight");
    private static final PyObject CLASS_NOT_FOUND =
        PythonUtil.get(PYGMENTS, "util", "ClassNotFound");

    // this could be public, but it exposes the raw Jython API
    private static PyObject getLexer(String name,
                                     Map<String, ? extends Object> options) {
        try {
            return PythonUtil.call(GET_LEXER,
                                   Collections.singletonList(name),
                                   options);
        }
        catch (PyException e) {
            if (!e.match(CLASS_NOT_FOUND)) {
                throw e;
            }
            throw new IllegalArgumentException("Unknown lexer: " + name, e);
        }
    }

    // this could be public, but it exposes the raw Jython API
    private static PyObject getFormatter(String name,
                                         Map<String, ? extends Object> options) {
        try {
            return PythonUtil.call(GET_FORMATTER,
                                   Collections.singletonList(name),
                                   options);
        }
        catch (PyException e) {
            if (!e.match(CLASS_NOT_FOUND)) {
                throw e;
            }
            throw new IllegalArgumentException("Unknown formatter: " + name, e);
        }
    }

    private final PyObject lexer;
    private final PyObject formatter;

    private Pygments(Builder b) {
        lexer = getLexer(b.lexerName, b.lexerOptions);
        formatter = getFormatter(b.formatterName, b.formatterOptions);
    }

    /**
     * Highlights text.
     * @param text The text to highlight.
     * @return The highlighted text.
     */
    public String highlight(String text) {
        List<Object> args = Arrays.asList(text, lexer, formatter);
        return PythonUtil.toJava(String.class,
                                 PythonUtil.call(HIGHLIGHT, args));
    }
}
