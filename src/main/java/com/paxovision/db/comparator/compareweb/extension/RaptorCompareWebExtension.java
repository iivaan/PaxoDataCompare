package com.paxovision.db.comparator.compareweb.extension;


import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
//import com.mlp.qa.junit5.GitRepository;
//import com.mlp.qa.junits.Helperutils;
import com.paxovision.db.comparator.ComparisonConfig;
//import com.paxovision.db.comparator.compareweb.CompareWebUploader;
//import com.paxovision.db.comparator.compareweb.CompareWebUploader.Configuration.Key;
import com.paxovision.db.comparator.context.TestContext;
import com.paxovision.db.comparator.model.MapDiffResult;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
@SuppressWarnings("squid:CallToDeprecatedMethod")
public class RaptorCompareWebExtension implements BeforeAllCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        AfterAllCallback {

    public static final ExtensionContext.Namespace NAMESPACE =
                        Namespace.create("junit5-compareweb-extension");

    private static final Logger LOGGER = LoggerFactory.getLogger(RaptorCompareWebExtension.class);
    private static final String TEST_CLASS_RELATIVE_PATH_TEMPLATE = "src/test/java/%s.java";
    //TODO: change the raptor name
    private static final String CONFIGURATION_FILE_NAME = "raptorcompareweb.properties";


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        LOGGER.debug("raptor-compare-web-extension beforeAll");
        CompareWebUploader.Configuration.load(CONFIGURATION_FILE_NAME);
    }

    private void setUpTestMetadata(Class testClass, Method testMethod, ExtensionContext context)
            throws URISyntaxException {

        if (StringUtils.isBlank(
                CompreWebUploader.Configuration. getValue(Key, COMPARE_WEB_API_URL)) ) {
            return;

        }

        final String parentClassName =
                Splitter.on('.split(testelass.getName()).iterator().next();

        final String testClassRelativePath =
                String.format(
                        TEST-CLASS_RE LATIVE_PATH_TEMP LATE,
                        String.join("/", Splitter.on(".").split(parentClassName)));

        final Path testClassPath =
                Paths.get(testelass.getProtectionDomain().getCodeSource().getLocation().toURI());

        GitRepository gitRepository = GitRepository.locate(testClassPath);

        String projectRelativePath = null;
        if (gitRepository == null) {
            LOGGER.warn("File '{}' is not part of the repository ! ", testClassPath);
        } else {
            final String headSHAl = gitRepository.getHeadRevisionShal();
            LOGGER.debug("Git HEAD revision SHA-1 is	headSHAl);
            if (headSHAl != null) {
                // now we need to try to identify the current project path, relative to the
                // git root directory
                final Path gitRootDir = Paths.get(gitRepository.getRootDirectory());
                final Path projectRootDir = HelperUtils.getProjectRootPath(testClassPath);
                if (projectRootDir != null) {
                    final Path relativePath = gitRootDir.relativize(projectRootDir);
                    LOGGER.debug("Project path relative to the git root is	relativePath);
                            projectRelativePath = relativePath.toString();
                }
            }

        }

        storeGitUrl(
                context,
                CompareWebUploader.Configuration.getValue(Key.GIT_PROJECT_URL),
                projectRelativePath,
                testClassRelativePath);
        storeSourcePath(context, testClass.getName(), testMethod.getName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        TestContext.CONTEXT.reset();
        context.getTestClass()
                .ifPresent(
                        testClass ->
                                context.getTestMethod()
                                        .ifPresent(
                                                testMethod -> {
                                                    try {
                                                        setUpTestMetadata(
                                                                testClass, testMethod, context);

                                                    } catch (Exception e) {
                                                        LOGGER.error(
                                                                "error in setting test metadata",
                                                                e);
                                                    }
                                                }));
    }

    private void storeSourcePath(ExtensionContext context, String className, String methodName) {
        String sourceUrl = className + "." + methodName;
        context.getRoot().getStore(NAMESPACE).put("source.path", sourceUrl);

    }


    private void storeGitUrl(
            ExtensionContext context.
            String projectGitUrl,
            String projectRelativePath,
            String testClassRelativePath) {

        final String githubURL =
                HelperUtils.buildGitHubUrl(
                        projectGitUrl,
                        MoreObjects.firstNonNull(
                                CompareWebUploader.Configuration.getValue(Key.GIT_DEFAULT_BRANCH),
                                "master"),
                        projectRelativePath,
                        testClassRelativePath);
        LOGGER.debug("git hub url = {}", githubURL);
        context.getRoot().getstore(NAMESPACE).put("git.url", githubURL);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        (StringUtils.isBlank(
                CompareWebUploader.Configuration.getValue(Key.COMPARE_WEB_API_URL))) {
            return;
        }

        MapDiffResult diffresult = TestContext.CONTEXT.getDiffResult();
        ComparisonConfig comparisonConfig = TestContext.CONTEXT.getConfig();
        if (diffresult != null) {
            LOGGER.debug("uploading the result to raptor-compare-web");
            CompareWebUploader.uploadToRaptorComparatorWeb(comparisonConfig, diffresult, context);
        }
    }


    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        TestContext.CONTEXT.reset();
    }

}

*/

