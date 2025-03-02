1. Built and tested with openjdk 17.0.12 on windows using IntelliJ
2. Have not used any DI framework (Spring Boot) or even lombok
3. Can be run by:
   - Executing class com.efx.vwap.Bootstrapper (no additional parameters required).
   - Some parameters like venue
   and instrument to be subscribed to can be configured from within the BootStrapper class. (Corresponding Enums need to
   be updated)
   - The *com.efx.vwap.SimulatedVenues* class is a simple test class to facilitate end to end running.

4. Some comments wrt the classes and intended design written as comments within code.
5. Logger:
   - Have used in the VWAPSubscriber class only and tried to give an example of how it really should be
   - All log statements will be predefined as char[] with additional space at the end of the array for any values that need to be logged (ideally only an id for each tick with the tick itself being recorded separately)
   - The actual logger in here will copy the char array to a disruptor slot from where an appender will pick the
   message up for actual writing (separate, non-pinned thread)

6. Ideal design attached - efx_design.png

7. TODO:
   - Introduce disruptors at the appropriate places - starting with a disruptor-based subscription manager.
   - Update the fixed-point arithmetic to cater for more scenarios and overflow checks(for sanity).
   - Extend logger as mentioned above and add more logging to each class.
