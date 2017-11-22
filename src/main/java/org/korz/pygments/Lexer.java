package org.korz.pygments;

import org.python.core.PyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Lexes text into Pygments tokens.
 * <p>
 * Although this class may be used directly, it is intended to be used by
 * a PygmentsContext instance.
 * <p>
 * The are multiple ways to create a Lexer:
 * <ol>
 * <li>Create a Lexer by short name or alias:
 * <pre><code>
 * Lexer lexer = Lexer.byName("java")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * <li>Create a Lexer by a filename:
 * <pre><code>
 * Lexer lexer = Lexer.forFile("Foo.java")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * <li>Create a Lexer by a MIME type:
 * <pre><code>
 * Lexer lexer = Lexer.byName("text/x-java")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * <li>Create a Lexer by analyzing a text fragment:
 * <pre><code>
 * Lexer lexer = Lexer.guess("public static void main(String[] args) {}")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * <li>Create a Lexer by analyzing a text fragment with a filename:
 * <pre><code>
 * Lexer lexer = Lexer.guessForFile("Foo.java", "void foo() {}")
 *     .setOption("encoding", "utf-8")
 *     .build();
 * </code></pre></li>
 * </ol>
 * All the code samples above create functionally equivalent Lexer instances.
 * @see PygmentsContext
 * @see Formatter
 * @see <a href="http://pygments.org/docs/lexers/">
 *      Pygments: Available Lexers</a>
 */
public class Lexer {
    /**
     * Constructs a lexer.
     */
    public static abstract class Builder {
        protected final Map<String, Object> options = new HashMap<>();

        protected Builder() {}

        /**
         * Creates a new Lexer instance.
         * @return A new instance.
         * @throws ClassNotFoundException If the lexer cannot be created.
         */
        public abstract Lexer build();

        /**
         * Sets a lexer option.
         * @param name The option name.
         * @param value The option value.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/lexers/">
         *      Pygments: Available Lexers</a>
         */
        public Builder setOption(String name, Object value) {
            options.put(name, value);
            return this;
        }

        /**
         * Sets multiple lexer options.
         * @param options The options.
         * @return This builder for method chaining.
         * @see <a href="http://pygments.org/docs/lexers/">
         *      Pygments: Available Lexers</a>
         */
        public Builder setOptions(Map<String, Object> options) {
            for (Map.Entry<String, Object> option : options.entrySet()) {
                setOption(option.getKey(), option.getValue());
            }
            return this;
        }
    }

    /**
     * Creates a lexer with the specified name or alias.
     * @param name The lexer name or alias.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/lexers/">
     *      Pygments: Available Lexers</a>
     * @see <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_by_name">
     *      pygments.lexers.get_lexer_by_name</a>
     */
    public static Builder byName(String name) {
        return new Builder() {
            @Override
            public Lexer build() {
                return new Lexer(Pygments.getLexer(name, options));
            }
        };
    }

    /**
     * Creates a lexer based on the specified filename.
     * @param file The filename.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/lexers/">
     *      Pygments: Available Lexers</a>
     * @see <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_for_filename">
     *      pygments.lexers.get_lexer_for_filename</a>
     */
    public static Builder forFile(String file) {
        return new Builder() {
            @Override
            public Lexer build() {
                return new Lexer(Pygments.getLexerForFile(file, options));
            }
        };
    }

    /**
     * Creates a lexer based on the specified MIME type.
     * @param mime The MIME type.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/lexers/">
     *      Pygments: Available Lexers</a>
     * @see <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_for_mimetype">
     *      pygments.lexers.get_lexer_for_mimetype</a>
     */
    public static Builder forMime(String mime) {
        return new Builder() {
            @Override
            public Lexer build() {
                return new Lexer(Pygments.getLexerForMime(mime, options));
            }
        };
    }

    /**
     * Creates a lexer based on a text fragment.
     * <p>
     * The lexer with the highest confidence score for the given text will be
     * created.
     * <p>
     * This builder will never fail because the text lexer accepts any input.
     * @param text The text fragment.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/api/#pygments.lexers.guess_lexer">
     *      pygments.lexers.guess_lexer</a>
     */
    public static Builder guess(String text) {
        return new Builder() {
            @Override
            public Lexer build() {
                return new Lexer(Pygments.guessLexer(text, options));
            }
        };
    }

    /**
     * Creates a lexer based on a filename and a text fragment.
     * <p>
     * The lexer with the highest confidence score for the given text will be
     * created.
     * @param file The filename.
     * @param text The text fragment.
     * @return A new builder.
     * @see <a href="http://pygments.org/docs/api/#pygments.lexers.guess_lexer_for_filename">
     *      pygments.lexers.guess_lexer_for_filename</a>
     */
    public static Builder guessForFile(String file, String text) {
        return new Builder() {
            @Override
            public Lexer build() {
                return new Lexer(
                    Pygments.guessLexerForFile(file, text, options));
            }
        };
    }

    private final PyObject delegate;

    private Lexer(PyObject delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the name of this Lexer.
     * @return The name of this Lexer.
     */
    public String getName() {
        return PythonUtil.get(String.class, delegate, "name");
    }

    /**
     * Lexes text into a Pygments token sequence.
     * @param text The text.
     * @return Pygments tokens.
     * @see Formatter#format
     */
    public Iterable<?> lex(String text) {
        return Pygments.lex(text, delegate);
    }
}
