package com.paxovision.db.comparator.compareweb;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
//import com.mlp.raptor.actor.RestClientActor;
import com.paxovision.db.comparator.ComparisonConfig;
//import com.paxovision.db.comparator.compareweb.CompareWebUploader.Configuration.Key;
//import com.paxovision.db.comparator.compareweb.extension.RaptorCompareWebExtension;
import com.paxovision.db.comparator.diffreport.DiffReport;
import com.paxovision.db.comparator.diffreport.JsonKeyedMapDiffResultToLines;
import com.paxovision.db.comparator.model.MapDiffResult;
//import com.paxovision.db.reportportal.utils.CompareWebUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class responsible to upload diff result to compare-web app */
@SuppressWarnings({"squid:S1444", "squid:S1075"})

public class CompareWebUploader {

    /*
    private static final Logger LOGGER = LoggerFactory.getLogger(CompareWebUploader.class);
    //TODO: Checge
    private static final String CSIDENTITYJJRL = "https://cs-identity.mlp.com";
    private static final String CSIDENTITY_PATH = "/api/v2.0/token?applications=QARaptorCompareWeb&tokenlife=3000";
    private static String identityToken;
    private static final Gson gson = new Gson();


    public static class Configuration {
        private static final Map<String, String> storage = new HashMap<>();

        public enum Key {
            COMPARE_WEB_API_URL("compare.web.api.url"),
            COMPARE_WEB_UI_URL("compare.web.ui.url"),
            USERNAME("username"),
            PASSWORD("password"),
            GIT_PROJECT_URL("git.project.url"),
            GIT_DEFAULT_BRANCH("git.default.branch");

            private final String value;

            private Key(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return value;
            }
        }


        @SuppressWarnings("squid:CallToDeprecatedMethod")
        public static void load(String resourceName) {
            final Properties properties = new Properties();
            final URL configurationURL =
                    CompareWebUploader.class.getClassLoader().getResource(resourceName);

            if (configurationURL == null) {
                LOGGER.error(
                        "Can't locate configuration file '{}' in the classpath. Extention won't be activated!",
                        resourceName);
            } else {
                try {
                    LOGGER.info("Loading configuration from {}", configurationURL);
                    properties.load(configurationURL.openStream());
                } catch (IOException ex) {
                    LOGGER.error(
                            "Can't load configuration properties from '{}'!", resourceName, ex);
                }
            }


            storage.putAll(
                    properties.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            e -> e.getKey().toString(),
                                            e -> e.getValue().toString())));

            EnumSet.allOf(Configuration.Key.class).stream()
                    .forEach(
                            key -> {
                                final String propertyName = key.getValue();
                                final String runtimeValue = System.getProperty(propertyName);
                                if (runtimeValue != null) {
                                    storage.put(propertyName, runtimeValue);
                                    LOGGER.debug(
                                            "Overriding configuration property '{}' value with '{}' from runtime property",
                                            propertyName,
                                            runtimeValue);
                                }
                            });

            LOGGER.debug("configuration properties {}", properties);
        }


        public static String getValue(Configuration.Key key) {
            return Strings.emptyToNull(storage.get(key.getValue()));
        }

        public static void setValue(Configuration.Key key, String value) {
            storage.put(key.getValue(), value);
        }
    }

     */

    private CompareWebUploader() {
    }


    /**
     * Get compare web ui url , fallback to system properties
     *
     * @param comparisonConfig
     * @return url
     */
    //@SuppressWarnings("squid:CallToDeprecatedMethod")
    /*
    private static String getCompareWebUiUrl(ComparisonConfig comparisonConfig) {
        if (StringUtils.isNotBlank(comparisonConfig.getComparatorWebUiURL())) {
            return comparisonConfig.getComparatorWebUiURL();
        }
        return Configuration.getValue(Key.COMPARE_WEB_UI_URL);
    }
    */


    /**
     * Get compare web api url , fallback to system properties
     *
     * param comparisonConfig
     * @return url
     */
    //@SuppressWarnings("squid:CallToDeprecatedMethod")
    /*
    private static String getCompareWebApiUrl(ComparisonConfig comparisonConfig) {
        if (StringUtils.isNotBlank(comparisonConfig.getComparatorWebApiURL())) {
            return comparisonConfig.getComparatorWebApiURL();
        }
        return Configuration.getValue(Key.COMPARE_WEB_API_URL);
    }
    */
    private static String getParentName(ExtensionContext context) {
        Optional<ExtensionContext> parentOpt = context.getParent();
        return parentOpt.isPresent() ? parentOpt.get().getDisplayName() : "";
    }

    private static String extractNameFromParameterizedTest(
            ExtensionContext context, String displayName) {
        String finalDisplayName = getParentName(context); // parent display name
        return finalDisplayName + " - " + displayName;
    }

    private static boolean isParameterizedTest(Method testMethod) {
        ParameterizedTest parameterized = testMethod.getDeclaredAnnotation(ParameterizedTest.class);
        return parameterized != null;
    }

