package org.korz.pygments;

import org.python.core.Py;
import org.python.core.PyObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Convenience methods for working with the embedded Python runtime.
 */
public class PythonUtil {
    private static final PyObject IMPORTER;
    static {
        try {
            // There seems to be little benefit to creating new PySystemState
            // instances since Python modules appear to be globally loaded.
            IMPORTER = Py.getSystemState()
                .getBuiltins()
                .__getitem__(Py.newString("__import__"));
        }
        catch (RuntimeException e) {
            throw new RuntimeException(
                "Failed to get Python import function", e);
        }
    }

    /**
     * Imports a Python module.
     * <p>
     * Note that Python modules are only loaded once and then cached, just
     * like Java class loading. The Python module loader is thread-safe, so
     * this method can be called without external synchronization.
     * @param module The module name.
     * @return The Python module.
     */
    public static PyObject importModule(String module) {
        return IMPORTER.__call__(Py.newString(module));
    }

    /**
     * Gets an attribute, possibly nested, from a Python object.
     * @param o The Python object to access.
     * @param names The attribute names to walk, in order.
     * @return The attribute value, or null if it does not exist.
     */
    public static PyObject get(PyObject o, String... names) {
        for (String name : names) {
            o = o.__getattr__(Py.newString(name));
            if (o == null) {
                break;
            }
        }
        return o;
    }

    /**
     * Converts a Python object to an instance of Java class.
     * @param <T> The desired return type.
     * @param clazz The desired return type.
     * @param o The Python object to convert.
     * @return A instance corresponding to the Python object.
     * @throws ClassCastException If the object cannot be converted to the
     *                            specified type.
     */
    public static <T> T toJava(Class<T> clazz, PyObject o) {
        return o == null ? null : clazz.cast(o.__tojava__(clazz));
    }

    /**
     * Calls a Python function with no arguments.
     * @param func The Python function.
     * @return The result of the function.
     */
    public static PyObject call(PyObject func) {
        return call(func, Collections.emptyList(), Collections.emptyMap());
    }

    /**
     * Calls a Python function with positional arguments.
     * @param func The Python function.
     * @param args The positional arguments.
     * @return The result of the function.
     */
    public static PyObject call(PyObject func, List<? extends Object> args) {
        return call(func, args, Collections.emptyMap());
    }

    /**
     * Calls a Python function with keyword arguments.
     * @param func The Python function.
     * @param kwargs The keyword arguments.
     * @return The result of the function.
     */
    public static PyObject call(PyObject func,
                                Map<String, ? extends Object> kwargs) {
        return call(func, Collections.emptyList(), kwargs);
    }

    /**
     * Calls a Python function with positional and keyword arguments.
     * @param func The Python function.
     * @param args The positional arguments.
     * @param kwargs The keyword arguments.
     * @return The result of the function.
     */
    public static PyObject call(PyObject func,
                                List<? extends Object> args,
                                Map<String, ? extends Object> kwargs) {
        // __call__ receives all arguments in a single array, with the
        // positional arguments before the keyword arguments.
        // the second array contains the keys for the keyword arguments.
        List<PyObject> pyArgs = new ArrayList<>(args.size() + kwargs.size());
        List<String> keys = new ArrayList<>(kwargs.size());
        for (Object arg : args) {
            pyArgs.add(Py.java2py(arg));
        }
        for (Map.Entry<String, ? extends Object> kwarg : kwargs.entrySet()) {
            keys.add(kwarg.getKey());
            pyArgs.add(Py.java2py(kwarg.getValue()));
        }
        return func.__call__(
                pyArgs.toArray(new PyObject[0]),
                keys.toArray(new String[0]));
    }

    private PythonUtil() {}
}
