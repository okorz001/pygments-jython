package org.korz.pygments;

import org.junit.Test;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PythonUtilTest {
    @Test
    public void importModule() {
        PyObject sys = PythonUtil.importModule("sys");
        String version = PythonUtil.get(String.class, sys, "version");
        assertThat(version, startsWith("2.7.1 "));
    }

    @Test
    public void importModuleInvalidName() {
        try {
            PythonUtil.importModule("sys; raise Exception('code injection')");
            fail();
        }
        catch (RuntimeException e) {}
    }

    @Test
    public void importModuleUnknown() {
        try {
            PythonUtil.importModule("asdf1234");
            fail();
        }
        catch (RuntimeException e) {}
    }

    @Test
    public void call() {
        PyObject test = PythonUtil.importModule("test");
        PyObject f = PythonUtil.get(test, "f");
        Map ret = PythonUtil.call(Map.class, f);

        PyDictionary expected = new PyDictionary();
        expected.put("x", 1);
        expected.put("y", 2);
        expected.put("a", new PyTuple());
        expected.put("kw", new PyDictionary());
        assertThat(ret, equalTo(expected));
    }

    @Test
    public void callArg() {
        PyObject test = PythonUtil.importModule("test");
        PyObject f = PythonUtil.get(test, "f");
        Map ret = PythonUtil.call(Map.class, f, Arrays.asList(10));

        PyDictionary expected = new PyDictionary();
        expected.put("x", 10);
        expected.put("y", 2);
        expected.put("a", new PyTuple());
        expected.put("kw", new PyDictionary());
        assertThat(ret, equalTo(expected));
    }

    @Test
    public void callArgs() {
        PyObject test = PythonUtil.importModule("test");
        PyObject f = PythonUtil.get(test, "f");
        Map ret = PythonUtil.call(Map.class, f, Arrays.asList(10, 20, 30, 40));

        PyDictionary expected = new PyDictionary();
        expected.put("x", 10);
        expected.put("y", 20);
        expected.put("a", new PyTuple(Py.java2py(30), Py.java2py(40)));
        expected.put("kw", new PyDictionary());
        assertThat(ret, equalTo(expected));
    }

    @Test
    public void callKwargs() {
        PyObject test = PythonUtil.importModule("test");
        PyObject f = PythonUtil.get(test, "f");
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("foo", 42);
        kwargs.put("bar", 0);
        Map ret = PythonUtil.call(Map.class, f, kwargs);

        PyDictionary expected = new PyDictionary();
        expected.put("x", 1);
        expected.put("y", 2);
        expected.put("a", new PyTuple());
        PyDictionary expectedKwargs = new PyDictionary();
        expectedKwargs.putAll(kwargs);
        expected.put("kw", expectedKwargs);
        assertThat(ret, equalTo(expected));
    }

    @Test
    public void callArgsKwargs() {
        PyObject test = PythonUtil.importModule("test");
        PyObject f = PythonUtil.get(test, "f");
        List<Object> args = Arrays.asList(10, 20, 30, 40);
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("foo", 42);
        kwargs.put("bar", 0);
        Map ret = PythonUtil.call(Map.class, f, args, kwargs);

        PyDictionary expected = new PyDictionary();
        expected.put("x", 10);
        expected.put("y", 20);
        expected.put("a", new PyTuple(Py.java2py(30), Py.java2py(40)));
        PyDictionary expectedKwargs = new PyDictionary();
        expectedKwargs.putAll(kwargs);
        expected.put("kw", expectedKwargs);
        assertThat(ret, equalTo(expected));
    }

    @Test
    public void getNull() {
        assertThat(PythonUtil.get(new PyObject(), "foo", "bar"), nullValue());
    }

    @Test
    public void toJavaNull() {
        assertThat(PythonUtil.toJava(String.class, null), nullValue());
    }
}
