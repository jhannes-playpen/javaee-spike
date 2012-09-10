package com.exilesoft.common;

import java.util.ArrayList;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.exilesoft.javaee.FakePersonRepository;
import com.exilesoft.javaee.JdbcPersonRepository;
import com.exilesoft.javaee.PersonRepository;

public class RepositoryTestRunner extends Suite {

    public static class RepoTestRunner extends BlockJUnit4ClassRunner {

        private PersonRepository personRepository;

        public RepoTestRunner(Class<?> testClass, PersonRepository personRepository) throws InitializationError {
            super(testClass);
            this.personRepository = personRepository;
        }

        @Override
        protected Object createTest() throws Exception {
            return getTestClass().getOnlyConstructor().newInstance(personRepository);
        }

        @Override
        protected String getName() {
            return String.format("%s[%s]", getTestClass().getJavaClass().getName(), personRepository.toString());
        }

        @Override
        protected String testName(final FrameworkMethod method) {
            return String.format("%s[%s]", method.getName(),
                    personRepository.toString());
        }

        @Override
        protected void validateConstructor(List<Throwable> errors) {
            validateOnlyOneConstructor(errors);
        }
    }

    private static JdbcConnectionPool dataSource;

    public RepositoryTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass, createRunners(testClass));
    }

    private static List<Runner> createRunners(Class<?> testClass) throws InitializationError {
        List<Runner> runners = new ArrayList<>();
        runners.add(new RepoTestRunner(testClass, new FakePersonRepository()));
        runners.add(new RepoTestRunner(testClass, new JdbcPersonRepository(dataSource)));
        return runners;
    }

    static {
        dataSource = JdbcConnectionPool.
                create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
        JdbcPersonRepository.createDatabaseSchema(dataSource);
    }
}
