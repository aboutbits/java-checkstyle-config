package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class CheckTestSupport {

    protected List<String> runCheck(
            Class<? extends AbstractCheck> checkClass,
            String fixtureResourcePath
    ) throws Exception {
        return runCheck(checkClass, fixtureResourcePath, Map.of());
    }

    protected List<String> runCheck(
            Class<? extends AbstractCheck> checkClass,
            String fixtureResourcePath,
            Map<String, String> properties
    ) throws Exception {
        var checkCfg = new DefaultConfiguration(checkClass.getName());
        for (var entry : properties.entrySet()) {
            checkCfg.addProperty(entry.getKey(), entry.getValue());
        }
        var twCfg = new DefaultConfiguration("TreeWalker");
        twCfg.addChild(checkCfg);
        var rootCfg = new DefaultConfiguration("Checker");
        rootCfg.addChild(twCfg);

        var checker = new Checker();
        checker.setModuleClassLoader(getClass().getClassLoader());
        checker.configure(rootCfg);

        var listener = new CapturingListener();
        checker.addListener(listener);

        var url = getClass().getResource(fixtureResourcePath);
        if (url == null) {
            throw new IllegalStateException("Fixture not found on classpath: " + fixtureResourcePath);
        }
        var file = new File(url.toURI());
        checker.process(List.of(file));
        checker.destroy();
        return Collections.unmodifiableList(listener.messages);
    }

    static final class CapturingListener implements AuditListener {
        final List<String> messages = new ArrayList<>();

        @Override
        public void auditStarted(AuditEvent event) {
        }

        @Override
        public void auditFinished(AuditEvent event) {
        }

        @Override
        public void fileStarted(AuditEvent event) {
        }

        @Override
        public void fileFinished(AuditEvent event) {
        }

        @Override
        public void addError(AuditEvent event) {
            messages.add(event.getLine() + ":" + event.getMessage());
        }

        @Override
        public void addException(AuditEvent event, Throwable throwable) {
            messages.add("EXCEPTION:" + throwable.getMessage());
        }
    }
}
