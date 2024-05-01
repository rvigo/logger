package com.rvigo.logger.boot.starter

class LoggerAutoConfigurationTest {

//    private val runner: ApplicationContextRunner = ApplicationContextRunner()
//        .withConfiguration(
//            AutoConfigurations.of(
//                LoggerAutoConfiguration::class.java
//            )
//        )
//
//    @Test
//    fun `should initialize the logger config`() {
//        runner.run {
//            assertThat(it).hasSingleBean(LoggerConfiguration::class.java)
//        }
//    }
//
//    @Test
//    fun `engine should use the same configuration bean`() {
//        runner.run {
//            val engine = LoggerEngine
//            val config = it.getBean(LoggerConfiguration::class.java)
//
//            assertThat(engine.configuration).isEqualTo(config)
//
//        }
//    }
//
//    @Test
//    fun `should use the default values`() {
//        runner.run {
//
//            val config = it.getBean(LoggerConfiguration::class.java)
//
//            assertThat(config.logLevel).isEqualTo(LogLevel.INFO)
//            assertThat(config.template).isInstanceOf(JsonLogTemplate::class.java)
//        }
//    }
//
//    @Test
//    fun `should use the properties values`() {
//        runner.withPropertyValues(
//            "logger.logLevel=DEBUG",
//            "logger.template=CONSOLE",
//            "logger.origin=test-origin"
//        ).run {
//            val config = it.getBean(LoggerConfiguration::class.java)
//
//            assertThat(config.logLevel).isEqualTo(LogLevel.DEBUG)
//            assertThat(config.template).isInstanceOf(ConsoleLogTemplate::class.java)
//            assertThat(config.origin).isEqualTo("test-origin")
//        }
//    }
}
