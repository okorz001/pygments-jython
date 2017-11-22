package org.korz.pygments;

import org.junit.Test;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ClassNotFoundExceptionTest {
    @Test
    public void getMessage() {
        try {
            PythonInterpreter py = new PythonInterpreter();
            py.exec("raise RuntimeError(\"oops\")");
            fail();
        }
        catch (PyException cause) {
            ClassNotFoundException cnf = new ClassNotFoundException(cause);
            assertThat(cnf.getMessage(), equalTo("oops"));
        }
    }
}