    private static String getCompareName(ExtensionContext context) {
        Optional<Method> method = context.getTestMethod();
        if (method.isPresent()) {
            if (isParameterizedTest(method.get())) {
                return extractNameFromParameterizedTest(context, context.getDisplayName());
            } else {
                return context.getDisplayName();
            }
        }
        //TODO: change the name
        return "raptor-compare-diffresult";
    }

    /*
    private static String getBearerToken() {
        if (identityToken != null) { // if token is there , dont request it again
            return identityToken;
        }
        String username = Configuration.getValue(Key.USERNAME);
        LOGGER.debug("username={}", username);
        if (username != null) { // backward compatibility
            LOGGER.debug("generate new identity token");
            RestClientActor restClientActor =
                    RestClientActor.newBuilder()
                            .withBaseURL(CSIDENTITYJJRL)
                            .withBasicAuth(username, Configuration.getValue(Key.PASSWORD))
                            .skipSSLChecks()
                            .build();

            identityToken =
                    restClientActor
                            .get(CSIDENTITY_PATH)
                            .expect(
                                    response ->
                                            response.match()
                                                    .statusCode(200)
                                                    .bodyAsJSON(
                                                            body ->
                                                                    body.extract()
                                                                            .jsonPathAsString(
                                                                                    "token")));

            return identityToken;
        } else {
            return null;
        }
    }
    */

    /*
    public static void uploadToRaptorComparatorWeb(
            ComparisonConfig comparisonConfig,
            MapDiffResult diffResult,
            ExtensionContext extensionContext) {
        try {
            String compareWebApiURL = getCompareWebApiUrl(comparisonConfig);
            String compareWebUiURL = getCompareWebUiUrl(comparisonConfig);
            if (StringUtils.isNotBlank(compareWebApiURL)
                    && StringUtils.isNotBlank(compareWebUiURL)) {

                String compareName = getCompareName(extensionContext);
                LOGGER.info("start uploading result to comparator web ({})", compareName);
                DiffReport instance = new JsonKeyedMapDiffResultToLines(0);
                Map diffResultJson = new HashMap();
                diffResultJson.put("name", compareName);

                diffResultJson.put(
                        "git.url",
                        extensionContext
                                .getRoot()
                                .getStore(RaptorCompareWebExtension.NAMESPACE)
                                .get("git.url"));

                diffResultJson.put(
                        "source.url",
                        extensionContext
                                .getRoot()
                                .getStore(RaptorCompareWebExtension.NAMESPACE)
                                .get("source.path"));

                diffResultJson.put("compareKey", comparisonConfig.getCompareKey());
                diffResultJson.put("summary", instance.getSummary(diffResult).get(0));
                diffResultJson.put("diff", instance.getLinesForDiff(diffResult));
                diffResultJson.put("all", instance.getLinesForAll(diffResult));
                diffResultJson.put("onlyIn1", instance.getLinesForExistsOnlyIn1(diffResult));
                diffResultJson.put("onlyIn2", instance.getLinesForExistsOnlyIn2(diffResult));
                diffResultJson.put("duplicatesIn1", instance.getLinesForDuplicatesIn1(diffResult));
                diffResultJson.put("duplicatesIn2", instance.getLinesForDuplicatesIn2(diffResult));

                RestClientActor restClientActor =
                        RestClientActor.newBuilder()
                                .withBaseURL(removeParameter(compareWebApiURL))
                                .withDefaultHeader("Content-Type", "application/json")
                                .withDefaultHeader("Accept", "application/json")
                                .withDefaultHeader("Authorization", "Bearer " + getBearerToken())
                                .skipSSLChecks()
                                .build();

                String jsonResponse =
                        restClientActor
                                .post("/raptor-upload/diff-result" + extractParameter(compareWebApiURL))
                                .withBody(gson.toJson(diffResultJson))
                                .expect(
                                        response ->
                                                response.match()
                                                        .accepted()
                                                        .statusCode(200)
                                                        .extract()
                                                        .bodyAsString());

                Map jsonResponseMap = gson.fromJson(jsonResponse, Map.class);
                String extractedUID = (String) jsonResponseMap.get("requestId");
                String error = (String) jsonResponseMap.get("error");
                LOGGER.info("diff result uploading is done {}/viewdiff/{}",
                        compareWebUiURL,
                        extractedUID);

                if (StringUtils.isNotBlank(error)) {
                    LOGGER.error("ERROR while uploading the diff-result ( {} )", error);
                }
                CompareWebUtil.uploadDiffResultLinkToRP(
                        String.format("%s/viewdiff/%s", compareWebUiURL, extractedUID));
            }

        } catch (JsonSyntaxException e) {
            LOGGER.error("Unable to upload to raptor-compare-web", e);
        }
    }
    */

    private static String removeParameter(String url) {
        if (url != null && url.indexOf('?') >= 0) {
            int firstIndexOf = url.indexOf('?');
            return url.substring(0, firstIndexOf);
        }
        return url;
    }


    private static String extractParameter(String url) {
        if (url != null && url.indexOf('?') >= 0) {
            int firstIndexOf = url.indexOf('?');
            return url.substring(firstIndexOf);
        }
        return "";
    }


}



