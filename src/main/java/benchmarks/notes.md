## Was mir aufgefallen ist bei den generierten Benchmarks:

1. Generierte Methoden müssen public sein
2. Methode assert muss zu assert_ werden, sonst kann der Compiler nicht zwischen der Methode und dem
   Java Keyword unterscheiden
3. Typo bei Constant
4. ArrayElement funktioniert eigentlich mit String als erstem Argument
5. Weiß nicht, ob program.stop() tatsächlich notwendig ist