package org.korz.pygments;

import org.python.core.PyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders Pygments tokens into highlighted text.
 * <p>
 * Although this class may be used directly, it is intended to be used by
 * a PygmentsContext instance.
 * <p>
 * The are two ways to create a Formatter:
 * <ol>
 * <li>Create a Formatter by short name or alias:
 * <pre><code>
 * Formatter formatter = Formatter.byName("html")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * <li>Create a Formatter by filename of an intended output file:
 * <pre><code>
 * Formatter formatter = Formatter.forFile("sample.html")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * </ol>
 * Both code samples above create functionally equivalent Formatter instances.
 * @see PygmentsContext
 * @see Lexer
 * @see <a href="http://pygments.org/docs/formatters/">
 *      Pygments: Available Formatters</a>
 */
public class Formatter {
    /**
     * Constructs a formatter.
     */
    public static abstract class Builder {
        protected final Map<String, Object> options = new HashMap<>();

        protected Builder() {}

        /**
         * Creates a new Formatter instance.
         * @return A new instance.
         * @throws ClassNotFoundException If the formatter cannot be created.
         */
        public abstract Formatter build();

        /**
         * Sets a formatter option.
         * @param name The option name.
         * @param value The option value.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/formatters/">
         *      Pygments: Available Formatters</a>
         */
        public Builder setOption(String name, Object value) {
            options.put(name, value);
            return this;
        }

        /**
         * Sets multiple formatter options.
         * @param options The options.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/formatters/">
         *      Pygments: Available Formatters</a>
         */
        public Builder setOptions(Map<String, Object> options) {
            for (Map.Entry<String, Object> option : options.entrySet()) {
                setOption(option.getKey(), option.getValue());
            }
            return this;
        }
    }

    /**
     * Creates a formatter with the specified name or alias.
     * @param name The formatter name or alias.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/formatters/">
     *      Pygments: Available Formatters</a>
     * @see <a href="http://pygments.org/docs/api/#pygments.formatters.get_formatter_by_name">
     *      pygments.formatters.get_formatter_by_name</a>
     */
    public static Builder byName(String name) {
        return new Builder() {
            @Override
            public Formatter build() {
                return new Formatter(Pygments.getFormatter(name, options));
            }
        };
    }

    /**
     * Creates a formatter based on the specified filename.
     * @param file The filename.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/formatters/">
     *      Pygments: Available Formatters</a>
     * @see <a href="http://pygments.org/docs/api/#pygments.formatters.get_formatter_for_filename">
     *      pygments.formatters.get_formatter_for_filename</a>
     */
    public static Builder forFile(String file) {
        return new Builder() {
            @Override
            public Formatter build() {
                return new Formatter(
                    Pygments.getFormatterForFile(file, options));
            }
        };
    }

    private final PyObject delegate;

    private Formatter(PyObject delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the name of this Formatter.
     * @return The name of this Formatter.
     */
    public String getName() {
        return PythonUtil.get(String.class, delegate, "name");
    }

    /**
     * Renders a Pygments token sequence.
     * @param tokens Pygments tokens
     * @return Highlighted text.
     * @see Lexer#lex
     */
    public String format(Iterable<?> tokens) {
        return Pygments.format(tokens, delegate);
    }
}
