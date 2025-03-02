1. Built and tested with openjdk 17.0.12 on windows using IntelliJ
2. Have not used any DI framework (Spring Boot) or even lombok
3. Can be run by:
   i. Executing class com.efx.vwap.Bootstrapper (no additional parameters required). ii. Some parameters like venue
   and instrument to be subscribed to can be configured from within the BootStrapper class. (Corresponding Enums need to
   be updated)
   iii. The *SimulatedVenues* class is a simple test class to facilitate end to end running.

4. Some comments wrt the classes and intended design written as comments within code.
5. Logger:
   i. Have used in the VWAPSubscriber class only and tried to give an example of how it really should be ii. All log
   statements will be predefined as char[] with additional space at the end of the array for any values that need to be
   logged (ideally only an id for each tick with the tick itself being recorded separately)
   iii. The actual logger in here will copy the char array to a disruptor slot from where an appender will pick the
   message up for actual writing (separate, non-pinned thread)

6. Ideal design attached - efx_design.png

7. TODO:
   i. Introduce disruptors at the appropriate places - starting with a disruptor-based subscription manager ii. Extend
   logger as mentioned above.