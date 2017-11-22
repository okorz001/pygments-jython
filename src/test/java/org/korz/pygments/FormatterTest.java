package org.korz.pygments;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {
    @Test
    public void byName() {
        Formatter formatter = Formatter.byName("html").build();
        assertThat(formatter.getName(), equalTo("HTML"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void byNameUnknown() {
        Formatter.byName("poopies").build();
    }

    @Test
    public void forFile() {
        Formatter formatter = Formatter.forFile("output.html").build();
        assertThat(formatter.getName(), equalTo("HTML"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void forFileUnknown() {
        Formatter.forFile("foo.poopies").build();
    }
}
