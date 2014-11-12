package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PostgresTest.class,SyncServerTest.class, MysqlTest.class,TestSynchronisation.class })
public class AllTests {

}


