package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.config.factory.PropertiesFactory;
import io.github.PlatovD.svarog.core.testbeans.container.NamedBean;
import io.github.PlatovD.svarog.core.testbeans.container.PrototypeBean;
import io.github.PlatovD.svarog.core.testbeans.container.Repository;
import io.github.PlatovD.svarog.core.testbeans.container.Service;
import io.github.PlatovD.svarog.exception.SvarogException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    private static final String TEST_CONFIG = "svarog-container-test.properties";

    private static Container createContainer() {
        return Container.builder()
                .config(new PropertiesFactory(TEST_CONFIG))
                .build();
    }

    @Test
    void getBean_singleton_returnsSameInstance() {
        try (Container container = createContainer()) {
            Repository first = container.getBean(Repository.class);
            Repository second = container.getBean(Repository.class);

            assertNotNull(first);
            assertSame(first, second);
        }
    }

    @Test
    void getBean_injectsDependencies() {
        try (Container container = createContainer()) {
            Service service = container.getBean(Service.class);

            assertNotNull(service.repository);
        }
    }

    @Test
    void getBean_singletonDependency_sameInstanceInjected() {
        try (Container container = createContainer()) {
            Service service = container.getBean(Service.class);
            Repository fromContainer = container.getBean(Repository.class);

            assertSame(fromContainer, service.repository);
        }
    }

    @Test
    void getBean_prototype_returnsNewInstance() {
        try (Container container = createContainer()) {
            PrototypeBean first = container.getBean(PrototypeBean.class);
            PrototypeBean second = container.getBean(PrototypeBean.class);

            assertNotSame(first, second);
        }
    }

    @Test
    void getBean_afterCreateCalled() {
        try (Container container = createContainer()) {
            Service service = container.getBean(Service.class);

            assertTrue(service.initialized);
        }
    }

    @Test
    void close_callsBeforeDestroy() {
        Container container = createContainer();
        Service service = container.getBean(Service.class);

        assertFalse(service.closed);

        container.close();

        assertTrue(service.closed);
    }

    @Test
    void getBean_byName_returnsBean() {
        try (Container container = createContainer()) {
            Object bean = container.getBean("specialBean");

            assertNotNull(bean);
            assertInstanceOf(NamedBean.class, bean);
        }
    }

    @Test
    void getBean_byName_unknown_throws() {
        try (Container container = createContainer()) {
            assertThrows(SvarogException.class,
                    () -> container.getBean("unknownBean"));
        }
    }

    @Test
    void getBean_unknownType_throws() {
        try (Container container = createContainer()) {
            assertThrows(SvarogException.class,
                    () -> container.getBean(String.class));
        }
    }

    @Test
    void containsBean_byType() {
        try (Container container = createContainer()) {
            assertTrue(container.containsBean(Repository.class));
            assertFalse(container.containsBean(String.class));
        }
    }

    @Test
    void containsBean_byName() {
        try (Container container = createContainer()) {
            assertTrue(container.containsBean("specialBean"));
            assertFalse(container.containsBean("unknown"));
        }
    }

    @Test
    void getBeansOfType_returnsList() {
        try (Container container = createContainer()) {
            List<Repository> list = container.getBeansOfType(Repository.class);

            assertEquals(1, list.size());
            assertInstanceOf(Repository.class, list.get(0));
        }
    }
}
