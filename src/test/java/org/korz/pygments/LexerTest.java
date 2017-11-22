package org.korz.pygments;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LexerTest {
    @Test
    public void byName() {
        Lexer lexer = Lexer.byName("c").build();
        assertThat(lexer.getName(), equalTo("C"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void byNameUnknown() {
        Lexer.byName("poopies").build();
    }

    @Test
    public void forFile() {
        Lexer lexer = Lexer.forFile("main.c").build();
        assertThat(lexer.getName(), equalTo("C"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void forFileUnknown() {
        Lexer.forFile("foo.poopies").build();
    }

    @Test
    public void forMime() {
        Lexer lexer = Lexer.forMime("text/x-csrc").build();
        assertThat(lexer.getName(), equalTo("C"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void forMimeUnknown() {
        Lexer.forMime("text/poopies").build();
    }

    @Test
    public void guess() {
        Lexer lexer = Lexer.guess("#include <stdio.h>").build();
        assertThat(lexer.getName(), equalTo("C"));
    }

    @Test
    public void guessForFile() {
        Lexer lexer = Lexer.guessForFile("main.c", "#include <stdio.h>")
            .build();
        assertThat(lexer.getName(), equalTo("C"));
    }

    @Test(expected = ClassNotFoundException.class)
    public void guessForFileUnknown() {
        Lexer.guessForFile("foo.poopies", "poopies!!").build();
    }
}
