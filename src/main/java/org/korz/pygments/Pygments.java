package org.korz.pygments;

import org.python.core.PyException;
import org.python.core.PyObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Java bindings for Pygments functions.
 * <p>
 * This class provides direct access to Pygments functions with minimal
 * coercing to Java types where appropriate. For a more idiomatic Java API,
 * see {@link PygmentsContext}, {@link Lexer}, and {@link Formatter}.
 * <p>
 * This API is used by the idiomatic API, but is provided as-is for anyone
 * who would prefer to use it. In particular, it may be simpler to use in
 * dynamically typed languages with Map literals.
 * @see PygmentsContext
 * @see <a href="http://pygments.org/docs/api/">
 *      Pygments: The full Pygments API</a>
 */
public class Pygments {
    // Python handles
    private static final PyObject PYGMENTS = PythonUtil.importModule(
        "pygments");
    private static final PyObject FORMAT = PythonUtil.get(
        PYGMENTS, "format");
    private static final PyObject HIGHLIGHT = PythonUtil.get(
        PYGMENTS, "highlight");
    private static final PyObject LEX = PythonUtil.get(
        PYGMENTS, "lex");
    private static final PyObject GET_FORMATTER = PythonUtil.get(
        PYGMENTS, "formatters", "get_formatter_by_name");
    private static final PyObject GET_FORMATTER_FOR_FILE = PythonUtil.get(
        PYGMENTS, "formatters", "get_formatter_for_filename");
    private static final PyObject GET_LEXER = PythonUtil.get(
        PYGMENTS, "lexers", "get_lexer_by_name");
    private static final PyObject GET_LEXER_FOR_FILE = PythonUtil.get(
        PYGMENTS, "lexers", "get_lexer_for_filename");
    private static final PyObject GET_LEXER_FOR_MIME = PythonUtil.get(
        PYGMENTS, "lexers", "get_lexer_for_mimetype");
    private static final PyObject GUESS_LEXER = PythonUtil.get(
        PYGMENTS, "lexers", "guess_lexer");
    private static final PyObject GUESS_LEXER_FOR_FILE = PythonUtil.get(
        PYGMENTS, "lexers", "guess_lexer_for_filename");
    private static final PyObject CLASS_NOT_FOUND = PythonUtil.get(
        PYGMENTS, "util", "ClassNotFound");

    // do the right thing and reduce typing
    @SafeVarargs
    private static <T> List<T> list(T... values) {
        if (values.length == 1) {
            return Collections.singletonList(values[0]);
        }
        else {
            return Arrays.asList(values);
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.format">
     * pygments.format</a>
     */
    public static String format(Iterable<?> tokens, Object formatter) {
        return PythonUtil.call(String.class, FORMAT, list(tokens, formatter));
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.highlight">
     * pygments.highlight</a>
     */
    public static String highlight(String text,
                                   Object lexer,
                                   Object formatter) {
        return PythonUtil.call(String.class,
                               HIGHLIGHT,
                               list(text, lexer, formatter));
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lex">pygments.lex</a>
     */
    public static Iterable<?> lex(String text, Object lexer) {
        return PythonUtil.call(Iterable.class, LEX, list(text, lexer));
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.formatters.get_formatter_by_name">
     * pygments.formatters.get_formatter_by_name</a>
     * @throws ClassNotFoundException If the formatter cannot be created.
     */
    public static PyObject getFormatter(String name, Map<String, ?> options) {
        try {
            return PythonUtil.call(GET_FORMATTER, list(name), options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.formatters.get_formatter_for_filename">
     * pygments.formatters.get_formatter_for_filename</a>
     * @throws ClassNotFoundException If the formatter cannot be created.
     */
    public static PyObject getFormatterForFile(String file,
                                               Map<String, ?> options) {
        try {
            return PythonUtil.call(GET_FORMATTER_FOR_FILE, list(file), options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_by_name">
     * pygments.lexers.get_lexer_by_name</a>
     * @throws ClassNotFoundException If the lexer cannot be created.
     */
    public static PyObject getLexer(String name, Map<String, ?> options) {
        try {
            return PythonUtil.call(GET_LEXER, list(name), options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_for_filename">
     * pygments.lexers.get_lexer_for_filename</a>
     * @throws ClassNotFoundException If the lexer cannot be created.
     */
    public static PyObject getLexerForFile(String file,
                                           Map<String, ?> options) {
        try {
            return PythonUtil.call(GET_LEXER_FOR_FILE, list(file), options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lexers.get_lexer_for_mimetype">
     * pygments.lexers.get_lexer_for_mimetype</a>
     * @throws ClassNotFoundException If the lexer cannot be created.
     */
    public static PyObject getLexerForMime(String mime,
                                           Map<String, ?> options) {
        try {
            return PythonUtil.call(GET_LEXER_FOR_MIME, list(mime), options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lexers.guess_lexer">
     * pygments.lexers.guess_lexer</a>
     * <p>
     * This function will never fail because the text lexer accepts any input.
     */
    public static PyObject guessLexer(String text, Map<String, ?> options) {
        return PythonUtil.call(GUESS_LEXER, list(text), options);
    }

    /**
     * <a href="http://pygments.org/docs/api/#pygments.lexers.guess_lexer">
     * pygments.lexers.guess_lexer</a>
     * @throws ClassNotFoundException If the lexer cannot be created.
     */
    public static PyObject guessLexerForFile(String file,
                                             String text,
                                             Map<String, ?> options) {
        try {
            return PythonUtil.call(GUESS_LEXER_FOR_FILE,
                                   list(file, text),
                                   options);
        }
        catch (PyException e) {
            if (e.match(CLASS_NOT_FOUND)) {
                throw new ClassNotFoundException(e);
            }
            throw e;
        }
    }

    private Pygments() {}
}
