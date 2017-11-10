#include <stdio.h>

#define ONE 1

int add(int a, int b) {
    return a + b;
}

int main(void) {
    puts("%d", add(ONE, 2));
    return 0;
}
