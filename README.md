# jade-test-suite

A test suite for the [JAVA Agent DEvelopment Framework](http://jade.tilab.com/) implemented for the needs of evaluating a software refactoring method and tool. The test suite is based on the Jade [Test Suite Framework v1.13](http://jade.tilab.com/dl.php?file=testSuiteAddOn-1.13.zip) add-on and operates on [version 4.4.0](http://jade.tilab.com/dl.php?file=JADE-src-4.4.0.zip) of the Jade project. For convenience reasons, the source code of Jade 4.4.0 is provided as part of this project ([folder jade-4.4.0](https://github.com/bzafiris/jade-test-suite/tree/master/jade-4.4.0)). 

The tests can be executed through the following [`ant`](http://ant.apache.org/) tasks:

1. `ant cleanall`
  * Clean all build artifacts
2. `ant build`
  * Build the test suite and jade project
3. `ant test`
  * Run JUnit tests
4. `ant TestMain`
  * Run integration tests based on the Test Suite Framework. Press "Run All" from the GUI and Ctrl+C to terminate the application.
5. `ant TestMainNIO`
  * Run integration tests that require a platform with NIO capabilities. Wait a few seconds for the IOEventServer start-up (check the logs) and use the GUI as above.
6. `ant TestMainUDP`
  * Run integration tests that require the activation of UDPNodeMonitoringService in the Main Container. Use the GUI as above.
7. `ant coverage_report`
  * Generate the coverage report by combining the Jacoco execution data produced by the previous tasks. The coverage report is available in folder `report`.

