package de.tautenhahn.spelling;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Can ignore parts of special files, like keys in JSON.
 */
public final class PreProcessor
{
    private static final Map<String, Function<String, String>> METHODS =
        Map.of("json", PreProcessor::removeJsonKey, "yml", PreProcessor::removeYamlKey, "yaml",
            PreProcessor::removeYamlKey);
    static boolean isActive;

    private PreProcessor()
    {
        // no instances
    }

    /**
     * Return a function to pre-process lines.
     *
     * @param path defines which interpretation of lines is used.
     * @return identity if inactive
     */
    public static Function<String, String> forPath(Path path)
    {
        if (!isActive)
        {
            return Function.identity();
        }
        String extension =
            Optional.ofNullable(path.getFileName()).map(p -> p.toString().toLowerCase(Locale.ENGLISH)).orElse("");
        int pos = extension.lastIndexOf('.');
        extension = pos > 0 ? extension.substring(pos + 1) : "";
        return METHODS.getOrDefault(extension, Function.identity());
    }

    private static String removeJsonKey(String line)
    {
        return line.replaceFirst("^\\s*\"[a-zA-Z0-9_\\-]+\"\\s*:", "\"\": ");
    }

    private static String removeYamlKey(String line)
    {
        return line.replaceFirst("^\\s*(- )?\\w[a-zA-Z0-9_\\-]*\\s*:", ": ");
    }
}
